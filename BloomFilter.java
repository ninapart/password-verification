import java.io.*;

public class BloomFilter {
	
	//FIELDS
	
	private boolean[] m; //binary array
	private int p = 15486907;//primary number 
	private HashFunctionsArray hashFunctions; //array of the hashFunctions that we use to update the filter
	private int base = 256; //base to the string to key function
	
	//CONSTRUCTOR
   
	//initiates the length of the bloom filter's table m, a binary array of length m and hash functions array
	public BloomFilter(String m, String hashFile) {
		try{
			int mSize = Integer.parseInt(m);
			if (mSize<=0)
				throw new RuntimeException("m must be a positive integer");
			this.m = new boolean[mSize];// binary array initiated with false values
			hashFunctions = new HashFunctionsArray(hashFile, mSize, p);
		}
		catch(NumberFormatException e){
			throw new RuntimeException("input m is not a valid number");
		}
	}
	
	//METHODS
	
	 //reads the file and activates all the hash functions of the array for each password.
	 //each calculated value represents an index in the binary array that is updated as true.
	public void updateTable(String badPasswords) {
		try{
			BufferedReader br = new BufferedReader(new FileReader(badPasswords));
			for (String password = br.readLine(); password!= null; password = br.readLine()){
				int key = strToKey(password);
				for (int i = 0; i<hashFunctions.data.length; i++){
					int index = hashFunctions.data[i].calc(key);
					m[index] = true;
				}
			}
			br.close();
		}
		catch(IOException ex) {
			throw new RuntimeException("Bad file");
		}
	}
	
	
	 // activates horner's rule with base 256 on a string password and returns a key as integer   
	private int strToKey (String password) {
		if (password == null)
			throw new RuntimeException("illegal input - null instead of String");
		int d = password.length();
		long key = (long) password.charAt(0);
		for(int i = 1; i < d; i++){
			key = ( password.charAt(i) + (key*base)) % p;
		}
		return (int) key;
	}
	
	
	 //returns the amount of passwords the bloom filter has rejected
	public String getRejectedPasswordsAmount(String file) {
		int bfCounter=0;
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			for (String data = br.readLine(); data!= null; data = br.readLine()) {
				if (isRejected(data))
					bfCounter++;
			} 
			br.close();
			return Integer.toString(bfCounter);
		}
		catch(IOException ex) {
			throw new RuntimeException("Bad file");
		}
	}
	
	/*for each password the function computes indices with the hash functions and check the value of the binary array at each index place.
	 returns true if the binary array at the index is true otherwise false*/
	public boolean isRejected(String password) {
		if (password == null)
			throw new RuntimeException("illegal input - null instead of String");
		int key = strToKey(password);
		// run over all hash functions and check if one of the cells is false (means the data isn't in the bloom filter)
		for (int i = 0; i<hashFunctions.data.length; i++){
			int index = hashFunctions.data[i].calc(key);
			if (m[index] == false)
				return false;
		}
		return true;
	}
	
	/*counts the amounts of passwords rejected by the bloom filter and by the hash table. 
	 * the false positive percentage is the bloom filter amount less the hash table amount divided by the amount of correct passwords. 
	 * returns a string of the false positive percentage value. */
	public String getFalsePositivePercentage(HashTable hashtable, String file){
		int bfCounter=0, hashCounter=0, passwordsCount=0;
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			for (String data = br.readLine(); data!= null; data = br.readLine()) {
				passwordsCount++;
				if (isRejected(data))
					bfCounter++;
				if (hashtable.isRejected(data))
					hashCounter++;
			} 
			br.close();
			int falseRejected = bfCounter - hashCounter;
			double percent = (double) falseRejected/(passwordsCount-hashCounter);
			return Double.toString(percent);
		}
		catch(IOException ex) {
			throw new RuntimeException("Bad file");
		}
	}
	
	
}
