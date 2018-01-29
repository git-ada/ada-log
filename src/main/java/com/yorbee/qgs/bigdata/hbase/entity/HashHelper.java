package com.yorbee.qgs.bigdata.hbase.entity;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.util.UUID;

public class HashHelper {
	private static final char[] HEXES = {
            '0', '1', '2', '3',
            '4', '5', '6', '7',
            '8', '9', 'a', 'b',
            'c', 'd', 'e', 'f'
    };
	

	private static String encrypt(byte[] data, String algorithm) throws Exception {
        // 1. 根据算法名称获实现了算法的加密实例
        MessageDigest digest = MessageDigest.getInstance(algorithm);

        // 2. 加密数据, 计算数据的哈希值
        byte[] cipher = digest.digest(data);

        // 3. 将结果转换为十六进制小写
        return bytes2Hex(cipher);
    }

  
    private static String encrypt(File file, String algorithm) throws Exception {
        InputStream in = null;

        try {
            // 1. 根据算法名称获实现了算法的加密实例
            MessageDigest digest = MessageDigest.getInstance(algorithm);

            in = new FileInputStream(file);
            byte[] buf = new byte[1024];
            int len = -1;
            while ((len = in.read(buf)) != -1) {
                // 2. 文件数据通常比较大, 使用 update() 方法逐步添加
                digest.update(buf, 0, len);
            }

            // 3. 计算数据的哈希值, 添加完数据后 digest() 方法只能被调用一次
            byte[] cipher = digest.digest();

            // 4. 将结果转换为十六进制小写
            return bytes2Hex(cipher);

        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e) {
                    // nothing
                }
            }
        }
    }

    private static String bytes2Hex(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }

        StringBuilder sb = new StringBuilder();

        for (byte b : bytes) {
            sb.append(HEXES[(b >> 4) & 0x0F]);
            sb.append(HEXES[b & 0x0F]);
        }

        return sb.toString();
    }
    
    public static String getOrderIdByUUId() {  
        int machineId = 9;//最大支持1-9个集群机器部署  
        int hashCodeV = UUID.randomUUID().toString().hashCode();  
        if(hashCodeV < 0) {//有可能是负数  
            hashCodeV = - hashCodeV;  
        }  
        // 0 代表前面补充0       
        // 4 代表长度为4       
        // d 代表参数为正数型  
        return machineId + String.format("%015d", hashCodeV);  
    }
    public static String getHashValueOfMD5() {
    	String md5 ="";
   
    	try {
	    	long ttl=Long.MAX_VALUE-System.currentTimeMillis();
	        String text1 =String.valueOf(ttl)+getOrderIdByUUId();
            md5 = encrypt(text1.getBytes("UTF-8"), "MD5");
            
    	}catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return md5;
    }
    
    public static String getHashValueOfSHA() {
    	String md5 ="";
   
    	try {
	    	long ttl=Long.MAX_VALUE-System.currentTimeMillis();
	        String text1 =String.valueOf(ttl)+getOrderIdByUUId();
            md5 = encrypt(text1.getBytes("UTF-8"), "SHA-1");
            
    	}catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return md5;
    }
}
