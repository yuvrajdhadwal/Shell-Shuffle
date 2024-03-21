import java.util.NoSuchElementException;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileWriter;

/**
 * @author Yuvraj Dhadwal
 * @date 3.20.24
 * @version 1.0.0
 *
 * Main method of the game.
 */

public class Main {
    /**
     * Main method to be running
     * @param args extraneous arguments
     */
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        // Read past accounts and add them to accountBank
        String fileName = "accountBank.txt";
        AccountBank accountBank = new AccountBank();
        try {
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;

            // Read each line until end of file
            while ((line = bufferedReader.readLine()) != null) {
                String[] parts = line.split(" ");
                String username = parts[0];
                String password = parts[1];
                int wins = Integer.parseInt(parts[2]);
                int losses = Integer.parseInt(parts[3]);
                Account account = new Account(username, password, wins, losses);
                accountBank.put(account);
            }
            bufferedReader.close();
        } catch (IOException e) {
            System.out.println("An error occurred while reading the file.");
            e.printStackTrace();
        }

        // Create new account / Log into existing account
        boolean newAccount = false;
        Account account;
        String username = "";
        String password = "";
        while (true) {
            System.out.println("Please enter your username. Enter (0) to create new account");
            username = scan.nextLine();
            if (GameLoop.isInteger(username) && Integer.valueOf(username) == 0) {
                newAccount = true;
                break;
            }
            System.out.println("Please enter your password.");
            password = scan.nextLine();
            try {
                if (accountBank.checkValue(username, password)) {
                    break;
                }
            } catch (NoSuchElementException e) {
                continue;
            }

        }
        // Create new account based on new credentials
        if (newAccount) {
            System.out.println("Please enter new username: ");
            username = scan.nextLine();
            System.out.println("Please enter new password: ");
            password = scan.nextLine();
            account = new Account(username, password);
        // Create account with existing values
        } else {
            Account oldAccount = accountBank.getAccount(new Account(username, password));
            account = new Account(oldAccount.getUsername(), oldAccount.getPassword(),
                    oldAccount.getWins(), oldAccount.getLosses());
        }

        // Create the players
        Player player = new Player(5, account.getUsername(), false);
        Player ai = new Player(5, "Your Opponent", true);

        int roundNumber = 1;
        int bullets;
        Shotgun shotgun = new Shotgun(5);
        boolean firstRound = true;
        GameLoop gameLoop = new GameLoop();

        System.out.println("Welcome to Shotgun Roulette!");
        System.out.println("A simple game. We have a shotgun in the middle and you have three choices.");
        System.out.println("Shoot the gun at your opponent.");
        System.out.println("Shoot the gun at yourself.");
        System.out.println("Or, use an item to gain an advantage.");
        System.out.println("The twist? Some of the bullets in the shotgun are blanks. "
                + "And if you shoot a blank at yourself you get a new turn.");


        // Whole Game Loop for every round
        while (firstRound || gameLoop.continueGame(player, ai, shotgun, account)) {
            firstRound = false;
            System.out.println("-----------------------------\nWe are in Round " + roundNumber);
            System.out.println(player.getName() + " has " + player.getHp() + " health and "
                    + ai.getName() + " has " + ai.getHp() + " health.");

            // Set bullets for the round
            bullets = roundNumber * 2 + 1;
            shotgun = new Shotgun(bullets);

            // Set items for the round
            ItemRack playerItemRack = new ItemRack();
            ItemRack aiItemRack = new ItemRack();
            for (int i = 0; i < bullets / 4; i++) {
                playerItemRack.addItem((int) (Math.random() * 5));
                aiItemRack.addItem((int) (Math.random() * 5));
            }

            // Load shotgun
            while (bullets > 0) {
                boolean isBlank = Math.random() > 0.5;
                shotgun.load(new Bullet(isBlank));
                bullets--;
            }

            System.out.println("A new shotgun is handed over. " + shotgun);
            if (playerItemRack.getSize() != 0) {
                System.out.println(player.getName() + "'s Item Rack contains " + playerItemRack);
                System.out.println(ai.getName() + "'s Item Rack contains " + aiItemRack);
            }

            // Free players from handcuffs at start of new round
            player.free();
            ai.free();

            // Round Loop
            shotgun = gameLoop.gameLoop(shotgun, player, ai, scan, playerItemRack, aiItemRack);

            roundNumber++;
        }

        // Game Over. Update account
        accountBank.put(account);

        // Print accountBank into a file
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));

            // Write each element of the array to the file
            for (int i = 0; i < accountBank.getTable().length; i++) {
                Account acc = accountBank.getTable()[i];
                if (acc != null) {
                    writer.write(acc.toFileString());
                    writer.newLine(); // Move to the next line
                }
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file.");
            e.printStackTrace();
        }

        // Print Leaderboard
        System.out.println(new Leaderboard(accountBank.getTable()));
    }
}