package facespace;
/* QueueArray<Item>  (array-based, iterable)
 * 
 * Methods
 * =======
 * boolean isEmpty()       : returns true if the queue is empty, false otherwise
 * int size()              : returns the number of elements in the queue
 * void enqueue(Item item) : adds item to the end of the queue
 * Item dequeue()          : removes the front-most item from the queue and returns it
 * Iterator<Item> iterator()     : returns a head-to-tail iterator for the queue
 */

import java.util.Iterator;
import java.util.Scanner;


public class QueueArray<Item> implements Iterable<Item>, Queue<Item>{
    
    private Item[] items;
    private int    head;
    private int    tail;
    private int    size;
    
    public QueueArray() {
        items = (Item[]) (new Object[1]);
        head = 0;
        tail = 0;
        size = 0;
    }
    
    public boolean isEmpty() {
        return (size == 0);
    }
    
    public int size() {
        return size;
    }
    
    public void enqueue(Item item) {
        if (size == items.length - 1) {    
            resize(2 * items.length);
        }
        
        items[tail++] = item;
        
        if (tail == items.length) {
            tail = 0;  
        }
        
        size++;
    }
    
    public Item dequeue() {
        if (this.isEmpty()) {
            throw new RuntimeException("Tried to dequeue an empty queue");
        }
        else {
            Item itemToReturn = items[head];
            items[head++] = null; //prevents loitering
            size--;
            if (head == items.length) {
                head = 0;
            }
            if (size == items.length / 4) {
                resize(items.length / 2);
            }
            
            return itemToReturn;
        }
    }
    
    private void resize(int capacity) {
        //Item[] newArray = new Item[capacity];  // <-- Boo, Hiss!
        Item[] newArray = (Item[]) new Object[capacity];
        for (int i = 0; i < size; i++) {
            newArray[i] = items[(head + i) % items.length];
        }
        items = newArray;
        head = 0;
        tail = size;
    }
    
    private class FirstToLastIterator implements Iterator<Item> {

        private int pos = head;
        private int count = 0;
        
        public boolean hasNext() {
            return (count < size);
        }

        public Item next() {
            Item item = items[pos++];
            if (pos == items.length) 
                pos = 0;
            count++;
            return item;
        }
    }
    
    public Iterator<Item> iterator() {
        return new FirstToLastIterator();
    }
    
    /* FOR TESTING */
    private void printInternalStructure() {
        // NOTE: This method is private and thus can't be used outside of this class.
        // Further, it is not required by any of the methods of this class, except the
        // main() method, which is solely for testing the class.  
        //
        // It is included here and called inside main() only as a tool for understanding 
        // how the queue is represented inside the associated array, and verify that the 
        // head and tail are updated appropriately with enqueue and dequeue operations
        // 
        // So as not to overly complicate this class with a lengthy method it doesn't even
        // need to function properly, a simplifying characteristic has been assumed:
        // This method will produce good output only when items stored in the queue  
        // will print as a single character.  
        
        // print array indices on first line
        System.out.print("index :  ");
        for (int i = 0; i < items.length; i++) {
            System.out.print(i + (i<9 ? "  " : " "));
        }
        System.out.println();
        
        //print array content on second line (use "." to indicate null)
        System.out.print("content: ");
        for (int i = 0; i < items.length; i++) {
            System.out.print((items[i] == null ? "." : items[i]) + "  ");
        }
        System.out.println();
        
        //print out markers for head (H) and tail (T)
        System.out.print("         ");
        for (int i = 0; i < items.length; i++) {
            if (head == i && tail == i) System.out.print("HT ");
            else if (head == i) System.out.print("H  ");
            else if (tail == i) System.out.print("T  ");
            else System.out.print("   ");
        }
        System.out.println();
        
        //print separating line
        System.out.println();
    }

    /* FOR TESTING */
    public static void main(String[] args) {
        System.out.print("Enter a series of characters to queue ");
        System.out.println("(cause a dequeue with a '-')");
        Scanner scanner = new Scanner(System.in);
        String inputStr = scanner.nextLine();
        scanner.close();
        System.out.println();
        
        // enqueue or dequeue as indicated by the inputStr, and show the internal structure
        // of the queue at each stage...
        QueueArray<String> q = new QueueArray<String>();
        scanner = new Scanner(inputStr);
        while (scanner.hasNext()) {
            
            String token = scanner.next();
            if (token.equals("-")) {
                String dequeuedStr = q.dequeue();
                System.out.println("dequeued " + dequeuedStr);
                q.printInternalStructure();
            }
            else {
                q.enqueue(token);
                System.out.println("enqueued " + token);
                q.printInternalStructure();
            }
        }
        scanner.close();
        
        // demonstrate use of iterator
        for (String item : q) {
            System.out.print(item + " ");
        }
    }
}