/*
 * Quang Trinh
 * cssc0759
 * Program 4
 * 05.05.18
 */
package data_structures;

import java.util.Iterator;
import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException; 

public class BinarySearchTree<K extends Comparable<K>,V> implements DictionaryADT<K,V> {
/////////////////////////////////////////////
	private class Node<K,V> {
		private K key;
		private V value;
		private Node<K,V> leftChild;
		private Node<K,V> rightChild;
		
		public Node(K k, V v) {
			key = k;
			value = v;
			leftChild = rightChild = null;
		}
		
		public int compareTo(Node<K,V> n) {
			return ((Comparable)key).compareTo((K)n.key);
		}
	}
/////////////////////////////////////////////
	
	private Node<K,V> root;	// first node of the tree
	private int currentSize;
	private long modCounter;
	private K found;
	
	/*
	 * Constructor
	 */
	public BinarySearchTree() {
		root = null;
		currentSize = 0;
		modCounter = 0;
		found = null;
	}
	
	public boolean contains(K key) {
		return find(key, root) != null;
	}
	
	/*
	 * Private helper for contains() method.
	 * @return V - value that match the key, if not found return null
	 */
	private V find(K key, Node<K,V> n) {
		if(n == null) return null;	// Base case
		if(key.compareTo(n.key) < 0)
			return find(key, n.leftChild);
		else if(key.compareTo(n.key) > 0)
			return find(key, n.rightChild);
		else
			return n.value;
	}

	public boolean add(K key, V value) {
		if(contains(key)) // Check duplicates
			return false;
		if(root == null)	 // Base case
			root = new Node<K,V>(key,value);
		else
			insert(key, value, root, null, false);
		currentSize++;
		modCounter++;
		return true;
	}
	
	/*
	 * Private Helper for add() method.
	 * Using recursion to keep the tree structure intact. 
	 */
	private void insert(K k, V v, Node<K,V> n, Node<K,V> parent, boolean wasLeft) {
		if(n == null) {		// Base case
			if(wasLeft)
				parent.leftChild = new Node<K,V>(k,v);
			else
				parent.rightChild = new Node<K,V>(k,v);
		}
		else if (k.compareTo((K)n.key) < 0) 
			insert(k, v, n.leftChild, n, true); 	 // Go left
		else
			insert(k, v, n.rightChild, n, false); // Go right
	}

	public boolean delete(K key) {
		if(isEmpty())
			return false;
		if(!remove(key, root, null, false))
			return false;
		currentSize--;
		modCounter++;
		return true;
	}

	/*
	 * Private helper for delete() method.
	 * Algorithm source: Riggins' Lecture Note
	 */
	private boolean remove(K key, Node<K,V> current, Node<K,V> parent, boolean wasLeft) {
		if (current == null) return false;
        if (key.compareTo(current.key) < 0)
            return remove(key, current.leftChild, current, true);
        else if (key.compareTo(current.key) > 0)
            return remove(key, current.rightChild, current, false);
        else {
            if (current.leftChild == null && current.rightChild == null) {
                if (parent == null)
                    root = null;
                else if (wasLeft)
                    parent.leftChild = null;
                else
                    parent.rightChild = null;
            } else if (current.leftChild == null) { //one right child
                if (parent == null)
                    root = current.rightChild;
                else if (wasLeft)
                    parent.leftChild = current.rightChild;
                else
                    parent.rightChild = current.rightChild;
            } else if (current.rightChild == null) { //one left child
                if (parent == null)
                    root = current.leftChild;
                else if (wasLeft)
                    parent.leftChild = current.leftChild;
                else
                    parent.rightChild = current.leftChild;
            } else { //two children
                Node<K, V> temp = getSuccessor(current.rightChild);
                if (temp == null) {
                    current.key = current.rightChild.key;
                    current.value = current.rightChild.value;
                    current.rightChild = current.rightChild.rightChild;
                } else {
                    current.key = temp.key;
                    current.value = temp.value;
                }
            }
        }
        return true;
	}
	
	/*
	 * Private helper for remove() method.
	 * Return successor node of a node.
	 */
	private Node<K,V> getSuccessor(Node<K,V> n) {
		 Node<K, V> parent = null;
	        while (n.leftChild != null) {
	            parent = n;
	            n = n.leftChild;
	        }
	        if (parent == null)
	            return null;
	        parent.leftChild = n.rightChild;
	        return n;
	}
	
	public V getValue(K key) {
		if(isEmpty()) 
			return null;
        return find(key, root);
	}

	public K getKey(V value) {
		found = null;
        if (isEmpty()) return null;
        keySearch(root, value);
        return found;
	}
	
	/*
	 * Private helper for getKey() method.
	 */
	private void keySearch(Node<K,V> n, V value) {
		if(n == null)
            return;
        if (((Comparable<V>) value).compareTo((V) n.value) == 0) {
            found = n.key;
            return;
        }
        keySearch(n.rightChild, value);
        keySearch(n.leftChild, value);
	}

	public int size() {
		return currentSize;
	}

	public boolean isFull() {
		return false;
	}

	public boolean isEmpty() {
		return currentSize == 0;
	}

	public void clear() {
		root = null;
		currentSize = 0;
		modCounter++;
	}

	public Iterator<K> keys() {
		return new KeyIteratorHelper();
	}

	public Iterator<V> values() {
		return new ValueIteratorHelper();
	}
	
	abstract class IteratorHelper<E> implements Iterator<E> {
        protected Node <K, V>[] nodes;
        protected int index, sortIndex;
        protected long modCheck;

        public IteratorHelper() {
            nodes = new Node[currentSize];
            index = sortIndex = 0;
            modCheck = modCounter;
            sort(root);
        }

        private void sort(Node<K, V> n) {
            while (n != null)
                sort(n.leftChild);
            nodes[sortIndex++] = n;
            sort(n.rightChild);
        }

        public boolean hasNext() {
            if (modCheck != modCounter)
                throw new ConcurrentModificationException();
            return index < currentSize;
        }

        public abstract E next();

        public void remove() {
            throw new UnsupportedOperationException();
        }

    }

    class KeyIteratorHelper<K> extends IteratorHelper<K> {
        public KeyIteratorHelper() {
            super();
        }

        public K next() {
            if (!hasNext()) 
            		throw new NoSuchElementException();
            return (K) nodes[index++].key; 
        }
    }

    class ValueIteratorHelper<V> extends IteratorHelper<V> {
        public ValueIteratorHelper() {
            super();
        }

        public V next() {
            if (!hasNext()) 
            		throw new NoSuchElementException();
            return (V) nodes[index++].value;
        }
    }

}
