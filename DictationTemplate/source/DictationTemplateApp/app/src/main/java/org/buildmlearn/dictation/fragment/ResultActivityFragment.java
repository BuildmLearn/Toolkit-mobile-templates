package org.buildmlearn.dictation.fragment;

import android.app.FragmentManager;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.buildmlearn.dictation.Constants;
import org.buildmlearn.dictation.R;
import org.buildmlearn.dictation.activities.DetailActivity;
import org.buildmlearn.dictation.data.DictContract;
import org.buildmlearn.dictation.data.DictDb;

import java.util.LinkedList;
import java.util.Locale;

import name.fraser.neil.plaintext.diff_match_patch;
import name.fraser.neil.plaintext.diff_match_patch.Diff;

/**
 * @brief Activity for displaying score to user in dictation template's app.
 *
 * Created by Anupam (opticod) on 4/7/16.
 */
public class ResultActivityFragment extends Fragment implements LoaderCallbacks<Cursor> {

    private static final int DETAIL_LOADER = 0;
    private final ContentValues dictValues;

    private View rootView;
    private String dict_Id;
    private String passageEntered;
    private DictDb db;

    public ResultActivityFragment() {
        setHasOptionsMenu(true);
        dictValues = new ContentValues();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        if (arguments != null) {
            dict_Id = arguments.getString(Intent.EXTRA_TEXT);
            passageEntered = arguments.getString(Constants.passage);
        }
        rootView = inflater.inflate(R.layout.fragment_result, container, false);

        db = new DictDb(getContext());
        db.open();

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(DETAIL_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (null != dict_Id) {
            switch (id) {
                case DETAIL_LOADER:

                    return new CursorLoader(getActivity(), null, Constants.DICT_COLUMNS, null, null, null) {
                        @Override
                        public Cursor loadInBackground() {
                            return db.getDictCursorById(Integer.parseInt(dict_Id));
                        }
                    };
                default: //do nothing
                    break;
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

                String passage = data.getString(Constants.COL_PASSAGE);
                passageEntered += " ";
                passage += " ";

                diff_match_patch obj = new diff_match_patch();
                LinkedList<Diff> llDiffs = obj.diff_WordMode(passageEntered, passage);
                String result[] = obj.diff_prettyHtml(llDiffs);

                int numTWords = passage.split(" ").length;
                ((TextView) rootView.findViewById(R.id.score)).setText(String.format(Locale.ENGLISH, "SCORE : %s / %d", result[1], numTWords));

                ((TextView) rootView.findViewById(R.id.checked_text)).setText(Html.fromHtml(result[0]));

                rootView.findViewById(R.id.restart).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        Intent intent = new Intent(getActivity(), DetailActivity.class)
                                .setType("text/plain")
                                .putExtra(Intent.EXTRA_TEXT, dict_Id)
                                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                });

                rootView.findViewById(R.id.exit).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });

                if (dictValues.size() == 0) {
                    dictValues.put(DictContract.Dict._ID, data.getString(Constants.COL_ID));
                    dictValues.put(DictContract.Dict.TITLE, data.getString(Constants.COL_TITLE));
                    dictValues.put(DictContract.Dict.PASSAGE, data.getString(Constants.COL_PASSAGE));
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown Loader");
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // This constructor is intentionally empty
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        db.close();
    }

}
