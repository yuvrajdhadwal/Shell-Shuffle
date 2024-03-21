import java.util.Scanner;

/**
 * Player class for each player of the game.
 */
public class Player {
    private int hp;
    private boolean alive;
    private String name;
    private boolean isAI;
    private boolean handcuffed;

    /**
     * Constructor for the player class
     * @param hp - Health Points at start of the game
     * @param name - Name of the Player
     * @param isAI - If player is AI or Human
     */
    public Player(int hp, String name, boolean isAI) {
        this.hp = hp;
        alive = true;
        this.name = name;
        this.isAI = isAI;
    }

    /**
     * Update health and check if alive
     * @param sawedOff - if true double damage
     * @return alive
     */
    public boolean shot(boolean sawedOff) {
        if (sawedOff) {
            hp = hp - 2;
        } else {
            hp--;
        }

        if (hp <= 0) {
            alive = false;
        }
        return alive;
    }

    /**
     * Increases hp if smoking
     */
    public void smoke() {
        hp++;
    }

    /**
     * Return player Health Points
     * @return hp
     */
    public int getHp() {
        return hp;
    }

    /**
     * Returns whether player is alive or not
     * @return alive
     */
    public boolean isAlive() {
        return alive;
    }

    /**
     * Returns name of player
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns whether player is AI
     * @return isAI
     */
    public boolean isAI() {
        return isAI;
    }

    /**
     * Handcuffs player
     */
    public void handcuff() {
        handcuffed = true;
    }

    /**
     * Returns if player is handcuffed
     * @return handcuffed
     */
    public boolean isHandcuffed() {
        return handcuffed;
    }

    /**
     * Frees player from handcuffs
     */
    public void free() {
        handcuffed = false;
    }

    /**
     * Player turn
     * @param shotgun current shotgun
     * @param otherPlayer other player
     * @param choice player choice
     * @return whether player gets to play again
     */
    public boolean turn(Shotgun shotgun, Player otherPlayer, int choice) {
        // Initially you cannot play again
        boolean playAgain = false;
        // Skip turn if handcuffed
        if (!handcuffed) {
            switch (choice) {
            case 0:
                if (shotgun.shoot().isBlank()) {
                    System.out.println(this.name + " shot a blank bullet!");
                } else {
                    System.out.println(this.name + " shot a loaded bullet!");
                    otherPlayer.shot(shotgun.isSawedOff());
                }
                break;
            case 1:
                if (shotgun.shoot().isBlank()) {
                    System.out.println(this.name + " shot a blank bullet at themself!");
                    playAgain = true;
                } else {
                    System.out.println(this.name + " shot a loaded bullet at themself!");
                    this.shot(shotgun.isSawedOff());
                }
                break;
            default:
                System.out.println("An issue has occurred. I am not sure why.");
            }
        }
        // Frees player at end of turn
        handcuffed = false;
        return playAgain;
    }

    /**
     * Turn but for Human Players
     * @param shotgun current shotgun
     * @param ai ai player
     * @param scan scanner object
     * @return if player gets to play again
     */
    public boolean playerTurn(Shotgun shotgun, Player ai, Scanner scan) {
        System.out.println("Would you like to shoot enemy (0) or shoot yourself (1)?");
        int reply;
        while (true) {
            if (scan.hasNextInt()) {
                reply = scan.nextInt();
                if (reply == 0 || reply == 1) {
                    break;
                }
            }
            System.out.println("Please enter the value (0) or (1).");
            scan.next();
        }
        return this.turn(shotgun, ai, reply);
    }

    /**
     * Generic random AI choice for how to use turn
     * @param shotgun current shotgun
     * @param human human player
     * @return whether ai gets to play again
     */
    public boolean aiTurn(Shotgun shotgun, Player human) {
        int choice = (int) (Math.random() * 2);
        return this.turn(shotgun, human, choice);
    }

    /**
     * AI choice after using magnifying glass and it knows exactly what move to make
     * @param shotgun - current shotgun
     * @param human - human player
     * @param choice - choice to make
     * @return true if ai gets to play again
     */
    public boolean aiTurn(Shotgun shotgun, Player human, int choice) {
        // if choice is invalid, call generic function
        if (choice > 2) {
            return this.aiTurn(shotgun, human);
        }
        return this.turn(shotgun, human, choice);
    }
}
