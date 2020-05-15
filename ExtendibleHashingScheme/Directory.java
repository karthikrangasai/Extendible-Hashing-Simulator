package ExtendibleHashingScheme;

import java.util.*;
/**
* This class represents the Directory Structure of the Extendible Hashing Scheme.
* This class has a list of records that maps the Global Hash Value to the Bucket.
* All the buckets of the Scheme are a part of the Data File.
* @author Karthik Rangasai
*/
public class Directory{
	/**
	* Global Depth of the Hashing Scheme.
	*/
	private int globalDepth;
	/**
	* The integer value K used in the Hash Function h(K) = key mod K.
	*/
	private int modGrouper;
	/**
	* The file which contains all the buckets.
	*/
	private DataFile dataFile;
	/**
	* The list of records for that maps the global hash value to the respective records.
	*/
	private ArrayList<DirectoryRecord> fileDirectory;
	/**
	* 
	*/
	private String currentBucket;
	/**
	* 
	*/
	private String hashvalue;

	public Directory(int globalDepth, int localDepth, int blockingFactor, int modGrouper){
		this.globalDepth = globalDepth;
		this.modGrouper = modGrouper;
		this.dataFile = new DataFile(blockingFactor, localDepth);
		this.fileDirectory = new ArrayList<DirectoryRecord>();
		
		// System.out.println("Global Depth is: " + globalDepth);
		// System.out.println("Number of records are " + (int)Math.pow(2, globalDepth));
		// for(int i=0; i<(int)Math.pow(2, globalDepth); i++){
		// 	String bitString = Utils.generateBitString(i);
		// 	System.out.println("Bit String is " + bitString + " trimmed to "  + Utils.trimToDepth(bitString, globalDepth));
		// }

		this.generateFileDirectory();
		// this.fileDirectory.add(new DirectoryRecord(true, dataFile.get(1)));
		System.out.println(Math.pow(2, globalDepth) + " " + this.dataFile.numberOfBuckets());
	}

	/**
	* Inserts the key into the respective Bucket.
	* @param key Key value for inserting
	*/
	public void insertKey(int key){
		// System.out.println("For key " + key + ": ");
		if(!this.searchKey(key)){
			int h = key % modGrouper;
			// System.out.println(key + " % " + modGrouper + " = Hashed key " + h + ": ");
			DirectoryRecord r = this.getDirectoryRecord(h);
			Bucket b = r.getBucket();
			this.hashvalue = Utils.trimToDepth(Utils.generateBitString(h), this.globalDepth);
			this.currentBucket = b.toString();
			// System.out.println("	Bucket is: " + b);
			if(b.canInsertKey()){
				// System.out.println("	Can Insert Key.");
				// System.out.println("My Current Index is: " + b.getCurrentIndex());
				b.insertKey(key);
				// System.out.println("	After inserting Key: " + b);
			} else {
				// System.out.println("	Cannot Insert Key.");
				if(b.getLocalDepth() >= globalDepth){
					++this.globalDepth;
					this.generateFileDirectory();
				}
				Bucket newBucket = b.splitBucket(modGrouper);
				// System.out.println("	Old Bucket: " + b);
				// System.out.println("	New Bucket: " + newBucket);
				if(b.getLocalHashValue().equals(Utils.trimToDepth(Utils.generateBitString(h), b.getLocalDepth()))){
					b.insertKey(key);
				} else {
					newBucket.insertKey(key);
				}
				// System.out.println("	Old Bucket: " + b);
				// System.out.println("	New Bucket: " + newBucket);
				this.dataFile.addBucket(newBucket);
				this.generateFileDirectory();
			}
		} else {
			System.out.println("Key is already present!!");
		}
		
	}

	/**
	* Searches for the presence of the key any Bucket.
	* @param key Key value for searching
	* @return boolean
	*/
	public boolean searchKey(int key){
		// System.out.println("For key " + key + ": ");
		int h = key % modGrouper;
		DirectoryRecord r = this.getDirectoryRecord(h);
		Bucket b = r.getBucket();
		this.hashvalue = Utils.trimToDepth(Utils.generateBitString(h), this.globalDepth);
		this.currentBucket = b.toString();
		// System.out.println("	Bucket is: " + b);
		if(b.isKeyPresent(key)){
			return true;
		}
		return false;
	}

	/**
	* Deletes the key from the respective Bucket.
	* @param key Key value for deleting
	*/
	public void deleteKey(int key){
		int h = key % modGrouper;
		DirectoryRecord r = this.getDirectoryRecord(h);
		Bucket b = r.getBucket();
		this.hashvalue = Utils.trimToDepth(Utils.generateBitString(h), this.globalDepth);
		this.currentBucket = b.toString();
		// System.out.println("	Bucket is: " + b);
		if(b.isKeyPresent(key)){
			// System.out.println("		Key " + key + " is present in " + b);
			b.deleteKey(key);
			// System.out.println("		After deleting:" + b);
			if(b.isEmpty()){
				// System.out.println("		" + b + " is empty.");
				// Get sister bucket - string size same and differ by one bit and sister bucket not more then (3/4) full
				Bucket sisterBucket = this.dataFile.getSisterBucket(b);
				// System.out.println("		" + sisterBucket + " is the sister bucket.");
				// Merge if total elements in both < blockingF actor
				// System.out.println("	Buckets can merge: " + b.canMergeWith(sisterBucket));
				if((sisterBucket != null) && (b.canMergeWith(sisterBucket))){
					this.dataFile.mergeBuckets(b, sisterBucket);
					this.generateFileDirectory();
				}
			}
			// else{
			// 	System.out.println("		" + Arrays.toString(b.getKeys()) + " at currently " + b.getCurrentIndex());
			// }
		}
		// System.out.println(this.dataFile);
	}

	/**
	* Fetches the Directory Record that has the key value in it's Bucket.
	* @param key Key value
	* @return Directory Record
	*/
	private DirectoryRecord getDirectoryRecord(int key){
		String bitString = Utils.trimToDepth(Utils.generateBitString(key), this.globalDepth);
		for(DirectoryRecord r : this.fileDirectory){
			if(bitString.equals(r.getGlobalHashValue())){
				return r;
			}
		}
		return null;
	}

	/**
	* Regenarates the Data File. Used whenever we change the Structure.
	*/
	private void generateFileDirectory(){
		this.fileDirectory.clear();
		// this.globalDepth = this.dataFile.getMaxLocalDepth();
		for(int i=0; i<Math.pow(2, this.globalDepth); i++){
			String bitString = Utils.generateBitString(i);
			this.fileDirectory.add(new DirectoryRecord(Utils.trimToDepth(bitString, globalDepth), dataFile.getBucket(bitString)));
		}
	}

	/**
	* String representation of the Directory.
	*/
	public String toString(){
		StringBuilder s = new StringBuilder("-------------------------------------------\n");
		for(DirectoryRecord r : this.fileDirectory){
			s.append(r.toString() + "  \n");
			// s.append("\n-------------------------------------------\n");
		}
		s.append("-------------------------------------------");
		return s.toString();
	}

	/**
	* String representation of the Directory for the GUI output.
	* @return String
	*/
	public String guiOutput(){
		StringBuilder s = new StringBuilder("-------------------------------------------<br/>");
		for(DirectoryRecord r : this.fileDirectory){
			s.append(r.toString() + "  <br />");
			// s.append("\n-------------------------------------------\n");
		}
		s.append("-------------------------------------------");
		return "<html>" + s.toString() + "<html/>";
	}

	public String getCurrentBucket(){
		return this.currentBucket;
	}

	public String getHashValue(){
		return this.hashvalue;
	}

}