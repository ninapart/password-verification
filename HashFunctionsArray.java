import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/* Data structure of hash functions
 * The constructor receive as input a file of hash functions represented by 2 numbers alpha_beta, m value and p value.
 * It inserts every hash function in an array.
 */

public class HashFunctionsArray {
	
	public HashFunction[] data; 

	public HashFunctionsArray(String hashFile, int mSize, int p) {
		try{
			BufferedReader br = new BufferedReader(new FileReader(hashFile)); 
		    int numOfFunctions = (int) br.lines().count();
		    br.close();
			data = new HashFunction[numOfFunctions];
			
			br = new BufferedReader(new FileReader(hashFile)); 
			for (int i = 0; i<numOfFunctions; i++ ){
				String curr = br.readLine();
				String[] arr = curr.split("_");
				int alpha = Integer.parseInt(arr[0]);
				int beta = Integer.parseInt(arr[1]);
				data[i] = new HashFunction(alpha,beta,mSize,p) ;
			}
			br.close();	
		}
		catch(IOException ex) {
		}
	}

}
