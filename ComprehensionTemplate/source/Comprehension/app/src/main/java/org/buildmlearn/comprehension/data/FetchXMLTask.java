package org.buildmlearn.comprehension.data;

import android.content.Context;
import android.os.AsyncTask;

/**
 * Created by Anupam (opticod) on 31/5/16.
 */
public class FetchXMLTask extends AsyncTask<String, Void, Void> {

    private final Context mContext;

    public FetchXMLTask(Context context) {
        mContext = context;
    }

    @Override
    protected Void doInBackground(String... params) {

        if (params.length == 0) {
            return null;
        }
        return null;
    }
}