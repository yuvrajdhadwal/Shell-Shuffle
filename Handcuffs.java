/**
 * Concrete implementation of abstract Item class as Handcuffs
 */
public class Handcuffs extends Item {
    /**
     * toString implementation of Handcuffs
     * @return string representation of Handcuffs
     */
    @Override
    public String toString() {
        return "Handcuff";
    }

    /**
     * Use implementation for Handcuffs
     * Sets otherPlayer as Handcuffed. This skips their turn.
     * @param caller - caller player
     * @param otherPlayer - other player
     * @param shotgun - current shotgun
     * @return dummy value
     */
    @Override
    public int use(Player caller, Player otherPlayer, Shotgun shotgun) {
        System.out.printf("%s has handcuffed %s. Their next turn is skipped.\n",
                caller.getName(), otherPlayer.getName());
        otherPlayer.handcuff();
        return 9;
    }
}
