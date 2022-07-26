package com.example.wordReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class WordReader {
	private String path;
	
	public WordReader(String path) {
		this.path = path;
	}
	
	public ArrayList<String> readFileIntoArray(ArrayList<String> wordList) {
		File file = new File(getPath());
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
			String str;
			while((str = in.readLine()) != null) {
				wordList.add(str);
			}
			in.close();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return wordList;
	}
	
	public static void main(String[] args) {
		System.out.println(System.getProperty("user.dir"));
	}
	

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

}
