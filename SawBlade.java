/**
 * Concrete implementation of abstract Item class as Saw Blade
 */
public class SawBlade extends Item {

    /**
     * Overrides toString from Object class
     * @return Saw Blade
     */
    @Override
    public String toString() {
        return "Saw Blade";
    }

    /**
     * Doubles all bullet damage for the current round
     * @param caller caller player
     * @param otherPlayer other player
     * @param shotgun current shotgun
     * @return dummy value
     */
    @Override
    public int use(Player caller, Player otherPlayer, Shotgun shotgun) {
        shotgun.sawOff();
        System.out.println(caller.getName() + " has sawed off the shotgun.");
        System.out.println("All shots on this shotgun do double damage.\nDouble the Stakes. Double the Fun.");
        return 9;
    }
}
