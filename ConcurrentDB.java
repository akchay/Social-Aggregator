package Shoutbox;
/*To implement the conDB functionality which needed to support writes from multiple threads 
and at the same time allow the reader thread to pick updates from the store and print it on the 
console in sorted order, we have used the tree map data structure in which at any time the key-value pairs
are in a sorted manner with the oldest update at the top as it will have the least date value which is the key.
The original tree-map data structure is not amenable for concurrent accesses so we had to syncronize by making use of 
locks.
we use the delegate model of using a data-structure and converting it to allow concurrent functionality*/
import java.util.Date;
import java.util.TreeMap;
import java.util.concurrent.locks.ReentrantLock;
public class ConcurrentDB<K,V> {
  //the core Tree map object is initialized here.
	TreeMap<K,V> core = new TreeMap<K,V>();
  //create  a lock object to control access to the data structure.
	ReentrantLock  lock = new ReentrantLock ();
  //constructor for the data structure.  
    public ConcurrentDB()
    {
        core = new TreeMap<K,V>();
    }
    //put is a mutating method which would change the data-structure, also it will be 
    //used by multiple threads at the same time.so we use locking to order access to this method.
    public void put(K key,V value)
    {
       lock.lock();
       try
       {
         core.put(key,value);
          }
       finally
       {
           lock.unlock();
       }
    }
    //pop object will be used by the reader thread while its going to
    //extract queries from the database,it is also a mutating operation so we
    //need a lock.
    public Entry pop()
    {
        lock.lock();
        try
        {  //get the first/oldest date(key) from the map.
           Date key =  (Date) core.firstKey(); 
           //get the value associated with this key, this would be an update(FB)/tweet.
           String value = (String) core.get(key);
           //print this on the console.
           System.out.println(key+":"+value);
           Entry retVal = new Entry(key,value);
           //remove the (key-value) pair from the map once it has been written, so in next pop we will
           //get the next oldeset update.
           core.remove(key);
           return retVal;
        }
        finally
        {
           lock.unlock();
        }
                
    }
    //size of the map changes after every operation, we cannot read it while
    //the structure is being modified.so use locks here too.
    public  int size(){
        lock.lock();
        try
        {
    	return core.size();}
        
        finally{
            lock.unlock();
        }
    }
}