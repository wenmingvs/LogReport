package com.wenming.library;

import android.content.Context;

/**
 * Created by wenmingvs on 2016/6/23.
 */
public class CacheManagerConfiguration {

    public static final int APPFOLDER_IN_DATA = 0x1;
    public static final int APPFOLDER_IN_ANDROID_DATA = 0x2;
    public static final int APPFOLDER_IN_INTERNAL_SD = 0x3;
    public static final int APPFOLDER_IN_EXTERNAL_SD = 0x4;
    public static final int APPFOLDER_IN_AUTO = 0x5;


    public String companyFolder;
    public String appRootName;
    public int appRootFolderParentPath;
    public Context context;

    public static CacheManagerConfiguration createDefault(Context context) {
        return new Builder(context).build();
    }

    private CacheManagerConfiguration(final Builder builder) {
        this.context = builder.context;

        this.appRootName = builder.appFolderName;
        this.companyFolder = builder.companyFolder;
        this.appRootFolderParentPath = builder.appRootFolderParentPath;
    }

    public static class Builder {

        public String companyFolder;
        public String appFolderName;
        public int appRootFolderParentPath;
        public Context context;

        public String getAppFolderName() {
            return appFolderName;
        }

        public void setAppFolderName(String appFolderName) {
            this.appFolderName = appFolderName;
        }
        
        public Builder(Context context) {
            this.context = context;

            this.companyFolder = "";
        }

        public String getCompanyFolder() {
            return companyFolder;
        }

        public void setCompanyFolder(String companyFolder) {
            this.companyFolder = companyFolder;
        }

        public void setAppRootFolderParentPath(int appRootFolderParentPath) {
            this.appRootFolderParentPath = appRootFolderParentPath;
        }

        public CacheManagerConfiguration build() {
            initEmptyFolderWithDefaultName();
            return new CacheManagerConfiguration(this);
        }

        /**
         * 根据设置创建默认的文件夹
         */
        private void initEmptyFolderWithDefaultName() {

        }

    }
}
