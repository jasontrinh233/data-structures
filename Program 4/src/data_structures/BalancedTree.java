/*
 * Quang Trinh
 * cssc0759
 * Program 4
 * 05.05.18
 */
package data_structures;

import java.util.Iterator;
import java.util.TreeMap;

public class BalancedTree<K extends Comparable<K>, V> implements DictionaryADT<K, V> {
    TreeMap balanacedTree;
    int currentSize;
    long modCounter;
    K key;

    /*
     * Constructor
     */
    public BalancedTree() {
        balanacedTree = new TreeMap();
        currentSize = 0;
        modCounter = 0;
    }

    public boolean contains(K key) {
        return balanacedTree.containsKey(key);
    }

    public boolean add(K key, V value) {
        if (balanacedTree.containsKey(key))
            return false;
        else
            balanacedTree.put(key, value);
        modCounter++;
        return true;

    }

    public boolean delete(K key) {
        return balanacedTree.remove(key) != null;
    }

    public V getValue(K key) {
        if (isEmpty())
            return null;
        modCounter++;
        return (V) balanacedTree.get(key);
    }

    public K getKey(V value) {
        Iterator<V> values = values();
        Iterator<K> keys = keys();
        key = null;
        while (values.hasNext()) {
            if (((Comparable<V>) values.next()).compareTo(value) == 0) {
                key = keys.next();
                return key;
            }
            keys.next();
        }
        return null;
    }

    public int size() {
        return balanacedTree.size();
    }

    public boolean isFull() {
        return false;
    }

    public boolean isEmpty() {
        return balanacedTree.size() == 0;
    }

    public void clear() {
        currentSize = 0;
        modCounter++;
        balanacedTree.clear();
    }

    public Iterator<K> keys() {
        return balanacedTree.keySet().iterator();
    }

    public Iterator<V> values() {
        return balanacedTree.values().iterator();
    }
}