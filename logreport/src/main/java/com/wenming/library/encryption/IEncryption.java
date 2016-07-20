package com.wenming.library.encryption;

/**
 * 加密的接口
 * Created by wenmingvs on 2016/7/6.
 */
public interface IEncryption {
    /**
     * 使用默认的密钥加密字符串
     *
     * @param conetent 需要加密的字符串
     * @return 返回已经加密完成的字符串
     */
    String encrypt(String conetent) throws Exception;

    /**
     * 使用自定义密钥加密字符串
     *
     * @param key 加密的密钥
     * @param src 需要加密的字符串
     * @return 加密完成的字符串
     * @throws Exception
     */
    String encrypt(String key, String src) throws Exception;


    /**
     * 使用自定义密钥解密字符串
     *
     * @param conetent 需要加密的字符串
     * @return 解密后的文本
     * @throws Exception
     */
    String decrypt(String key, String conetent) throws Exception;

    /**
     * 使用默认的密钥解密字符串
     *
     * @param conetent 需要解密的字符串
     * @return 返回已经解密完成的字符串
     */
    String decrypt(String conetent) throws Exception;


}
