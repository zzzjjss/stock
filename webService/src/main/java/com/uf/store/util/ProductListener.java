package com.uf.store.util;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
@Component
public class ProductListener {
	@Value("${automatic.listenFolder}") 
	private String listenFolder;

	@PostConstruct
	public  void startToListener() {
		Thread listenerThread=new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					WatchService watcher=FileSystems.getDefault().newWatchService();
					Path path=Paths.get(listenFolder);
					path.register(watcher, StandardWatchEventKinds.ENTRY_CREATE,StandardWatchEventKinds.ENTRY_MODIFY);
					WatchKey key=null;
					do {
						try {
							key=watcher.take();
							List<WatchEvent<?>> events=key.pollEvents();
							System.out.println(events.size());
							for(WatchEvent<?> event:events) {
								WatchEvent.Kind<?> kind=event.kind();
								Path eventPath=(Path)event.context();
								System.out.println(eventPath.toAbsolutePath()+":"+kind.name());
							}
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}while(key.reset());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}, "productListenerThread");
		listenerThread.start();
	}
	
	
	
}
