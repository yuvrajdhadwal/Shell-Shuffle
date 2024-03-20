import java.util.NoSuchElementException;

/**
 * Data Structure of Accounts implemented as a Linear Probing HashMap.
 */
public class AccountBank {

    /**
     * The initial capacity of the LinearProbingHashMap when created with the
     * default constructor.
     */
    public static final int INITIAL_CAPACITY = 13;

    /**
     * The max load factor of the LinearProbingHashMap
     */
    public static final double MAX_LOAD_FACTOR = 0.67;

    private Account[] table;
    private int size;

    /**
     * Constructs a new LinearProbingHashMap.
     * The backing array should have an initial capacity of INITIAL_CAPACITY.
     * Use constructor chaining.
     */
    public AccountBank() {
        this(INITIAL_CAPACITY);
    }

    /**
     * Constructs a new LinearProbingHashMap.
     * @param initialCapacity the initial capacity of the backing array
     */
    public AccountBank(int initialCapacity) {
        size = 0;
        table = new Account[initialCapacity];
    }

    /**
     * Adds the given account to the map. If an entry in the map
     * already has this key, replace the entry's value with the new one
     * passed in.
     *
     * In the case of a collision, use linear probing as resolution
     * strategy.
     *
     * Before actually adding any data to the HashMap, checks to
     * see if the array would violate the max load factor if the data was
     * added. For example, let's say the array is of length 5 and the current
     * size is 3 (LF = 0.6). For this example, assume that no elements are
     * removed in between steps. If another entry is attempted to be added,
     * before doing anything else, it should check whether (3 + 1) / 5 = 0.8
     * is larger than the max LF. It is, so it would trigger a resize before
     * it even attempts to add the data or figure out if it's a duplicate.
     *
     * When regrowing, resize the length of the backing table to
     * 2 * old length + 1. Uses the resizeBackingTable method to do so.
     *
     * Return null if the key was not already in the map. If it was in the map,
     * return the old username associated with it.
     *
     * @param account - the account to add to the AccountBank (LinearProbingHashMap)
     * @return null if the key was not already in the map. If it was in the
     * map, return the old username associated with it
     * @throws java.lang.IllegalArgumentException if key or value is null
     */
    public String put(Account account) {
        // Checks if valid input
        if (account.getUsername() == null || account.getPassword() == null) {
            throw new IllegalArgumentException("Key or Value cannot be null");
        }

        // Checks if it needs to resize
        double loadFactor = (double) (size + 1) / table.length;
        if (loadFactor > MAX_LOAD_FACTOR) {
            resizeBackingTable(table.length * 2 + 1);
        }

        // Adds element at index
        int curr = Math.abs(account.getUsername().hashCode() % table.length);
        int delIndex = -1;
        int probeSize = 0;
        String out = null;
        // While values at index and past index are not null and not equal to account we want to add
        // and are removed already and our current run is not longer than how many elements in the table keep probing
        while (table[curr] != null && probeSize < size
                && !(table[curr].getUsername().equals(account.getUsername()) && table[curr].isRemoved())) {
            // First DEL marker found, save to place new account. Keep incrementing
            if (table[curr].isRemoved() && delIndex < 0) {
                delIndex = curr;
                curr = Math.abs((curr + 1) % table.length);
            // Found the same account
            } else if (table[curr].getUsername().equals(account.getUsername())) {
                out = table[curr].getUsername();
                break;
            // Keep probing
            } else {
                probeSize = table[curr].isRemoved() ? probeSize : probeSize + 1;
                curr = Math.abs((curr + 1) % table.length);
            }
        }
        // If there was a DEL marker before where we found the next best spot to put the value
        if (delIndex < 0) {
            if (table[curr] == null || table[curr].isRemoved()) {
                size++;
            }
            table[curr] = new Account(account.getUsername(), account.getPassword(), account.getWins(),
                    account.getLosses());
            return out;
        // No earlier DEL marker. Spot we have found is best spot for new Account
        } else {
            if (!table[delIndex].isRemoved()) {
                out = table[delIndex].getUsername();
            } else {
                size++;
            }
            table[delIndex] = new Account(account.getUsername(), account.getPassword(), account.getWins(),
                    account.getLosses());
            return out;
        }
    }


