/*
 * Program #1
 * Quang Trinh
 * cssc0759
 */

package data_structures;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class OrderedArrayPriorityQueue<E extends Comparable<E>> implements PriorityQueue<E> {
	private E[] storage;
	private int currentSize;
	private int maxSize;
	private long modCounter;

	@SuppressWarnings("unchecked")
	public OrderedArrayPriorityQueue(int size) {
		maxSize = size;
		storage = (E[]) new Comparable[maxSize];
		currentSize = 0;
		modCounter = 0;
	}

	public OrderedArrayPriorityQueue() {
		this(DEFAULT_MAX_CAPACITY);
	}

	// Worst case: O(n)
	public boolean insert(E object) {
		if (isFull())
			return false;
		int where = findInsertionPoint(object, 0, currentSize - 1);
		for (int i = currentSize - 1; i >= where; i--) // Shift right to make room for new item.
			storage[i + 1] = storage[i];
		storage[where] = object;
		currentSize++;
		modCounter++;
		return true;
	}

	// Worst case: O(1)
	public E remove() {
		if (isEmpty())
			return null;
		modCounter++;
		return storage[--currentSize];
	}

	public boolean delete(E obj) {
		if (!contains(obj))
			return false;
		int first = findInsertionPoint(obj, 0, currentSize - 1);
		int last = lastPrioritizedIndex(obj, 0, currentSize - 1);
		int unit = (last + 1) - first;

		if (last != currentSize - 1) {
			for (int i = first; i < currentSize - unit; i++)
				storage[i] = storage[i + unit];
			currentSize = currentSize - unit;
			return true;
		} else {
			currentSize = currentSize - unit;
			return true;
		}
	}

	public E peek() {
		if (isEmpty())
			return null;
		return storage[currentSize - 1];
	}

	public boolean contains(E obj) {
		if (isEmpty())
			return false;
		if (obj.compareTo(storage[currentSize - 1]) < 0) // Best case complexity O(1)
			return false;
		if (indexOf(obj) != -1)
			return true;
		return false;
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

	/*
	 * Return current position of an item, if not found return -1.
	 */
	private int indexOf(E obj) {
		int lo = 0;
		int hi = currentSize - 1;
		while (lo <= hi) {
			int mid = (lo + hi) / 2;
			if (obj.compareTo(storage[mid]) == 0)
				return mid;
			else if (obj.compareTo(storage[mid]) < 0)
				lo = mid + 1;
			else
				hi = mid + 1;
		}
		return -1; // Not found
	}

	/*
	 * Return position needed to be inserted.
	 */
	private int findInsertionPoint(E obj, int lo, int hi) {
		if (hi < lo)
			return lo;
		int mid = (lo + hi) / 2;
		if (obj.compareTo(storage[mid]) >= 0)
			return findInsertionPoint(obj, lo, mid - 1);
		return findInsertionPoint(obj, mid + 1, hi);
	}

	/*
	 * Return position of the last Prioritized item.
	 */
	private int lastPrioritizedIndex(E obj, int lo, int hi) {
		if (hi < lo)
			return hi;
		int mid = (lo + hi) / 2;
		if (obj.compareTo(storage[mid]) > 0)
			return lastPrioritizedIndex(obj, lo, mid - 1);
		return lastPrioritizedIndex(obj, mid + 1, hi);
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
			if (stateCheck != modCounter)
				return false; // ConcurrentModificationException
			return iterIndex < currentSize;
		}

		public E next() {
			if (!hasNext())
				throw new NoSuchElementException();
			return storage[iterIndex++];
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}
	}
	//////////////////////////////////////////////////
}
