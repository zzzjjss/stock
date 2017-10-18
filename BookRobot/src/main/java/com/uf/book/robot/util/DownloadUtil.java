package com.uf.book.robot.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.persist.EntityCursor;
import com.sleepycat.persist.EntityStore;
import com.sleepycat.persist.PrimaryIndex;
import com.sleepycat.persist.StoreConfig;
import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.PrimaryKey;

public class DownloadUtil {
	public static final int LIVE_TIME_MINUTE = 1;
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

	public static String generateDownloadPath(String localFilePath) {
		String uuid=UUID.randomUUID().toString();
		DownloadInfo info=new DownloadInfo();
		info.setFilePath(localFilePath);
		info.setGenerateTime(System.currentTimeMillis());
		info.setUuid(uuid);
		index.put(info);
		return uuid;
	}

	public static String findLocalFilePath(String downloadPath) {
		DownloadInfo info = index.get(downloadPath);
		if (info != null && (System.currentTimeMillis() - info.getGenerateTime()) >= 1000 * 60 * LIVE_TIME_MINUTE) {
			index.delete(downloadPath);
			return null;
		}
		return info.getFilePath();
	}

	public static List<DownloadInfo> listAllDownloadInfos() {
		EntityCursor<DownloadInfo> entityCursor = index.entities();
		List<DownloadInfo> infos = new ArrayList<DownloadInfo>();
		entityCursor.forEach(info -> {
			infos.add(info);
		});
		return infos;
	}

	@Entity
	static class DownloadInfo {
		@PrimaryKey
		private String uuid;
		private String filePath;
		private long generateTime;

		public String getUuid() {
			return uuid;
		}

		public void setUuid(String uuid) {
			this.uuid = uuid;
		}

		public String getFilePath() {
			return filePath;
		}

		public void setFilePath(String filePath) {
			this.filePath = filePath;
		}

		public long getGenerateTime() {
			return generateTime;
		}

		public void setGenerateTime(long generateTime) {
			this.generateTime = generateTime;
		}
	}

	public static void main(String[] args) {
//		for (int i = 0; i < 10000; i++) {
//			DownloadInfo info = new DownloadInfo();
//			String filePath = "c:/jason/book/test.txt";
//			String downloadPath=generateDownloadPath(filePath);
//			System.out.println(findLocalFilePath(downloadPath));
//
//		}
		// store.close();
		// envmnt.close();

		// DownloadInfo info=findDownloadInfo("fce7d05c09b4762f1df9ee3c616857ff1535");
		// System.out.println(info.getFilePath());
//		try {
//			Thread.sleep(10 * 1000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		System.out.println(listAllDownloadInfos().size());

	}
}