    /**
     * Removes the entry with a matching key from map by marking the entry as
     * removed.
     *
     * @param account account to be removed
     * @return username based off the key
     * @throws java.lang.IllegalArgumentException if account is null
     * @throws java.util.NoSuchElementException   if the account is not in the map
     */
    public String remove(Account account) {
        // Checks if account to be removed is null
        if (account == null) {
            throw new IllegalArgumentException("Account cannot be null");
        }
        // Saves username of account as key
        String key = account.getUsername();

        int index = Math.abs(key.hashCode() % table.length);

        int counted = 0;
        // While we have not null elements and have not found valid account and not past size, keep probing
        while (table[index] != null && !table[index].getUsername().equals(key) && counted < size) {
            counted = table[index].isRemoved() ? counted : counted + 1;
            index = Math.abs((index + 1) % table.length);

        }
        // If did not find account or account has already been removed
        if (table[index] == null || table[index].isRemoved()) {
            throw new NoSuchElementException("Account is not in map");
        } else {
            table[index].setRemoved(true);
            size--;
            return table[index].getUsername();
        }
    }

    /**
     * Checks whether there is account in HashMap that has the same username and password as the parameters
     * @param username username provided by user
     * @param password password provided by user
     * @return true if valid account credentials. false if invalid account credentials
     */
    public boolean checkValue(String username, String password) {
        return getPassword(new Account(username, password)).equals(password);
    }

    /**
     * Gets the password associated with the given key. Helper method for checkValue()
     *
     * @param account the account to search for in the map
     * @return the password associated with the given account
     * @throws java.lang.IllegalArgumentException if account is null
     * @throws java.util.NoSuchElementException   if the account is not in the map
     */
    private String getPassword(Account account) {
        // Checks if account is null
        if (account == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }

        String key = account.getUsername();
        int counted = 0;
        int index = Math.abs(key.hashCode() % table.length);
        // While current element is not null and not account we want and removed,
        // and we have not probed through full HashMap
        while (table[index] != null
                && !(table[index].getUsername().equals(key) && table[index].isRemoved())
                && (counted < size)) {
            if (table[index].getUsername().equals(key)) {
                return table[index].getPassword();
            }
            counted = table[index].isRemoved() ? counted : counted + 1;
            index = (index + 1) % table.length;
        }
        throw new NoSuchElementException("Key is not in map");
    }

    /**
     * Returns account in AccountBank associated with a certain username. Meant to be called once we confirm user
     * knows account username and password. Meant for getting wins and losses values and updating them.
     * @param account account knowing username
     * @return account given username, password, wins, and losses
     */
    public Account getAccount(Account account) {
        // Checks if account is null
        if (account == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }

        String key = account.getUsername();
        int counted = 0;
        int index = Math.abs(key.hashCode() % table.length);

        // Similar while loop to previous functions
        while (table[index] != null
                && !(table[index].getUsername().equals(key) && table[index].isRemoved())
                && (counted < size)) {
            if (table[index].getUsername().equals(key)) {
                return table[index];
            }
            counted = table[index].isRemoved() ? counted : counted + 1;
            index = (index + 1) % table.length;
        }
        throw new NoSuchElementException("Key is not in map");
    }

    /**
     * Resize the backing table to length.
     *
     * @param length new length of the backing table
     * @throws java.lang.IllegalArgumentException if length is less than the
     *                                            number of items in the hash
     *                                            map
     */
    public void resizeBackingTable(int length) {
        if (length < size) {
            throw new IllegalArgumentException("Cannot resize to length smaller than HashMap size");
        }
        Account[] temp = table;
        int newSize = 0;
        table = new Account[length];
        for (Account element : temp) {
            // Breaks for loop if we have added all the elements to the new array
            if (newSize == size) {
                break;
            }
            // if element is not null and not removed we should add it to the hashmap.
            if (element != null && !element.isRemoved()) {
                int curr = Math.abs(element.getUsername().hashCode() % table.length);
                int delIndex = -1;

                while (table[curr] != null) {
                    if (element.isRemoved() && delIndex < 0) {
                        delIndex = curr;
                    } else {
                        curr = Math.abs((curr + 1) % table.length);
                    }
                }
                if (delIndex < 0) {
                    table[curr] = element;
                    newSize++;
                } else {
                    table[delIndex] = element;
                }
            }
        }

    }

    /**
     * Clears the map.
     * Resets the table to a new array of the INITIAL_CAPACITY and resets the
     * size.
     * Must be O(1).
     */
    public void clear() {
        size = 0;
        table = new Account[INITIAL_CAPACITY];
    }

    /**
     * Returns the table of the map.
     * @return the table of the map
     */
    public Account[] getTable() {
        return table;
    }

    /**
     * Returns the size of the map.
     * @return the size of the map
     */
    public int size() {
        return size;
    }

    /**
     * Overrides Object's toString Method
     * @return String representation of AccountBank
     */
    @Override
    public String toString() {
        String out = "----------------------------------\nAccounts:\n----------------------------------\n";
        for (Account account : table) {
            if (account == null) {
                continue;
            }
            out += account;
        }
        return out;
    }


}
