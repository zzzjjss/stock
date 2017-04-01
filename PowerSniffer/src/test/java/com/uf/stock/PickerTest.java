package com.uf.stock;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.junit.Test;

import com.uf.stock.picker.KLinePicker;
import com.uf.stock.picker.PickResult;
import com.uf.stock.util.StockUtil;

public class PickerTest {

  @Test
  public void testTKlinePicker() {
    Calendar calendar=Calendar.getInstance();
    KLinePicker picker=new KLinePicker();
    SortedMap<Integer, Integer> statitc=new  TreeMap<Integer, Integer>();
    for (int i=0;i<300;i++) {
      calendar.add(Calendar.DATE, -1);
      PickResult result=picker.pickByTKLine(calendar.getTime());
      if (result.getAllTradeInfos()!=null) {
        int upDays=StockUtil.howmanyDaysToTargetUpPercent(result.getAllTradeInfos(), result.getPickIndex(), 2f);
        System.out.println(upDays);
        Integer tmp=statitc.get(upDays);
        if (tmp==null) {
          tmp=1;
        }else {
          tmp++;
        }
        statitc.put(upDays, tmp);
      }
    }
    System.out.println(statitc);
    int targetDays=5;
    int win=0,lose=0;
    for (Map.Entry<Integer, Integer> sta : statitc.entrySet()) {
      Integer key=sta.getKey();
      if (key<=targetDays) {
        win=win+sta.getValue();
     }else {
       lose=lose+sta.getValue();
     }
   }
    System.out.println("win:"+win+" lose:"+lose);
    float rate=((float)(win)/(float)(win+lose))*100;
    System.out.println("winRate:"+rate);
    float accept=(rate*2f)/(100-rate);
    System.out.println("accept downPercent:"+accept+"%");
    float sum=0f;
    
  }

}
