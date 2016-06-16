package daily.y2016.m06.d15;

public class QuadraticProbingHashTable<T> {
	
	private static class HashEntry<T> {
		public T element;
		public boolean isActive;
		
		public HashEntry(T e) {
			this(e, true);
		}
		
		public HashEntry(T e, boolean i) {
			element = e;
			isActive = i;
		}
	}
	
	private static final int DEFAULT_TABLE_SIZE = 11;
	
	private HashEntry<T> [] array;
	private int currentSize;
	
	public QuadraticProbingHashTable() {
		this(DEFAULT_TABLE_SIZE);
	}
	
	public QuadraticProbingHashTable(int size) {
		allocateArray(size);
		makeEmpty();
	}
	
	private void allocateArray(int arraySize) {
		array = new HashEntry[arraySize];
	}
	public void makeEmpty() {
		currentSize = 0;
		for(int i=0;i<array.length;i++) 
			array[i] = null;
	}
	
	public boolean contains(T x) {
		int currentPos = findPos(x) ;
		return isActive(currentPos);
	}
	
	private int findPos(T x) {
		int offset = 1;
		int currentPos = myhash(x);
		
		while(array[currentPos] !=null && !array[currentPos].element.equals(x)) {
			currentPos += offset ;
			offset += 2;
			if(currentPos >= array.length)
				currentPos = currentPos - array.length;
		}
		return currentPos; 
	}
	
	private boolean isActive (int currentPos) {
		return array[currentPos] !=null && array[currentPos].isActive;
	}
	
	public void insert(T x) {
		int currentPos = findPos(x);
		if(isActive(currentPos))
			return ;
		
		array[currentPos] = new HashEntry<T> (x, true);
		
		if(++currentSize>array.length/2)
			rehash();
	}
	
	public void remove(T x) {
		int currentPos = findPos(x);
		if(isActive(currentPos)) 
			array[currentPos].isActive = false;
	}
	
	private void rehash() {
		HashEntry<T> [] oldArray = array;
		allocateArray(nextPrime(2 * oldArray.length));
		currentSize = 0;
		for(int i=0;i<oldArray.length;i++) {
			if(oldArray[i] !=null && oldArray[i].isActive)
				insert(oldArray[i].element);
		}
	}
	
	private int myhash(T x) {
		int hashVal = x.hashCode();
		
		hashVal = hashVal % array.length;
		
		if(hashVal<0) {
			hashVal = hashVal + array.length;
		}
		return hashVal;
	}
	
	private static int nextPrime(int n) {
		if(n%2==0) 
			n++;
		
		for(;!isPrime(n);n=n+2)
			;
		return n;
	}
	
	private static boolean isPrime(int n) {
		if(n==2||n==3)
			return true;
		
		if(n==1|| n%2 ==0)
			return false;
		
		for(int i=3;i*i<=n;i=i+2)
			if(n%i==0)
				return false;
		
		return true;
	}
	
}
