package com.wenming.library.encryption.imp;

import android.util.Base64;

import com.wenming.library.encryption.IEncryption;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * DES加密工具类
 * Created by wenmingvs on 2016/7/6.
 */
public class DESEncode implements IEncryption {

    /**
     * DES的工作模式与填充模式，客户端与服务器端务必统一，不然解密会不一致
     */
    private static final String transformation = "DES/CBC/PKCS5Padding";

    /**
     * 初始化向量，客户端与服务器端务必统一，不然解密会不一致
     */
    private static byte[] iv = {1, 2, 3, 4, 5, 6, 7, 8};

    /**
     * 默认使用的key,DES的密钥一定要是8位数，否则加密会失败
     */
    private final static String DEFAULT_KEY = "wenmingv";


    /**
     * @param src 加密的内容
     * @return 加密后的密文
     * @throws Exception
     */
    public String encrypt(String src) throws Exception {
        return encrypt(DEFAULT_KEY, src);
    }

    /****
     * @param src 加密的内容
     * @return 返回解密的内容
     * @throws Exception
     */
    public String decrypt(String src) throws Exception {
        return decrypt(DEFAULT_KEY, src);
    }

    /**
     * @param key 加密的密钥
     * @param src 加密的内容
     * @return 加密后的密文
     * @throws Exception
     */
    public String encrypt(String key, String src) throws Exception {
        // 实例化IvParameterSpec对象，使用指定的初始化向量
        IvParameterSpec zeroIv = new IvParameterSpec(iv);
        // 实例化SecretKeySpec类，根据字节数组来构造SecretKey
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "DES");
        // 创建密码器
        Cipher cipher = Cipher.getInstance(transformation);
        // 用秘钥初始化Cipher对象
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, zeroIv);
        // 执行加密操作
        byte[] encryptedData = cipher.doFinal(src.getBytes());
        return Base64.encodeToString(encryptedData, Base64.DEFAULT);
    }


    /****
     * @param key 解密的密钥
     * @return 返回解密后的内容
     * @throws Exception
     */
    public String decrypt(String key, String encodeString) throws Exception {
        byte[] byteMi = Base64.decode(encodeString, Base64.DEFAULT);
        // 实例化IvParameterSpec对象，使用指定的初始化向量
        IvParameterSpec zeroIv = new IvParameterSpec(iv);
        // 实例化SecretKeySpec类，根据字节数组来构造SecretKey
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "DES");
        // 创建密码器
        Cipher cipher = Cipher.getInstance(transformation);
        // 用秘钥初始化Cipher对象
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, zeroIv);
        // 执行解密操作
        byte[] decryptedData = cipher.doFinal(byteMi);
        return new String(decryptedData);
    }

}
