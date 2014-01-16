package com.tft.libs;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.view.View;

public class TFTActivity extends Activity {
	BufferedReader br;
	List<String> stringList  = new ArrayList<String>();;
	GlobalData gd;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        				setContentView(R.layout.main);
//        setTitle("Tools for Teachers");
        gd = GlobalData.getInstance();
        
        				
        ListView listView = (ListView) findViewById(R.id.mylist);
//        String[] values = new String[] { "Android", "iPhone", "WindowsMobile",
//          "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
//          "Linux", "OS/2" };
        
        ReadIndex(TFTActivity.this);

        // First paramenter - Context
        // Second parameter - Layout for the row
        // Third parameter - ID of the TextView to which the data is written
        // Forth - the Array of data
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
          android.R.layout.simple_list_item_1, android.R.id.text1, stringList);

        // Assign adapter to ListView
        listView.setAdapter(adapter); 

        listView.setOnItemClickListener(new OnItemClickListener() {
        	  public void onItemClick(AdapterView<?> parent, View view,
        	    int position, long id) {
        	  
        		  gd.iTitle = stringList.get(position);
        		  gd.iSelectedIndex = position;
        		  Intent myIntent = new Intent(view.getContext(),DetailView.class);
                  startActivityForResult(myIntent, 0);
        	  }
        	}
        ); 
        
    }
    
    public void ReadIndex(Context myContext) {
		try {
			br = new BufferedReader(new InputStreamReader(myContext.getAssets().open("index.txt"))); // throwing a FileNotFoundException?
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
    
}