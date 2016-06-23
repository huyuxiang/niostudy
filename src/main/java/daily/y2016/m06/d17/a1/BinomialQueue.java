package daily.y2016.m06.d17.a1;

import daily.template.datastructures.ch04.UnderflowException;

public class BinomialQueue<T extends Comparable<? super T>> {

	
	private static final int DEFAULT_TREES = 1;
	
	private int currentSize;
	private BinNode<T> [] theTrees;
	
	private static class BinNode<T> {
		BinNode(T theElement) {
			this(theElement, null, null);
		}
		
		BinNode(T theElement, BinNode<T> lt, BinNode<T> nt) {
			element = theElement;
			leftChild = lt;
			nextSibling = nt;
		}
		
		T element;
		BinNode<T> leftChild;
		BinNode<T> nextSibling;
	}
	
	private int capacity() {
		return (1<<theTrees.length) -1;
	}
	
	public BinomialQueue() {
		theTrees = new BinNode[DEFAULT_TREES];
		makeEmpty();
	}
	
	public BinomialQueue(T item) {
		currentSize = 1;
		theTrees = new BinNode[1];
		theTrees[0] = new BinNode<>(item, null, null);
	}
	
	private BinNode<T> combineTrees(BinNode<T> t1, BinNode<T> t2) {
		if(t1.element.compareTo(t2.element) >0)
			return combineTrees(t2, t1);
		t2.nextSibling = t1.leftChild;
		t1.leftChild = t2;
		return t1;
	}
	
	private void expandTheTrees(int newNumTrees) {
		BinNode<T> [] old = theTrees;
		int oldNumTrees = theTrees.length;
		
		theTrees = new BinNode[newNumTrees];
		for(int i=0;i<oldNumTrees;i++)
			theTrees[i] = old[i];
		
		for(int i=oldNumTrees;i<newNumTrees;i++)
			theTrees[i] = null;
	}
	
	public void merge(BinomialQueue<T> rhs) {
		if(this==null) 
			return ;
		
		currentSize += rhs.currentSize;
		
		if(currentSize >capacity()) {
			int newNumTrees = Math.max(theTrees.length, rhs.theTrees.length) + 1;
			expandTheTrees(newNumTrees);
		}
		
		BinNode<T> carry = null;
		for(int i=0,j=1;j<=currentSize ;i++,j*=2) {
			BinNode<T> t1 = theTrees[i];
			BinNode<T> t2 = i<rhs.theTrees.length? rhs.theTrees[i] : null;
			
			int whichCase = t1==null?0:1;
			whichCase += t2 ==null ? 0 : 2;
			whichCase += carry ==null? 0: 4;
			
			switch(whichCase) {
			case 0:
			case 1:
				break;
			case 2:
				theTrees[i] = t2;
				rhs.theTrees[i] =null;
				break;
			case 4:
				theTrees[i] = carry;
				carry = null;
				break;
			case 3:
				carry = combineTrees(t1, t2);
				theTrees[i] = rhs.theTrees[i] = null;
				break;
			case 5:
				carry = combineTrees(t1, carry);
				theTrees[i] = null;
				break;
			case 6: 
				carry = combineTrees(t2, carry);
				rhs.theTrees[i] = null;
				break;
				
			case 7:
				theTrees[i] = carry;
				carry = combineTrees(t1, t2);
				rhs.theTrees[i] = null;
				break;
			}
		}
		
		for(int k = 0;k<rhs.theTrees.length;k++) 
			rhs.theTrees[k] = null;
		rhs.currentSize = 0;
	}
	
	public void insert(T x) {
		merge(new BinomialQueue<>(x));
	}
	
	public boolean isEmpty() {
		return currentSize == 0;
	}
	
	public void makeEmpty() {
		currentSize = 0;
		for(int i=0;i<theTrees.length;i++)
			theTrees[i] = null;
	}
	
	private int findMinIndex() {
		int i ;
		int minIndex;
		for(i =0;theTrees[i]  == null;i++)
			;
		
		for(minIndex = i;i<theTrees.length;i++)
			if(theTrees[i] !=null
			&& theTrees[i].element.compareTo(theTrees[minIndex].element) <0	)
				minIndex = i;
		return minIndex;
	}
	
	public T findMin() {
		if(isEmpty())
			throw new UnderflowException();
		return theTrees[findMinIndex()].element;
	}
	
	public T deleteMin() {
		if(isEmpty())
			throw new UnderflowException();
		
		int minIndex= findMinIndex();
		T minItem = theTrees[minIndex].element;
		
		BinNode<T> deletedTree = theTrees[minIndex].leftChild;
		
		//construct h''
		BinomialQueue<T> deletedQueue = new BinomialQueue<>();
		deletedQueue.expandTheTrees(minIndex + 1);
		
		deletedQueue.currentSize = (1<<minIndex) -1;
		
		for(int j=minIndex -1 ;j>=0;j--) {
			deletedQueue.theTrees[j] = deletedTree;
			deletedTree = deletedTree.nextSibling;
			deletedQueue.theTrees[j].nextSibling = null;
		}
		
		//construct h'
		theTrees[minIndex] = null;
		currentSize -= deletedQueue.currentSize + 1;
		
		merge(deletedQueue);
		
		return minItem;
	}
	
}
