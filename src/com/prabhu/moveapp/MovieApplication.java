package com.prabhu.moveapp;

import java.awt.*;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.border.TitledBorder;
import java.io.*;
import javax.swing.border.LineBorder;

/**
 * Created by basnetPrabhu on 9/9/17.
 * 
 * MainApplication contains all gui components
 * and all methods. Application starts from here.
 */
public class MovieApplication extends JFrame {

	private Movie movieObj;
	private JPanel contentPane;
	private JTextArea textArea;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MovieApplication frame = new MovieApplication();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MovieApplication() {
		initContentPane();
		makeMenus();
		movieObj = new Movie();
	}


	/**
	 * Render Menu Bar with menus and menu items
	 * Contains listener for all menus
	 * */
	public void makeMenus() {
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnMovieData = new JMenu("Movie Data");
		menuBar.add(mnMovieData);

		JMenuItem menuItmRead = new JMenuItem("Read Data");
		menuItmRead.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String status = readMovieDataFromFile(movieObj,Movie.getInputFileName(),false);
				String distReadStatus = readMovieDataFromFile(movieObj,Movie.getDistinfofilename(),true);
				if(!status.contains("Error") && !distReadStatus.contains("Error")) {
					JOptionPane.showMessageDialog(contentPane, status, "Read Movie Data", JOptionPane.INFORMATION_MESSAGE);
				}else {
					JOptionPane.showMessageDialog(contentPane, status, "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		mnMovieData.add(menuItmRead);

		JMenuItem menuItmSave = new JMenuItem("Save Data");
		menuItmSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int statusCode = saveDataToFile(movieObj);
				if(statusCode == 1) {
					JOptionPane.showMessageDialog(contentPane, "Movie Data save successful.", "Save Movie Data", JOptionPane.INFORMATION_MESSAGE);
				}else if(statusCode == -1){
					JOptionPane.showMessageDialog(contentPane, "No data found!", "Error", JOptionPane.ERROR_MESSAGE);	
				}else if(statusCode == -2){
					JOptionPane.showMessageDialog(contentPane, "File not found!", "Error", JOptionPane.ERROR_MESSAGE);
				}
				else {
					JOptionPane.showMessageDialog(contentPane, "Error occurred while saving data!", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		mnMovieData.add(menuItmSave);

		JMenu mnActivities = new JMenu("Activities");
		menuBar.add(mnActivities);

		JMenuItem menuItmList = new JMenuItem("List Movies");
		menuItmList.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(isDataEmpty(movieObj.getMovieDataList())) {
					makeTable(getColumnNames(false),movieObj.getMovieDataList());	
				}else {
					JOptionPane.showMessageDialog(contentPane, "No Records Found!", "Error", JOptionPane.ERROR_MESSAGE);
				}
				
			}
		});
		mnActivities.add(menuItmList);

		JMenuItem menuItmSort = new JMenuItem("Sort Movies By Name");
		menuItmSort.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(isDataEmpty(movieObj.getMovieDataList())) {
					movieObj.setMovieDataList(quickSort(movieObj.getMovieDataList()));
					makeTable(getColumnNames(false),movieObj.getMovieDataList());
				}else {
					JOptionPane.showMessageDialog(contentPane, "No Records Found!", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		mnActivities.add(menuItmSort);

		JMenuItem menuItmSrch = new JMenuItem("Search Movie By Ticket Price");
		menuItmSrch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String ticketPrice = JOptionPane.showInputDialog(contentPane,"Enter movie ticket price for searching");
				boolean invalid = false;
				String message = "";
				Double ticketPrcDbl = null;
				try {
					if(!ticketPrice.trim().isEmpty()) {
						ticketPrcDbl = Double.parseDouble(ticketPrice);
						if(ticketPrcDbl<2) {
							message = "Please enter a value greater than 2!";
							invalid = true;
						}
					}else {
						message = "Ticket price cannot be empty!";
						invalid = true;
					}
				}catch(Exception ex) {
					invalid = true;
					message = "Please enter a valid ticket price!";
					ex.printStackTrace();
				}
				if(invalid) {
					JOptionPane.showMessageDialog(contentPane,message,"Error",JOptionPane.ERROR_MESSAGE);
				}else {
					if(isDataEmpty(movieObj.getMovieDataList())) {
						ArrayList<ArrayList<String>> searchResult = linearSearch(movieObj.getMovieDataList(),ticketPrcDbl);
						if(isDataEmpty(searchResult)){
							makeTable(getColumnNames(false),searchResult);
						}else{
							JOptionPane.showMessageDialog(contentPane, "No Records Found!", "Error", JOptionPane.ERROR_MESSAGE);
						}
					}else {
						JOptionPane.showMessageDialog(contentPane, "No Records Found!", "Error", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		mnActivities.add(menuItmSrch);

		JMenu menuDistributors = new JMenu("Distributors");
		menuBar.add(menuDistributors);

		JMenuItem menuItmDtls = new JMenuItem("Details");
		menuItmDtls.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(isDataEmpty(movieObj.getDistributorDataList())) {
					makeTable(getColumnNames(true),movieObj.getDistributorDataList());	
				}else {
					JOptionPane.showMessageDialog(contentPane, "No Records Found!", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		menuDistributors.add(menuItmDtls);

		JMenu mnExit = new JMenu("Exit");
		menuBar.add(mnExit);

		JMenuItem menuItmExit = new JMenuItem("Exit");
		menuItmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int confirmStatus = JOptionPane.showConfirmDialog(contentPane, "Are you sure want to exit?","Exit",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
				if(confirmStatus==0)
					System.exit(JFrame.EXIT_ON_CLOSE);
			}
		});
		mnExit.add(menuItmExit);
	}


	/**
	 * checks whether data is loaded into object
	 *
	 * @return boolean status
	 * **/
	private boolean isDataEmpty(ArrayList<ArrayList<String>> data){
		if(data!=null && !data.isEmpty()){
			return  true;
		}else{
			return  false;
		}
	}

	/**
	 * render border after column and at the end
	 * **/
	private void displayBorder(){
		for(int i = 0; i<120;i++){
			textArea.append("=");
		}
	}


	/**
	 * render formatted data in text area
	 * */
	private void makeTable(String[] columnNames, ArrayList<ArrayList<String>> data){
		//clear text editor
		textArea.setText("");
		for(int i = 0; i<columnNames.length;i++){
			String output = String.format("%-25s",columnNames[i]); //output is formatted with equal widths
			textArea.append(output);
		}

		textArea.append("\n");
		displayBorder();
		textArea.append("\n");
		for(int i = 0; i<data.size();i++){
			for(int j = 0; j<data.get(0).size();j++){
				String output = String.format("%-25s",data.get(i).get(j));
				textArea.append(output);
			}
			textArea.append("\n");
		}
		displayBorder();
		textArea.setCaretPosition(0); //brings cursor to initial position to render scroll bar at initial position

	}


	/**
	 * Initializes the frame with textarea and other initial GUI Components
	 * */
	private void initContentPane() {
		setTitle("Movie Data Processing Algorithm");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 920, 500);

		contentPane = new JPanel();
		contentPane.setBackground(UIManager.getColor("Button.background"));
		contentPane.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), "DisplayArea", TitledBorder.LEADING, TitledBorder.ABOVE_TOP, null, new Color(128, 128, 128)));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		textArea = new JTextArea();
		textArea.setFont(new Font("monospaced",Font.PLAIN,14));
		textArea.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(textArea);
		scrollPane.getHorizontalScrollBar().setValue(10);
		contentPane.add(scrollPane, BorderLayout.CENTER);
	}

	/**
	 * reads movie data from file and stores in memory,
	 * data is stored in Collection Object.
	 * @return      A String containing file process status.
	 * */
	private String readMovieDataFromFile(Movie movie, String fileName, boolean isDist){
		ArrayList<ArrayList<String>> dataObj = new ArrayList<ArrayList<String>>();
		String status;
		File file = new File(fileName);
		try{
			BufferedReader bufferedReader = new BufferedReader(new FileReader(file.getAbsolutePath()));
			String currentLine;
				while ((currentLine = bufferedReader.readLine()) != null) {
					ArrayList<String> dataElem = new ArrayList<String>();
					String[] eachElem = currentLine.split(",");
					for (String element : eachElem) {
						dataElem.add(element);
					}
					dataObj.add(dataElem);
				}
				if (!isDist) {
					movie.setMovieDataList(dataObj);
				} else {
					movie.setDistributorDataList(dataObj);
				}
			status = "Movie Data read successful.";
		}catch (FileNotFoundException e){
			status = "Error : File Not Found!";
		}catch (IOException e){
			e.printStackTrace();
			status = "Error occurred while reading data!";
		}
		return status;
	}
	
	 /**
     * writes movie data from memory to new file
     * a new file with processed movie data is created.
     * */
    private int saveDataToFile(Movie movie){
        int status;
        ArrayList<ArrayList<String>> movieDataList = movie.getMovieDataList();
        if(movieDataList!=null && !movieDataList.isEmpty()){
            File newFile = new File(Movie.getOutputFileName());
            try{
                if(newFile.exists())
                    newFile.delete();
                BufferedWriter buffwriter = new BufferedWriter(new FileWriter(newFile));
                int count = 0;
                for (ArrayList<String> record : movieDataList) {
                    count ++;
                    try{
                    	buffwriter.append(record.get(0));
                    	buffwriter.append(",");
                    	buffwriter.append(record.get(3));
                    	buffwriter.append(",");
                    	buffwriter.append(record.get(4));
                        buffwriter.append("\n");
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
                buffwriter.write("Total Number of Movies: "+count+"\n");
                buffwriter.flush();
                buffwriter.close();
                System.out.println("New File "+Movie.getOutputFileName()+" created!");
                status = 1;
            }catch (FileNotFoundException e){
                status = -2;
            }catch (IOException e){
                e.printStackTrace();
                status = 0;
            }
        }else{
            status = -1;
        }
        return status;
    }


    /**
	 * retrieve column names to render in table
	 *
	 * @return columnNames set in data Object
	 * */
    private String[] getColumnNames(boolean isDist) {
    	String[] columnNames = movieObj.getMovieDataColumns();
    	String[] distColumnNames = movieObj.getMovieDistributorsColumns();
    	if(isDist) {
    		return distColumnNames;
    	}
    	return columnNames;
    }

    /**
     * implements linearsearch on objects
     * linear search yields faster result as
     * index is not provided.
     *
     * @return    An ArrayList of matched sets.
     * */
	private ArrayList<ArrayList<String>> linearSearch(ArrayList<ArrayList<String>> data, Double ticketPrice) {
		ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
		try{
			for (ArrayList<String> record : data) {
				if (Double.parseDouble(record.get(4))==ticketPrice) {
					result.add(record);
				}
			}
		}catch (Exception e){
			e.printStackTrace();
			return null;
		}

		return result;
	}

	/**
	 * implements quicksort algorithm on objects
	 * uses divide and conquer rule to sort the elements
	 * data is compared with a pivot element and divided into two halfs
	 * uses recursion
	 *
	 * @return    An ArrayList of sorted sets by name.
	 * */
	private ArrayList<ArrayList<String>> quickSort(ArrayList<ArrayList<String>> list){
		ArrayList<ArrayList<String>> sorted = null;
		try{
			if (list.isEmpty())
				return list;
			ArrayList<ArrayList<String>> smaller = new ArrayList<ArrayList<String>>();
			ArrayList<ArrayList<String>> greater = new ArrayList<ArrayList<String>>();
			ArrayList<String> pivot = list.get(0);
			int i;
			ArrayList<String> j;
			for (i=1;i<list.size();i++)
			{
				j=list.get(i);
				if (compareNames(j.get(0),pivot.get(0))<1){
					smaller.add(j);
				}
				else{
					greater.add(j);
				}
			}
			smaller=quickSort(smaller);
			greater=quickSort(greater);
			smaller.add(pivot);
			smaller.addAll(greater);
			sorted = smaller;
		}catch(Exception e){
			e.printStackTrace();
		}

		return sorted;
	}

	/**
	 * compares strings alphabetically
	 * uses compareTo method
	 * @return int value 0, >1 or <1
	 * */
	private int compareNames (String str1, String str2){
		return str1.compareTo(str2);
	}

}