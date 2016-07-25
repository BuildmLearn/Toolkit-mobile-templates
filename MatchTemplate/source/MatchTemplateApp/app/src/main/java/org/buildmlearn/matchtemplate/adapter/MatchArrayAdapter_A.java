package org.buildmlearn.matchtemplate.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import org.buildmlearn.matchtemplate.Constants;
import org.buildmlearn.matchtemplate.R;

/**
 * Created by Anupam (opticod) on 11/6/16.
 */
public class MatchArrayAdapter_A extends CursorAdapter {

    public MatchArrayAdapter_A(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_info_a, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);
        return view;
    }

    /*
     *  This is where we fill-in the views with the contents of the cursor.
    */
    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        final ViewHolder viewHolder = (ViewHolder) view.getTag();

        String title = cursor.getString(Constants.COL_MATCH_A);
        viewHolder.text.setText(title);

    }

    public static class ViewHolder {

        public final TextView text;

        public ViewHolder(View view) {
            text = (TextView) view.findViewById(R.id.text);
        }
    }
}
