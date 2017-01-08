package org.buildmlearn.videocollection.fragment;

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
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import org.buildmlearn.videocollection.Constants;
import org.buildmlearn.videocollection.R;
import org.buildmlearn.videocollection.activities.DetailActivity;
import org.buildmlearn.videocollection.activities.LastActivity;
import org.buildmlearn.videocollection.data.VideoContract;
import org.buildmlearn.videocollection.data.VideoDb;

/**
 * @brief Fragment containing details of video items in video collection template's app.
 *
 * Created by Anupam (opticod) on 13/5/16.
 */
public class DetailActivityFragment extends Fragment implements LoaderCallbacks<Cursor> {

    private static final int DETAIL_LOADER = 0;
    private final ContentValues videoValues;

    private View rootView;
    private WebView player;
    private String video_Id;
    private VideoDb db;

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

        db = new VideoDb(getContext());
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
        if (null != video_Id) {
            switch (id) {
                case DETAIL_LOADER:

                    return new CursorLoader(getActivity(), null, Constants.VIDEO_COLUMNS, null, null, null) {
                        @Override
                        public Cursor loadInBackground() {
                            return db.getVideoCursorById(Integer.parseInt(video_Id));
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
                String title = data.getString(Constants.COL_TITLE);

                ((TextView) rootView.findViewById(R.id.title))
                        .setText(title);

                String description = data.getString(Constants.COL_DESCRIPTION);

                ((TextView) rootView.findViewById(R.id.description))
                        .setText(Html.fromHtml(description));

                player = (WebView) rootView.findViewById(R.id.player);

                player.setWebChromeClient(new WebChromeClient());
                player.setWebViewClient(new WebViewClient());

                player.getSettings().setJavaScriptEnabled(true);
                player.getSettings().setAppCacheEnabled(true);
                player.getSettings().setDomStorageEnabled(true);
                player.getSettings().setAllowFileAccess(true);
                player.getSettings().setLoadWithOverviewMode(true);
                player.getSettings().setUseWideViewPort(true);
                player.getSettings().setTextZoom(140);

                String link = data.getString(Constants.COL_LINK);
                if (link.contains("youtube.com")) {

                    int pos = link.indexOf("watch?v=");
                    String videoId = link.substring(pos + 8);

                    String playVideo = "<html><body style=\"margin: 0; padding: 0\">" +
                            " <iframe class=\"player\" type=\"text/html\" width=\"100%\" height=\"850\" allowfullscreen src=\"http://www.youtube.com/embed/" + videoId + "\">" +
                            "</body></html>";

                    player.loadData(playVideo, "text/html", "utf-8");

                } else if (link.contains("vimeo.com")) {
                    int pos;

                    if (link.contains("/")) {
                        pos = link.lastIndexOf("/");
                    } else {
                        pos = link.lastIndexOf("\\");
                    }
                    String videoId = link.substring(pos + 1);

                    player.loadUrl("http://player.vimeo.com/video/" + videoId + "?player_id=player&autoplay=1&title=0&byline=0&portrait=0&api=1&maxheight=480&maxwidth=800");

                } else if (link.contains("dailymotion.com")) {
                    int pos;

                    if (link.contains("/")) {
                        pos = link.lastIndexOf("/");
                    } else {
                        pos = link.lastIndexOf("\\");
                    }
                    String videoId = link.substring(pos + 1);

                    String playVideo = "<html><body style=\"margin: 0; padding: 0\">" +
                            " <iframe class=\"player\" type=\"text/html\" width=\"100%\" height=\"850\" src=\"http://www.dailymotion.com/embed/video/" + videoId + "\">" +
                            "</body></html>";

                    player.loadData(playVideo, "text/html", "utf-8");
                } else if(link.contains("metacafe.com")) {

                    String embedlink = link.replaceFirst("metacafe.com/watch","metacafe.com/embed");
                    String playVideo = "<html><body style=\"margin: 0; padding: 0\">" +
                            " <iframe class=\"player\" type=\"text/html\" width=\"100%\" height=\"850\" src=\"" + embedlink + "\">" +
                            "</body></html>";

                    player.loadData(playVideo, "text/html", "utf-8");

                }

                rootView.findViewById(R.id.next).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        long numColumns = db.getCount();

                        long nextVideoId = Integer.parseInt(video_Id) + 1;

                        if (nextVideoId <= numColumns) {
                            Intent intent = new Intent(getActivity(), DetailActivity.class)
                                    .setType("text/plain")
                                    .putExtra(Intent.EXTRA_TEXT, String.valueOf(nextVideoId));
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(getActivity(), LastActivity.class)
                                    .setType("text/plain");
                            startActivity(intent);
                        }
                    }
                });

                if (Integer.parseInt(video_Id) == 1) {

                    rootView.findViewById(R.id.previous).setVisibility(View.INVISIBLE);

                } else {

                    rootView.findViewById(R.id.previous).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            int prevVideoId = Integer.parseInt(video_Id) - 1;

                            if (prevVideoId >= 1) {
                                Intent intent = new Intent(getActivity(), DetailActivity.class)
                                        .setType("text/plain")
                                        .putExtra(Intent.EXTRA_TEXT, String.valueOf(prevVideoId));
                                startActivity(intent);
                            }
                        }
                    });
                }

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
        // This constructor is intentionally empty
    }

    @Override
    public void onPause() {
        super.onPause();
        if (player != null) {
            player.onPause();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (player != null) {
            player.onResume();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        db.close();
    }
}
