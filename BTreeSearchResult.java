
public class BTreeSearchResult {
	
	/* This class use for the results representation of the function search in BTree class.
	 * Its constructor receive as input a node and an index
	 * The fields are public for permitting direct access to the data.
	 */
	
	public BTreeNode node;
	public int keyIndex;

	public BTreeSearchResult(BTreeNode node,int keyIndex) {
		this.node = node;
		this.keyIndex = keyIndex;
	}
	

}
