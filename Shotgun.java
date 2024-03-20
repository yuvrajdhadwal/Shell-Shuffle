import java.util.NoSuchElementException;

/**
 * Shotgun class which is an Array-Backed Queue.
 */
public class Shotgun {
    private int bulletCount;
    private Bullet[] chamber;
    private int front;
    private boolean isSawedOff;

    /**
     * Constructor
     * @param chamberSize size of shotgun chamber
     */
    public Shotgun(int chamberSize) {
        chamber = new Bullet[chamberSize];
        front = 0;
        bulletCount = 0;
        isSawedOff = false;
    }

    /**
     * Loads a bullet into the shotgun
     * @param bullet bullet to be loaded in
     */
    public void load(Bullet bullet) {
        if (chamber.length == bulletCount) {
            throw new IndexOutOfBoundsException("Cannot add more bullets shotgun is full");
        }
        // ArrayBackedQueue.enqueue();
        int i = Math.abs((front + bulletCount) % chamber.length);
        chamber[i] = bullet;
        bulletCount++;
    }

    /**
     * Removes the next the bullet and shoots it
     * @return shot bullet
     */
    public Bullet shoot() {
        if (bulletCount == 0) {
            throw new NoSuchElementException("Cannot shoot/rack bullets from empty shotgun");
        }
        // ArrayBackedQueue.dequeue();
        Bullet racked = chamber[front];
        chamber[front] = null;
        front++;
        bulletCount--;
        return racked;
    }

    /**
     * Checks the next bullet of the shotgun
     * @return the next bullet
     */
    public Bullet peek() {
        if (bulletCount == 0) {
            throw new NoSuchElementException("Cannot peek empty chamber");
        }
        return chamber[front];
    }

    /**
     * Overrides Object's toString function
     * @return String representation of Shotgun
     */
    @Override
    public String toString() {
        int blanks = 0;
        for (Bullet bullet : chamber) {
            blanks = bullet.isBlank() ? blanks + 1 : blanks;
        }
        return "This shotgun has " + bulletCount + " bullets with " + (bulletCount-blanks) + " live rounds and " + blanks + " blanks.";
    }

    /**
     * Returns bullets left in shotgun
     * @return bulletCount
     */
    public int getBulletCount() {
        return bulletCount;
    }

    /**
     * Returns whether shotgun has been sawed off
     * @return isSawedOff
     */
    public boolean isSawedOff() {
        return isSawedOff;
    }

    /**
     * Sets isSawedOff to true
     */
    public void sawOff() {
        isSawedOff = true;
    }
}
