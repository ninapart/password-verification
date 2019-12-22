
public class BTreeNode{
	//FIELDS
	protected int n; //number of keys stored
	protected  String[] keys;//arrays of keys 
	protected BTreeNode[] c;//array of children pointers
	protected boolean isLeaf;//true if is a leaf
	protected int t;//minimum degree
	
	//CONSTRUCTOR
	
	/*initialize arrays with the minimum degree inputed : keys array size of 2*t-1 and children array size of 2*t
	  number of keys is zero
      is leaf receives true value
    */
	
	public BTreeNode(int t) {
		if(t<2)
			throw new RuntimeException("minimum dregree must be positive");
		this.t = t;
		keys = new String [2*t-1];
		c = new BTreeNode[2*t];
		isLeaf = true;
		n = 0;
	}
	
	// split child at c[i] place
	protected void splitChild(int i){
		if (i>n | i<0)
			throw new RuntimeException("index i out of range");
		//new node that take t-1 keys of y
		BTreeNode z = new BTreeNode(t), y = c[i];
		z.isLeaf = y.isLeaf;
		z.n = t-1;
		//copy the last t-1 keys to z
		moveHalf(y,z);
		// move children and keys to the right to make room for the new child
		pushArraysRight(i);
		// move y's middle key to the gap, and the created new child
		keys[i] = y.keys[t-1];
		y.keys[t-1] = null;
		c[i+1] = z;
		n++;
		//reduces keys in y
		y.n = t-1;
	}
	
	// help function for splitChild - moves half the keys&children from y to z
	private void moveHalf(BTreeNode y, BTreeNode z) {
		for(int j=0; j<t-1; j++){
			z.keys[j] = y.keys[j+t];
			y.keys[j+t] = null;
			//copy children - if y is leaf we'll move nulls and it doesnt matter
			z.c[j] = y.c[j+t];
			y.c[j+t] = null;
		}
		// don't forget the last child
		z.c[t-1] = y.c[2*t-1];
		y.c[2*t-1] = null;
	}
	
	
	//inserts a key in non full node (number of keys < 2*t-1)
	protected void insertNonFull(String key){
		// find place of insertion of the new key
		int i = findKey(key);
		if (isLeaf){
			//move the greater keys ahead
			//insert the new key
			pushArraysRight(i);
			keys[i] = key;
			n++;
		}
		else{ // not a leaf, insert to the tree rooted at the proper child			
			//if the child is full, split it
			if(c[i].n == 2*t-1){
				splitChild(i);
				//see which of the children is going to have the new key, after the splitting
				i = findKey(key);
			}
			c[i].insertNonFull(key);
		}
	}
	
	// returns the index of the first key that >= key
	private int findKey(String key){
		int i = 0;
		while (i<n && keys[i].compareTo(key)<0)
			i++;
		
		return i;
	}
	
    // Search the key in the tree rooted at the current node
	protected BTreeSearchResult search(String key){
		int i = findKey(key);
		
		if (i<n && keys[i].compareTo(key)==0)
			return new BTreeSearchResult(this,i);
		
		if (isLeaf)
			return null;
		
		return c[i].search(key);
	}
	
	//recursive methods that returns the keys and their node depth in-order 
	protected String toString(int depth){
		String s = "";
		for (int i = 0; i<n; i++){
			if (!isLeaf)
				s+=c[i].toString(depth+1);
			s += keys[i] + "_" + depth + ",";
		}
		if (!isLeaf)
			s+=c[n].toString(depth+1);
		
		return s;
	}
	
	// DELETION
	protected void deleteKey(String key){
		// Search the key in the current node
		int i = findKey(key);
		// if we found it, remove and rearrange the keys list
		if (i<n && keys[i].compareTo(key)==0) {
			if (isLeaf) 
				deleteFromLeaf(i);
			else
				deleteFromInnerNode(i);
			return;
		}
		// if we didn't find it, and its a leaf- it does'nt exist in the tree
		if (isLeaf)
			return;
		// if it's not a leaf, delete from next child (with filling)
		fillDelete(i, key);
	}
	
	// delete key from child i, but fill it first (if needed)
	private void fillDelete(int i, String key) {
		if (c[i].n < t)
			i = fillChild(i);
		c[i].deleteKey(key);		
	}
	
	private void deleteFromLeaf(int i){
		if(i<0)
			throw new RuntimeException("index must be positive");
		// just run over the key
		pushArraysLeft(i);
		n--;
	}

