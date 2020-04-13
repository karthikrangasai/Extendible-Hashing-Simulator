package ExtendibleHashingScheme;

import java.util.*;

/**
*/
class DataFile{
	int blockingFactor;
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
		Utils.sortBuckets(this.dataFileBuckets);
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
	// 				Utils.sortBuckets(this.dataFileBuckets);
	// 			}
	// 		}
	// 	}
	// }

	protected void addBucket(Bucket newBucket){
		this.dataFileBuckets.add(newBucket);
		Utils.sortBuckets(this.dataFileBuckets);
	}

	protected void mergeBuckets(Bucket emptyBucket, Bucket mergeToBucket){
		mergeToBucket.merge();
		this.dataFileBuckets.remove(emptyBucket);
		Utils.sortBuckets(this.dataFileBuckets);
	}

	protected Bucket getBucket(String bitString){
		for(Bucket b : this.dataFileBuckets){
			String trimmedVal = Utils.trimToDepth(bitString, b.getLocalDepth());
			if(trimmedVal.equals(b.getLocalHashValue())){
				return b;
			}
		}
		return null;
	}

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

	protected int numberOfBuckets(){
		return this.dataFileBuckets.size();
	}

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