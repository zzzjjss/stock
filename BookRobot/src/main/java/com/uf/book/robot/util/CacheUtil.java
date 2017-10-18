package com.uf.book.robot.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.persist.EntityCursor;
import com.sleepycat.persist.EntityStore;
import com.sleepycat.persist.PrimaryIndex;
import com.sleepycat.persist.StoreConfig;
import com.uf.book.robot.bean.DownloadInfo;

public class CacheUtil {
	private static PrimaryIndex<String, DownloadInfo> index;
	private static EntityStore store;
	private static Environment envmnt;
	static {
		EnvironmentConfig envConfig = new EnvironmentConfig();
		StoreConfig storeConfig = new StoreConfig();
		envConfig.setAllowCreate(true);
		// envConfig.setTransactional(true);
		// storeConfig.setTransactional(true);
		storeConfig.setAllowCreate(true);

		File envHome = new File("c:/jason/je");
		// Open the environment and entity store
		envmnt = new Environment(envHome, envConfig);
		store = new EntityStore(envmnt, "EntityStore", storeConfig);
		index = store.getPrimaryIndex(String.class, DownloadInfo.class);
	}

	public static void addEntity(DownloadInfo info) {
		index.put(info);
	}

	public static DownloadInfo findDownloadInfo(String md5) {
		return index.get(md5);
	}

	public static void listAllDownloadInfos() {
		EntityCursor<DownloadInfo> entityCursor = index.entities();
		List<String> md5s = new ArrayList<String>();
		entityCursor.forEach(info -> {
			System.out.println(info.getMd5Str());
			md5s.add(info.getMd5Str());
		});
		System.out.println(md5s.size());
	}

	public static void main(String[] args) {
		for (int i = 0; i < 10000; i++) {
			DownloadInfo info = new DownloadInfo();
			String filePath = "c:/jason/book/test.txt";
			long generateTime = System.currentTimeMillis();
			String md5 = DigestUtil.md5(filePath + Long.toString(generateTime));
			info.setFilePath(filePath);
			info.setGenerateTime(generateTime);
			info.setMd5Str(md5 + i);
			addEntity(info);
			System.out.println(findDownloadInfo(info.getMd5Str()).getFilePath());
			
		}
//		store.close();
//		envmnt.close();

		// DownloadInfo info=findDownloadInfo("fce7d05c09b4762f1df9ee3c616857ff1535");
		// System.out.println(info.getFilePath());
		try {
			Thread.sleep(10*1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		listAllDownloadInfos();

	}
}
