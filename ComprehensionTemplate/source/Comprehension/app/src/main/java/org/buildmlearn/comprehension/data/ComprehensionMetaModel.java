package org.buildmlearn.comprehension.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Anupam (opticod) on 1/6/16.
 */
public class ComprehensionMetaModel implements Parcelable {
    public static final String TITLE_TAG = "meta_title";
    public static final String PASSAGE_TAG = "meta_passage";
    public static final String TIMER_TAG = "meta_timer";
    private static final String ROOT_TAG = "meta_details";
    private String title;
    private String passage;
    private long time;

    public ComprehensionMetaModel(String title, String passage, long time) {
        this.title = title;
        this.passage = passage;
        this.time = time;
    }

    private ComprehensionMetaModel(Parcel in) {
        this.title = in.readString();
        this.passage = in.readString();
        this.time = in.readLong();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(passage);
        dest.writeLong(time);
    }

    public String getTitle() {
        return this.title;
    }

    public String getPassage() {
        return this.passage;
    }

    public long getTime() {
        return this.time;
    }

}
