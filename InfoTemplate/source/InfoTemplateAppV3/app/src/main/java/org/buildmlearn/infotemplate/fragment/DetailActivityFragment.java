package org.buildmlearn.infotemplate.fragment;

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

import org.buildmlearn.infotemplate.Constants;
import org.buildmlearn.infotemplate.R;
import org.buildmlearn.infotemplate.activities.DetailActivity;
import org.buildmlearn.infotemplate.data.InfoContract;
import org.buildmlearn.infotemplate.data.InfoDb;

/**
 * Created by Anupam (opticod) on 11/6/16.
 */
public class DetailActivityFragment extends Fragment implements LoaderCallbacks<Cursor> {

    private static final int DETAIL_LOADER = 0;
    private final ContentValues infoValues;

    private View rootView;
    private String info_Id;
    private InfoDb db;

    public DetailActivityFragment() {
        setHasOptionsMenu(true);
        infoValues = new ContentValues();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        if (arguments != null) {
            info_Id = arguments.getString(Intent.EXTRA_TEXT);
        }
        rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        db = new InfoDb(getContext());

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(DETAIL_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (null != info_Id) {
            switch (id) {
                case DETAIL_LOADER:

                    return new CursorLoader(getActivity(), null, Constants.INFO_COLUMNS, null, null, null) {
                        @Override
                        public Cursor loadInBackground() {
                            db.open();
                            return db.getInfoCursorById(Integer.parseInt(info_Id));
                        }
                    };
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

                db.open();

                final long numColumns = db.getCount();

                if (Integer.parseInt(info_Id) == numColumns) {

                    rootView.findViewById(R.id.next).setVisibility(View.INVISIBLE);
                    rootView.findViewById(R.id.exit).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Intent.ACTION_MAIN);
                            intent.addCategory(Intent.CATEGORY_HOME);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    });

                } else {

                    rootView.findViewById(R.id.exit).setVisibility(View.INVISIBLE);
                    rootView.findViewById(R.id.next).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            long nextInfoId = Integer.parseInt(info_Id) + 1;

                            Intent intent = new Intent(getActivity(), DetailActivity.class)
                                    .setType("text/plain")
                                    .putExtra(Intent.EXTRA_TEXT, String.valueOf(nextInfoId));
                            startActivity(intent);

                        }
                    });
                }

                if (Integer.parseInt(info_Id) == 1) {

                    rootView.findViewById(R.id.previous).setVisibility(View.INVISIBLE);

                } else {

                    rootView.findViewById(R.id.previous).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            int prevInfoId = Integer.parseInt(info_Id) - 1;

                            if (prevInfoId >= 1) {
                                Intent intent = new Intent(getActivity(), DetailActivity.class)
                                        .setType("text/plain")
                                        .putExtra(Intent.EXTRA_TEXT, String.valueOf(prevInfoId));
                                startActivity(intent);
                            }
                        }
                    });
                }

                if (infoValues.size() == 0) {
                    infoValues.put(InfoContract.Info._ID, data.getString(Constants.COL_ID));
                    infoValues.put(InfoContract.Info.TITLE, data.getString(Constants.COL_TITLE));
                    infoValues.put(InfoContract.Info.DESCRIPTION, data.getString(Constants.COL_DESCRIPTION));
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown Loader");
        }
        db.close();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

}
