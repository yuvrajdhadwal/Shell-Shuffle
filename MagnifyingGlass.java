/**
 * Concrete implementation of abstract class Item as Magnifying Glass
 */
public class MagnifyingGlass extends Item {
    /**
     * Checks the next bullet in the chamber to see if it is a blank or not
     * @param caller caller player
     * @param otherPlayer other player
     * @param shotgun current shotgun
     * @return if ai called this method, 0 if should shoot human, 1 if should shoot itself, and dummy value if not ai
     */
    @Override
    public int use(Player caller, Player otherPlayer, Shotgun shotgun) {
        Bullet bullet = shotgun.peek();
        System.out.println(caller.getName() + " has used a Magnifying Glass. They can see what the next bullet is.");

        if (caller.isAI()) {
            if (bullet.isBlank()) {
                return 1;
            } else {
                return 0;
            }
        } else {
            System.out.println("The next bullet is " + bullet);
        }

        return 9;
    }

    /**
     * Object's toString method overriden
     * @return Magnifying Glass
     */
    @Override
    public String toString() {
        return "Magnifying Glass";
    }
}
