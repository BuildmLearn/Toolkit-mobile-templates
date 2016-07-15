package org.buildmlearn.dictation.fragment;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import org.buildmlearn.dictation.Constants;
import org.buildmlearn.dictation.R;
import org.buildmlearn.dictation.activities.MainActivity;
import org.buildmlearn.dictation.activities.ResultActivity;
import org.buildmlearn.dictation.data.DictContract;
import org.buildmlearn.dictation.data.DictDb;

import java.util.HashMap;
import java.util.Locale;

/**
 * Created by Anupam (opticod) on 4/7/16.
 */
public class DetailActivityFragment extends Fragment implements LoaderCallbacks<Cursor> {

    private static final int DETAIL_LOADER = 0;
    private final ContentValues dictValues;

    private View rootView;
    private String dict_Id;
    private DictDb db;
    private TextToSpeech tts;
    private ProgressDialog progress;

    public DetailActivityFragment() {
        setHasOptionsMenu(true);
        dictValues = new ContentValues();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        if (arguments != null) {
            dict_Id = arguments.getString(Intent.EXTRA_TEXT);
        }
        rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        db = new DictDb(getContext());
        db.open();

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

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

                final EditText passageText = (EditText) rootView.findViewById(R.id.enter_passage);
                final String passage = data.getString(Constants.COL_PASSAGE);

                rootView.findViewById(R.id.ico_speak).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        progress = new ProgressDialog(getActivity());
                        progress.setCancelable(false);
                        progress.setMessage("Loading TTS Engine...");
                        progress.show();

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            String utteranceId = passage.hashCode() + "";
                            tts.speak(passage, TextToSpeech.QUEUE_FLUSH, null, utteranceId);
                        } else {
                            HashMap<String, String> map = new HashMap<>();
                            map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "dict");
                            tts.speak(passage, TextToSpeech.QUEUE_FLUSH, map);
                        }
                    }
                });

                rootView.findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String passage_usr = passageText.getText().toString();
                        Intent intent = new Intent(getActivity(), ResultActivity.class)
                                .setType("text/plain")
                                .putExtra(Intent.EXTRA_TEXT, String.valueOf(dict_Id))
                                .putExtra(Constants.passage, passage_usr);
                        startActivity(intent);

                    }
                });

                rootView.findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent homeIntent = new Intent(getActivity(), MainActivity.class);
                        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(homeIntent);
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
        db.close();
        super.onDestroyView();
    }

    @Override
    public void onPause() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        tts = new TextToSpeech(getContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = tts.setLanguage(Locale.US);
                    if (result == TextToSpeech.LANG_MISSING_DATA
                            || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Toast.makeText(getContext(), "US English is not supported. Playing in device's default installed language.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Initialization Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String utteranceId) {
                progress.dismiss();
            }

            @Override
            public void onDone(String utteranceId) {

            }

            @Override
            public void onError(String utteranceId) {

            }
        });

        SharedPreferences prefs = getActivity().getSharedPreferences("Radio", getContext().MODE_PRIVATE);
        float rate = prefs.getInt("radio_b", 1);
        if (rate == 0) {
            rate = 0.5F;
        }
        tts.setSpeechRate(rate);
    }


}
