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