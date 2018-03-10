import java.util.Iterator;
import java.util.ConcurrentModificationException;
import data_structures.*;

public class DictionaryTester {
    public static void main(String [] args) {
        final int SIZE = 1000000;
        long start, stop;
        int [] array = new int[SIZE];
        DictionaryADT<Integer,Integer> dictionary = 
//            new Hashtable<Integer,Integer>(SIZE);
//            new BinarySearchTree<Integer,Integer>();
            new BalancedTree<Integer,Integer>();                    
        for(int i=0; i < SIZE; i++) 
            array[i] = (i+1);
        for(int i=0; i < SIZE; i++) {
            int index = (int)(SIZE*Math.random());
            int tmp = array[i];            
            array[i] = array[index];
            array[index] = tmp;
            }                        
            
        System.out.println("Adding elements to dictionary");
        start = System.currentTimeMillis();            
        for(int i=0; i < SIZE; i++)
            if(!dictionary.add(array[i] , array[i])) {
                System.out.println("ERROR, insertion failed!");
                System.exit(0);
                }
        stop = System.currentTimeMillis();
        System.out.println("Time for insertion of " + SIZE + " elements: " +
            (stop-start));                
        if(dictionary.size() != SIZE)
            System.out.println("ERROR in size(), should be " + SIZE +
                " but the method retured " + dictionary.size());
                
        System.out.println("Now doing lookups");  
        start = System.currentTimeMillis();              
        for(int i=0; i < SIZE; i++) {
            Integer tmp = dictionary.getValue(array[i]);
            if(tmp == null) {
                System.out.println("ERROR, getValue failed!");
                System.exit(0);
                }
            }
        stop = System.currentTimeMillis();
        System.out.println("Time for getValue with " + SIZE + " elements: " +
            (stop-start));            
            
        for(int i=0; i < 100; i++) {
            Integer tmp = dictionary.getKey(array[i]);
            if(tmp == null) {
                System.out.println("ERROR, getKey failed!");
                System.exit(0);
                }
            } 
        
        System.out.println("Now Doing deletion");  
        start = System.currentTimeMillis();          
        for(int i=0; i < SIZE; i++)
            if(!dictionary.delete(array[i])) {
                System.out.println("ERROR, deletion failed!");
                System.exit(0);
                } 
        stop = System.currentTimeMillis();
        System.out.println("Time for deletion with " + SIZE + " elements: " +
            (stop-start));        
                
        if(dictionary.size() != 0)
            System.out.println("ERROR in size(), should be 0 " +
                " but the method retured " + dictionary.size());  
                
        for(int i=0; i < SIZE; i++) {
            Integer tmp = dictionary.getValue(array[i]);
            if(tmp != null) {
                System.out.println("ERROR, getValue failed, found a deleted value at index " + i+"!");
                System.exit(0);
                }
            }  
            
        dictionary.clear();
        
        for(int i=1; i <= 10; i++)
            dictionary.add(i,i);                                                 
                     
        Iterator<Integer> keys = dictionary.keys();
        Iterator<Integer> values = dictionary.values();
        
        System.out.println("The iterators should print 1 .. 10");
        while(keys.hasNext()) {
            System.out.print(keys.next());
            System.out.print("   " + values.next());
            System.out.println();
        }        
        
        try {
            keys = dictionary.keys();
            values = dictionary.values();
            dictionary.add(100,100);   // add element to taint iterators         
            while(keys.hasNext()) {
                Integer tmp = keys.next();
                Integer tmp2 = values.next();
                System.out.println("ERROR, iterator is not fail-fast");
                }       
            }
        catch(ConcurrentModificationException e) {
            System.out.println("OK, iterators are fail-fast");
            }
        catch(Exception e) {
            System.out.println("Iterators are fail-fast, but threw the " +
                "wrong exception " + e);
            }
        dictionary.clear();    
                
        keys = dictionary.keys();
        System.out.println("Now calling iterator on EMPTY structure: ");
        System.out.println("NO output should follow this line ");        
        while(keys.hasNext()) 
            System.out.print(keys.next()+" ");                  
    }
}
