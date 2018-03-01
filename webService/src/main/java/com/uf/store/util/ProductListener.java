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
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.PostConstruct;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
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
					Executor pool = Executors.newFixedThreadPool(5);
					WatchService watcher = FileSystems.getDefault().newWatchService();
					Path path = Paths.get(listenFolder);
					path.register(watcher, StandardWatchEventKinds.ENTRY_CREATE);
					WatchKey key = null;
					do {
						try {
							key = watcher.take();
							List<WatchEvent<?>> events = key.pollEvents();
							for (WatchEvent<?> event : events) {
								WatchEvent.Kind<?> kind = event.kind();
								if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
									Path eventPath = (Path) event.context();
									Path prodPath = path.resolve(eventPath);
									if (prodPath.toFile().isDirectory()) {
										logger.info(prodPath.toFile().getAbsolutePath() + ":" + kind.name());
										pool.execute(new AddProductTask(prodPath.toFile()));
									}
								}
							}
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					} while (key.reset());
				} catch (IOException e) {
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
				prods.addAll(Arrays.asList("0", "1", "2", "3", "4", "productInfo"));
				File files[] = null;
				int i = 10;
				while (prods.size() > 0) {
					if (i < 0) {
						logger.info("folder  " + folder.getAbsolutePath() + "  doesn't  contain right files");
						return;
					}
					files = folder.listFiles();
					for (File file : files) {
						String baseName = FilenameUtils.getBaseName(file.getName());
						prods.remove(baseName);
					}
					if (prods.size() > 0) {
						try {
							Thread.sleep(5 * 1000);
							i--;
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
				logger.info("add  product  from " + folder.getAbsolutePath());
				if (files != null) {
					Map<String, byte[]> images = new HashMap<String, byte[]>();
					Product product=new Product();
					List<ProductProperties> productProperties=new ArrayList<ProductProperties>();
					for (File file : files) {
						try {
							if (FilenameUtils.getExtension(file.getName()).equalsIgnoreCase("jpg")) {
								images.put(file.getName(), FileUtils.readFileToByteArray(file));
							} else if (file.getName().equalsIgnoreCase("productInfo.properties")) {
								Properties properties = new Properties();
								Path path = file.toPath();
					            Stream <String> lines = Files.lines(path);
					            List <String> replaced = lines.map(line -> line.replaceAll("：", ":")).collect(Collectors.toList());
					            Files.write(path, replaced);
					            lines.close();
								properties.load(new InputStreamReader(new FileInputStream(file), "UTF-8"));	
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
										continue;
									}
									ProductProperties pro=new ProductProperties();
									pro.setProduct(product);
									pro.setPropKey(key);
									pro.setPropValue(value);
									productProperties.add(pro);
								}
							}
							
						} catch (IOException e) {
							logger.error("", e);
						}
					}
					product.setUpdateTime(new Date());
					try {
						productService.saveProduct(product, images);
						productService.saveProductProperties(productProperties);
					} catch (Exception e) {
						logger.error("",e);
					}
				}
			}
		}
	}
}
