/*
 * Program #1
 * Quang Trinh
 * cssc0759
 */

package data_structures;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class UnorderedArrayPriorityQueue<E extends Comparable<E>> implements PriorityQueue<E> {
	private E [] storage;
	private int currentSize;
	private int maxSize;
	private long modCounter;
	
	@SuppressWarnings("unchecked")
	public UnorderedArrayPriorityQueue(int size) {
		maxSize = size;
		storage = (E[]) new Comparable[maxSize];
		currentSize = 0;
		modCounter = 0;
	}
	
	public UnorderedArrayPriorityQueue() {
		this(DEFAULT_MAX_CAPACITY);
	}

	// Worst case: O(1)
	public boolean insert(E object) {
		if(isFull())
			return false;
		storage[currentSize++] = object;
		modCounter++;
		return true;
	}

	// Worst case: O(n)
	public E remove() {
		if(isEmpty())
			return null;
		int index = highestPriorityIndex(storage);
		E item = storage[index];
		for(int i=index; i < currentSize-1; i++) // Remove the object and shift array left.
			storage[i] = storage[i+1];
		currentSize--;
		modCounter++;
		return item;
	}

	public boolean delete(E obj) {
		if(!contains(obj))
			return false;
		for(int i=0; i < currentSize; i++) {
			if(storage[i].compareTo(obj) == 0) {
				storage[i] = storage[i+1];
				i = 0;
				currentSize--;
			}

		}
		modCounter++;
		return true;
	}

	public E peek() {
		if(isEmpty())
			return null;
		return storage[highestPriorityIndex(storage)];
	}

	public boolean contains(E obj) {
		if(isEmpty()) return false;
		for(int i = 0; i < currentSize; ++i) {
			if(obj.compareTo(storage[i]) == 0)
				return true;
		}
		return false;
	}

	/*
	 * Return the index of the highest priority item in an array.
	 */
	private int highestPriorityIndex(E[] array) {
		int indexMax = 0;
		for(int i = 1; i < currentSize; i++) {
			if(array[indexMax].compareTo(array[i]) > 0 ) {
				indexMax = i;
			}
		}
		return indexMax;
	}	
	
	public int size() {
		return currentSize;
	}

	public void clear() {
		currentSize = 0;
		modCounter = 0;
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
	
	/////////////////////////////////////////////
	class IteratorHelper implements Iterator<E> {
		int iterIndex;
		long stateCheck;
		
		public IteratorHelper() {
			iterIndex = 0;
			stateCheck = modCounter;
		}
	
		public boolean hasNext() {
			if(stateCheck != modCounter)
				return false;	// ConcurrentModificationException
			return iterIndex < currentSize;
		}
	
		public E next() {
			if(!hasNext())
				throw new NoSuchElementException();
			return storage[iterIndex++];
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}
	}
	//////////////////////////////////////////////////
}
