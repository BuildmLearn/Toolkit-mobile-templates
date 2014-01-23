package com.buildmlearn.spellingstemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.content.Context;

public class DataManager {
	private static DataManager instance = null;
	private String mTitle = null;
	private String mAuthor = null;
	private BufferedReader br;
	private ArrayList<WordModel> mList = new ArrayList<WordModel>();
	private int countIndex = 0;
	private int countCorrect = 0;
	private int countWrong = 0;

	public static DataManager getInstance() {
		if (instance == null) {
			instance = new DataManager();
		}
		return instance;
	}

	public void readContent(Context myContext) {
		reset();
		try {
			br = new BufferedReader(new InputStreamReader(myContext.getAssets()
					.open("spelling_content.txt"))); // throwing a
														// FileNotFoundException?
			mTitle = br.readLine();
			mAuthor = br.readLine();
			String text;
			while ((text = br.readLine()) != null) {
				if (text.contains("==")) {
					String[] spelling = text.split("==");
					int startIndex = spelling[0].length() + 2;
					String des = text.substring(startIndex);
					mList.add(new WordModel(spelling[0], des));
				}
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

	public ArrayList<WordModel> getList() {

		return mList;
	}

	public String getTitle() {
		return mTitle;
	}

	public String getAuthor() {
		return mAuthor;
	}

	public int getActiveWordCount() {
		return countIndex;

	}

	public void increaseCount() {
		countIndex++;
	}

	public void incrementCorrect() {
		countCorrect++;
	}

	public int getCorrect() {
		return countCorrect;
	}

	public int getWrong() {
		return countWrong;
	}

	public void incrementWrong() {
		countWrong++;
	}

	public void reset() {
		countCorrect = 0;
		mList.clear();
		countIndex = 0;
		countWrong = 0;
	}

}
