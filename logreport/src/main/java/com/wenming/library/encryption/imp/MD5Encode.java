package com.wenming.library.encryption.imp;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 不可逆的加密方式
 * Created by wenmingvs on 2016/7/6.
 */
public class MD5Encode {

    /***
     * @param data 需要加密的内容
     * @return 返回加密内容
     */
    public static String encrypt(String data) {
        try {
            // 实例化一个指定摘要算法为MD5的MessageDigest对象
            MessageDigest algorithm = MessageDigest.getInstance("MD5");
            // 重置摘要以供再次使用
            algorithm.reset();
            // 使用bytes更新摘要
            algorithm.update(data.getBytes());
            // 使用指定的byte数组对摘要进行最的更新，然后完成摘要计算
            return toHexString(algorithm.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * @param bytes 将字符串中的每个字符转换为十六进制
     * @return 返回十六进制的内容
     */
    private static String toHexString(byte[] bytes) {
        StringBuilder hexstring = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xFF & b);
            if (hex.length() == 1) {
                hexstring.append('0');
            }
            hexstring.append(hex);
        }
        return hexstring.toString();
    }
}
