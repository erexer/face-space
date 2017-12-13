package facespace;
/* BagArray (generic array-based bag)
 *
 * Methods
 * =======
 * boolean isEmpty()         : returns true if bag is empty, false otherwise
 * int size()                : returns the number of items in the bag
 * void add(Item item)       : adds item to the bag
 * Iterator<Item> iterator() : returns first-added-to-last-added iterator
 */
 
import java.util.Iterator;

public class BagArray<Item> implements Bag<Item>, Iterable<Item>{
    
    private Item[] items;        
    private int size;
    
    public BagArray() {
        items = (Item[]) (new Object[1]);  
        size = 0; //superfluous
    }
    
    public boolean isEmpty() {
        return (size == 0);
    }
    
    public int size() {
        return size;
    }
    
    public void resize(int capacity) {
        Item[] newArray = (Item[]) (new Object[capacity]);  
        for (int i = 0; i < size; i++) {
            newArray[i] = items[i];
        }
        items = newArray;
    }
    
    public void add(Item item) {   
        if (size == items.length) {
            resize( 2 * items.length );
        }
        items[size++] = item;
    }
    
    private class BagIterator implements Iterator<Item> {

        private int i = 0;

        public boolean hasNext() {
            return i < size;
        }

        public Item next() {
            return items[i++];
        }
    }
    
    public Iterator<Item> iterator() {
        return new BagIterator();
    }
    
    /* FOR TESTING */
    public static void main(String[] args) {
        Bag<String> b = new BagArray<String>();
        b.add("a");
        b.add("b");
        b.add("c");
        
        System.out.println("Bag b elements after adding a, b, and c, in that order");
        for (String item : b) {
            System.out.print(item + " ");
        }
        System.out.println();
        
        System.out.println("Bag b contains " + b.size() + " items");
        System.out.println("Bag b is " + (b.isEmpty() ? "" : "not ") + "empty");
    }
}