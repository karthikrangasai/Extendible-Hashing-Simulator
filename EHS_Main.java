import java.util.*;
import java.io.*;
import ExtendibleHashingScheme.*;

class EHS_Main {
	public static void main(String[] args) throws IOException {
		System.out.println("####################################################################################################");
		System.out.println("##                                  Extendible Hashing Simulator:                                 ##");
		System.out.println("##            This program simulates how extendible hashing scheme used in RDBMS works.           ##");
		System.out.println("####################################################################################################");
		System.out.println("##  Input Format:                                       ##  Input Example:                        ##");
		System.out.println("##  b g l m                                             ##  3 2 1 10                              ##");
		System.out.println("##  k o                                                 ##  28 I                                  ##");
		System.out.println("##  b - Blocking Factor, g - Global Depth               ##  573 I                                 ##");
		System.out.println("##  l - Local Depth, m - No to compute Hash             ##  69 I                                  ##");
		System.out.println("##  k - Key for Input, o - Operation to execute         ##  28 S                                  ##");
		System.out.println("##  Operations are Insert - I, Search - S, Delete - D   ##  573 D                                 ##");
		//System.out.println("##  Type \"quit\" to exit the program                     ##                                        ##");
		System.out.println("#### Type \"quit\" to exit the program ###############################################################");
		
		
		BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
		int blockingFactor, globalDepth, localDepth, m, key;
		Directory fileDirectory = null;

		System.out.print("> Input Initial Parameters: ");
		String s = r.readLine();
		StringTokenizer st = new StringTokenizer(s, " ");
		
		while(st.hasMoreTokens()){
			blockingFactor = Integer.parseInt(st.nextToken());
			globalDepth = Integer.parseInt(st.nextToken());
			localDepth = Integer.parseInt(st.nextToken());
			m = Integer.parseInt(st.nextToken());
			fileDirectory = new Directory(globalDepth, localDepth, blockingFactor, m);
			System.out.println(fileDirectory);
		}

		///////// FOR SCRIPTED INPUT /////////
		int[] keys = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,9,8,7,6,5,4,3,2,1};
		char[] ops = {'I','I','I','I','I','I','I','I','I','I','I','I','I','I','I','D','D','D','D','D','D','D','D','D'};
		if(keys.length == ops.length){
			int size = keys.length;
			int index = 0;
			do{
				key = keys[index];
				switch(ops[index]){
					case 'I': {
						System.out.println("Inserting key " + key + ": ");
						fileDirectory.insertKey(key);
						break;
					}
					case 'S': {
						System.out.println("Searching key " + key + ": ");
						if(fileDirectory.searchKey(key)){
							System.out.println("Key is present");
						} else {
							System.out.println("Key is absent");
						}
						break;
					}
					case 'D': {
						System.out.println("Deleting key " + key + ": ");
						fileDirectory.deleteKey(key);
						break;
					}
					default : {
						System.out.println("Please enter a valid operation.");
						break;
					}
				}
				System.out.println(fileDirectory);
				++index;
			} while(index < size);
		} else {
			System.out.println("Keys and Operations sizes don't match !!");
		}
		

		///////// FOR RUNNING INPUT /////////
		// do{
		// 	System.out.print(">");
		// 	s = r.readLine();
		// 	if(s != null && !s.equals("quit")){
		// 		st = new StringTokenizer(s, " ");
		// 		if(st.countTokens() < 2){
		// 			System.out.println("Syntax Error. Please check input.");
		// 		} else {
		// 			key = Integer.parseInt(st.nextToken());
		// 			switch(st.nextToken()){
		// 				case "I": {
		// 					System.out.println("Inserting key " + key + ": ");
		// 					fileDirectory.insertKey(key);
		// 					break;
		// 				}
		// 				case "S": {
		// 					System.out.println("Searching key " + key + ": ");
		// 					if(fileDirectory.searchKey(key)){
		// 						System.out.println("Key is present");
		// 					} else {
		// 						System.out.println("Key is absent");
		// 					}
		// 					break;
		// 				}
		// 				case "D": {
		// 					System.out.println("Deleting key " + key + ": ");
		// 					fileDirectory.deleteKey(key);
		// 					break;
		// 				}
		// 				default : {
		// 					System.out.println("Please enter a valid operation.");
		// 					break;
		// 				}
		// 			}
		// 		}
		// 	}
		// 	System.out.println(fileDirectory);
		// } while(!s.equals("quit"));

		

		// int blockingFactor = 3;
		// int globalDepth = 2;
		// int localDepth = 1;
		// int m = 10;
		// Directory fileDirectory = new Directory(globalDepth, localDepth, blockingFactor);
		System.out.println(fileDirectory);

		// int[] keys = {2, 28, 43, 15, 66, 27, 86, 54, 35, 98, 72};
		// System.out.println(Arrays.toString(keys));
		// // int[] keys = {12, 19, 10, 34, 23, 42, 86};
		// for(int key : keys){
		// 	fileDirectory.insertKey(key);
		// }
		System.out.println("After the all Keys are done:\n" + fileDirectory);
	}
}