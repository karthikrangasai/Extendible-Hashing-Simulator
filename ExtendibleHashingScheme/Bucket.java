package ExtendibleHashingScheme;

import java.util.*;
/**
* This class represents the concept Bucket from Extendible Hashing.
* @author Karthik Rangasai
*/
class Bucket{
	/**
	* The String used to indentify different buckets.
	*/
	protected String bucketName;
	/**
	* The integer value that determines which bits
	* to consider from the LSB.
	*/
	private int localDepth;
	/**
	* The bit string of the key generated of length equal to local depth value number of bits.
	*/
	private StringBuilder localHashValue;
	/**
	* The numbers of keys that can be stored.
	*/
	private int blockingFactor;
	/**
	* The array that stores the keys.
	*/
	private int[] keys;
	/**
	* Points to the last added keys.
	*/
	private int currentIndex;

	protected Bucket(int localDepth, int blockingFactor, boolean bit){
		this.localDepth = localDepth;
		this.localHashValue = new StringBuilder();
		if(bit){
			this.localHashValue.insert(0, 1);
		} else {
			this.localHashValue.insert(0, 0);
		}
		this.blockingFactor = blockingFactor;
		this.keys = new int[blockingFactor];
		this.currentIndex = -1;
		this.bucketName = "B" + this.localHashValue.toString();
	}

	protected Bucket(int localDepth, String localHashValue, int blockingFactor, boolean bit){
		this.localDepth = localDepth;
		this.localHashValue = new StringBuilder(localHashValue);
		if(bit){
			this.localHashValue.insert(0, 1);
		} else {
			this.localHashValue.insert(0, 0);
		}
		this.blockingFactor = blockingFactor;
		this.keys = new int[blockingFactor];
		this.currentIndex = -1;
		this.bucketName = "B" + this.localHashValue.toString();
	}

	/**
	* Checks if the "keys" array is can accomadate anymore keys.
	* @return boolean
	*/
	protected boolean canInsertKey(){
		// return !(this.currentIndex == this.blockingFactor);
		return ((this.currentIndex + 1) < this.blockingFactor);
	}

	/**
	* Checks if the "keys" array is empty.
	* @return boolean
	*/
	protected boolean isEmpty(){
		return (this.currentIndex < 0);
	}

	/**
	* Updates the currentIndex pointer and inserts thet key to the array.
	* @param key Integer value to add to the keys array
	*/
	protected void insertKey(int key){
		try {
			++this.currentIndex;
			this.keys[this.currentIndex] = key;
		} catch(ArrayIndexOutOfBoundsException e){
			--this.currentIndex;
			System.out.println("Please choose appropriate sequence of keys or change the blocking factor.");
		}
	}

	/**
	* Splits the bucket the into 2 new buckets when it can't accomadate any new keys.
	* @return The newly formed after splitting.
	*/
	protected Bucket splitBucket(){
		++this.localDepth;
		Bucket newBucket = new Bucket(this.localDepth, this.localHashValue.toString(), this.blockingFactor, true);
		this.localHashValue.insert(0, 0);
		this.updateBucketName();
		// Distribue Keys
		// this.distributeKeys(newBucket, 1);
		return newBucket;
	}
	
	/**
	* Splits the bucket the into 2 new buckets when it can't accomadate any new keys.
	* @param hashComputer The integer used in the hash function to compute the hash value.
	* @return The newly formed after splitting.
	*/
	protected Bucket splitBucket(int hashComputer){
		++this.localDepth;
		Bucket newBucket = new Bucket(this.localDepth, this.localHashValue.toString(), this.blockingFactor, true); // new Bucket(2, "1", 3, true) : B1 -> B11
		this.localHashValue.insert(0, 0); // -> B01
		this.updateBucketName();
		this.distributeKeys(newBucket, hashComputer);
		return newBucket;
	}

	/**
	* Splits the keys among the two buckets based on their local hash value.
	* @param newBucket The newly formed Bucket from the current Bucket.
	* @param hashComputer The integer used in the hash function to compute the hash value.
	*/
	private void distributeKeys(Bucket newBucket, int hashComputer){
		ArrayList<Integer> oldKeys = new ArrayList<Integer>();
		for(int k : this.keys){
			oldKeys.add(k);
		}
		Iterator<Integer> oldKeysIt = oldKeys.iterator();
		while(oldKeysIt.hasNext()){
			int key = oldKeysIt.next();
			int hashedKey = key % hashComputer;
			String keyHash = Utils.trimToDepth(Utils.generateBitString(hashedKey), this.localDepth);
			if(!this.localHashValue.toString().equals(keyHash)){
				if(newBucket.canInsertKey()){
					newBucket.insertKey(key);
					oldKeysIt.remove();
					--this.currentIndex;
				}
			}
		}
		Arrays.fill(this.keys, 0);
		for(int i=0; i<oldKeys.size(); i++){
			this.keys[i] = oldKeys.get(i);
		}
		// System.out.println("		Current Index of old Bucket: " + this.currentIndex);
		// System.out.println("		Current Index of new Bucket: " + newBucket.getCurrentIndex());
	}

