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

public class Hashtable<K extends Comparable<K>,V> implements DictionaryADT<K,V> {
	private int maxSize, currentSize, tableSize;
	private long modCounter;
	private LinkedListDS<DictionaryNode<K,V>> [] list;

///////////////////////////////////////////////////////////////////////////////////
	private class DictionaryNode<K,V> implements Comparable<DictionaryNode<K,V>> {
		K key;
		V value;
		
		public DictionaryNode(K key, V value) {
			this.key = key;
			this.value = value;
		}
		
		public int compareTo(DictionaryNode<K,V> node) {
			return ((Comparable<K>)key).compareTo((K)node.key);
		}
	}
///////////////////////////////////////////////////////////////////////////////////
	
	/*
	 * Constructor
	 */
	public Hashtable(int n) {
		currentSize = 0;
		maxSize = n;
		modCounter = 0;
		tableSize = (int)(maxSize * 1.3f);
		list = new LinkedListDS[tableSize];
		for(int i = 0; i < tableSize; i++)
			list[i] = new LinkedListDS<DictionaryNode<K,V>>();
	}
	
	public boolean contains(K key) {
		return list[getHashCode(key)].contains(new DictionaryNode<K,V>(key,null));
	}

	public boolean add(K key, V value) {
		if(isFull() || contains(key))
			return false;
		list[getHashCode(key)].insert(new DictionaryNode<K,V>(key,value));
		currentSize++;
		modCounter++;
		return true;
	}

	public boolean delete(K key) {
		if(isEmpty() || !contains(key))
			return false;
		list[getHashCode(key)].remove(new DictionaryNode<K,V>(key,null));
		currentSize--;
		modCounter++;
		return true;
	}
	
	/*
	 * Generate index for the storage array list[].
	 */
	private int getHashCode(K key) {
		return (key.hashCode() & 0x7FFFFFFF) % tableSize;
	}
	
	public V getValue(K key) {
		if(isEmpty())
			return null;
		DictionaryNode<K,V> temp = list[getHashCode(key)].get(new DictionaryNode<K,V>(key,null));
		if(temp == null) // not found
			return null;
		return temp.value;
	}

	public K getKey(V value) {
		if(isEmpty())
			return null;
		for(int i=0; i<tableSize; i++) {	
			for(DictionaryNode n : list[i]) {
				if(((Comparable<V>) value).compareTo((V) n.value) == 0)
					return (K) n.key;
			}
		}
		return null;
	}

	public int size() {
		return currentSize;
	}

	public boolean isFull() {
		return currentSize == maxSize;
	}

	public boolean isEmpty() {
		return currentSize == 0;
	}

	public void clear() {
		for(int i=0; i<tableSize; i++) {
			list[i].clear();
		}
		currentSize = 0;
		modCounter++;	
	}

	abstract class IteratorHelper<E> implements Iterator<E> {
        protected DictionaryNode<K,V>[] nodes;
        protected int index;
        protected long modCheck;

        public IteratorHelper() {
            nodes = new DictionaryNode[currentSize];
            index = 0;
            modCheck = modCounter;
            for (int i=0; i<tableSize; i++) {
                for (DictionaryNode<K,V> n : list[i]) {
                    nodes[index++] = n;
                }
            }
            nodes = (DictionaryNode<K,V>[]) shellSort(nodes);
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
            return (K) nodes[index++].key;
        }
    }

    class ValueIteratorHelper<V> extends IteratorHelper<V> {
        public ValueIteratorHelper() {
            super();
        }

        public V next() {
            return (V) nodes[index++].value;
        }
    }
    
	public Iterator<K> keys() {
		return new KeyIteratorHelper();
	}

	public Iterator<V> values() {
		return new ValueIteratorHelper();
	}
	
	private DictionaryNode<K,V>[] shellSort(DictionaryNode[] n) {
        int in, out, h = 1;
        DictionaryNode<K,V> temp = null;
        int size = n.length;

        while(h <= size/3)
            h = h*3+1;
        while(h > 0) {
            for(out=h; out < size; out++) {
                temp = n[out];
                in = out;
                while(in > h-1 &&
                        ((Comparable)n[in-h]).compareTo(temp) >= 0) {
                    n[in] = n[in-h];
                    in -= h;
                }
                n[in] = temp;

            } // end for
            h = (h-1) / 3;
        } // end while
        return n;
    }
}

class LinkedListDS<E extends Comparable<E>> implements Iterable<E> {
	private class Node<T> {
		T data;
		Node<T> next;
		
		public Node(T obj) {
			data = obj;
			next = null;
		}
	}
		
	private Node<E> head,tail;
	private int currentSize;
	private long modCounter;
	
	public LinkedListDS() {
		currentSize = 0;
		modCounter = 0;
		head = tail = null;
	}
	
	/*
	 * Insert a node to the end of a linked list
	 */
	public void insert(E obj) {
		Node<E> newNode = new Node<E>(obj);
		if(head == null)
			head = tail = newNode;
		else {
			tail.next = newNode;
			tail = newNode;
		}
		currentSize++;
		modCounter++;
	}
	
	/*
	 * Remove an object from linked list.
	 */
	public E remove(E obj) {
		int index = find(obj);
        if (index == -1)
            return null;
        Node<E> previous = null, current = head;
        for (int i = 1; i < index; i++) {
            previous = current;
            current = current.next;
        }
        if (previous == null) {
            head = head.next;
        } else if (current == null) {
            previous.next = null;
            tail = previous;
        } else {
            previous.next = current.next;
        }
        currentSize--;
        modCounter++;
        return current.data;
	}
	
	/*
	 * Private Helper for remove() method.
	 * Return a position (index) of a particular object.
	 * Return -1 if not found.
	 */
	private int find(E obj) {
		Node<E> temp = head;
		if (isEmpty())
			return -1;
	    int index = 1;
	    while (temp != null && obj.compareTo(temp.data) != 0) {
	        index++;
	        temp = temp.next;
	    }
	    if (temp == null)
	    		return -1;
	    return index;
	}
	
	/*
	 * Return data field of the parameter (obj).
	 * Return null if obj not found.
	 */
	public E get(E obj) {
		Node<E> temp = head;
		int index = find(obj); // get index of this object
		if(!contains(obj) || temp == null)
			return null;  // not found
		for(int i=1; i<index; i++)
			temp = temp.next;
		return temp.data;
	}
	
	/*
	 * Return true if found object, otherwise return false.
	 */
	public boolean contains(E obj) {
		return find(obj) != -1;
	}
	
	public boolean isEmpty() {
		return currentSize == 0;
	}
	
	public boolean isFull() {
		return false;
	}
	
	public int size() {
		return currentSize;
	}
	
	public void clear() {
		head = tail = null;
		currentSize = 0;
		modCounter++;
	}
	
	public Iterator<E> iterator() {
		return new IteratorHelper();
	}
	
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
			return current != null;
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
}
