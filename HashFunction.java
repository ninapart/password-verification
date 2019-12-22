

public class HashFunction {
	
	// FIELDS
	
	private int alpha;
	private int beta;
	private int m;
	private int p;
	
	//CONSTRUCTOR
	
	//receives as input the parameters of hash function (alpha, beta, m and p)
	public HashFunction(int alpha, int beta, int m, int p) {
		this.alpha = alpha;
		this.beta = beta;
		this.m = m;
		this.p = p;
	}
    
	//returns the results of the hash function calculation
	public int calc(int k){
		return ((alpha*k + beta) % p) % m;
	}
	
	
}
