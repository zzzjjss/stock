package com.uf.store.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.common.base.Splitter;
import com.uf.store.dao.mysql.po.Product;
import com.uf.store.dao.mysql.po.ProductProperties;
import com.uf.store.service.ProductManageService;

@Component
public class ProductListener {
	private Logger logger = LoggerFactory.getLogger(ProductListener.class);
	@Value("${automatic.listenFolder}")
	private String listenFolder;
	@Autowired
	private ProductManageService productService;

	@PostConstruct
	public void startToListener() {
		Thread listenerThread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					WatchService watcher = FileSystems.getDefault().newWatchService();
					Path path = Paths.get(listenFolder);
					path.register(watcher, StandardWatchEventKinds.ENTRY_CREATE);
					WatchKey key = null;
					int i=0;
					while((key=watcher.take())!=null) {
						List<WatchEvent<?>> events = key.pollEvents();
						for (WatchEvent<?> event : events) {
							try {
								WatchEvent.Kind<?> kind = event.kind();
								if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
									Path eventPath = (Path) event.context();
									Path prodPath = path.resolve(eventPath);
									if (prodPath.toFile().isDirectory()) {
										logger.info(prodPath.toFile().getAbsolutePath() + ":" + kind.name());
//										pool.execute(new AddProductTask(prodPath.toFile()));
										new AddProductTask(prodPath.toFile()).run();
										i++;
										logger.info(i+" save product "+prodPath.toFile().getAbsolutePath());
									}
								}
								
							}catch (Exception e) {
								e.printStackTrace();
							}
						}
						key.reset();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, "productListenerThread");
		listenerThread.start();
	}

	class AddProductTask implements Runnable {
		private File folder;

		public AddProductTask(File productPath) {
			folder = productPath;
		}
		@Override
		public void run() {
			if (folder.exists() && folder.isDirectory()) {
				List<String> prods = new ArrayList<String>();
				File files[] = null;
				int i = 10;
				Product product=new Product();
				List<ProductProperties> productProperties=new ArrayList<ProductProperties>();
				List<String> imgs=null;
				while(true) {
					files=folder.listFiles();
					if(files!=null&&files.length>0) {
						for(File file:files) {
							try {
								if(file.getName().equalsIgnoreCase("productInfo.properties")) {
									Properties properties = new Properties();
									Path path = file.toPath();
						            Stream <String> lines = Files.lines(path);
						            List <String> replaced = lines.map(line -> line.replaceAll("：", ":")).collect(Collectors.toList());
						            Files.write(path, replaced);
						            lines.close();
						            FileInputStream fInputStream=new FileInputStream(file);
									properties.load(new InputStreamReader(fInputStream, "UTF-8"));	
									Set<String> keys=properties.stringPropertyNames();
									for(String key:keys) {
										String value=properties.getProperty(key);
										if (key.equalsIgnoreCase("name")) {
											product.setName(value);
											continue;
										}
										if(key.equalsIgnoreCase("price")) {
											product.setSellPrice(Float.parseFloat(value));
											continue;
										}
										if(key.equalsIgnoreCase("brand")) {
											product.setBrand(value);
											product.setSearchKeywords(value);
											continue;
										}
										if(key.equalsIgnoreCase("imgs")) {
											imgs=Splitter.on(",").splitToList(value);
											continue;
										}
										if(key.equalsIgnoreCase("id")&&NumberUtils.isCreatable(value)) {
											Product product2=productService.getProductById(Long.parseLong(value));
											if (product2!=null) {
												product.setId(product2.getId());
												product.setSearchKeywords(product2.getSearchKeywords());
											}
											continue;
										}
										ProductProperties pro=new ProductProperties();
										pro.setProduct(product);
										pro.setPropKey(key);
										pro.setPropValue(value);
										productProperties.add(pro);
									}
									fInputStream.close();
									break;
								}
							} catch (Exception e) {
								logger.error("",e);
							}
						}
					}
					i--;
					if(imgs!=null||i==0) {
						break;
					}else {
						try {
							logger.info("wait productInfo.properties   ");
							Thread.sleep(3*1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
				
				if (imgs != null) {
					Map<String, byte[]> images = new HashMap<String, byte[]>();
					waitCopy:while(true) {
						for (String imgInfo:imgs) {
							try {
								List<String> info=Splitter.on("=").splitToList(imgInfo);
								File imgFile=new File(folder,info.get(0));
								if (imgFile.length()==Long.parseLong(info.get(1))) {
									images.put(imgFile.getName(), FileUtils.readFileToByteArray(imgFile));
								}else {
									logger.info("wait img copy ");
									Thread.sleep(1000);
									continue waitCopy;
								}
							} catch (Exception e) {
								logger.error("", e);
							}
						}
						break;
					}
					product.setUpdateTime(new Date());
					try {
						productService.saveProduct(product, images);
						productService.saveProductProperties(productProperties);
						logger.info("add  product  from " + folder.getAbsolutePath());
						FileUtils.deleteDirectory(folder);
					} catch (Exception e) {
						logger.error("",e);
					}
				}
			}
		}
	}
}