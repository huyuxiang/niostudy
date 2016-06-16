package daily.y2016.m06.d16.a1;

import java.util.LinkedList;
import java.util.List;

public class SeparateChainingHashTable<T> {
	
	private static final int DEFAULT_TABLE_SIZE = 101;
	
	private List<T> [] theLists;
	private int currentSize;
	
	public SeparateChainingHashTable() {
		this(DEFAULT_TABLE_SIZE);
	}
	
	public SeparateChainingHashTable(int size) {
		theLists = new LinkedList[nextPrime(size)];
		for(int i=0;i<theLists.length;i++) {
			theLists[i] = new LinkedList<T>();
		}
	}
	
	public void makeEmpty() {
		for(int i=0;i<theLists.length;i++) {
			theLists[i].clear();
		}
		
		currentSize = 0;
	}
	
	public boolean contains(T x) {
		List<T> whichList = theLists[myhash(x)];
		return whichList.contains(x);
	}
	
	public void insert(T x) {
		List<T> whichList = theLists[myhash(x)];
		if(!whichList.contains(x)) {
			whichList.add(x);
			if(++currentSize>theLists.length)
				rehash();
		}
	}
	
	public void remove(T x) {
		List<T> whichList = theLists[myhash(x)];
		if(whichList.contains(x)) {
			whichList.remove(x);
			currentSize--;
		}
	}
	
	private int myhash(T x) {
		int hashVal = x.hashCode();
		
		hashVal = hashVal % theLists.length;
		
		if(hashVal<0) 
			hashVal = hashVal + theLists.length;
		
		return hashVal;
	}
	
	private void rehash() {
		List<T> [] oldLists = theLists;
		
		theLists = new List[nextPrime(2 * theLists.length)];
		for(int j=0;j<theLists.length;j++){
			theLists[j] = new LinkedList<T>();
		}
		currentSize = 0;
		for(int i=0;i<oldLists.length;i++) 
			for(T item: oldLists[i])
				insert(item);
	}
	
	private static int nextPrime(int n) {
		if(n%2 ==0)
			n++;
		for(;!isPrime(n);n=n+2) 
			;
		return n;
	}
	
	private static boolean isPrime(int n) {
		if(n==2|| n==3) 
			return true;
		if(n==1|| n%2 ==0)
			return false;
		
		for(int i=3;i*i<=n;i=i+2)
			if(n%i==0)
				return false;
		
		return true;
	}
	
	
}
