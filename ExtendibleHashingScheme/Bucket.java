package ExtendibleHashingScheme;

import java.util.*;
/**
*/
class Bucket{
	protected String bucketName;
	private int localDepth;
	private StringBuilder localHashValue;
	private int blockingFactor;
	private int[] keys;
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

	protected boolean canInsertKey(){
		// return !(this.currentIndex == this.blockingFactor);
		return ((this.currentIndex + 1) < this.blockingFactor);
	}

	protected boolean isEmpty(){
		return (this.currentIndex < 0);
	}

	protected void insertKey(int key){
		try {
			++this.currentIndex;
			this.keys[this.currentIndex] = key;
		} catch(ArrayIndexOutOfBoundsException e){
			System.out.println("Please choose appropriate sequence of keys or change the blocking factor.");
		}
	}

	protected Bucket splitBucket(){
		++this.localDepth;
		Bucket newBucket = new Bucket(this.localDepth, this.localHashValue.toString(), this.blockingFactor, true);
		this.localHashValue.insert(0, 0);
		this.updateBucketName();
		// Distribue Keys
		this.distributeKeys(newBucket, 1);
		return newBucket;
	}
	
	protected Bucket splitBucket(int hashComputer){
		++this.localDepth;
		Bucket newBucket = new Bucket(this.localDepth, this.localHashValue.toString(), this.blockingFactor, true);
		this.localHashValue.insert(0, 0);
		this.updateBucketName();
		this.distributeKeys(newBucket, hashComputer);
		return newBucket;
	}

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
	}

	protected boolean isKeyPresent(int key){
		for(int i=0; i<blockingFactor; i++){
			if(this.keys[i] == key){
				return true;
			}
		}
		return false;
	}

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
		--this.currentIndex;
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

	protected boolean canMergeWith(Bucket mergeTo){
		return ((mergeTo.getLocalDepth() > 1) && (mergeTo.getCurrentIndex()/this.blockingFactor <= 0.5));
	}

	protected void merge(){
		--this.localDepth;
		this.localHashValue.deleteCharAt(0);
		this.updateBucketName();
	}

	private void updateBucketName(){
		this.bucketName = "B" + this.localHashValue.toString();
	}

	public String toString(){
		StringBuilder s = new StringBuilder();
		int len = 11 - this.bucketName.length();
		for(int i=0; i<len; i++){
			s.append(" ");
		}
		s.append(this.bucketName + ": ");
		s.append(Arrays.toString(this.keys));
		// for(int key : this.keys){
		// 	s.append(" " + key);
		// }
		// s.append(" ]");
		return s.toString();
	}

	public boolean equals(Object O){
		Bucket b = (Bucket)O;
		return this.localHashValue.toString().equals(b.getLocalHashValue());
	}

	protected int getLocalDepth(){
		return this.localDepth;
	}
	protected String getLocalHashValue(){
		return this.localHashValue.toString();
	}
	protected int getBlockingFactor(){
		return this.blockingFactor;
	}
	protected int[] getKeys(){
		return this.keys;
	}
	protected int getCurrentIndex(){
		return this.currentIndex;
	}

}