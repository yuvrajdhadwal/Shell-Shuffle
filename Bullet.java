/**
 * Bullet object within Shotgun. Important because needs to classify between loaded and blank bullets.
 */
public class Bullet {
    private final boolean isBlank;

    /**
     * Creates bullet and blank depending on parameter
     * @param isBlank whether new Bullet object is a blank or loaded
     */
    public Bullet(boolean isBlank) {
        this.isBlank = isBlank;
    }

    /**
     * Returns whether the bullet is blank.
     * @return isBlank
     */
    public boolean isBlank() {
        return isBlank;
    }

    /**
     * Gives string representation of Bullet
     * @return Bullet
     */
    @Override
    public String toString() {
        return isBlank ? "Blank" : "Loaded";
    }
}
