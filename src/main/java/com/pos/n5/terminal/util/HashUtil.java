package com.pos.n5.terminal.util;
/**
 * @author slai
 * 
 * Created Date: Mar 12, 2005
 *
 * Copyright 2004 Bank of China Credit Card (International) Ltd. All Rights Reserved.
 * 
 * This software is the proprietary information of Bank of China Credit Card (International) Ltd.  
 * Use is subject to license terms.
 * 
 * Update History:
 * Date				Author			Changes
 * Mar 12, 2005		slai			Document Created
 */


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/*
 * HashUtility.java
 * 
 * Class Description:
 * 
 */
public class HashUtil {
	protected static final int BUFFER_SIZE = 4096;

	

	
	
	public static String desToHex(byte[] bytes) {
        StringBuffer sb = new StringBuffer("");
        for (byte b : bytes) {
            String st = String.format("%02X", b);
            sb.append(st);
        }
        return sb.toString();
    }
	
	public static String getMD5(byte[] data) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5"); // or SHA(longer)
			md.update(data);
			return Base64.encodeBytes(md.digest());
		} catch (NoSuchAlgorithmException e) {
			e.getMessage();
			e.printStackTrace();
		}
		return "";
	}

	public static String getMD5(File file) {
		MessageDigest md = null;
		FileInputStream fin = null;

		byte[] buffer = new byte[BUFFER_SIZE];
		try {
			md = MessageDigest.getInstance("MD5"); // or SHA(longer)
			fin = new FileInputStream(file);

			int n = 0;
			while (-1 != (n = fin.read(buffer))) {
				md.update(buffer, 0, n);
			}
			return Base64.encodeBytes(md.digest());
		} catch (NoSuchAlgorithmException e) {
			e.getMessage();
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fin.close();
			} catch (Exception e) {
			}
		}
		return "";
	}

	public static String getMD5(String data) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5"); // or SHA(longer)
			md.update(data.getBytes());
			return Base64.encodeBytes(md.digest());
		} catch (NoSuchAlgorithmException e) {
			e.getMessage();
			e.printStackTrace();
		}
		return "";
	}

	public static byte[] getMD5WithoutBase64(String data) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5"); // or SHA(longer)
			md.update(data.getBytes());
			return md.digest();
		} catch (NoSuchAlgorithmException e) {
			e.getMessage();
			e.printStackTrace();
		}
		return new byte[0];
	}

	public static boolean checkMD5(String input, String digest) {
		String base64MD = digest;

		String xmlString = input;
		/**
		 * Hash the XML message and get the hash value
		 */
		String xmlMD = getMD5(xmlString);
		/**
		 * Message Digest Checking
		 */
		if (!base64MD.equals(xmlMD)) {
			return false;
		} else
			return true;
	}

	public static String getSHA1(String randomNum) {
		String B64 = "";
		try {
			MessageDigest sha = MessageDigest.getInstance("SHA-1");
			sha.reset();
			sha.update(randomNum.getBytes());
			byte[] pwhash = sha.digest();
			B64 = Base64.encodeBytes(pwhash);
		} catch (NoSuchAlgorithmException nsae) {
			System.out.println(nsae.getMessage());
		}
		return B64;

	}
}
