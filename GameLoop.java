import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Game functions as a loop of similar code. Placed all of that code in this file to simplify Main.java's code
 */
public class GameLoop {
    /**
     * Static helper method that tells me whether a string can be turned into an int
     * @param str - string to be tested
     * @return true if string can be int, false if string cannot be int
     */
    public static boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Checks whether game should continue.
     * If character is dead it will end the game.
     * If shotgun is empty it will end the round.
     * @param player - Human player
     * @param ai - AI opponent
     * @param shotgun - Shotgun in play
     * @param account - Account of Human Player
     * @return true if the game shall continue and new round should start. false if game should end.
     */
    public boolean continueGame(Player player, Player ai, Shotgun shotgun, Account account) {
        if (!player.isAlive()) {
            System.out.println(player.getName() + " died!");
            account.lose();
            return false;
        } else if (!ai.isAlive()) {
            System.out.println(player.getName() + " won!");
            account.win();
            return false;
        } else if (shotgun.getBulletCount() == 0) {
            System.out.println("Shotgun is empty");
        }
        return true;
    }

    /**
     * The main crux of the game aspect of this game.
     * Loops each turn for the player and the AI until the gun is out of bullets or a character has died
     * @param shotgun - Current shotgun in play
     * @param player - Human player
     * @param ai - AI player
     * @param scan - Scanner object instance
     * @param playerItemRack - Human player items
     * @param aiItemRack - AI player items
     * @return shotgun at the end of the round so that if round is over we can check to see why it is over
     */
    public Shotgun gameLoop(Shotgun shotgun, Player player, Player ai, Scanner scan,
                            ItemRack playerItemRack, ItemRack aiItemRack) {
        // This boolean means that it is the human player's turn if true
        // First turn of every round goes to Human Player
        boolean playerTurn = true;

        // While gun is not empty and both characters are alive
        while (shotgun.getBulletCount() > 0 && player.isAlive() && ai.isAlive()) {
            // Checks whether gun is not empty and it is the human player's turn
            if (shotgun.getBulletCount() > 0 && playerTurn) {
                // Checks if the turn should be skipped cause the player is handcuffed
                if (player.isHandcuffed()) {
                    // Turn is skipped but player is free afterwards
                    player.free();
                    playerTurn = aiTurn(player, ai, aiItemRack, shotgun, playerTurn);
                // It is the player's turn to play and they are not handcuffed
                } else {
                    // Dummy Value
                    int reply = 3;
                    // Keeps asking for items to use until player no longer wants items or no more items left
                    while (reply != 0 && playerItemRack.getSize() > 0) {
                        System.out.println("Would you like to use an item? Yes (1)? Or No (0)?");
                        // Tries to get valid input
                        while (true) {
                            if (scan.hasNextInt()) {
                                reply = scan.nextInt();
                                if (reply == 0 || reply == 1) {
                                    break;
                                } else {
                                    System.out.println("Please enter one of the numbers (0) or (1).");
                                }
                            } else {
                                System.out.println("Please enter one of the numbers (0) or (1).");
                                scan.next();
                            }
                        }
                        // If user wants to use item
                        if (reply == 1) {
                            // Keeps trying for valid input
                            while (true) {
                                try {
                                    playerItemRack.use(this.useItem(scan), player, ai, shotgun);
                                    break;
                                } catch (NoSuchElementException e) {
                                    System.out.println("Cannot use item that you do not have.");
                                    System.out.println("Please enter one of the numbers (0), (1), (2), "
                                            + "(3), (4), or (5).");
                                    scan.next();
                                }
                            }
                        }
                    }
                    System.out.println("\n");
                    // Now that user has used all items or does not want to use them they can finally play their turn
                    if (shotgun.getBulletCount() > 0) {
                        // Player turn updated after play is completed
                        // (player turn again if they shot themselves with blank else player turn is false)
                        playerTurn = player.playerTurn(shotgun, ai, scan);
                    }
                }
            } else {
                // if it is ai turn run ai turn
                playerTurn = aiTurn(player, ai, aiItemRack, shotgun, playerTurn);
            }
        }
        return shotgun;
    }

    /**
     * Helper method to get the proper values to use item
     * @param scan Scanner instance
     * @return index of what item to use
     */
    private int useItem(Scanner scan) {
        System.out.println("Which item would you like to choose? \nSaw Blade (0), Magnifying Glass (1), Beer (2), "
                + "Cigarette (3), Handcuffs (4), or general item information (5).");
        int item;
        // Keeps looping until valid input given
        while (true) {
            if (scan.hasNextInt()) {
                item = scan.nextInt();
                if (item < 5 && item >= 0) {
                    break;
                } else if (item == 5) {
                    System.out.println("Saw Blade will double the damage of all "
                            + "shotgun bullets for the current round.");
                    System.out.println("Magnifying Glass allows you to see what the next bullet in the chamber is.");
                    System.out.println("Beer allows you to remove the next bullet being shot without shooting it.");
                    System.out.println("Cigarette increases the user's health by one health point.");
                    System.out.println("Handcuffs skip the opponent's next turn.");
                    System.out.println("Please enter one of the numbers (0), (1), (2), (3), (4), or (5).");
                } else {
                    System.out.println("Please enter one of the numbers (0), (1), (2), (3), (4), or (5).");
                }
            } else {
                System.out.println("Please enter one of the numbers (0), (1), (2), (3), (4), or (5).");
                scan.next();
            }
        }
        return item;
    }

    /**
     * AI turn logic
     * @param player - Human Player
     * @param ai - AI Player
     * @param aiItemRack - AI Items Rack
     * @param shotgun - Shotgun in play
     * @param playerTurn - What is the player's turn currently
     * @return playersTurn after method is run. false if it is AI turn again. true if ai turn over.
     */
    public boolean aiTurn(Player player, Player ai, ItemRack aiItemRack, Shotgun shotgun, boolean playerTurn) {
        // Dummy initialization
        int shoot = 9;
        // AI has 3/5 chance to use item
        int choice = (int) (Math.random() * 5);
        while (choice >= 2
                // AI still has items to use
                && aiItemRack.getSize() > 0
                // AI is not handcuffed
                && !ai.isHandcuffed()
                // AI has not made decision on whether to shoot or not (magnifying glass will force ai to make decision)
                && shoot > 1
                // Human player is also not handcuffed. This way AI doesn't double handcuff.
                && !player.isHandcuffed()) {
            // AI has 1/3 chance to use another item
            choice = (int) (Math.random() * 3);
            // Uses random item
            boolean usedItem = false;
            while (!usedItem) {
                try {
                    // Random item is chosen
                    int item = (int) (Math.random() * 5);
                    // if use function returns 0 or 1, ai has made decision, else all rest are dummy values
                    shoot = aiItemRack.use(item, ai, player, shotgun);
                    usedItem = true;
                } catch (NoSuchElementException e) {
                    continue;
                }
            }

        }
        // Finally play AI turn
        if (shotgun.getBulletCount() > 0 && !ai.aiTurn(shotgun, player, shoot)) {
            // it has to be player's turn now
            return true;
        }
        // return current player turn
        return playerTurn;
    }
}
