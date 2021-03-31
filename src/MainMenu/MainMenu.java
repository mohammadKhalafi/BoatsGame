package MainMenu;

import Game.GameManager;
import Scan.Scan;
import Enum.SomeOutputs;
import User.User;

import java.util.Collections;
import java.util.regex.*;

import Shop.Shop;

public class MainMenu {

    private String username;

    private void setUsername(String username){
        this.username = username;
    }

    public void run(String username){
        setUsername(username);

        while (true){
            String command = Scan.scanner.nextLine();
            if(command.compareTo("help") == 0){
                help();
            }
            else if (command.compareTo("logout") == 0){
                logout();
                return;
            }
            else if (command.compareTo("scoreboard") == 0){
                System.out.print(User.getScoreBoard());
            }
            else if (command.matches("^new_game ([^ ]+$)")){
                newGame(getMatcher(command, "^new_game ([^ ]+$)"));
            }
            else if(command.equals("shop")){
                Shop.shopScanner(username);
            }
            else if (command.equals("list_users")){
                showListUsers();
            }
            else {
                System.out.println(SomeOutputs.INVALID_COMMAND.getValue());
            }
        }
    }

    private void showListUsers(){
        Collections.sort(User.getUsernames());
        for(String username : User.getUsernames()){
            System.out.println(username);
        }
    }

    private void help(){
        System.out.println("new_game [username]\n" +
                "scoreboard\n" +
                "list_users\n" +
                "shop\n" +
                "help\n" +
                "logout");
    }

    private void newGame(Matcher matcher){
        matcher.find();
        String rivalUsername = matcher.group(1);

        if(!isUsernameFormatValid(rivalUsername)){
            System.out.println(SomeOutputs.INVAILID_USERNAME_FORMAT.getValue());
            return;
        }
        if(isThatYourUsername(rivalUsername)){
            System.out.println(SomeOutputs.IT_IS_YOU.getValue());
            return;
        }
        if(!doesUserWithThisUsernameExist(rivalUsername)){
            System.out.println(SomeOutputs.INVALID_USERNAME.getValue());
            return;
        }

        GameManager gameManager = new GameManager(username, rivalUsername);
        gameManager.run();
    }

    private boolean isUsernameFormatValid(String username) {
        if (username.matches("[\\w]+")) {
            return true;
        } else {
            return false;
        }
    }

    private boolean doesUserWithThisUsernameExist(String username){
        if(User.getUserByUsername(username) == null){
            return false;
        }
        return true;
    }

    private boolean isThatYourUsername(String username){
        return this.username.compareTo(username) == 0;
    }

    private Matcher getMatcher(String matchingStr, String regex) {
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(matchingStr);
    }

    private void logout(){
        System.out.println(SomeOutputs.SUCCESSFULLY_LOGGEDOUT.getValue());
    }

}




















