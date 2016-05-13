package org.buildmlearn.videocollection;

import org.buildmlearn.videocollection.data.VideoContract;

/**
 * Created by Anupam (opticod) on 13/5/16.
 */
public class Constants {
    public static final String[] VIDEO_COLUMNS = {
            VideoContract.Videos.TABLE_NAME + "." + VideoContract.Videos._ID,
            VideoContract.Videos.TITLE,
            VideoContract.Videos.DESCRIPTION,
            VideoContract.Videos.LINK,
            VideoContract.Videos.THUMBNAIL_URL
    };
    public static String XMLFileName = "video_content.xml";
    public static int COL_ID = 0;
    public static int COL_TITLE = 1;
    public static int COL_DESCRIPTION = 2;
    public static int COL_LINK = 3;
    public static int COL_THUMBNAIL_URL = 4;
}