	/**
	* Checks for the first occurence input key value is present in the Bucket.
	* @param key Integer to check the existence for.
	* @return boolean
	*/
	protected boolean isKeyPresent(int key){
		for(int i=0; i<blockingFactor; i++){
			if(this.keys[i] == key){
				return true;
			}
		}
		return false;
	}

	/**
	* Splits the keys among the two buckets based on their local hash value.
	* @param key Removes the key from the "keys" array in the Bucket.
	*/
	protected void deleteKey(int key){
		System.out.println("		Deleting " + key);
		int index = -1;
		for(int i=0; i<this.blockingFactor; i++){
			if(this.keys[i] == key){
				index = i;
				break;
			}
		}
		for(int i=index; i<(this.blockingFactor-1); i++){
			this.keys[i] = this.keys[i+1];
			++index;
		}
		for(int i=index; i<this.blockingFactor; i++){
			this.keys[i] = 0;
		}
		// System.out.println("			Curr Index b " + this.currentIndex);
		--this.currentIndex;
		// System.out.println("			Curr Index a " + this.currentIndex);
		// ArrayList<Integer> keys = new ArrayList<Integer>();
		// for(int k : this.keys){
		// 	keys.add(k);
		// }
		// keys.remove(Integer.parseInt("" + key));
		// if(keys.size() == 0){
		// 	System.out.println("Need to merge the fckin buckets");
		// } else {
		// 	Arrays.fill(this.keys, 0);
		// 	for(int i=0; i<keys.size(); i++){
		// 		this.keys[i] = keys.get(i);
		// 	}
		// }
	}

	/**
	* Checks if the input Bucket can be merged with the current Bucket.
	* @param mergeTo The Bucket to merge the current Bucket with.
	* @return boolean
	*/
	protected boolean canMergeWith(Bucket mergeTo){
		return ((mergeTo.getLocalDepth() > 1) && (mergeTo.getCurrentIndex()/this.blockingFactor <= 0.5));
	}

	/**
	* Changes the current Bucket to it's previous version i.e. before Splitting.
	*/
	protected void merge(){
		--this.localDepth;
		this.localHashValue.deleteCharAt(0);
		this.updateBucketName();
	}

	/**
	* Updates the Bucket name using the localHashValue member variable.
	*/
	private void updateBucketName(){
		this.bucketName = "B" + this.localHashValue.toString();
	}

	/**
	* String representation of the Bucket.
	* @return String
	*/
	public String toString(){
		StringBuilder s = new StringBuilder();
		int len = 11 - this.bucketName.length();
		for(int i=0; i<len; i++){
			s.append(" ");
		}
		s.append(this.bucketName + ": ");
		// s.append(Arrays.toString(this.keys));
		for(int key : this.keys){
			if(key == 0){
				s.append("-");
			} else {
				s.append(" " + key);
			}
		}
		// s.append(" ]");
		return s.toString();
	}

	/**
	* Tests the equality for two Buckets using the Bucket name.
	*/
	public boolean equals(Object O){
		Bucket b = (Bucket)O;
		return this.localHashValue.toString().equals(b.getLocalHashValue());
	}

	/**
	* Returns the local depth value.
	* @return Integer
	*/
	protected int getLocalDepth(){
		return this.localDepth;
	}
	/**
	* Returns the local hash value.
	* @return String
	*/
	protected String getLocalHashValue(){
		return this.localHashValue.toString();
	}
	/**
	* Returns the blocking factor.
	* @return Integer
	*/
	protected int getBlockingFactor(){
		return this.blockingFactor;
	}
	/**
	* Returns the array of keys.
	* @return Array of Integesr
	*/
	protected int[] getKeys(){
		return this.keys;
	}
	/**
	* Returns the pointer to the last entered key.
	* @return Integer index value
	*/
	protected int getCurrentIndex(){
		return this.currentIndex;
	}

}