
public class HashListElement {
	
	//FIELDS
	
	private String data; // password
	private HashListElement next; // pointer to the next element
	
	
	// CONSTRUCTOR
	
	//initiates the data password and the pointer to the next element
	public HashListElement (String data, HashListElement next) {
		if (data==null)
			throw new RuntimeException("illegal input");
		this.data = data;
		this.next = next;
	}
	
	
	// METHODS
	
	public HashListElement getNext(){
		return next;
	}
	
	public String getData() {
	    return data;
	}
	
	public String toString() {
	    return data.toString();
	}
}
