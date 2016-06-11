package org.buildmlearn.infotemplate;

import org.buildmlearn.infotemplate.data.InfoContract;

/**
 * Created by Anupam (opticod) on 13/5/16.
 */
public class Constants {
    public static final String[] INFO_COLUMNS = {
            InfoContract.Info.TABLE_NAME + "." + InfoContract.Info._ID,
            InfoContract.Info.TITLE,
            InfoContract.Info.DESCRIPTION
    };
    public static final String XMLFileName = "info_content.xml";
    public static final int COL_ID = 0;
    public static final int COL_TITLE = 1;
    public static final int COL_DESCRIPTION = 2;
    public static final String firstrun = "firstRun";
}
