/**
 * Each player has their own account. Each account has their wins and losses associated with it.
 */
public class Account implements Comparable<Account> {
    private String username;
    private String password;
    private int wins;
    private int losses;
    private double winRate;
    private boolean isRemoved;

    /**
     * Account creation
     * @param username - What is the username for this account?
     * @param password - What is the password for this account?
     * @param wins - How many wins are associated with this account?
     * @param losses - How many losses are associated with this account?
     */
    public Account(String username, String password, int wins, int losses) {
        this.username = username;
        this.password = password;
        this.wins = wins;
        this.losses = losses;
        // Simple Win Rate calculation done in most games (wins/total games)
        this.winRate = (wins) / (wins + losses + 0.0);
        // Added for robust AccountBank (HashMap) functionality (not used in this game but is available)
        this.isRemoved = false;
    }

    /**
     * A more generic account creation that takes advantage of constructor chaining
     * @param username - What is the username for this new account?
     * @param password - What is the password for this new account?
     */
    public Account(String username, String password) {
        this(username, password, 0, 0);
    }

    /**
     * Returns Win Rate of the account.
     * @return double winRate
     */
    public double getWinRate() {
        return winRate;
    }

    /**
     * Overrides Object's compareTo function. Used for Leaderboard DownHeap functionality.
     * @param other the object to be compared.
     * @return positive number if current winrate > other winrate, negative number if less than, and zero if equal
     */
    @Override
    public int compareTo(Account other) {
        Double winRate = this.winRate;
        return winRate.compareTo(other.getWinRate());
    }

    /**
     * Returns username
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Returns password
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Returns if account has been removed for AccountBank (implemented as HashMap)
     * This functionality is not used but is option for future updates/changes
     * @return true if account is removed (a DEL marker for Linear Probing HashMap)
     */
    public boolean isRemoved() {
        return isRemoved;
    }

    /**
     * Sets the value of isRemoved field to parameter
     * @param value new value of isRemoved
     */
    public void setRemoved(boolean value) {
        isRemoved = value;
    }

    /**
     * Overrides Object's toString function. Used for Leaderboards toString method.
     * @return String displaying account name as well as win rate associated with the account
     */
    @Override
    public String toString() {
        String out = "";
        out += username;
        out += String.format("\t\tWinrate: %.1f", winRate * 100);
        out += "%\n";
        return out;
    }

    /**
     * Returns Wins
     * @return Wins
     */
    public int getWins() {
        return wins;
    }

    /**
     * Returns Losses
     * @return Losses
     */
    public int getLosses() {
        return losses;
    }

    /**
     * Updates wins parameter after game is over and is won
     */
    public void win() {
        this.wins++;
    }

    /**
     * Updates losses parameter after game is over and is lost
     */
    public void lose() {
        this.losses++;
    }

    /**
     * Converts valuable account details into String so it can be stored into file.
     * @return String version of valuable account data needed to be saved
     */
    public String toFileString() {
        return username + " " + password + " " + wins + " " + losses;
    }
}

