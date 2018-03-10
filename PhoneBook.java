/*  Micah Joseph Grande
    cssc0900
*/

import data_structures.*;

import java.util.Iterator;
import java.io.*;

public class PhoneBook <K extends Comparable<K>,V> {
	private DictionaryADT <K,V> list;

	// Constructor. There is no argument-less contructor, or default size
	public PhoneBook(int maxSize) {
		list = (DictionaryADT<K,V>) new Hashtable<Integer,Integer>(maxSize);
//		list = (DictionaryADT<K,V>) new BinarySearchTree<Integer,Integer>();
//		list = (DictionaryADT<K,V>) new BalancedTree<Integer,Integer>();
	}
	
	// Reads PhoneBook data from a text file and loads the data into
	// the PhoneBook. Data is in the form "key=value" where a phoneNumber
	// is the key and a name in the format "Last, First" is the value
	public void load(String filename) {
		String line;
		try {
			BufferedReader in = new BufferedReader(new FileReader(filename));
			while((line = in.readLine()) != null) {
				String[] split = line.split("=");
				PhoneNumber tmp = new PhoneNumber(split[0]);
				addEntry(tmp,split[1]);
			}
			in.close();
		} catch(Exception e) {
			System.out.println(e);
		}
	}
	
	// Returns the name associated with the given PhoneNumber, if it is
	// in the PhoneBook, null if it is not.
	public String numberLookup(PhoneNumber number) {
		return list.getValue((K)number).toString();
	}
	
	// Returns the PhoneNumber associated with the given name value.
	// There nay be duplicate values, return the first one found.
	// Return null if the name is not in the PhoneBook.
	public PhoneNumber nameLookup(String name) {
		return (PhoneNumber) list.getKey((V)name);
	}
	
	// Adds a new PhoneNumber = name pair to the PhoneBook. All
	// names should be in the form "Last, First".
	// Duplicate entries are *not* allowed. Return true if the
	// insertion succeeds otherwise false (PhoneBook is full or
	// the new record is a duplicate). Does not change the datafile on disk
	public boolean addEntry(PhoneNumber number, String name) {
		return(list.add((K)number, (V)name));
	}
	
	// Deletes the record associated with the PhoneNumber if it is
	// in the PhoneBook. Returns true if the number was found and
	// its record deleted, otherwise false. Does not change the datafile on disk.
	public boolean deleteEntry(PhoneNumber number) {
		return(list.delete((K)number));
			
		
	}
	
	// Prints a directory of all PhoneNumbers with their associated
	// names, in sorted order (ordered by PhoneNumber).
	public void printAll() {
		Iterator<PhoneNumber> keys = list.keys();
		while(keys.hasNext()) {
			PhoneNumber tmp = keys.next();
			System.out.println(tmp.toString() + ": " + list.getValue((K)tmp));;
		}
	}
	
	// Prints all records with the given Area Code in ordered
	// sorted by PhoneNumber.
	public void printByAreaCode(String code) {
		Iterator<PhoneNumber> keys = list.keys();
		while(keys.hasNext()) {
			PhoneNumber area = keys.next();
			if(code.compareTo(area.areaCode) == 0)
				System.out.println(area.toString() + ": " + list.getValue((K)area));
				
		}
	}
	
	// Prints all of the names in the directory, in sorted order (by name,
	// not by number). There may be duplicates as these are the values.
	public void printNames() {
		Iterator<String> values = list.values();
		String[] Vvalues = new String[list.size()];
		int i = 0;
		while (values.hasNext())
			Vvalues[i] = values.next();
		for(int j = 0; j == list.size(); j++)
			System.out.println(Vvalues[j]);
	}
	
	private String[] ShellSort(String[] array) {
		String[] n = array;
		String tmp;
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
