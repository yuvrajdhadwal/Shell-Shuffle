/**
 * Abstract representation of Item to help concrete implementations of items in this game.
 */
public abstract class Item {
    private Item next = null;

    /**
     * Overriding Object's toString function
     * @return String representation of Item
     */
    @Override
    public abstract String toString();

    /**
     * Usage of item abstract method.
     * Some item will require these parameters. These parameters are discarded for other items
     * @param caller caller player
     * @param otherPlayer other player
     * @param shotgun current shotgun
     * @return 0 if shoot other player. 1 if shoot current player. Other numbers are dummy
     */
    public abstract int use(Player caller, Player otherPlayer, Shotgun shotgun);

    /**
     * Set next Item in LinkedList
     * @param next Item
     */
    public void setNext(Item next) {
        this.next = next;
    }

    /**
     * Gets the next Item in LinkedList
     * @return next Item
     */
    public Item getNext() {
        return next;
    }
}
