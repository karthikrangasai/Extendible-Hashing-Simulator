import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import javax.swing.*;
import ExtendibleHashingScheme.*;

/* <applet code="EHS_Main" width="720" height="480"> */

public class EHS{
	public static void addComponentsToPane(Container pane) {
        pane.setLayout(null);

		// int blockingFactor, globalDepth, localDepth, m, key;
		// Directory fileDirectory;
		java.util.regex.Pattern regexPattern = java.util.regex.Pattern.compile("[0-9]+");
		int blockingFactor = 3;
		int globalDepth = 2;
		int localDepth = 1;
		int m = 10;
		Directory fileDirectory = new Directory(globalDepth, localDepth, blockingFactor);

		JLabel heading = new JLabel("Extendible Hashing Simulator");
		JLabel details = new JLabel("Blocking Factor = " + blockingFactor + ", Initial Global Depth = " + globalDepth + ", Initial Local Depth = " + localDepth + ", m = " + m);
		JLabel inputLabel = new JLabel("Please Enter Key Here:");
		JLabel errorLabel = new JLabel("Error Label");
		JLabel hashValueLabel = new JLabel("Hash value :");
		JLabel hashValue = new JLabel();
		JLabel bucketNameLabel = new JLabel("Bucket is:");
		JLabel bucketName = new JLabel();
		String ex = "-------------------------------------------\n|         000  |          B00: [28, 54, 98]  |\n|         001  |          B01: [15, 35, 0]  |\n|         010  |         B010: [2, 72, 0]  |\n|         011  |          B11: [43, 27, 0]  |\n|         100  |          B00: [28, 54, 98]  |\n|         101  |          B01: [15, 35, 0]  |\n|         110  |         B110: [66, 86, 0]  |\n|         111  |          B11: [43, 27, 0]  |\n-------------------------------------------";
		// JLabel info = new JLabel("<html>" + ex.replaceAll("\n", "<br />") + "<html />");
		
		JLabel info  = new JLabel();

		JTextField input = new JTextField("");;

        JButton insert = new JButton("INSERT");
        JButton search = new JButton("SEARCH");
        JButton delete = new JButton("DELETE");

		Insets insets = pane.getInsets();
        Dimension size;// = b1.getPreferredSize();

        pane.add(heading);
		heading.setBounds(250, 40, 250, 30);
        
		pane.add(details);
		details.setBounds(100, 80, 600, 20);
        
		pane.add(inputLabel);
		inputLabel.setBounds(200, 120, 175, 20);
		
		pane.add(input);
		input.setBounds(375, 120, 70, 20);
		
		pane.add(insert);
		insert.setBounds(175, 150, 100, 30);
		insert.addActionListener( new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					String inputStr = input.getText();
					System.out.println(">> Inserting key " + inputStr);
					java.util.regex.Matcher matchInput = regexPattern.matcher(inputStr);
					if(matchInput.matches()){
						errorLabel.setText("");
						int key = Integer.parseInt(inputStr);
						fileDirectory.insertKey(key);
						hashValue.setText(fileDirectory.hashvalue);
						bucketName.setText(fileDirectory.currentBucket);
						info.setText(fileDirectory.guiOutput());
						System.out.println(fileDirectory);
					} else {
						info.setText("Please enter a proper input !!");
						System.out.println("Please enter a proper input !!");
					}
				}
			}
		);
        
		pane.add(search);
		search.setBounds(300, 150, 100, 30);
		search.addActionListener( new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					String inputStr = input.getText();
					System.out.println(">> Searching key " + inputStr);
					java.util.regex.Matcher matchInput = regexPattern.matcher(inputStr);
					if(matchInput.matches()){
						errorLabel.setText("");
						int key = Integer.parseInt(inputStr);
						boolean keyPresent = fileDirectory.searchKey(key);
						hashValue.setText(fileDirectory.hashvalue);
						if(keyPresent){
							bucketName.setText(fileDirectory.currentBucket);
							info.setText("Key is present in the bucket above.");
							System.out.println("Key is present");
						} else {
							bucketName.setText("Key doesn't exist.");
							info.setText("");
							System.out.println("Key is not present");
						}
					} else {
						info.setText("Please enter a proper input !!");
						System.out.println("Please enter a proper input !!");
					}
				}
			}
		);
        
		pane.add(delete);
		delete.setBounds(425, 150, 100, 30);
		delete.addActionListener( new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					String inputStr = input.getText();
					System.out.println(">> Inserting key " + inputStr);
					java.util.regex.Matcher matchInput = regexPattern.matcher(inputStr);
					if(matchInput.matches()){
						errorLabel.setText("");
						int key = Integer.parseInt(inputStr);
						fileDirectory.deleteKey(key);
						hashValue.setText(fileDirectory.hashvalue);
						bucketName.setText(fileDirectory.currentBucket);
						info.setText(fileDirectory.guiOutput());
						System.out.println(fileDirectory);
						// boolean keyPresent = fileDirectory.deleteKey(key);
						// hashValue.setText(fileDirectory.hashvalue);
						// if(keyPresent){
						// 	bucketName.setText(fileDirectory.currentBucket);
						// 	info.setText(fileDirectory.guiOutput());
						// } else {
						// 	bucketName.setText("Key doesn't exist.");
						// }
					} else {
						info.setText("Please enter a proper input !!");
						System.out.println("Please enter a proper input !!");
					}
				}
			}
		);
        
		pane.add(hashValueLabel);
		hashValueLabel.setBounds(150, 200, 100, 30);
        
		pane.add(hashValue);
		hashValue.setBounds(250, 200, 100, 30);

		pane.add(bucketNameLabel);
        bucketNameLabel.setBounds(400, 200, 75, 30);

		pane.add(bucketName);
        bucketName.setBounds(480, 200, 150, 30);

		pane.add(info);
		info.setBounds(150, 250, 400, 150);

        
		// Set bounds = x, y, width, height
        // b1.setBounds(25 + insets.left, 5 + insets.top, size.width, size.height);
        // size = b2.getPreferredSize();
        // b2.setBounds(55 + insets.left, 40 + insets.top, size.width, size.height);
        // size = b3.getPreferredSize();
        // b3.setBounds(150 + insets.left, 15 + insets.top, size.width + 50, size.height + 20);
    }
	
	
	

	private static void createAndShowGUI(){
        JFrame frame = new JFrame("Extendible Hashing Simulator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        addComponentsToPane(frame.getContentPane());

        Insets insets = frame.getInsets();
        // frame.setSize(300 + insets.left + insets.right, 125 + insets.top + insets.bottom);
		frame.setSize(720, 480);
        frame.setVisible(true);
	}

	public static void main(String[] args){
		try{
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					createAndShowGUI();
				}
			});
		} catch(Exception e){
			e.printStackTrace();
		}
		

		
		// Directory fileDirectory = new Directory(globalDepth, localDepth, blockingFactor);
		// System.out.println(fileDirectory);

		// int[] keys = {2, 28, 43, 15, 66, 27, 86, 54, 35, 98, 72};
		// System.out.println(Arrays.toString(keys));
		// // int[] keys = {12, 19, 10, 34, 23, 42, 86};
		// for(int key : keys){
		// 	fileDirectory.insertKey(key);
		// }
		// System.out.println("After the all Keys are done:\n" + fileDirectory);
	}
}