package com.wenming.library.helper;

import android.util.Base64;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by wenmingvs on 2016/7/1.
 */
public class CommentHelper {

    /**
     * 查找路径下是否存在此文件夹，如果没有，是否需要创建
     *
     * @param parent       父路径
     * @param folderNames  要寻找的文件夹的名称，只做文件夹搜索哦，如果是文件是无效的
     * @param recursion    是否递归查找
     * @param createIfnull 如果没有，是否需要创建
     * @return 如果文件存在，会返回文件的绝对路径。不存在又不创建，则返回parent路径
     */
    public String getChildFolder(String parent, String folderNames, boolean recursion, boolean createIfnull) {
        //优先在子路径查找，查找不到再递归查找
        File file = new File(parent + folderNames + File.separator);
        if (file.exists()) {
            return file.getAbsolutePath();
        } else {
            if (createIfnull) {
                file.mkdirs();
                return file.getAbsolutePath();
            }
            if (recursion) {
                file = searchFile(new File(parent), folderNames);
                if (file != null && file.exists()) {
                    return file.getAbsolutePath();
                }
            }
        }
        if ((file == null || !file.exists()) && createIfnull) {
            file.mkdirs();
        }
        if (file == null) {
            return parent;
        }

        if (file.exists()) {
            return file.getAbsolutePath();
        }

        return parent;
    }

    /**
     * 获取名称加密过的文件夹
     *
     * @param parent       父路径
     * @param encodeName   加密后的名字，如果只知道原名，可以调用base64Encode(encodeName)获取加密后的名字
     * @param recursion    是否递归查找
     * @param createIfnull 如果没有，是否需要创建
     * @return 如果文件存在，会返回文件的绝对路径。不存在又不创建，则返回parent路径
     */
    public String getEncodeChildFolder(String parent, String encodeName, boolean recursion, boolean createIfnull) {
        return getChildFolder(parent, base64Encode(encodeName), recursion, createIfnull);
    }

    private File searchFile(File parent, String folderNames) {
        ArrayList<File> list = new ArrayList<File>();
        if (parent.isDirectory()) {
            File[] all_file = parent.listFiles();
            if (all_file != null) {
                for (File tempf : all_file) {
                    if (tempf.isDirectory()) {
                        if (tempf.getName().equals(folderNames)) {
                            list.add(tempf);
                        }
                        list.addAll(findFiles(tempf, folderNames));
                    } else {
                        if (tempf.getName().equals(folderNames)) {
                            list.add(tempf);
                        }
                    }
                }
            }
        }
        if (list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    private static ArrayList<File> findFiles(File file, String key_search) {
        ArrayList<File> list = new ArrayList<File>();
        if (file.isDirectory()) {
            File[] all_file = file.listFiles();
            if (all_file != null) {
                for (File tempf : all_file) {
                    if (tempf.isDirectory()) {
                        if (tempf.getName().toLowerCase().lastIndexOf(key_search) > -1) {
                            list.add(tempf);
                        }
                        list.addAll(findFiles(tempf, key_search));
                    } else {
                        if (tempf.getName().toLowerCase().lastIndexOf(key_search) > -1) {
                            list.add(tempf);
                        }
                    }
                }
            }
        }
        return list;
    }

    /**
     * 对文件夹的名字做加密
     *
     * @param folderName
     * @return
     */
    public String base64Encode(String folderName) {
        String encode = Base64.encodeToString(folderName.getBytes(), Base64.URL_SAFE);
        Log.d("CommentHelper", "加密：" + encode);
        return encode;
    }

    /**
     * 对文件夹的名称做解密
     *
     * @param encode
     * @return
     */
    public String base64Decode(String encode) {
        return new String(Base64.decode(encode.getBytes(), Base64.URL_SAFE));
    }

}
