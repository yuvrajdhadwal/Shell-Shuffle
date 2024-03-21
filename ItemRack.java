import java.util.NoSuchElementException;

/**
 * Array of Items. Each element is head node of a LinkedList of same item. Similar to external chaining hashmap
 * except without most of the functionality of a hashmap.
 */
public class ItemRack {
    private Item[] backingArray;
    private int size;

    /**
     * Constructor for ItemRack
     */
    public ItemRack() {
        backingArray = new Item[5];
        size = 0;
    }

    /**
     * Adds item to the array of linkedLists
     * @param i index of array item should be added to
     */
    public void addItem(int i) {
        switch (i) {
        case 0:
            addHelper(new SawBlade(), 0);
            break;
        case 1:
            addHelper(new MagnifyingGlass(), 1);
            break;
        case 2:
            addHelper(new Beer(), 2);
            break;
        case 3:
            addHelper(new Cigerette(), 3);
            break;
        case 4:
            addHelper(new Handcuffs(), 4);
            break;
        default:
            System.out.println("Something is wrong");
        }
        size++;
    }

    /**
     * Helper method to add that actually adds the items to the array of Linked Lists.
     * @param item item to be added
     * @param i index of item
     */
    private void addHelper(Item item, int i) {
        // LinkedList.addFront(Item item);
        if (backingArray[i] != null) {
            item.setNext(backingArray[i]);
        }
        backingArray[i] = item;
    }

    /**
     * Removed method of the array of LinkedLists
     * @param i index of item to be removed
     * @param caller caller of this method
     * @param otherPlayer other player in the game
     * @param shotgun current shotgun
     * @return 0 if shoot other player, 1 if shoot yourself, other if dummy value
     */
    public int use(int i, Player caller, Player otherPlayer, Shotgun shotgun) {
        // Checks if null
        if (backingArray[i] == null) {
            throw new NoSuchElementException("Cannot use non-existent items");
        }
        // LinkedList.removeFront();
        Item item = backingArray[i];
        backingArray[i] = backingArray[i].getNext();
        size--;
        System.out.println(caller.getName() + " is using " + item.toString());
        // Uses item
        return item.use(caller, otherPlayer, shotgun);
    }

    /**
     * Overriding Object's toString Method
     * @return String representation of the ItemRack class
     */
    @Override
    public String toString() {
        String out = "";
        for (Item item : backingArray) {
            int count = 0;
            if (item != null) {
                count++;
                while (item.getNext() != null) {
                    count++;
                    item = item.getNext();
                }
                String itemName = count == 1 ? item.toString() : item.toString() + "s";
                out += count + " " + itemName + ", ";
            }
        }
        return out;
    }

    /**
     * Returns size
     * @return size
     */
    public int getSize() {
        return size;
    }
}
