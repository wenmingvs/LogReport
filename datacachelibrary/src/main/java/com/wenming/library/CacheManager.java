package com.wenming.library;

import android.content.Context;

import com.wenming.library.helper.AppFolderHelper;
import com.wenming.library.helper.CleanHelper;
import com.wenming.library.helper.DataAndroidHelper;
import com.wenming.library.helper.DataHepler;
import com.wenming.library.helper.SDcardHelper;

/**
 * Created by wenmingvs on 2016/6/21.
 */
public class CacheManager {

    private static final String ERROR_INIT_CONFIG_WITH_NULL = "CacheManager configuration can not be initialized with null";
    private static CacheManager instance;
    private CacheManagerConfiguration config;
    public CleanHelper cleanHelper;
    public DataAndroidHelper dataAndroidHelper;
    public DataHepler dataHepler;
    public SDcardHelper sdcardFolder;
    public AppFolderHelper appRootHelper;
    private Context context;

    private CacheManager() {
    }

    public static CacheManager getInstance() {
        if (instance == null) {
            synchronized (CacheManager.class) {
                if (instance == null) {
                    instance = new CacheManager();
                }
            }
        }
        return instance;
    }

    public void init(CacheManagerConfiguration configuration) {
        if (configuration == null) {
            throw new IllegalArgumentException(ERROR_INIT_CONFIG_WITH_NULL);
        }
        this.config = configuration;
        this.context = configuration.context;
        this.cleanHelper = new CleanHelper(context);
        this.dataAndroidHelper = new DataAndroidHelper(context);
        this.dataHepler = new DataHepler(context);
        this.sdcardFolder = new SDcardHelper();
        appRootHelper = new AppFolderHelper(config, cleanHelper, dataAndroidHelper, dataHepler, sdcardFolder);
    }


}
