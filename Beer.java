/**
 * Concrete representation of Item class as Beer
 */
public class Beer extends Item {
    /**
     * Overrides the Item's abstract use method. When called, will remove the next bullet from the shotgun.
     * @param caller the person who called it
     * @param otherPlayer the other player
     * @param shotgun current shotgun
     * @return dummy number not necessary for this number
     */
    @Override
    public int use(Player caller, Player otherPlayer, Shotgun shotgun) {
        if (shotgun.shoot().isBlank()) {
            System.out.println(caller.getName() + " racked a blank bullet!");
        } else {
            System.out.println(caller.getName() + " racked a loaded bullet!");
        }
        return 9;
    }

    /**
     * String representation of this item
     * @return Beer
     */
    @Override
    public String toString() {
        return "Beer";
    }
}
