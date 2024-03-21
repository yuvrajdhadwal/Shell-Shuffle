import java.util.HashSet;
import java.util.NoSuchElementException;

/**
 * Top 10 Leaderboard of the AccountBank. Implemented as a MaxHeap.
 */
public class Leaderboard {

    // The initial capacity of the MaxHeap when created with the default constructor.
    public static final int INITIAL_CAPACITY = 13;

    private Account[] backingArray;
    private int size;

    /**
     * Constructs a new LeaderBoard.
     *
     * The backing array should have an initial capacity of INITIAL_CAPACITY.
     */
    public Leaderboard() {
        backingArray = new Account[INITIAL_CAPACITY];
        size = 0;
    }

    /**
     * Creates a properly ordered heap from a set of initial values.
     *
     * Uses BuildHeap algorithm to convert array to MaxHeap data structure.
     * As a reminder, this is the algorithm that involves building the heap
     * from the bottom up by repeated use of downHeap operations.
     *
     * The backingArray should have capacity 2n + 1 (including the empty 0
     * index) where n is the number of data in the passed in ArrayList (not
     * INITIAL_CAPACITY). Index 0 should remain empty, indices 1 to n should
     * contain the data in proper order, and the rest of the indices should
     * be empty.
     *
     * @param data a list of data to initialize the heap with
     * @throws java.lang.IllegalArgumentException if data is null
     */
    public Leaderboard(Account[] data) {
        if (data == null) {
            throw new IllegalArgumentException("Build Heap cannot be done on null data structure");
        }

        // Creates complete binary tree
        backingArray = new Account[2 * data.length + 1];
        for (int i = 0; i < data.length; i++) {
            if (data[i] == null) {
                continue;
            }
            Account acc = data[i];
            backingArray[size + 1] = new Account(acc.getUsername(), acc.getPassword(), acc.getWins(), acc.getLosses());
            size++;
        }

        // Runs DownHeap on all internal nodes to convert to MaxHeap
        int numInternalNodes = data.length / 2;
        while (numInternalNodes > 0) {
            downHeap(numInternalNodes);
            numInternalNodes--;
        }
    }

    /**
     * Downheaps parent node with its children
     * @param index index of parent node
     */
    private void downHeap(int index) {
        // Checks if node is actually an internal node
        if (index > size / 2) {
            return;
        }

        Account parent = backingArray[index];
        Account leftChild = backingArray[index * 2];
        Account rightChild = backingArray[index * 2 + 1];

        // Checks if bad relationship between parent and right child and if right child worse than left child
        if (rightChild != null && rightChild.compareTo(leftChild) > 0) {
            if (rightChild.compareTo(parent) > 0) {
                // Swaps values
                backingArray[index] = rightChild;
                backingArray[index * 2 + 1] = parent;
                // Downheap on the new right child
                downHeap(index * 2 + 1);
            }
        // Checks if bad relationship between parent and left child
        } else if (leftChild.compareTo(parent) > 0) {
            // swap values
            backingArray[index] = leftChild;
            backingArray[index * 2] = parent;
            // Downheap on new left child
            downHeap(index * 2);
        }
    }

    /**
     * Adds the data to the heap.
     *
     * If sufficient space is not available in the backing array (the backing
     * array is full except for index 0), resize it to double the current
     * length.
     *
     * @param data the account to add
     * @throws java.lang.IllegalArgumentException if account is null
     */
    public void add(Account data) {
        // If given data is null
        if (data == null) {
            throw new IllegalArgumentException("Data added cannot be null");
        }

        // Resize if needed
        if (size == backingArray.length - 1) {
            Account[] temp = backingArray;
            backingArray = new Account[backingArray.length * 2];
            for (int i = 0; i < temp.length; i++) {
                backingArray[i] = temp[i];
            }
        }

        // Ensures complete binary tree
        backingArray[size + 1] = data;
        size = size + 1;
        // Upheap to make sure it is MaxHeap
        upHeap(size);
    }

    /**
     * Upheaps nodes in the maxHeap
     * @param index index of node to be upHeaped
     */
    private void upHeap(int index) {
        // Checks to see if reached root node
        if (index == 1) {
            return;
        }

        Account child = backingArray[index];
        Account parent = backingArray[index / 2];

        // If bad relationship with parent
        if (child.compareTo(parent) > 0) {
            // swap
            backingArray[index] = parent;
            backingArray[index / 2] = child;
            // upheap on new parent
            upHeap(index / 2);
        }

    }

    /**
     * Removes and returns the root of the heap.
     *
     * Do not shrink the backing array.
     *
     * Replace any unused spots in the array with null.
     *
     * @return the data that was removed
     * @throws java.util.NoSuchElementException if the heap is empty
     */
    public Account remove() {
        // Checks if Leaderboard is empty
        if (size == 0) {
            throw new NoSuchElementException("Cannot remove from empty heap");
        }
        // Removes root and replaces it with last value to ensure complete binary tree
        Account out = backingArray[1];
        backingArray[1] = backingArray[size];
        backingArray[size] = null;
        size--;
        // Downheap on root to ensure it is still MaxHeap
        downHeap(1);
        return out;
    }

    /**
     * Returns the maximum element in the heap.
     *
     * @return the maximum element
     * @throws java.util.NoSuchElementException if the heap is empty
     */
    public Account getMax() {
        // Check if Leaderboard is empty
        if (size == 0) {
            throw new NoSuchElementException("Cannot get max from empty heap");
        }

        return backingArray[1];
    }

    /**
     * Returns whether the heap is empty.
     *
     * @return true if empty, false otherwise
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Clears the heap.
     *
     * Resets the backing array to a new array of the initial capacity and
     * resets the size.
     */
    public void clear() {
        backingArray = (Account[]) new Comparable[INITIAL_CAPACITY];
        size = 0;
    }

    /**
     * Returns the backing array of the heap.
     *
     * @return the backing array of the list
     */
    public Account[] getBackingArray() {
        return backingArray;
    }

    /**
     * Returns the size of the heap.
     * @return the size of the list
     */
    public int size() {
        return size;
    }

    /**
     * Overrides Object's toString Method
     * @return String representation of Leaderboard
     */
    @Override
    public String toString() {
        String out = "----------------------------------\nLeaderboard:\n";
        out += toStringHelper();
        out += "----------------------------------\n";
        return out;
    }

    /**
     * Helper method for toString.
     * MaxHeap remove returns largest value of the MaxHeap. Repeat this 10 times to get Top 10.
     * Add these top 10 back into the MaxHeap to ensure it is the same still.
     * @return String representation of top 10 accounts
     */
    private String toStringHelper() {
        HashSet<Account> hashSet = new HashSet<>();
        String out = "";
        for (int i = 0; i < 10; i++) {
            if (size == 0) {
                break;
            }
            Account removed = remove();
            hashSet.add(removed);
            out += (i + 1) + ". " + removed;
        }
        for (Account account : hashSet) {
            add(account);
        }
        return out;
    }
}