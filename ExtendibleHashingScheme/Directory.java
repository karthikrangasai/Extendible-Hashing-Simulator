package ExtendibleHashingScheme;

import java.util.*;
/**
*/
public class Directory{
	public int globalDepth;
	public int modGrouper;
	private DataFile dataFile;
	private ArrayList<DirectoryRecord> fileDirectory;

	public String currentBucket, hashvalue;
	
	public Directory(int globalDepth, int localDepth, int blockingFactor){
		this.globalDepth = globalDepth;
		this.modGrouper = 10;
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

	public void insertKey(int key){
		System.out.println("For key " + key + ": ");
		int h = key % modGrouper;
		DirectoryRecord r = this.getDirectoryRecord(h);
		Bucket b = r.getBucket();
		this.hashvalue = Utils.trimToDepth(Utils.generateBitString(h), this.globalDepth);
		this.currentBucket = b.toString();
		System.out.println("	Bucket is: " + b);
		if(b.canInsertKey()){
			System.out.println("	Can Insert Key.");
			b.insertKey(key);
			System.out.println("	After inserting Key: " + b);
		} else {
			System.out.println("	Cannot Insert Key.");
			// if(this.dataFile.numberOfBuckets() >= Math.pow(2, globalDepth)){
			if(b.getLocalDepth() >= globalDepth){
				++this.globalDepth;
				this.generateFileDirectory();
			}
			Bucket newBucket = b.splitBucket(modGrouper);
			System.out.println("	Old Bucket: " + b);
			System.out.println("	New Bucket: " + newBucket);
			if(b.getLocalHashValue().equals(Utils.trimToDepth(Utils.generateBitString(h), b.getLocalDepth()))){
				b.insertKey(key);
			} else {
				newBucket.insertKey(key);
			}
			System.out.println("	Old Bucket: " + b);
			System.out.println("	New Bucket: " + newBucket);
			this.dataFile.addBucket(newBucket);
			this.generateFileDirectory();
		}
	}

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

	public void deleteKey(int key){
		int h = key % modGrouper;
		DirectoryRecord r = this.getDirectoryRecord(h);
		Bucket b = r.getBucket();
		this.hashvalue = Utils.trimToDepth(Utils.generateBitString(h), this.globalDepth);
		this.currentBucket = b.toString();
		System.out.println("	Bucket is: " + b);
		if(b.isKeyPresent(key)){
			System.out.println("		Key " + key + " is present in " + b);
			b.deleteKey(key);
			System.out.println("		After deleting:" + b);
			if(b.isEmpty()){
				System.out.println("		" + b + " is empty.");
				// Get sister bucket - string size same and differ by one bit and sister bucket not more then (3/4) full
				Bucket sisterBucket = this.dataFile.getSisterBucket(b);
				System.out.println("		" + sisterBucket + " is the sister bucket.");
				// Merge if total elements in both < blockingF actor
				if((sisterBucket != null) && (b.canMergeWith(sisterBucket))){
					this.dataFile.mergeBuckets(b, sisterBucket);
					this.generateFileDirectory();
				}
			}
		}
	}

	private DirectoryRecord getDirectoryRecord(int key){
		String bitString = Utils.trimToDepth(Utils.generateBitString(key), this.globalDepth);
		for(DirectoryRecord r : this.fileDirectory){
			if(bitString.equals(r.getGlobalHashValue())){
				return r;
			}
		}
		return null;
	}

	private void generateFileDirectory(){
		this.fileDirectory.clear();
		for(int i=0; i<Math.pow(2, globalDepth); i++){
			String bitString = Utils.generateBitString(i);
			this.fileDirectory.add(new DirectoryRecord(Utils.trimToDepth(bitString, globalDepth), dataFile.getBucket(bitString)));
		}
	}

	public String toString(){
		StringBuilder s = new StringBuilder("-------------------------------------------\n");
		for(DirectoryRecord r : this.fileDirectory){
			s.append(r.toString() + "  |\n");
			// s.append("\n-------------------------------------------\n");
		}
		s.append("-------------------------------------------");
		return s.toString();
	}

	public String guiOutput(){
		StringBuilder s = new StringBuilder("-------------------------------------------<br/>");
		for(DirectoryRecord r : this.fileDirectory){
			s.append(r.toString() + "  |<br />");
			// s.append("\n-------------------------------------------\n");
		}
		s.append("-------------------------------------------");
		return "<html>" + s.toString() + "<html/>";
	}
}