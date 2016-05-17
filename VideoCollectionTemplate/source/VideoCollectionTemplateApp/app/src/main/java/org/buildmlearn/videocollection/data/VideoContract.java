package org.buildmlearn.videocollection.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Anupam (opticod) on 13/5/16.
 */

public class VideoContract {

    public static final String CONTENT_AUTHORITY = "org.buildmlearn.videocollection"; //TODO:: make unique
    public static final String PATH_VIDEOS = "videos";
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final class Videos implements BaseColumns {

        public static final String TABLE_NAME = "videos";

        public static final String TITLE = "title";
        public static final String DESCRIPTION = "description";
        public static final String LINK = "link";
        public static final String THUMBNAIL_URL = "thumbnail_url";

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_VIDEOS).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_VIDEOS;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_VIDEOS;

        public static Uri buildVideosUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        //content://org....../videos/videoId
        public static Uri buildVideosUriWithVideoId(String videoId) {
            return CONTENT_URI.buildUpon().appendPath(videoId).build();
        }

        public static Uri buildVideoUri() {
            return CONTENT_URI.buildUpon().build();
        }

        public static String getIDFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }

    }
}
