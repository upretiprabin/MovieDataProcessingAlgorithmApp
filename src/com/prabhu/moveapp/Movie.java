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

	/**contains movie data information in a text file in a CSV format*/
	private static final String inputFileName = "MovieData.txt";

	/**contains movie distributors data information in a text file in a CSV format*/
	private static final String distInfoFileName = "DistributorInfo.txt";

	/**This file is generated each time if data is saved, contains processed data in CSV format*/
	private static final String outputFileName = "NewMovieData.txt";

	/**field to hold movie data*/
	private ArrayList<ArrayList<String>> movieDataList;

	/**field to hold movie distributors data*/
	private ArrayList<ArrayList<String>> distributorDataList;

	/**holds column names for movie data*/
	private String[] movieDataColumns = {"Movie Name","Lead Actor","Lead Actress","Theater Name","TicketPrice"};

	/**holds column names for movie distributor data*/
	private String[] movieDistributorsColumns = {"Distributor Name","Address","Phone Number"};

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

	public String[] getMovieDataColumns() {
		return movieDataColumns;
	}

	public String[] getMovieDistributorsColumns() {
		return movieDistributorsColumns;
	}

}
