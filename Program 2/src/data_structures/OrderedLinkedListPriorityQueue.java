/*
 * Program #2
 * Quang Trinh
 * cssc0759
 */

package data_structures;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class OrderedLinkedListPriorityQueue<E extends Comparable<E>> implements PriorityQueue<E> {
	//////////////////////////////////////
	private class Node<T>{
		T data;
		Node<T> next;
		
		public Node(T data) {
			this.data = data;
			next = null;
		}
	}
	//////////////////////////////////////
	
	private Node<E> head; 
	private int currentSize;
	private long modCounter;
	
	// Constructor
	public OrderedLinkedListPriorityQueue() {
		head = null;
		currentSize = 0;
		modCounter = 0;
	}
	
	// Worst case: O(n)
	// Insert in ascending order
	public boolean insert(E object) {
		Node<E> newNode = new Node<E>(object);
		Node<E> prev = null, current = head;
		
		while(current != null && 
				((Comparable<E>)object).compareTo(current.data) >= 0) {
			prev = current;
			current = current.next;
		}
		if(prev == null){				// insert at front
			newNode.next = head;	
			head = newNode;
			currentSize++;
		} else if(current == null) {    // insert at last
			prev.next = newNode;
			currentSize++;
		}
		else {							// insert in middle
			newNode.next = current;
			prev.next = newNode;
			currentSize++;
		}
		modCounter++;
		return true;
	}

	// Worst case: O(1)
	public E remove() {
		E temp = null;
		if(isEmpty())
			return null;
		// always remove first node
		temp = head.data;
		head = head.next;
		currentSize--;
		modCounter++;
		return temp;
	}

	public boolean delete(E obj) {
		Node<E> prev = null, current = head;
		if(!contains(obj))
			return false;

		// Iterate entire list
		while(current != null) {
			// Remove from the front
			if(prev == null && ((Comparable<E>)obj).compareTo(current.data) == 0) {
				head = head.next;
				current = current.next; // update pointer for next iteration
				currentSize--;
			} 
			// Found obj in the middle of list
			else if( ((Comparable<E>)obj).compareTo(current.data) == 0) {
				prev.next = current.next;
				current = current.next; 
				currentSize--;
			} 
			// If not found, keep moving
			else {
				prev = current;
				current = current.next;
			}
		}
		modCounter++;
		return true;
	}

	public E peek() {
		if(isEmpty())
			return null;
		return head.data;
	}

	public boolean contains(E obj) {
		Node<E> current = head;
		if(isEmpty())
			return false;
		while(current != null) {
			if( ((Comparable<E>)obj).compareTo(current.data) == 0)
				return true;
			current = current.next;
		}
		return false;
	}

	public int size() {
		return currentSize;
	}

	public void clear() {
		head = null;
		currentSize = 0;
		modCounter = 0;
	}

	public boolean isEmpty() {
		return (head == null);
	}

	public boolean isFull() {
		return false;
	}

	public Iterator<E> iterator() {
		return new IteratorHelper();
	}

	//////////////////////////////////////////////////////
	private class IteratorHelper implements Iterator<E> {
		private Node<E> current; 
		private long modCheck;

		public IteratorHelper(){
			current = head; // start at the front of list
			modCheck = modCounter;
		}

		public boolean hasNext() {
			if(modCheck != modCounter)
				throw new ConcurrentModificationException();
			return (current != null);
		}

		public E next() {
			if(!hasNext())
				throw new NoSuchElementException();
			E temp = current.data;
			current = current.next;
			return temp;
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}
	}
	////////////////////////////////////////////////////

}
