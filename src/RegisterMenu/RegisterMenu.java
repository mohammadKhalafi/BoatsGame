package RegisterMenu;

import MainMenu.MainMenu;
import Scan.Scan;
import Enum.SomeOutputs;
import User.User;

import java.util.*;
import java.util.regex.*;

public class RegisterMenu {

    public void run() {

        while (true) {
            String command = Scan.scanner.nextLine();
            if (command.matches("^register ([^ ]+) ([^ ]+)$")) {
                register(getMatcher(command, "register ([^ ]+) ([^ ]+)"));
            }
            else if (command.matches("^login ([^ ]+) ([^ ]+)$")){
                if(canLogin(getMatcher(command, "^login ([^ ]+) ([^ ]+)$"))){
                    System.out.println(SomeOutputs.SUCCESSFULLY_LOGGEDIN.getValue());
                    String username = getUsernameForLogin(getMatcher(command, "^login ([^ ]+) ([^ ]+)$"));
                    new MainMenu().run(username);
                }
            }
            else if(command.matches("^remove ([^ ]+) ([^ ]+)$")){
                remove(getMatcher(command, "^remove ([^ ]+) ([^ ]+)$"));
            }
            else if (command.compareTo("list_users") == 0){
                showListUsers();
            }
            else if (command.compareTo("help") == 0){
                help();
            }
            else if (command.compareTo("exit") == 0){
                exit();
                return;
            }
            else{
                System.out.println(SomeOutputs.INVALID_COMMAND.getValue());
            }
        }
    }


    private void exit(){
        System.out.println(SomeOutputs.PROGRAM_ENDED.getValue());
    }

    private void help(){
        System.out.println("register [username] [password]\n" +
                "login [username] [password]\n" +
                "remove [username] [password]\n" +
                "list_users\n" +
                "help\n" +
                "exit");

    }


    private void showListUsers(){
        Collections.sort(User.getUsernames());
        for(String username : User.getUsernames()){
            System.out.println(username);
        }
    }

    private void remove(Matcher matcher){
        matcher.find();
        String username = matcher.group(1);
        String password = matcher.group(2);

        if(!isUsernameFormatValid(username)){
            System.out.println(SomeOutputs.INVAILID_USERNAME_FORMAT.getValue());
            return;
        }
        if(!isPasswordFormatValid(password)){
            System.out.println(SomeOutputs.INVALID_PASSWORD_FORMAT.getValue());
            return;
        }
        if(!doesUserWithThisUsernameExist(username)){
            System.out.println(SomeOutputs.INVALID_USERNAME.getValue());
            return;
        }
        if(!isPasswordTure(username, password)){
            System.out.println(SomeOutputs.INCORRECT_PASSWORD.getValue());
            return;
        }

        User.getUserByUsername(username).removeUser();
        System.out.println("removed " + username + " successfully");
    }

    private boolean canLogin(Matcher matcher){
        matcher.find();
        String username = matcher.group(1);
        String password = matcher.group(2);
        if(!isUsernameFormatValid(username)){
            System.out.println(SomeOutputs.INVAILID_USERNAME_FORMAT.getValue());
            return false;
        }
        if(!isPasswordFormatValid(password)){
            System.out.println(SomeOutputs.INVALID_PASSWORD_FORMAT.getValue());
            return false;
        }
        if(!doesUserWithThisUsernameExist(username)){
            System.out.println(SomeOutputs.INVALID_USERNAME.getValue());
            return false;
        }
        if(!isPasswordTure(username, password)){
            System.out.println(SomeOutputs.INCORRECT_PASSWORD.getValue());
            return false;
        }

        return true;
    }

    private String getUsernameForLogin(Matcher matcher){
        matcher.find();
        return matcher.group(1);
    }

    private boolean isPasswordTure(String username, String password){
        return User.getUserByUsername(username).checkPassword(password);
    }

    private void register(Matcher matcher) {
        matcher.find();
        if (!isUsernameFormatValid(matcher.group(1))) {
            System.out.println(SomeOutputs.INVAILID_USERNAME_FORMAT.getValue());
            return;
        }
        if (!isPasswordFormatValid(matcher.group(2))) {
            System.out.println(SomeOutputs.INVALID_PASSWORD_FORMAT.getValue());
            return;
        }
        if (doesUserWithThisUsernameExist(matcher.group(1))) {
            System.out.println(SomeOutputs.USERNAME_HAS_USED.getValue());
            return;
        }
        new User(matcher.group(1), matcher.group(2));
        System.out.println(SomeOutputs.SUCCESSFULLY_REGISTERED.getValue());

    }

    private boolean isUsernameFormatValid(String username) {
        if (username.matches("[\\w\\d]+")) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isPasswordFormatValid(String password){
        if (password.matches("[\\w\\d]+")) {
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

    private Matcher getMatcher(String matchingStr, String regex) {
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(matchingStr);
    }
}

