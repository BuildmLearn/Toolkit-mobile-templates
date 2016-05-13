package org.buildmlearn.videocollection.fragment;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.buildmlearn.videocollection.Constants;
import org.buildmlearn.videocollection.R;
import org.buildmlearn.videocollection.data.VideoContract;

/**
 * Created by Anupam (opticod) on 13/5/16.
 */
public class DetailActivityFragment extends Fragment implements LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = DetailActivityFragment.class.getSimpleName();

    private static final int DETAIL_LOADER = 0;
    private ContentValues videoValues;

    private View rootView;
    private String video_Id;

    public DetailActivityFragment() {
        setHasOptionsMenu(true);
        videoValues = new ContentValues();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        if (arguments != null) {
            video_Id = arguments.getString(Intent.EXTRA_TEXT);
        }
        rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(DETAIL_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (null != video_Id) {
            switch (id) {
                case DETAIL_LOADER:

                    return new CursorLoader(
                            getActivity(),
                            VideoContract.Videos.buildMoviesUriWithMovieId(video_Id),
                            Constants.VIDEO_COLUMNS,
                            null,
                            null,
                            null
                    );

            }
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, final Cursor data) {
        if (!data.moveToFirst()) {
            return;
        }
        switch (loader.getId()) {
            case DETAIL_LOADER:
                String title = data.getString(Constants.COL_TITLE);

                ((TextView) rootView.findViewById(R.id.title))
                        .setText(title);

                String description = data.getString(Constants.COL_DESCRIPTION);

                ((TextView) rootView.findViewById(R.id.description))
                        .setText(description);

                if (videoValues.size() == 0) {
                    videoValues.put(VideoContract.Videos._ID, data.getString(Constants.COL_ID));
                    videoValues.put(VideoContract.Videos.TITLE, data.getString(Constants.COL_TITLE));
                    videoValues.put(VideoContract.Videos.DESCRIPTION, data.getString(Constants.COL_DESCRIPTION));
                    videoValues.put(VideoContract.Videos.LINK, data.getString(Constants.COL_LINK));
                    videoValues.put(VideoContract.Videos.THUMBNAIL_URL, data.getString(Constants.COL_THUMBNAIL_URL));
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown Loader");
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }
}
