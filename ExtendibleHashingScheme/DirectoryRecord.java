package ExtendibleHashingScheme;

import java.util.*;
/**
* The DirectoryRecord represents the HashMap used to map the
* global hash value to it's corresponding Bucket.
* @author Karthik Rangasai
*/
class DirectoryRecord{
	/** 
	* The bit string of the key generated of length equal to global depth value number of bits.
	*/
	StringBuilder globalHashValue;
	/** 
	* The bit string of the key generated of length equal to global depth value number of bits.
	*/
	Bucket bucket;
	
	protected DirectoryRecord(boolean bit, Bucket bucket){
		this.globalHashValue = new StringBuilder();
		if(bit){
			this.globalHashValue.insert(0, 1);
		} else {
			this.globalHashValue.insert(0, 0);
		}
		this.bucket = bucket;
	}

	protected DirectoryRecord(String bitString, Bucket bucket){
		this.globalHashValue = new StringBuilder(bitString);
		this.bucket = bucket;
	}

	protected DirectoryRecord(String bitString, boolean bit, Bucket bucket){
		this.globalHashValue = new StringBuilder(bitString);
		if(bit){
			this.globalHashValue.insert(0, 1);
		} else {
			this.globalHashValue.insert(0, 0);
		}
		this.bucket = bucket;
	}

	/**
	* Checks equality based on the Global Hash Value.
	*/
	public boolean equals(Object O){
		DirectoryRecord dR = (DirectoryRecord)O;
		return this.globalHashValue.toString().equals(dR.getGlobalHashValue());
	}

	/**
	* String representation for the Directory Record.
	*/
	public String toString(){
		StringBuilder s = new StringBuilder();
		int len = 10 - this.globalHashValue.length();
		s.append("|  ");
		for(int i=0; i<len; i++){
			s.append(" ");
		}
		s.append(this.globalHashValue.toString());
		s.append("  |  ");
		s.append(this.bucket.toString());
		// len = 11 - this.bucket.bucketName.length();
		// for(int i=0; i<len; i++){
		// 	s.append(" ");
		// }
		// s.append(this.bucket.bucketName);
		// s.append("  |");
		return s.toString(); // 3 + 10 + 5 + 11 + 3 = 6 + 15 + 11 = 6 + 26 = 32
	}

	/**
	* Returns the Global Hash Value.
	* @return String
	*/
	protected String getGlobalHashValue(){
		return this.globalHashValue.toString();
	}
	/**
	* Returns the bucket belonging to the record.
	* @return Bucket
	*/
	protected Bucket getBucket(){
		return this.bucket;
	}
}