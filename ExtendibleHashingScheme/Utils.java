package ExtendibleHashingScheme;

import java.util.*;
/**
*/
class Utils{
	protected static String generateBitString(int n){
		// Assuming a 10 bit string.
		int num = n;
		StringBuilder bitString = new StringBuilder();
		for(int i=0; i<10; i++){
			bitString.insert(0, num % 2);
			num = num / 2;
		}
		return bitString.toString();
	}

	protected static String trimToDepth(String bitString, int depth){
		if(bitString.length() > depth){
			return bitString.substring((bitString.length() - depth), bitString.length());
		} else if (bitString.length() == depth){
			return bitString;
		}
		return null;
	}

	protected static void sortBuckets(ArrayList<Bucket> buckets){
		Collections.sort(buckets, new SortBitString());
	}
}

class SortBitString implements Comparator<Bucket>{
	public int compare(Bucket a, Bucket b){
		return a.getLocalHashValue().compareTo(b.getLocalHashValue());
	}
}