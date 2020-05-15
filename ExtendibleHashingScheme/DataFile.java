package ExtendibleHashingScheme;

import java.util.*;

/**
* This class represents a file in the OS which is divided into many blocks. 
* Here we have chosen each block to be equal to a bucket.
* @author Karthik Rangasai
*/
class DataFile{
	/**
	* The numbers of keys that can be stored in a Bucket.
	*/
	int blockingFactor;
	/**
	* The list of the buckets that belong to this file.
	*/
	ArrayList<Bucket> dataFileBuckets;

	protected DataFile(int blockingFactor, int localDepth){
		int count = localDepth-1;
		this.blockingFactor = blockingFactor;
		this.dataFileBuckets = new ArrayList<Bucket>();
		Bucket b0 = new Bucket(1, blockingFactor, false);
		Bucket b1 = new Bucket(1, blockingFactor, true);
		// System.out.println("	Adding: " + b0.bucketName);
		this.dataFileBuckets.add(b0);
		// System.out.println("	Adding: " + b1.bucketName);
		this.dataFileBuckets.add(b1);
		for(int i=0; i<count; i++){
			int len = this.dataFileBuckets.size();
			for(int j=0; j<len;j++){
				Bucket b = this.dataFileBuckets.get(j).splitBucket();
				this.dataFileBuckets.add(b);
			}
		}
		this.sortBuckets(this.dataFileBuckets);
	}

	// protected void insertKey(int key){
	// 	String keyBitString = Utils.generateBitString(key);
	// 	for(Bucket b : this.dataFileBuckets){
	// 		if(b.getLocalHashValue().equals(Utils.trimToDepth(keyBitString, b.getLocalDepth()))){
	// 			if(b.canInsertKey()){
	// 				b.insertKey(key);
	// 			} else {
	// 				Bucket newBucket = b.splitBucket();
	// 				if(b.getLocalHashValue().equals(Utils.trimToDepth(keyBitString, b.getLocalDepth()))){
	// 					b.insertKey(key);
	// 				} else {
	// 					newBucket.insertKey(key);
	// 				}
	// 				this.dataFileBuckets.add(newBucket);
	// 				this.sortBuckets(this.dataFileBuckets);
	// 			}
	// 		}
	// 	}
	// }

	/**
	* Adds a new Bucket to the list of the Buckets to this File.
	* @param newBucket Bucket to be added to the list.
	*/
	protected void addBucket(Bucket newBucket){
		this.dataFileBuckets.add(newBucket);
		this.sortBuckets(this.dataFileBuckets);
	}

	/**
	* Merges an Empty Bucket to the Bucket it was split from.
	* @param emptyBucket Empty Bucket that needs to be merged.
	* @param mergeToBucket The Bucket that the empty bucket will be merged with.
	*/
	protected void mergeBuckets(Bucket emptyBucket, Bucket mergeToBucket){
		mergeToBucket.merge();
		this.dataFileBuckets.remove(emptyBucket);
		this.sortBuckets(this.dataFileBuckets);
	}

	/**
	* The numbers of keys that can be stored.
	* @param bitString The bit string of the key generated to search for the bucket it exists in.
	* @return The Bucket which has the key.
	*/
	protected Bucket getBucket(String bitString){
		for(Bucket b : this.dataFileBuckets){
			String trimmedVal = Utils.trimToDepth(bitString, b.getLocalDepth());
			if(trimmedVal.equals(b.getLocalHashValue())){
				return b;
			}
		}
		return null;
	}

	/**
	* Returns the bucket that the input Bucket was split from.
	* @param bucket The bucket whose other split half we want.
	* @return The Bucket which the other split half.
	*/
	protected Bucket getSisterBucket(Bucket bucket){
		String bucketName = bucket.getLocalHashValue();
		char bit = bucketName.charAt(0);
		StringBuilder s = new StringBuilder(bucketName.substring(1));
		if(bit == '0'){
			s.insert(0, 1);
		} else if(bit == '1'){
			s.insert(0, 0);
		} else {
			return null;
		}
		for(Bucket b : this.dataFileBuckets){
			if(b.getLocalHashValue().equals(s.toString())){
				return b;
			}
		}
		return null;
	}

	protected int getMaxLocalDepth(){
		int localDepth = this.dataFileBuckets.get(0).getLocalDepth();
		for(int i=1; i<this.dataFileBuckets.size(); i++){
			if(localDepth < this.dataFileBuckets.get(i).getLocalDepth()){
				localDepth = this.dataFileBuckets.get(i).getLocalDepth();
			}
		}
		return localDepth;
	}

	/** 
	* Sorts the list of Buckets based on the Local Hash Value.
	* @param buckets The list of buckets to be sorted
	*/
	protected static void sortBuckets(ArrayList<Bucket> buckets){
		Collections.sort(buckets, new SortBitString());
	}

	/**
	* Retuns the numbers of buckets currently present in the DataFile.
	* @return Integer
	*/
	protected int numberOfBuckets(){
		return this.dataFileBuckets.size();
	}

	/**
	* String representation of the DataFile.
	*/
	public String toString(){
		StringBuilder s = new StringBuilder();
		for(Bucket b : this.dataFileBuckets){
			s.append("|  ");
			int len = 11 - b.bucketName.length();
			for(int i=0; i<len; i++){
				s.append(" ");
			}
			s.append(b.bucketName + "  | ");
			int[] keys = b.getKeys();
			for(int i=0; i<b.getBlockingFactor(); i++){
				s.append(" " + keys[i] + " ");
			}
			s.append(" |\n");
		}
		return s.toString();
	}
}

/**
 * The helper class to provide the sortBuckets method with a comparator method.
 * @author Karthik Rangasai
 * @see DataFile#sortBuckets
 */
class SortBitString implements Comparator<Bucket>{
	public int compare(Bucket a, Bucket b){
		return a.getLocalHashValue().compareTo(b.getLocalHashValue());
	}
}