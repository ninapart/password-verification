import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Locale;

public class BTree{
	// FIELDS
	
	private int tValue; //minimum degree
	private BTreeNode root;
	
	//CONSTRUCTOR
		
	// initiates the t value and the root 
	public BTree(String tValue) {
		try {
			this.tValue = Integer.parseInt(tValue);
			if (this.tValue<2)
				throw new RuntimeException("tValue must be a positive integer");
			root = new BTreeNode(this.tValue);
		}
		catch(NumberFormatException e){
			throw new RuntimeException("input tValue is not a valid integer");
		}
	}
	
	//METHODS
	
	// reads the bad passwords file and inserts them into the BTree
	public void createFullTree(String file){
		try{
			BufferedReader br = new BufferedReader(new FileReader(file));
			for (String data = br.readLine(); data!= null; data = br.readLine()){
				insert(data);
			}
			br.close();
		}
		catch(IOException ex) {
			throw new RuntimeException("Bad file");
			}
	}
	

	 // inserts passwords into the BTree - in lower case
	public void insert(String key){
		if (key == null)
			throw new RuntimeException("illegal input - null");
		key = key.toLowerCase();
		// if root has maximum keys, split it
		if (root.n == 2*tValue - 1){
			BTreeNode s = new BTreeNode(this.tValue);
		    s.isLeaf = false;
		    s.n = 0;
		    s.c[0] = root;
		    s.splitChild(0);
		    s.insertNonFull(key);
		    root = s;
		}
		else
			root.insertNonFull(key);
	}
	
	//returns the node and index of the key if exists in the tree, else returns null
	public BTreeSearchResult search(String key){
		if (key == null)
			throw new RuntimeException("illegal input - null");
		return root.search(key);
	}
	
	// prints all the keys (passwords) of the tree in inorder  with the depth of the node that contains the key
	public String toString(){
		if (root == null)
			return "";
		String s = root.toString(0);
		// Remove the last- ,
		if (s.length()>0 && s.charAt(s.length()-1)==',')
			s=s.substring(0, s.length()-1);
		return s;
	}
	
	//checks for each password if it appears in the BTree and returns time execution  in millisecond 
	public String getSearchTime(String file){
		double searchTime=0;
		try{
			BufferedReader br = new BufferedReader(new FileReader(file));
			
			long startTime = System.nanoTime();
			
			for (String data = br.readLine(); data!= null; data = br.readLine()){
				search(data);
			}
			long endTime = System.nanoTime();
			searchTime = (double) (endTime - startTime)/1000000;
			br.close();
		}
		catch(IOException ex) {
		}
	
		return String.format(Locale.ROOT,"%.4f",searchTime);//
	}
	
	// deletes keys of a file from the BTree
	public void deleteKeysFromTree(String file){
		try{
			BufferedReader br = new BufferedReader(new FileReader(file));
			
			for (String data = br.readLine(); data!= null; data = br.readLine()){
				deleteKey(data);
			}
			br.close();
		}
		catch(IOException ex) {
			System.out.println("Bad file!");
		}
		
	}
	
	//returns true is the tree is empty 
	public boolean isEmpty(){
		return root.n == 0;
	}
	
	// delete key from the tree, if exists
	public void deleteKey(String key){
		if (key == null) 
			throw new RuntimeException("illegal input - null");
		if (isEmpty())
			throw new RuntimeException("the tree is empty");
		root.deleteKey(key);
		// check if the root is empty
		if (root.n==0)
			if (root.isLeaf) // means we deleted the key from root and the tree is empty
				root = null;
			else // will happen if we merged the root into it's child
				root = root.c[0];
	}
	
}
	
	


