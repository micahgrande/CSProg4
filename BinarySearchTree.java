/*  Micah Joseph Grande
    cssc0900
*/

package data_structures;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;


	
public class BinarySearchTree<K extends Comparable<K>,V> implements DictionaryADT<K,V> {
	private Node<K,V> root;
	private int currentSize;
	private long modCounter;
	private K getK;
	
	//Tree Constructor
	public BinarySearchTree() {
		root = null;
		currentSize = 0;
	}
	
	//Tree Node Constructor
	private class Node <K,V> {
		private K key;
		private V value;
		private Node<K,V> left;
		private Node<K,V> right;
		
		public Node (K k, V v) {
			key = k;
			value = v;
			left = right = null;
		}
	}

	// Returns true if the dictionary has an object identified by
	// key in it, otherwise false.
	public boolean contains(K key) {
		return (find(key, null) != null);
		
	}

	// Adds the given key/value pair to the dictionary.  Returns 
	// false if the dictionary is full, or if the key is a duplicate.
	// Returns true if addition succeeded.
	public boolean add(K key, V value) {
		if(contains(key)) return false;
		if(root == null) root = new Node<K,V>(key, value);
		else insert(key, value, root, null, false);
		currentSize++;
		modCounter++;
		return true;
	}
	
	private void insert(K key, V value, Node<K,V> n, Node<K,V> parent, boolean wentLeft) {
		if (n == null) {
			if(wentLeft) parent.left = new Node<K,V>(key, value);
			else parent.right = new Node<K,V> (key, value);
		}
		else if (((Comparable<K>)key).compareTo(n.key) < 0)
			insert(key, value, n.left, n, true);
		else insert(key, value, n.right, n, false);
	}

	// Deletes the key/value pair identified by the key parameter.
	// Returns true if the key/value pair was found and removed,
	// otherwise false.
	public boolean delete(K key) {
		if(root == null) return false;
		if(!deleteHelper(key, root, null, false)) return false;
		currentSize--;
		modCounter++;
		return true;
	}
	
	//Checks if the node passed in is the one to delete and deletes it if found.
	//Returns true if node was in list. Returns false if node is not found
	private boolean deleteHelper(K key, Node<K,V> node, Node<K,V> parent, boolean wentLeft){
		if(node == null) return false;
		if(node.key.compareTo(key) == 0) {
			if (node.left == null && node.right == null) {
				if (parent == null) root = null;
				else if (wentLeft) parent.left = null;
				else parent.right = null;
			}
			else if (node.left == null) {
				if (parent == null) root = node.right;
				else if (wentLeft) parent.left = node.right;
				else parent.right = node.right;
			}
			else if (node.right == null) {
				if (parent == null) root = node.left;
				else if (wentLeft) parent.left = node.left;
				else parent.right = node.left;
			}
			else {
				Node<K,V> tmp = successorParent(node);
				if (tmp == null) {
					node.key = node.right.key;
					node.value = node.right.value;
					node.right = node.right.right;					
				}
				else {
					node.key = tmp.key;
					node.value = tmp.value;
				}
			}
		}
		else if (((Comparable<K>)key).compareTo(node.key) < 0)
			deleteHelper(key, node.left, node, true);
		else 
			deleteHelper(key, node.right, node, false);
		return true;
	}
	
	//Returns the In Order Successor of any given Node
	private Node<K,V> successorParent(Node<K,V> node){
		Node<K,V> parent = null;
		Node<K,V> child = node.right;
		while (child.left != null) {
			parent = child;
			child = child.left;
		}
		if (parent == null)
			return null;
		else
			parent.left = child.right;
		return parent;
	}

	// Returns the value associated with the parameter key.  Returns
	// null if the key is not found or the dictionary is empty.
	public V getValue(K key) {
		if(isEmpty())
			return null;
		return find(key, root);
	}
	
	private V find(K key, Node<K,V> n) {
		if (n == null) return null;
		if(((Comparable<K>)key).compareTo(n.key) < 0)
			return find(key,n.left);
		if(((Comparable<K>)key).compareTo(n.key) > 0)
			return find (key, n.right);
		return (V) n.value;
	}

	// Returns the key associated with the parameter value.  Returns
	// null if the value is not found in the dictionary.  If more 
	// than one key exists that matches the given value, returns the
	// first one found. 
	public K getKey(V value) {
		if (isEmpty()) return null;
		getK = null;
		findK(root, value);
		return getK;
	}
	
	private void findK(Node<K,V> n, V value) {
		if (n == null) return;
		if (((Comparable<V>)value).compareTo(n.value) == 0) {
			getK = n.key;
			return;
		}
		findK(n.left, value);
		findK(n.right, value);
	}

	// Returns the number of key/value pairs currently stored 
	// in the dictionary
	public int size() {
		return currentSize;
	}

	// Returns true if the dictionary is at max capacity
	public boolean isFull() {
		return false;
	}

	// Returns true if the dictionary is empty
	public boolean isEmpty() {
		return(currentSize == 0);
	}

	// Returns the Dictionary object to an empty state.
	public void clear() {
		root = null;
		currentSize = 0;
		modCounter = 0;
	}
	

	// Returns an Iterator of the keys in the dictionary, in ascending
	// sorted order.  The iterator must be fail-fast.
	public Iterator keys() {
		return new KeyIteratorHelper<K>();
	}

	// Returns an Iterator of the values in the dictionary.  The
	// order of the values must match the order of the keys. 
	// The iterator must be fail-fast. 
	public Iterator values() {
		return new ValueIteratorHelper<K>();
	}
	
	//Iterator helper for both keys and values
	abstract class IteratorHelper<E> implements Iterator<E>{
		
		protected Node<K,V> [] nodes;
		protected int index, j;
		protected long modCheck;
		
		public IteratorHelper() {
			nodes = new Node[currentSize];
			index = 0;
			j = 0;
			modCheck = modCounter;
			inOrder(root);
		}
		
		public boolean hasNext() {
			if(modCheck != modCounter)
				throw new ConcurrentModificationException();
			return (index < currentSize);
		}
		
		private void inOrder(Node<K,V> node) {
			if (node == null)
				return;
			inOrder(node.left);
			nodes[j++] = node;
			inOrder(node.right);
		}
		
		public abstract E next();
		
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}
	
	
	//Iterator Helper specific for keys
	class KeyIteratorHelper<K> extends IteratorHelper<K> {
		
		public KeyIteratorHelper() {
			super();
		}
		
		public K next() {
			if(!hasNext())
				throw new NoSuchElementException();
			return (K) nodes[index++].key;
		}		
	}
		
	//Iterator Helper specific to values
	class ValueIteratorHelper<V> extends IteratorHelper<V>{
			
		public ValueIteratorHelper() {
			super();
		}
			
		public V next() {
			if(!hasNext())
				throw new NoSuchElementException();
			return (V) nodes[index++].value;
		}
	}

}
