package org.buildmlearn.matchtemplate.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import org.buildmlearn.matchtemplate.R;
import org.buildmlearn.matchtemplate.adapter.MatchArrayAdapter_A;
import org.buildmlearn.matchtemplate.adapter.MatchArrayAdapter_B;
import org.buildmlearn.matchtemplate.data.MatchDb;
import org.buildmlearn.matchtemplate.data.MatchModel;

import java.util.ArrayList;

/**
 * Created by Anupam (opticod) on 11/6/16.
 */
public class MainActivityFragment extends Fragment {

    private static final String SELECTED_KEY_A = "selected_position_a";
    private static final String SELECTED_KEY_B = "selected_position_b";
    private static final int INFO_LOADER = 0;

    private MatchArrayAdapter_A matchListAdapterA;
    private MatchArrayAdapter_B matchListAdapterB;
    private int mPositionA = ListView.INVALID_POSITION;
    private int mPositionB = ListView.INVALID_POSITION;
    private ListView listViewA;
    private ListView listViewB;

    private View clickSource;
    private View touchSource;

    private int offset = 0;
    private ArrayList<MatchModel> matchListA;
    private ArrayList<MatchModel> matchListB;
    private View rootView;
    private MatchDb db;

    public MainActivityFragment() {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("matchListA", matchListA);
        outState.putParcelableArrayList("matchListB", matchListB);
        if (mPositionA != ListView.INVALID_POSITION) {
            outState.putInt(SELECTED_KEY_A, mPositionA);
        }
        if (mPositionB != ListView.INVALID_POSITION) {
            outState.putInt(SELECTED_KEY_B, mPositionB);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null || !savedInstanceState.containsKey("matchListA") || !savedInstanceState.containsKey("matchListB")) {
            matchListA = new ArrayList<>();
            matchListB = new ArrayList<>();
        } else {
            matchListA = savedInstanceState.getParcelableArrayList("matchListA");
            matchListB = savedInstanceState.getParcelableArrayList("matchListB");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        db = new MatchDb(getContext());
        db.open();

        Cursor cursorMatchesA = db.getMatchCursor();
        Cursor cursorMatchesB = db.getMatchCursor();

        matchListAdapterA =
                new MatchArrayAdapter_A(
                        getActivity(), cursorMatchesA);

        matchListAdapterB =
                new MatchArrayAdapter_B(
                        getActivity(), cursorMatchesB);

        rootView = inflater.inflate(R.layout.fragment_main, container, false);

        listViewA = (ListView) rootView.findViewById(R.id.list_view_match_A);
        listViewB = (ListView) rootView.findViewById(R.id.list_view_match_B);

        listViewA.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (touchSource == null) {
                    touchSource = v;
                }
                if (v == touchSource) {
                    listViewB.dispatchTouchEvent(event);
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        clickSource = v;
                        touchSource = null;
                    }
                }
                return false;
            }
        });

        listViewB.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (touchSource == null) {
                    touchSource = v;
                }
                if (v == touchSource) {
                    listViewA.dispatchTouchEvent(event);
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        clickSource = v;
                        touchSource = null;
                    }
                }
                return false;
            }
        });

        listViewA.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (parent == clickSource) {
                    //do something
                }
            }
        });


        listViewB.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (parent == clickSource) {
                    //do something
                }
            }
        });

        listViewA.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (view == clickSource) {
                    listViewB.setSelectionFromTop(firstVisibleItem, view.getChildAt(0).getTop() + offset);
                }
            }
        });

        listViewB.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (view == clickSource) {
                    listViewA.setSelectionFromTop(firstVisibleItem, view.getChildAt(0).getTop() + offset);
                }
            }
        });

        listViewA.setAdapter(matchListAdapterA);
        listViewB.setAdapter(matchListAdapterB);

        listViewA.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                mPositionA = position;
            }
        });

        listViewA.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                mPositionB = position;
            }
        });

        View header_A = getLayoutInflater(savedInstanceState).inflate(R.layout.fragment_main_header_a, null);
        View footer_A = getLayoutInflater(savedInstanceState).inflate(R.layout.fragment_main_footer_a, null);
        listViewA.addHeaderView(header_A);
        listViewA.addFooterView(footer_A);

        View header_B = getLayoutInflater(savedInstanceState).inflate(R.layout.fragment_main_header_b, null);
        View footer_B = getLayoutInflater(savedInstanceState).inflate(R.layout.fragment_main_footer_b, null);
        listViewB.addHeaderView(header_B);
        listViewB.addFooterView(footer_B);

        if (savedInstanceState != null && savedInstanceState.containsKey(SELECTED_KEY_A) && savedInstanceState.containsKey(SELECTED_KEY_B)) {
            mPositionA = savedInstanceState.getInt(SELECTED_KEY_A);
            mPositionB = savedInstanceState.getInt(SELECTED_KEY_B);
        }

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        db.close();
    }

}
