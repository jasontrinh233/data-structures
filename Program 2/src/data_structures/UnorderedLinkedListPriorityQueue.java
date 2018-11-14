/*
 * Program #2
 * Quang Trinh
 * cssc0759
 */

package data_structures;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class UnorderedLinkedListPriorityQueue<E extends Comparable<E>> implements PriorityQueue<E> {
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
	public UnorderedLinkedListPriorityQueue() {
		head = null;
		currentSize = 0;
		modCounter = 0;
	}

	// Worst case: O(1)
	public boolean insert(E object) {
		Node<E> newNode = new Node<E>(object);
		newNode.next = head;
		head = newNode;
		currentSize++;
		modCounter++;
		return true;
	}

	// Worst case: O(n)
	public E remove() {
		if(isEmpty())
			return null;
		Node<E> prevWhere, where, prev, current;
		current = where = head;
		prevWhere = prev = null;

		// After the loop, "where" points to remove position,
		// "prevWhere" points to the previous-remove-position
		while(current != null) {
			if(prev != null && ((Comparable<E>)(current.data)).compareTo(where.data) <= 0) {
				prevWhere = prev;
				where = current;
			}
			prev = current;
			current = current.next;
		}
		// Remove position is first node of the list
		if(prevWhere == null)
			head = head.next;
		else
			prevWhere.next = where.next;
		currentSize--;
		modCounter++;
		return where.data;
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

		Node<E> current, prev, where;
		current = where = head;
		prev = null;
		while(current != null) {
			if(prev != null && ((Comparable<E>)(current.data)).compareTo(where.data) <= 0) {
				where = current;
			}
			prev = current;
			current = current.next;
		}
		return where.data;
	}

	public boolean contains(E obj) {
		Node<E> temp = head;
		if(isEmpty())
			return false;
		while(temp != null) {
			if( ((Comparable<E>)obj).compareTo(temp.data) == 0)
				return true;
			temp = temp.next;
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
	//////////////////////////////////////////////////////
}
