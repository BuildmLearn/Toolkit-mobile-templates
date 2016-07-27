package org.buildmlearn.matchtemplate.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.buildmlearn.matchtemplate.Constants;
import org.buildmlearn.matchtemplate.R;
import org.buildmlearn.matchtemplate.activities.DetailActivity;
import org.buildmlearn.matchtemplate.adapter.MatchArrayAdapter_A;
import org.buildmlearn.matchtemplate.adapter.MatchArrayAdapter_B;
import org.buildmlearn.matchtemplate.data.MatchDb;
import org.buildmlearn.matchtemplate.data.MatchModel;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Anupam (opticod) on 24/7/16.
 */
public class MainActivityFragment extends Fragment {

    private static final String SELECTED_KEY_A = "selected_position_a";
    private static final String SELECTED_KEY_B = "selected_position_b";

    private MatchArrayAdapter_A matchListAdapterA;
    private MatchArrayAdapter_B matchListAdapterB;
    private int mPositionA = ListView.INVALID_POSITION;
    private int mPositionB = ListView.INVALID_POSITION;
    private ListView listViewA;
    private ListView listViewB;

    private ArrayList<MatchModel> matchListA;
    private ArrayList<MatchModel> matchListB;
    private MatchDb db;

    private int selectedPositionA = -1;
    private int selectedPositionB = -1;

    private View selectedViewA;
    private View selectedViewB;
    private View clickSourceA;

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

        Cursor cursorMatchesA = db.getRandMatchCursor();
        Cursor cursorMatchesB = db.getRandMatchCursor();

        Cursor meta = db.getMetaCursor();

        if (cursorMatchesA != null) {
            while (cursorMatchesA.moveToNext()) {
                MatchModel match = new MatchModel();
                match.setMatchA(cursorMatchesA.getString(Constants.COL_MATCH_A));
                match.setMatchB(cursorMatchesA.getString(Constants.COL_MATCH_B));
                matchListA.add(match);
            }
            cursorMatchesA.close();
        }

        if (cursorMatchesB != null) {
            while (cursorMatchesB.moveToNext()) {
                MatchModel match = new MatchModel();
                match.setMatchA(cursorMatchesB.getString(Constants.COL_MATCH_A));
                match.setMatchB(cursorMatchesB.getString(Constants.COL_MATCH_B));
                matchListB.add(match);
            }
            cursorMatchesB.close();
        }

        matchListAdapterA =
                new MatchArrayAdapter_A(
                        getActivity(), matchListA);

        matchListAdapterB =
                new MatchArrayAdapter_B(
                        getActivity(), matchListB);

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        listViewA = (ListView) rootView.findViewById(R.id.list_view_match_A);
        listViewB = (ListView) rootView.findViewById(R.id.list_view_match_B);

        handleListViewListeners();

        listViewA.setAdapter(matchListAdapterA);
        listViewB.setAdapter(matchListAdapterB);

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

        handleButtonListener(rootView);

        rootView.findViewById(R.id.check_answer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DetailActivity.class)
                        .setType("text/plain")
                        .putParcelableArrayListExtra(Constants.first_list, matchListA)
                        .putParcelableArrayListExtra(Constants.second_list, matchListB);
                startActivity(intent);
            }
        });

        meta.moveToFirst();
        ((TextView) rootView.findViewById(R.id.first_list_title)).setText(meta.getString(Constants.COL_FIRST_TITLE));
        ((TextView) rootView.findViewById(R.id.second_list_title)).setText(meta.getString(Constants.COL_SECOND_TITLE));

        return rootView;
    }

    private void handleListViewListeners() {
        listViewA.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (parent == clickSourceA) {
                    highlightListA(position, view);
                }
            }
        });

        listViewB.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (parent != clickSourceA) {
                    highlightListB(position, view);
                }
            }
        });

        listViewA.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    clickSourceA = v;
                }
                return false;
            }
        });

        listViewB.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                View v = view.getChildAt(0);
                if (v != null)
                    listViewA.setSelectionFromTop(firstVisibleItem, v.getTop());
            }
        });

    }

    private void handleButtonListener(View rootView) {
        rootView.findViewById(R.id.first_list_up).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedPositionA != -1 && selectedPositionA >= 1 && selectedPositionA < matchListA.size()) {
                    Collections.swap(matchListA, selectedPositionA, selectedPositionA - 1);
                    matchListAdapterA.notifyDataSetChanged();
                    highlightListA(selectedPositionA, listViewA.getChildAt(selectedPositionA));
                }
            }
        });

        rootView.findViewById(R.id.first_list_down).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedPositionA != -1 && selectedPositionA >= 0 && selectedPositionA <= matchListA.size() - 2) {
                    Collections.swap(matchListA, selectedPositionA, selectedPositionA + 1);
                    matchListAdapterA.notifyDataSetChanged();
                    highlightListA(selectedPositionA + 2, listViewA.getChildAt(selectedPositionA + 2));
                }
            }
        });

        rootView.findViewById(R.id.second_list_up).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedPositionB != -1 && selectedPositionB >= 1 && selectedPositionB < matchListB.size()) {
                    Collections.swap(matchListB, selectedPositionB, selectedPositionB - 1);
                    matchListAdapterB.notifyDataSetChanged();
                    highlightListB(selectedPositionB, listViewB.getChildAt(selectedPositionB));
                }
            }
        });

        rootView.findViewById(R.id.second_list_down).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedPositionB != -1 && selectedPositionB >= 0 && selectedPositionB <= matchListB.size() - 2) {
                    Collections.swap(matchListB, selectedPositionB, selectedPositionB + 1);
                    matchListAdapterB.notifyDataSetChanged();
                    highlightListB(selectedPositionB + 2, listViewB.getChildAt(selectedPositionB + 2));
                }
            }
        });
    }

    private void highlightListA(int position, View view) {
        if (position == 0) {
            return;
        }

        if (selectedPositionA == position - 1) {
            selectedPositionA = -1;
            if (view instanceof CardView) {
                ((CardView) view).setCardBackgroundColor(Color.WHITE);
            } else {
                view.setBackgroundResource(0);
            }
        } else {
            if (selectedViewA != null) {
                if (selectedViewA instanceof CardView) {
                    ((CardView) selectedViewA).setCardBackgroundColor(Color.WHITE);
                } else {
                    selectedViewA.setBackgroundResource(0);
                }
            }
            selectedViewA = view;
            selectedPositionA = position - 1;
            if (view instanceof CardView) {
                ((CardView) view).setCardBackgroundColor(Color.LTGRAY);
            } else {
                view.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.color_divider));
            }
        }
    }

    private void highlightListB(int position, View view) {
        if (position == 0) {
            return;
        }

        if (selectedPositionB == position - 1) {
            selectedPositionB = -1;
            if (view instanceof CardView) {
                ((CardView) view).setCardBackgroundColor(Color.WHITE);
            } else {
                view.setBackgroundResource(0);
            }
        } else {
            if (selectedViewB != null) {
                if (selectedViewB instanceof CardView) {
                    ((CardView) selectedViewB).setCardBackgroundColor(Color.WHITE);
                } else {
                    selectedViewB.setBackgroundResource(0);
                }
            }
            selectedViewB = view;
            selectedPositionB = position - 1;
            if (view instanceof CardView) {
                ((CardView) view).setCardBackgroundColor(Color.LTGRAY);
            } else {
                view.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.color_divider));
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        db.close();
    }
}
