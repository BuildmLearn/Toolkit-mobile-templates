package com.tft.libs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class DetailView extends Activity{
	GlobalData gd;
	BufferedReader br;
	TextView title;
	TextView details;
	Button back;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        				setContentView(R.layout.detail);
       
        gd = GlobalData.getInstance();
        
        title = (TextView) findViewById(R.id.titleText);
        details = (TextView) findViewById(R.id.detailText);
        
        
        title.setText(gd.iTitle);
        details.setText(ReadFile(DetailView.this, gd.iTitle + ".txt"));
        
        
    }

    public String ReadFile(Context myContext, String file) {
  		try {
  			br = new BufferedReader(new InputStreamReader(myContext.getAssets().open(file))); // throwing a FileNotFoundException?
  			String text = "";
  			String tmp = "";
  			while ((tmp = br.readLine()) != null) {
  				text = text + tmp;
  			}
  			return text;
  		} catch (IOException e) {
  			e.printStackTrace();
  		} finally {
  			try {
  				br.close(); // stop reading
  			} catch (IOException ex) {
  				ex.printStackTrace();
  			}
  		}
		return "";
  	}
}
