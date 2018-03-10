/*  Micah Joseph Grande
    cssc0900
*/

package data_structures;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class Hashtable<K extends Comparable<K>,V> implements DictionaryADT<K,V>{
	
	private long modCounter;
	private int currentSize, maxSize, tableSize;
	private LinearListADT <DictionaryNode<K,V>>[] list;
	
	//Constructor for hash table
	public Hashtable(int size) {
		maxSize = size;
		currentSize = 0;
		tableSize = (int)(maxSize *1.3f);
		list = new LinearList[tableSize];
		for(int i = 0; i < tableSize; i++)
			list[i] = new LinearList<DictionaryNode<K,V>>();
	}
	
	// Returns true if the dictionary has an object identified by
	// key in it, otherwise false.
	public boolean contains(K key) {
		if( list[getIndex(key)].find(new DictionaryNode <K,V>(key,null)) == null) return false;
		return true;
	}

	// Adds the given key/value pair to the dictionary.  Returns 
	// false if the dictionary is full, or if the key is a duplicate.
	// Returns true if addition succeeded.
	public boolean add (K key, V value) {
		if (contains(key)) return false;
		if(isFull()) return false;
		DictionaryNode<K, V> newNode = new DictionaryNode <K,V>(key,value);
		list[getIndex(key)].addLast(newNode);
		currentSize++;
		modCounter++;
		return true;
	}

	// Deletes the key/value pair identified by the key parameter.
	// Returns true if the key/value pair was found and removed,
	// otherwise false.
	public boolean delete(K key) {
		if (!contains(key)) return false;
		if (isEmpty()) return false;
		list[getIndex(key)].remove(new DictionaryNode <K,V> (key, null));
		currentSize--;
		modCounter++;
		return true;
		
	}

	// Returns the value associated with the parameter key.  Returns
	// null if the key is not found or the dictionary is empty.
	public V getValue(K key) {
		DictionaryNode<K,V> tmp = list[getIndex(key)].find(new DictionaryNode <K,V>(key,null));
		if (tmp == null) return null;
		return tmp.value;
	}
	
	//Returns the index of the array based on the output of the hashCode
	private int getIndex(K key) {
		return (key.hashCode() & 0X7FFFFFFF) % tableSize;
	}

	// Returns the key associated with the parameter value.  Returns
	// null if the value is not found in the dictionary.  If more 
	// than one key exists that matches the given value, returns the
	// first one found. 
	public K getKey(V value) {
		for(int i = 0; i < tableSize; i++)
			for(DictionaryNode<K,V> n : list[i])
				if(((Comparable<V>)value).compareTo(n.value)==0) {
					return n.key;				
				}
		return null;
	}

	// Returns the number of key/value pairs currently stored 
	// in the dictionary
	public int size() {
		return currentSize;
	}

	// Returns true if the dictionary is at max capacity
	public boolean isFull() {
		if(currentSize == maxSize) return true;
		return false;
	}

	// Returns true if the dictionary is empty
	public boolean isEmpty() {
		if (currentSize == 0) return true;
		return false;
	}

	// Returns the Dictionary object to an empty state.
	public void clear() {
		currentSize = 0;
		modCounter = 0;
		for (int i = 0; i < tableSize; i++)
			list[i].clear();
	}

	// Returns an Iterator of the keys in the dictionary, in ascending
	// sorted order.  The iterator must be fail-fast.
	public Iterator<K> keys() {
		return new KeyIteratorHelper<K>();
		
	}

	// Returns an Iterator of the values in the dictionary.  The
	// order of the values must match the order of the keys. 
	// The iterator must be fail-fast. 
	public Iterator<V> values() {
		return new ValueIteratorHelper<V>();
	}
	
	//Iterates through the array in sorted order
	abstract class IteratorHelper<E> implements Iterator<E>{
		
		protected DictionaryNode<K,V> [] nodes;
		protected int index;
		protected long modCheck;
		
		//Constructor for Iterator Helper that iterates in ascending order
		public IteratorHelper() {
			nodes = new DictionaryNode[currentSize];
			index = 0;
			int j = 0;
			modCheck = modCounter;
			for(int i = 0; i < tableSize; i++)
				for(DictionaryNode<K,V> n : list[i])
					nodes[j++] = n;
			nodes = (DictionaryNode<K,V>[]) shellSort(nodes);
		}
		
		public boolean hasNext() {
			if(modCheck != modCounter)
				throw new ConcurrentModificationException();
			return index < currentSize;
		}
		
		public abstract E next();
		
		public void remove() {
			throw new UnsupportedOperationException();
		}
		
		//Returns a sorted array of the array passed in
		private DictionaryNode<K,V>[] shellSort(DictionaryNode<K,V>[] array) {
			DictionaryNode<K,V>[] n = array;
			DictionaryNode<K,V> tmp;
			int in, out, h = 1;
			int size = n.length;
			
			while (h <= size/3) //calculate gaps
				h = h*3+1;
			while(h>0) {
				for(out = h; out < size; out++) {
					tmp = n[out];
					in = out;
					while(in > h-1 && !(n[in-h].compareTo(tmp) == -1)) {
						n[in] = n[in-h];
						in -= h;
					}
				n[in] = tmp;
				}
			h = (h-1)/3;	
			}
			return n;
		}
	}
	
	//Calls an Iterator Helper specific for keys
	class KeyIteratorHelper<K> extends IteratorHelper<K> {
		
		public KeyIteratorHelper() {
			super();
		}
		
		public K next() {
			return (K) nodes[index++].key;
		}		
	}
	
	//Calls an Iterator Helper specific to values
	class ValueIteratorHelper<V> extends IteratorHelper<V>{
		
		public ValueIteratorHelper() {
			super();
		}
		
		public V next() {
			return (V) nodes[index++].value;
		}
	}
	
	//Wrapper node to contain both values
	class DictionaryNode <K,V> implements Comparable <DictionaryNode<K,V>>{
		K key;
		V value;
		DictionaryNode(K key, V value){
			this.key = key;
			this.value = value;
		}
		public int compareTo (DictionaryNode<K,V> node) {
			return ((Comparable<K>)key).compareTo((K)node.key);
		}
	}

	}
