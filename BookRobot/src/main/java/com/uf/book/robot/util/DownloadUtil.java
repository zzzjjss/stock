package com.uf.book.robot.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.persist.EntityCursor;
import com.sleepycat.persist.EntityStore;
import com.sleepycat.persist.PrimaryIndex;
import com.sleepycat.persist.StoreConfig;
import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.PrimaryKey;

@Component
public class DownloadUtil {
	private int liveTimeMinute = 10;
	private static PrimaryIndex<String, DownloadInfo> index;
	private static EntityStore store;
	private static Environment envmnt;
	private String storePath;

	public DownloadUtil(@Value("${cacher.live_time_minute}") int liveTimeMinute, @Value("${cacher.store_path}") String storePath) {
		this.liveTimeMinute = liveTimeMinute;
		this.storePath = storePath;
		EnvironmentConfig envConfig = new EnvironmentConfig();
		StoreConfig storeConfig = new StoreConfig();
		envConfig.setAllowCreate(true);
		// envConfig.setTransactional(true);
		// storeConfig.setTransactional(true);
		storeConfig.setAllowCreate(true);
		File envHome = new File(storePath);
		envmnt = new Environment(envHome, envConfig);
		store = new EntityStore(envmnt, "EntityStore", storeConfig);
		index = store.getPrimaryIndex(String.class, DownloadInfo.class);
	}

	public  String generateDownloadPath(String localFilePath) {
		String uuid = UUID.randomUUID().toString();
		DownloadInfo info = new DownloadInfo();
		info.setFilePath(localFilePath);
		info.setGenerateTime(System.currentTimeMillis());
		info.setUuid(uuid);
		index.put(info);
		store.sync();
		return uuid;
	}

	public  String findLocalFilePath(String downloadPath) {
		DownloadInfo info = index.get(downloadPath);
		if (info == null) {
			return null;
		}
		if (info != null && (System.currentTimeMillis() - info.getGenerateTime()) >= 1000 * 60 *liveTimeMinute ) {
			index.delete(downloadPath);
			System.out.println("delete the timeout  downloadPath:" + downloadPath);
			return null;
		}
		return info.getFilePath();
	}

	public  List<DownloadInfo> listAllDownloadInfos() {
		EntityCursor<DownloadInfo> entityCursor = index.entities();
		List<DownloadInfo> infos = new ArrayList<DownloadInfo>();
		entityCursor.forEach(info -> {
			infos.add(info);
		});
		return infos;
	}


	public static void main(String[] args) {
		// for (int i = 0; i < 10000; i++) {
		// DownloadInfo info = new DownloadInfo();
		// String filePath = "c:/jason/book/test.txt";
		// String downloadPath=generateDownloadPath(filePath);
		// System.out.println(findLocalFilePath(downloadPath));
		//
		// }
		// store.close();
		// envmnt.close();

		// DownloadInfo info=findDownloadInfo("fce7d05c09b4762f1df9ee3c616857ff1535");
		// System.out.println(info.getFilePath());
		// try {
		// Thread.sleep(10 * 1000);
		// } catch (InterruptedException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// System.out.println(listAllDownloadInfos().size());
	}
}
