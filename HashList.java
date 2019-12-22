

public class HashList { 
	
	//FIELDS
    private HashListElement first;// first hash function element of the list
    
    //CONSTRUCTOR
    
    //initiates the first element as null
    public HashList(){
    	this.first = null;
    }
   
    //METHODS
    
    // returns the number of elements in the list
    public int size() {
		int counter = 0;
		for(HashListElement curr = first; curr != null; curr = curr.getNext())
			counter = counter + 1;
		return counter;
	}
	
	//Returns true if this list contains no elements.
	public boolean isEmpty() {
		return first == null;
	}
	
	//Adds element to the beginning of this list
	public void addFirst(String data) {
		if (data == null)
			throw new RuntimeException("illegal input - null instead of String");
		first = new HashListElement(data, first);
	}
	
	//Returns true if this list contains the specified element
	public boolean contains(String element){
		if (element == null)
			throw new RuntimeException("illegal input - null instead of String");
		boolean output = false;
		for(HashListElement curr = first; curr != null & !output; curr = curr.getNext())
			output = element.equals(curr.getData());
		return output;		
	}
	
}
