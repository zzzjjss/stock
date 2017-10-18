package com.uf.book.robot.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Hex;

public class DigestUtil {
	private static MessageDigest md5Digest;
	static {
		try {
			md5Digest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	public static synchronized String md5(String str) {
		byte []bs=str.getBytes();
		md5Digest.reset();
		md5Digest.update(bs, 0, bs.length);
		return Hex.encodeHexString(md5Digest.digest());
		
	}
}
