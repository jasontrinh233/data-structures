/*
 * Program #3
 * Quang Trinh
 * cssc0759
 */

package data_structures;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class BinaryHeapPriorityQueue<E extends Comparable<E>> implements PriorityQueue<E> {
	////////////////////////////////////////////////////////////////////////////////////
	private class Wrapper<T> implements Comparable<Wrapper<T>> {
		long number;
		T data;
		
		public Wrapper(T d) {
			number = entryNumber++;  // from parent class
			data = d;
		}
	
		public int compareTo(Wrapper<T> o) {
			if(((Comparable<T>)data).compareTo(o.data) == 0)  // if data is equal, use
				return (int)(number - o.number);        		 // sequence number.
			return ((Comparable<T>)data).compareTo(o.data);
		}
	}
	/////////////////////////////////////////////////////////////////////////////////////
	
	private long entryNumber;
	private int currentSize;
	private int maxSize;
	private Wrapper<E> []array;
	private long modCounter;
	
	// Default constructor
	public BinaryHeapPriorityQueue() {
		this(DEFAULT_MAX_CAPACITY);
	}
	
	// Customized constructor
	public BinaryHeapPriorityQueue(int size) {
		entryNumber = 0;
		maxSize = size;
		array = new Wrapper[maxSize];
		modCounter = 0;
	}
	
	public boolean insert(E object) {
		if(isFull())
			return false;
		Wrapper<E> newNode = new Wrapper<E>(object);
		// Place item into heap at bottom
		array[currentSize++] = newNode;
		modCounter++;
		// Correcting heap ordering
		trickleUp();
		return true;
	}

	public E remove() {
		if(isEmpty())
			return null;
		E result = peek();
		// Place last item at root
		array[0] = array[currentSize-1];
		currentSize--;
		// Correcting heap ordering
		trickleDown(0);  // trickleDown from the root
		modCounter++;
		return result;
	}
	
	/*
	 * This method will loop until there is no instance of the object left in the array.
	 * By implementing trickleDown(int), the array will be able to delete element and correct
	 * heap ordering at the same time before going to the next loop.
	 */
	public boolean delete(E obj) {
		if(!contains(obj))
			return false;
		// Delete until array has no instance of the object
		while(contains(obj)) {
			for(int i = 0; i < currentSize; i++) {
				if(array[i].data.compareTo(obj) == 0) { // Find index of needed item
					array[i] = array[currentSize-1];
					currentSize--;
					trickleDown(i); 	 // trickleDown from i
				}
			}
		}
		modCounter++;
		return true;
	}

	public E peek() {
		if(isEmpty())
			return null;
		return array[0].data;
	}

	public boolean contains(E obj) {
		if(isEmpty())
			return false;
		for(int i = 0; i < currentSize; ++i) {
			if(obj.compareTo(array[i].data) == 0)
				return true;
		}
		return false;
	}

	public int size() {
		return currentSize;
	}

	public void clear() {
		currentSize = 0;
		modCounter = 0;
		entryNumber = 0;	
	}

	public boolean isEmpty() {
		return currentSize == 0;
	}

	public boolean isFull() {
		return currentSize == maxSize;
	}

	public Iterator<E> iterator() {
		return new IteratorHelper();
	}
	
	/*
	 * TrickleUp method for insertion.
	 */
	private void trickleUp() {
		int newIndex = currentSize - 1;
		int parentIndex = (newIndex-1) >> 1;
		Wrapper<E> newNode = array[newIndex];
		while(parentIndex >= 0 && newNode.compareTo(array[parentIndex]) < 0) {
			array[newIndex] = array[parentIndex];
			newIndex = parentIndex;
			parentIndex = (parentIndex-1) >> 1; 
		}
		array[newIndex] = newNode;
	}

	/*
	 * This method trickleDown the heap from index position that passed in.
	 */
	private void trickleDown(int index) {
		int current = index;
		int child = getNextChild(current);
		Wrapper<E> removedNode = array[current];
		while(child != -1 && array[child].compareTo(array[current]) < 0) {
			swap(child, current);
			current = child;
			child = getNextChild(current);
		}
		array[current] = removedNode;
	}

	/*
	 * Private helper for trickleDown(int) method.
	 * Return of appropriate child index when called.
	 * If a node has 2 child, choose the smaller.
	 * If a node has 1 child, choose that child.
	 */
	private int getNextChild(int current) {
		int left = (current << 1) + 1;
		int right = left+1;
		if(right < currentSize) { 	// there are 2 children
			if(array[left].compareTo(array[right]) < 0)
				return left; 		// the left child is smaller
			return right; 			// the right child is smaller
		}
		if(left < currentSize)  		// there is only one child 
			return left;
		return -1; 					// no children
	}

	/*
	 * Private helper for trickleDown(int) method.
	 * Swap 2 elements of an array.
	 */
	private void swap(int index1, int index2) {
		Wrapper<E> tmp = array[index1];
		array[index1] = array[index2];
		array[index2] = tmp;
	}

	/////////////////////////////////////////////////
	class IteratorHelper implements Iterator<E> {
		int iterIndex;
		long stateCheck;

		public IteratorHelper() {
			iterIndex = 0;
			stateCheck = modCounter;
		}

		public boolean hasNext() {
			if (stateCheck != modCounter)
				throw new ConcurrentModificationException();
			return iterIndex < currentSize;
		}

		public E next() {
			if (!hasNext())
				throw new NoSuchElementException();
			return array[iterIndex++].data;
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}
	}
	//////////////////////////////////////////////////
}
