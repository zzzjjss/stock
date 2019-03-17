package com.uf.stock.service.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.uf.stock.bean.UpDownPower;
import com.uf.stock.data.bean.AlarmStock;
import com.uf.stock.data.bean.StockInfo;
import com.uf.stock.data.bean.StockTradeInfo;
import com.uf.stock.data.bean.StockTradeInfoWithAnalysisResult;
import com.uf.stock.data.dao.AlarmStockDao;
import com.uf.stock.data.dao.StockInfoDao;
import com.uf.stock.data.dao.StockTradeInfoDao;
import com.uf.stock.data.dao.StockTradeInfoWithAnalysisResultDao;
import com.uf.stock.data.exception.DataSyncException;
import com.uf.stock.data.sync.StockDataSynchronizer;
import com.uf.stock.service.DataSyncService;
import com.uf.stock.util.Constant;

@Service("dataSyncService")
public class DataSyncServiceImpl implements DataSyncService {
  @Autowired
  private StockInfoDao stockInfoDao;
  @Autowired
  private StockDataSynchronizer dataSyncher;
  @Autowired
  private AlarmStockDao alarmStockDao;
  @Autowired
  private StockTradeInfoDao tradeInfoDao;
  @Autowired
  private StockTradeInfoWithAnalysisResultDao  tradeInfoWithResultDao;
  private Map<Integer, AlarmStock> alarmStockCache = new HashMap<Integer, AlarmStock>();
  private Map<Integer,String> codeToSymbol=new HashMap<Integer, String>();
  private Logger logger = LogManager.getLogger(DataSyncServiceImpl.class);
  public int syncAllStocksBaseInfo() {
    List<StockInfo> stockInfo = dataSyncher.syncAllStocksInfo();
    int add=0;
    for (StockInfo info : stockInfo) {
      StockInfo old=stockInfoDao.findStockByStockCode(info.getCode());
      if(old!=null){
    	  continue;
      }else{
        stockInfoDao.insert(info);
        logger.info("add  stock "+info.getName());
        add++;
      }
    }
    return add;
  }

