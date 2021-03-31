package User;

import java.util.ArrayList;

public class User {
    private String username;
    private String password;
    private int numberOfWins = 0;
    private int numberOflosses = 0;
    private int numberOfDraws = 0;
    private int score = 0;
    private int numberOfMines = 0;
    private int numberOfAntiAircrafts = 0;
    private int numberOfInvisibles = 0;
    private int numberOfScanners = 0;
    private int numberOfAirplanes = 0;
    private int credit = 50;


    private static ArrayList<User> users = new ArrayList<>();
    private static ArrayList<String> usernames = new ArrayList<>();

    public User(String username, String password) {
        if (!didUsernameUse(username)) {
            this.username = username;
            this.password = password;
            users.add(this);
            usernames.add(username);
        }
    }

    private boolean didUsernameUse(String username) {
        for (User tempUser : users) {
            if (tempUser.getUsername().compareTo(username) == 0) {
                return true;
            }
        }
        return false;
    }


    public void setNumberOfAntiAircrafts(int numberOfAntiAircrafts) {
        this.numberOfAntiAircrafts = numberOfAntiAircrafts;
    }

    public void setNumberOfDraws(int numberOfDraws) {
        this.numberOfDraws = numberOfDraws;
    }

    public void setNumberOflosses(int numberOflosses) {
        this.numberOflosses = numberOflosses;
    }

    public void setNumberOfWins(int numberOfWins) {
        this.numberOfWins = numberOfWins;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getScore() { return score; }

    public void setNumberOfInvisibles(int numberOfInvisibles) {
        this.numberOfInvisibles = numberOfInvisibles;
    }

    public void setNumberOfMines(int numberOfMines) {
        this.numberOfMines = numberOfMines;
    }

    public void setCredit(int credit) { this.credit = credit; }

    public void setNumberOfAirplanes(int numberOfAirplanes) { this.numberOfAirplanes = numberOfAirplanes; }

    public void setNumberOfScanners(int numberOfScanners) { this.numberOfScanners = numberOfScanners; }

    public int getNumberOfAirplanes() { return numberOfAirplanes; }

    public int getNumberOfScanners() { return numberOfScanners; }

    public int getCredit() { return credit; }

    public int getNumberOfMines() { return numberOfMines; }

    public int getNumberOfAntiAircrafts() {
        return numberOfAntiAircrafts;
    }

    public int getNumberOfInvisibles() {
        return numberOfInvisibles;
    }

    public String getUsername() {
        return username;
    }

    public int getNumberOfDraws() {
        return numberOfDraws;
    }

    public int getNumberOfWins() {
        return numberOfWins;
    }

    public int getNumberOflosses() {
        return numberOflosses;
    }

    public static ArrayList<String> getUsernames() {
        return usernames;
    }

    public boolean checkPassword(String password) {
        if (this.password.compareTo(password) == 0) {
            return true;
        }
        return false;
    }

    public static User getUserByUsername(String username) {
        for (User user : users) {
            if (user.getUsername().compareTo(username) == 0) {
                return user;
            }
        }
        return null;
    }

    public static String getScoreBoard() {

        StringBuilder scoreBoard = new StringBuilder();
        User tempUser;
        for (int i = 0; i < users.size(); i++) {
            for (int j = 0; j < users.size() - i - 1; j++) {
                if (shouldSwapp(users.get(j), users.get(j + 1))) {
                    tempUser = users.get(j);
                    users.set(j, users.get(j + 1));
                    users.set(j + 1, tempUser);
                }
            }
        }
        for (User user : users) {
            scoreBoard.append(user.username + " " +
                    user.score + " " +
                    user.numberOfWins + " "
                    + user.numberOfDraws + " "
                    + user.numberOflosses + "\n");
        }
        return scoreBoard.toString();
    }

    private static boolean shouldSwapp(User user1, User user2) {

        if (user1.score > user2.score) {
            return false;
        }
        if (user1.score < user2.score) {
            return true;
        }
        //equal score

        if (user1.numberOfWins > user2.numberOfWins) {
            return false;
        }
        if (user1.numberOfWins < user2.numberOfWins) {
            return true;
        }
        //equal num wins

        if (user1.numberOfDraws > user2.numberOfDraws) {
            return false;
        }
        if (user1.numberOfDraws < user2.numberOfDraws) {
            return true;
        }
        //equal num draws

        if (user1.numberOflosses < user2.numberOflosses) {
            return false;
        }
        if (user1.numberOflosses > user2.numberOflosses) {
            return true;
        }
        //equal num losses

        if (user1.username.compareTo(user2.username) < 0) {
            return false;
        }
        return true;
    }


    public void removeUser() {
        users.remove(this);
        usernames.remove(this.username);
    }

}