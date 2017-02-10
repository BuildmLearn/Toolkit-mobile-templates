/* Copyright (c) 2012, BuildmLearn Contributors listed at http://buildmlearn.org/people/
 All rights reserved.
 
 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions are met:
 
 * Redistributions of source code must retain the above copyright notice, this
 list of conditions and the following disclaimer.
 
 * Redistributions in binary form must reproduce the above copyright notice,
 this list of conditions and the following disclaimer in the documentation
 and/or other materials provided with the distribution.
 
 * Neither the name of the BuildmLearn nor the names of its
 contributors may be used to endorse or promote products derived from
 this software without specific prior written permission.
 
 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. */

package com.buildmlearn.infotemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.util.Linkify;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class TFTActivity extends ActionBarActivity {
    BufferedReader br;
    List<String> stringList = new ArrayList<String>();

    GlobalData gd;

    /**
     * Called when the activity is first created.
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        // setTitle("Tools for Teachers");
        gd = GlobalData.getInstance();
        gd.readXml(getApplicationContext(), "info_content.xml");
        setTitle(gd.mTitle);

        ListView listView = (ListView) findViewById(R.id.mylist);
        // String[] values = new String[] { "Android", "iPhone",
        // "WindowsMobile",
        // "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
        // "Linux", "OS/2" };

        //ReadIndex(TFTActivity.this);

        // First paramenter - Context
        // Second parameter - Layout for the row
        // Third parameter - ID of the TextView to which the data is written
        // Forth - the Array of data
        /*ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.list_item, R.id.text_element, stringList);

*/        // Assign adapter to ListView
        InfoListAdapter adapter = new InfoListAdapter(this);

        listView.setAdapter(adapter);
        adapter.setList(gd.mList);

        listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Intent myIntent = new Intent(view.getContext(),
                        DetailView.class);
                myIntent.putExtra("position", position);
                startActivity(myIntent);
            }
        });
        Toast.makeText(TFTActivity.this, "Author - " + gd.mAuthor, Toast.LENGTH_LONG).show();

    }

    public void ReadIndex(Context myContext) {
        try {
            br = new BufferedReader(new InputStreamReader(myContext.getAssets()
                    .open("index.txt"))); // throwing a FileNotFoundException?
            String text;
            while ((text = br.readLine()) != null) {
                stringList.add(text);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close(); // stop reading
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_info) {

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    TFTActivity.this);

            // set title
            alertDialogBuilder.setTitle("About Us");

            // set dialog message
            alertDialogBuilder
                    .setMessage(getString(R.string.about_us))
                    .setCancelable(false)
                    .setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    dialog.cancel();
                                    dialog.dismiss();
                                }
                            });

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();
            // show it
            alertDialog.show();
            TextView msg = (TextView) alertDialog
                    .findViewById(android.R.id.message);
            Linkify.addLinks(msg, Linkify.WEB_URLS);

            return super.onOptionsItemSelected(item);
        }
        return true;
    }

}