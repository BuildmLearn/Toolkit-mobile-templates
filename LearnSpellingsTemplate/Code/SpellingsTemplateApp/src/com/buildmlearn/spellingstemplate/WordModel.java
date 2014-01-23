package com.buildmlearn.spellingstemplate;

public class WordModel {

	private String mWord;
	private String mDescription;
	public WordModel(String word,String description)
	{
		mWord=word;
		mDescription=description;
		
	}
	public String getWord() {
		return mWord;
	}
	public void setWord(String mWord) {
		this.mWord = mWord;
	}
	public String getDescription() {
		return mDescription;
	}
	public void setDescription(String mDescription) {
		this.mDescription = mDescription;
	}
	
}
