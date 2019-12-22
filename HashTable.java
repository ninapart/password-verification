import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Locale;

public class HashTable {
	//FIELDS
	private int m;//length of table
	private HashList[] hashList;//array of hash lists
	int p = 15486907;
	
	//CONSTRUCTOR
	
	//initiates array's length m and creates hash lists array . 
	public HashTable(String m) {
		try {
			this.m = Integer.parseInt(m);
			if (this.m<=0)
				throw new RuntimeException("m must be a positive integer");
			hashList = new HashList[this.m];
			for(int i=0; i<this.m; i++) {
				hashList[i] = new HashList();
			}
		}
		catch(NumberFormatException e){
			throw new RuntimeException("input m is not a valid number");
		}
	}	
	
	// METHODS
	
	//computes the hash function on the inputed password key.
	// returns index in hash list 
	private int hashFunction(int key){
		if (key < 0)
			throw new RuntimeException("illegal input - negative integer");
		return key % m;
	}
	
	// transforms the password into a key and calculates hash list index by hash function computation.
	// returns true if the hash list at index place contains the password 
	public boolean isRejected(String data) {
		if (data == null)
			throw new RuntimeException("illegal input - null instead of String");
		int key = strToKey(data);
		int index = hashFunction(key);
		return hashList[index].contains(data);
	}
	
	//inserts bad passwords in the hash list at the index computed by hash function method
	public void updateTable(String file) {
		try{
			BufferedReader br = new BufferedReader(new FileReader(file));
			
			for (String data = br.readLine(); data!= null; data = br.readLine()){
				int key = strToKey(data);
				int index = hashFunction(key);
				hashList[index].addFirst(data);
			}
			br.close();
		}
		catch(IOException ex) {
			throw new RuntimeException("Bad file");
		}
	}
	
	//activates horner's rule with base 256 on a string and returns key as integer 
	private int strToKey (String data){
		if (data == null)
			throw new RuntimeException("illegal input - null instead of String");
		int d = data.length();
		int x = 256;
		long key = (long) data.charAt(0);
		for(int i = 1; i < d; i++){
			key = ( data.charAt(i) + (key*x)) % p;
		}
		return (int) key;
	}
	

    //checks for each password if it is rejected or not by the hash table and returns the execution time
	public String getSearchTime(String file) {
		double searchTime=0;
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			
			long startTime = System.nanoTime();
			
			for (String data = br.readLine(); data!= null; data = br.readLine()){
				isRejected(data);
			}
			long endTime = System.nanoTime();
			
			searchTime = (double) (endTime - startTime)/1000000;
			br.close();
		}
		catch(IOException ex) {
			throw new RuntimeException("Bad file");
		}
	
		return String.format(Locale.ROOT,"%.4f", searchTime);
	}
}