	// fill c[i]- lend key and child from left sibling (c[i-1])
	private void fillFromLeft(int i){
		if(i<=0)
			throw new RuntimeException("bad index");
		BTreeNode sibling = c[i-1];
		//insert the borrowed key at the beginning
		c[i].pushArraysRight(0);
		c[i].keys[0] = keys[i-1];
		c[i].n++;
		keys[i-1] = sibling.keys[sibling.n-1];
		c[i].c[0] = sibling.c[sibling.n];
		sibling.n--;
	}
	
	// fill c[i]- lend key and child from right sibling (c[i+1])
	private void fillFromRight(int i){
		if(i<0)
			throw new RuntimeException("index must be positive");
		BTreeNode sibling = c[i+1];
		// get the key from father and child from sibling
		c[i].keys[c[i].n] = keys[i];
		c[i].n++;
		c[i].c[n+1] = sibling.c[0];
		// move the sibling's key to father
		keys[i] = sibling.keys[0];
		// update sibling's keys&children 
		sibling.pushArraysLeft(0);
		sibling.n--;
	}
	
	// fill child with minimum keys. i is the index of the child to fill, AND also the index of the father's key.
	// return the index of the merged child
	private int fillChild(int i){
		if(i<0)
			throw new RuntimeException("index must be positive");
		// if one of the children has more than minimum keys, borrow one:
		// check left sibling
		if (i>0 && c[i-1].n >= t) 
			fillFromLeft(i);
		// right sibling
		else if (i<n && c[i+1].n >= t) 
			fillFromRight(i);
		else { 
			// both siblings has minimum keys, merge the child with one of them (that exists)
			if (i>0) //if its not the first child, merge with the one before
				i--;
			merge(i);
		}
		return i;
	}

	// push all keys and children to the left, running over i.
	// if the node is a leaf, changing the children array does'nt matter.. (nulls or ignored values)
	private void pushArraysLeft(int i){
		if(i<0)
			throw new RuntimeException("index must be positive");
		for(; i < n-1; i++){
			keys[i] = keys[i+1];
			c[i] = c[i+1];
		}
		c[i] = c[i+1]; // There are n+1 children
		keys[i] = null;
		c[i+1] = null;
	}
	
	// push all keys and children to the right, making a gap in i.
	// if the node is a leaf, changing the children array does'nt matter.. (nulls or ignored values)
	private void pushArraysRight(int i){
		if(i<0)
			throw new RuntimeException("index must be greater");
		if(n== 2*t-1)
			throw new RuntimeException("Can't push right maximum keys");
		c[n+1] = c[n]; // There are n+1 children
		for(int j = n; j >= i+1 ; j--) {
			c[j] = c[j-1];
		    keys[j] = keys[j-1];
		}		
	}

	// gets key index i to delete
	private void deleteFromInnerNode(int i){
		if(i<0)
			throw new RuntimeException("index must be positive");
		// if left child has more than minimum keys, take it's subtree maximum key
		String replacer = null;
		int childIndex = i;
		if (c[i].n >= t){
			replacer = c[i].getMaxKey();
		} 
		// if right child has more than minimum, take it's min key
		else if (c[i+1].n >= t) {
			replacer = c[i+1].getMinKey();
			childIndex++;
		}
		if (replacer!=null){
			keys[i] = replacer;
			// delete the stolen key from the child's tree
			fillDelete(childIndex, replacer);
		}
		else { //both children has t-1 keys- merge them with the key and delete it from the merged node.
			BTreeNode merged = merge(i);
			// merged.n must be 2t-1 so no need to check if it needs to be filled 
			merged.deleteKey(merged.keys[t-1]); // The key is now in index t-1 since each child had t-1 keys (and its in the middle)
		}
	}
	
	// gets key index i, and merges it's children (with key i as median)
	private BTreeNode merge(int i){
		if(i<0)
			throw new RuntimeException("index must be positive");
		BTreeNode childA = c[i], childB = c[i+1];
		// insert the center key
		childA.keys[childA.n] = keys[i];
		childA.n++;
		// merge the right child's keys and children into the left child
		int j=0; // we want it to keep the last value
		for(;j<childB.n; j++){
			childA.keys[childA.n + j] = childB.keys[j];
			childA.c[childA.n + j] = childB.c[j]; // if childB is a leaf we'll move null so it doesn't matter
		}
		childA.c[childA.n + j] = childB.c[j];
		childA.n+=j; // update the amount of children we added
		// delete the key from the current node and rearrange the children array
		c[i+1] = childA;
		pushArraysLeft(i);
		n--;
		return childA;
	}
	
	protected String getMinKey(){
		if (isLeaf)
			return keys[0];
		else
			return c[0].getMinKey();
	}
	
	protected String getMaxKey(){
		if (isLeaf)
			return keys[n-1];
		else
			return c[n].getMaxKey();
	}
	
}
