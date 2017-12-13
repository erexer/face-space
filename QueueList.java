package facespace;

/* QueueList<Item>  (linked-list based, iterable)
 * 
 * Methods
 * =======
 * boolean isEmpty()           : returns true if the queue is empty, false otherwise
 * int size()                  : returns the number of elements in the queue
 * void enqueue(Item item)     : adds item to the end of the queue
 * Item dequeue()              : removes the front-most item from the queue and returns it
 * Iterator iterator()         : returns a head-to-tail iterator for the queue
 */

import java.util.Iterator;

public class QueueList<Item> implements Queue<Item>, Iterable<Item> {
    
    private class Node {
        Item item;
        Node next;
    }
   
    private Node first;
    private Node last; 
    private int size;
    
    public boolean isEmpty() {
        return (size == 0);
    }
    
    public int size() {
    	return size;
    }
    
    public void enqueue(Item item) {
        Node node = new Node();
        node.item = item;
        
        if (this.isEmpty()) 
            first = node;
        else  
            last.next = node;
        last = node;
        size++;
    }
   
    public Item dequeue() {            // Three cases to consider:
        if (this.isEmpty()) {          // 0 nodes in the queue
            return null; 
        }
        else if (first == last) {      // 1 node in the queue
            Item item = first.item;
            first = null;
            last = null;
            size--;
            return item;               
        }
        else {                         // 2+ nodes in the queue
            Item item = first.item;      
            first = first.next; 
            size--;
            return item;  
        }
    }

    private class FirstToLastIterator implements Iterator<Item>{
        Node node = first;

        public boolean hasNext() {
            return (node != null);
        }

        public Item next() {
            Item itemToReturn = node.item;
            node = node.next;
            return itemToReturn;
        }
    }
    
    public Iterator<Item> iterator() {
        return new FirstToLastIterator();
    }

    /* FOR TESTING */
    public static void main(String[] args) {
        QueueList<String> q = new QueueList<String>();
        q.enqueue("a");
        q.enqueue("b");
        q.enqueue("c");
        q.enqueue("d");
        
        System.out.println("queue elements after enqueuing a, b, c, d in that order");
        for (String s : q) {
        	System.out.print(s + " ");
        }
        System.out.println();
        
        q.dequeue();
        q.dequeue();
        
        System.out.println("queue elements after dequeuing two elements");
        for (String s : q) {
        	System.out.print(s + " ");
        }
        System.out.println();
        
        System.out.println("q now has size " + q.size());
        System.out.println("q is " + (q.isEmpty() ? "" : "not ") + "empty");
    }
}