  public List<UpDownPower> calculateStocksCurrentPower(List<StockInfo> stocks) {
    List<UpDownPower> result = new LinkedList<UpDownPower>();
    if (stocks == null || stocks.size() == 0)
      return result;
    Map<String, StockInfo> cache = new HashMap<String, StockInfo>();
    for (StockInfo stock : stocks) {
      cache.put(stock.getSymbol(), stock);
    }

    Map<String, StockTradeInfo> allStockTradeInfos = new HashMap<String, StockTradeInfo>();
    List<Future<Map<String, StockTradeInfo>>> futures = new ArrayList<Future<Map<String, StockTradeInfo>>>();
    ExecutorService pool = Executors.newCachedThreadPool();
    List<String> symbols = Lists.newArrayList(cache.keySet());
    int begin = 0, syncStockNum = 20;
    while (begin <= symbols.size()) {
      int end = begin + syncStockNum;
      if (end > symbols.size()) {
        end = symbols.size();
      }
      Future<Map<String, StockTradeInfo>> syncResult = pool.submit(new SyncStockTradeInfoTask(symbols.subList(begin, end)));
      futures.add(syncResult);
      begin = begin + syncStockNum;
    }
    pool.shutdown();
    for (Future<Map<String, StockTradeInfo>> future : futures) {
      Map<String, StockTradeInfo> tradeInfos;
      try {
        tradeInfos = future.get();
        allStockTradeInfos.putAll(tradeInfos);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    List<UpDownPower> upPowerList = new ArrayList<UpDownPower>();
    for (String key : allStockTradeInfos.keySet()) {
      StockTradeInfo tradeInfo = allStockTradeInfos.get(key);
      StockInfo stock = cache.get(key);
      if (tradeInfo.getTradeAmount() == null) {
        continue;
      }
      tradeInfo.setTurnoverRate(((float) (tradeInfo.getTradeAmount() / stock.getTotalAAmount())) * 100);
      float power = 0f;
      if (tradeInfo.getTurnoverRate() != 0) {
        power =Math.abs(tradeInfo.getUpDownRate() / tradeInfo.getTurnoverRate());
      }
      UpDownPower powerObj = new UpDownPower();
      powerObj.setUpdownPowerValue(power);
      if(tradeInfo.getUpDownRate()>0){
    	  powerObj.setUpPower(true);
      }else{
    	  powerObj.setUpPower(false);
      }
      powerObj.setStockPeRatio(stock.getPeRatio());
      powerObj.setStockName(stock.getName());
      powerObj.setTradeInfo(tradeInfo);
      upPowerList.add(powerObj);
      result.add(powerObj);
    }
    return result;
  }

  class SyncStockTradeInfoTask implements Callable<Map<String, StockTradeInfo>> {
    private List<String> stockSymbol;

    public SyncStockTradeInfoTask(List<String> stockSymbol) {
      this.stockSymbol = stockSymbol;
    }

    @Override
    public Map<String, StockTradeInfo> call() throws Exception {
      return dataSyncher.syncStocksCurrentTradeInfo(stockSymbol);
    }
  }

  @Override
  public List<StockInfo> findStocksPeRatioBetween(Float min, Float max) {
    return (List<StockInfo>) stockInfoDao.findByHql("from StockInfo  s where  s.peRatio<? and s.peRatio>?", max, min);
  }

  @Override
  public AlarmStock findAlarmStockInfoByStockCode(Integer stockCode) {
    AlarmStock alarm = alarmStockCache.get(stockCode);
    if (alarm == null) {
      alarm = alarmStockDao.findByStockCode(stockCode);
      if (alarm != null) {
        alarmStockCache.put(stockCode, alarm);
      }
    }
    if (alarm == null) {
      StockInfo stock = stockInfoDao.findStockByStockCode(stockCode);
      if (stock != null) {
        alarm = new AlarmStock();
        alarm.setStockCode(stockCode);
        alarm.setAlarmBuyPrice(0.0f);
        alarm.setAlarmSellPrice(0.0f);
        alarm.setStockName(stock.getName());
        alarmStockDao.insert(alarm);
      }
    }
    return alarm;
  }

  @Override
  public List<AlarmStock> findAllAlarmStocks() {
    return alarmStockDao.findAll(AlarmStock.class);
  }

  @Override
  public Map<String, StockTradeInfo> getCurrentStocksTradeInfo(List<String> stockSymbols) {
    Map<String, StockTradeInfo> allStockTradeInfos = new HashMap<String, StockTradeInfo>();
    if (stockSymbols == null || stockSymbols.size() == 0)
      return allStockTradeInfos;

    List<Future<Map<String, StockTradeInfo>>> futures = new ArrayList<Future<Map<String, StockTradeInfo>>>();
    ExecutorService pool = Executors.newCachedThreadPool();
    int begin = 0, syncStockNum = 20;
    while (begin <= stockSymbols.size()) {
      int end = begin + syncStockNum;
      if (end > stockSymbols.size()) {
        end = stockSymbols.size();
      }
      Future<Map<String, StockTradeInfo>> syncResult = pool.submit(new SyncStockTradeInfoTask(stockSymbols.subList(begin, end)));
      futures.add(syncResult);
      begin = begin + syncStockNum;
    }
    pool.shutdown();
    for (Future<Map<String, StockTradeInfo>> future : futures) {
      Map<String, StockTradeInfo> tradeInfos;
      try {
        tradeInfos = future.get();
        allStockTradeInfos.putAll(tradeInfos);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return allStockTradeInfos;
  }

  @Override
  public int syncStockTradeInfos(String stockSymbol) {
    int totalRecord = 0;
    try {
      StockTradeInfo latestTi = tradeInfoDao.findLatestDateStockTradeInfo(stockSymbol);
      DateFormat format = new SimpleDateFormat("yyyyMMdd");
      Date startDate = null;
      Date today = new Date();
      today=format.parse(format.format(today));
      if (latestTi == null) {
        try {
          logger.info("no tradeinfo ,the stockSymbol is "+stockSymbol);
          startDate = format.parse("20130101");
        } catch (ParseException e) {
          e.printStackTrace();
        }
      } else {
    	 if(format.format(today).equals(latestTi.getTradeDate())){
    		 return totalRecord;
    	 }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(latestTi.getTradeDate());
        calendar.add(Calendar.DATE, 1);
        startDate = calendar.getTime();
      }
      Calendar calendar = Calendar.getInstance();
      while (true) {
    	calendar.setTime(startDate);
    	Date start=calendar.getTime();
        calendar.add(Calendar.MONTH, 1);
        Date end = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        startDate=calendar.getTime();
        if (end.getTime() >= today.getTime()) {
          end = today;
        }
        long startTime=System.currentTimeMillis();
        List<StockTradeInfo> tradeInfos = dataSyncher.syncStockDateTradeInfos(stockSymbol, start, end);
        long cost=System.currentTimeMillis()-startTime;
        logger.info("start:"+startDate.toString()+" to  end:"+end.toString()+" ,result:"+tradeInfos.size()+" stockSymbol:"+stockSymbol+" costTime:"+cost+"ms");
        
        
        if (tradeInfos != null && tradeInfos.size() > 0) {
//          Collections.sort(tradeInfos);
          StockTradeInfo latestTradeInfo=null;
          for (StockTradeInfo info : tradeInfos) {
//            System.out.println(format.format(info.getTradeDate()));
//            if(beforeTradeInfo!=null){
//              float downPercent=(beforeTradeInfo.getClosePrice()-info.getOpenPrice())/beforeTradeInfo.getClosePrice();
//              if(downPercent>=0.2){
//                logger.info(stockSymbol+" is ex-right ,the ex-right date is "+format.format(info.getTradeDate()));
//                tradeInfoDao.exrightBeforeDate(info.getStock().getCode(), info.getTradeDate(), downPercent);
//              }
//            }
           tradeInfoDao.insert(info);
           if(Constant.latestTradeDate!=null&&info.getTradeDate().getTime()>Constant.latestTradeDate.getTime()){
        	   Constant.latestTradeDate=info.getTradeDate();
           }
            totalRecord++;
            latestTradeInfo=info;
          }
          StockInfo stockInfo=stockInfoDao.findStockBySymbol(latestTradeInfo.getStockSymbol());
          if (stockInfo!=null&&latestTradeInfo.getTurnoverRate()!=null&&latestTradeInfo.getTurnoverRate()!=0f) {
            stockInfo.setTotalAAmount((latestTradeInfo.getTradeAmount()*100)/latestTradeInfo.getTurnoverRate());
            stockInfoDao.update(stockInfo);
          }
        }
        if (end.getTime() >= today.getTime()) {
          break;
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
      logger.info("sync :"+stockSymbol+" trade info  has exception");
      throw new RuntimeException();
    }
    return totalRecord;
  }

@Override
public String transToStockSymbolFromStockCode(Integer stockCode) {
	String symbol=codeToSymbol.get(stockCode);
	if(symbol==null){
		StockInfo stock=stockInfoDao.findStockByStockCode(stockCode);
		if(stock!=null){
			symbol=stock.getSymbol();
			codeToSymbol.put(stockCode, symbol);
		}
	}
	return symbol;
}

public int syncAllStocksTradeInfo() {
	
	int syncTotalCount=0;
//	List<StockInfo> allStocks=stockInfoDao.findAll(StockInfo.class);
//	ExecutorService pool = Executors.newCachedThreadPool();
//	List<Future<Integer>> results=new ArrayList<Future<Integer>>(); 
//	for(StockInfo stock:allStocks){
//		final String stockSymbol=stock.getSymbol();
//		Future<Integer> future=pool.submit(new Callable<Integer>() {
//			public Integer  call(){
//				return syncStockTradeInfos(stockSymbol);
//			}
//		});	
//		results.add(future);
//	}
//	pool.shutdown();
//	for(Future<Integer> result:results){
//		try {
//			Integer syncCount=result.get();
//			logger.info("sync tradeInfor coutn:"+syncCount);
//			syncTotalCount=syncTotalCount+syncCount;
//		} catch (Exception e) {
//			e.printStackTrace();
//		} 
//	}
	return syncTotalCount;
}

@Override
public List<StockInfo> findStocksInMonitor() {
  return (List<StockInfo>)stockInfoDao.findByHql("from StockInfo s where s.isInAlarmMonitor=?", true);
}

@Override
public StockTradeInfo findOldestStockTradeInfo(Integer stockCode) {
  List<StockTradeInfo> infos=tradeInfoDao.findLimitByHql("from StockTradeInfo t where t.stock.code=? order by t.tradeDate asc", 1, stockCode);
  if(infos!=null&&infos.size()>0){
    return infos.get(0);
  }
  return null;
}

@Override
public StockInfo findStockInfoByStockSymbol(String stockSymbol) {
  List<StockInfo> stocks=(List<StockInfo>)stockInfoDao.findByHql("from StockInfo s where s.symbol=?",stockSymbol);
  if(stocks!=null&&stocks.size()>0){
    return stocks.get(0);
  }
  return null;
}

@Override
public StockTradeInfo findOneDayTradeInfo(Integer stockCode, Date date) {
  List<StockTradeInfo> infos=tradeInfoDao.findLimitByHql("from StockTradeInfo t where t.stock.code=? and t.tradeDate=?", 1, stockCode,date);
  if(infos!=null&&infos.size()>0){
    return infos.get(0);
  }
  return null;
}

public List<StockTradeInfo> findDateAscTradeInfosBeforeDate(Integer stockCode,Date date,int limitDays){
  return tradeInfoDao.findDateAscTradeInfosBeforeDate(stockCode, date, limitDays);
}

@Override
public float calculateAvgPriceBeforeDate(Integer stockCode, Date date) {
  return tradeInfoDao.calculateAvgPriceBeforeDate(stockCode, date);
}

@Override
public void setAlarmStock(StockInfo stock) {
  float lowest=tradeInfoDao.calculateLowestPrice(stock.getCode());
  AlarmStock  alarm=alarmStockDao.findByStockCode(stock.getCode());
  if(alarm!=null){
    alarm.setAlarmBuyPrice(lowest);
  }else{
    alarm=new AlarmStock();
    alarm.setAlarmBuyPrice(lowest);
    alarm.setStockCode(stock.getCode());
    alarm.setStockName(stock.getName());
  }
  alarmStockDao.saveOrUpdate(alarm);
}

@Override
public boolean isStockStopTrade(Integer stockCode) {
	if(Constant.latestTradeDate==null){
		Constant.latestTradeDate=tradeInfoDao.getLatestDate();
	}
	StockTradeInfo info=tradeInfoDao.findLatestDateStockTradeInfo(stockCode);
	if(info!=null&&Constant.latestTradeDate!=null&&Constant.latestTradeDate.getTime()==info.getTradeDate().getTime()){
		return false;
	}
	return true;
}

@Override
public List<StockTradeInfo> findLimitTradeInfosBeforeDate(Integer stockCode, Date date, int limitTradeInfos) {
  return (List<StockTradeInfo>)tradeInfoDao.findLimitByHql("from StockTradeInfo t where t.stock.code=? and t.tradeDate<? order by t.tradeDate desc", limitTradeInfos, stockCode,date);
} 
public List<StockTradeInfo> findAllTradeInfosOrderByDateAsc(Integer stockCode){
  return (List<StockTradeInfo>)tradeInfoDao.findByHql("from StockTradeInfo t where t.stock.code=?  order by t.tradeDate asc", stockCode);
}

@Override
public int syncStockTradeInfoWithAnalysisResult(String stockSymbol) {
  int i=0;
  try {
    StockTradeInfoWithAnalysisResult latest=tradeInfoWithResultDao.findLatestDateStockTradeInfo(stockSymbol);
    List<StockTradeInfoWithAnalysisResult> results=null;
    if (latest==null) {
      results=dataSyncher.syncStockDateTradeInfosWithAnalysisResult(stockSymbol, null);
    }else{
      results=dataSyncher.syncStockDateTradeInfosWithAnalysisResult(stockSymbol, latest.getTradeDate());
    }
    if (results!=null) {
      tradeInfoWithResultDao.batchInsertInfos(results);
      i=results.size();
    }
  } catch (Exception e) {
    logger.error("sync :"+stockSymbol+" tradeinfoWithResult  has exception",e);
    throw new RuntimeException(e);
  }
  return i;
}

@Override
public void addStockToMonitor(String stockSymbol, Float alarmLoseSellPrice,Float alarmWinSellPrice, Float alarmBuCangPrice) {
  StockInfo stockInfo=stockInfoDao.findStockBySymbol(stockSymbol);
  if (stockInfo!=null) {
    stockInfo.setIsInAlarmMonitor(true);
    stockInfo.setAlarmLoseSellPrice(alarmLoseSellPrice);
    stockInfo.setAlarmWinSellPrice(alarmWinSellPrice);
    stockInfo.setAlarmBuCangPrice(alarmBuCangPrice);
    stockInfoDao.saveOrUpdate(stockInfo);
  }
}

@Override
public void removeFromMonitor(String stockSymbol) {
  StockInfo stockInfo=stockInfoDao.findStockBySymbol(stockSymbol);
  if (stockInfo!=null) {
    stockInfo.setIsInAlarmMonitor(false);
    stockInfoDao.saveOrUpdate(stockInfo);
  }
}
private Set<String> allTradeDate=null;
@Override
public boolean isTradeDate(Date date) {
  if (allTradeDate==null) {
    allTradeDate=findAllTradeDate();
  }
  DateFormat format=new SimpleDateFormat("yyyyMMdd");
  return allTradeDate.contains(format.format(date));
}

@Override
public Set<String> findAllTradeDate() {
  Set<String> result=new HashSet<String>();
  DateFormat format=new SimpleDateFormat("yyyyMMdd");
  List<Date> allDates=tradeInfoDao.findAllTradeDate();
  for(Date date:allDates){
    result.add(format.format(date));
  }
  return result;
}
public StockTradeInfoWithAnalysisResult syncCurrentStockTradeInfoWithAnalysisResult(String stockSymbol){
  StockTradeInfoWithAnalysisResult result=null;
  try {
    result=dataSyncher.syncCurrentStockTradeInfoWithAnalysisResult(stockSymbol);
  } catch (DataSyncException e) {
    // TODO Auto-generated catch block
    e.printStackTrace();
  }
  return result;
}


private Map<Integer, List<StockTradeInfo>>  cache=new HashMap<Integer, List<StockTradeInfo>>();
@Override
public List<StockTradeInfo> findAllTradeInfosOrderByDateAscByCache(Integer stockCode) {
  if (cache.containsKey(stockCode)) {
    return cache.get(stockCode);
  }else {
    List<StockTradeInfo> infos=(List<StockTradeInfo>)tradeInfoDao.findByHql("from StockTradeInfo t where t.stock.code=?  order by t.tradeDate asc", stockCode);
    cache.put(stockCode, infos);
    return infos;
  }
}
}
