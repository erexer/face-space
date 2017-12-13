package facespace;
import java.util.Iterator;

public interface Bag<Item> extends Iterable<Item> {
    boolean isEmpty();         // returns true if bag is empty, false otherwise
    int size();                // returns the number of items in the bag
    void add(Item item);       // adds item to the bag
    Iterator<Item> iterator(); // returns first-added-to-last-added iterator
}