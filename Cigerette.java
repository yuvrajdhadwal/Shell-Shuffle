/**
 * Cigarette concrete class of abstract Item class
 */
public class Cigerette extends Item {

    /**
     * Override Item's toString method
     * @return String representation of Cigarette
     */
    @Override
    public String toString() {
        return "Cigarette";
    }

    /**
     * Overrides Item's use class. Adds one HP for the user.
     * @param caller - player who is calling this object
     * @param otherPlayer - other player
     * @param shotgun - shotgun in play
     * @return returns dummy int
     */
    @Override
    public int use(Player caller, Player otherPlayer, Shotgun shotgun) {
        caller.smoke();
        System.out.println(caller.getName() + " has smoked a cigarette. They gain one hp.");
        return 9;
    }
}
