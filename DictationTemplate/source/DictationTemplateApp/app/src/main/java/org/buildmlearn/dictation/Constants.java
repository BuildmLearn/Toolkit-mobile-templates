package org.buildmlearn.dictation;

import org.buildmlearn.dictation.data.DictContract;

/**
 * Created by Anupam (opticod) on 4/7/16.
 */
public class Constants {
    public static final String[] DICT_COLUMNS = {
            DictContract.Dict.TABLE_NAME + "." + DictContract.Dict._ID,
            DictContract.Dict.TITLE,
            DictContract.Dict.PASSAGE
    };
    public static final String XMLFileName = "dictation_content.xml";
    public static final int COL_ID = 0;
    public static final int COL_TITLE = 1;
    public static final int COL_PASSAGE = 2;
    public static final String firstrun = "firstRun";
    public static final String passage = "PASSAGE";
}
