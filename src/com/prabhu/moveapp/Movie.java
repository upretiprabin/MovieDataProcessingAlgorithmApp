package com.prabhu.moveapp;

import java.util.ArrayList;

/**
 * Created by basnetPrabhu on 9/9/17.
 * 
 * Class to read/write movie data from/into memory.
 * Carries fields to hold data in memory, external file locations
 * containing data.
 *
 */
public class Movie {
	
	private static final String inputFileName = "MovieData.txt";
	private static final String distInfoFileName = "DistributorInfo.txt";
	private static final String outputFileName = "NewMovieData.txt";
	
	private ArrayList<ArrayList<String>> movieDataList;
	
	private ArrayList<ArrayList<String>> distributorDataList;

	public ArrayList<ArrayList<String>> getDistributorDataList() {
		return distributorDataList;
	}

	public void setDistributorDataList(ArrayList<ArrayList<String>> distributorDataList) {
		this.distributorDataList = distributorDataList;
	}

	public ArrayList<ArrayList<String>> getMovieDataList() {
		return movieDataList;
	}

	public void setMovieDataList(ArrayList<ArrayList<String>> movieDataList) {
		this.movieDataList = movieDataList;
	}
	
	public static String getOutputFileName() {
		return outputFileName;
	}

	public static String getInputFileName() {
		return inputFileName;
	}

	public static String getDistinfofilename() {
		return distInfoFileName;
	}

}
