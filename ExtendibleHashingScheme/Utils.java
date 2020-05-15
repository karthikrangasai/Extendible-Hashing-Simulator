package ExtendibleHashingScheme;

import java.util.*;
/**
* This class has static methods that are used as helper functions and 
* don't belong to any specific class.
* @author Karthik Rangasai
*/
class Utils{
	/**
	* Generates a 10 bit long Bit String of the given integer.
	* @param n The integer that has to be converted to a bit string.
	* @return String
	*/
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

	/**
	* Trims the 10 bit long Bit String to a given depth from the LSB.
	* @param bitString The bit string we wish to trim.
	* @param depth The number of bits from LSB we should consider.
	* @return String
	*/
	protected static String trimToDepth(String bitString, int depth){
		if(bitString.length() > depth){
			return bitString.substring((bitString.length() - depth), bitString.length());
		} else if (bitString.length() == depth){
			return bitString;
		}
		return null;
	}
}