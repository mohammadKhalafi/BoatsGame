package Shop;

import Scan.Scan;
import User.User;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Enum.SomeOutputs;
public class Shop {
    private static User user;

    private static HashMap<String, Integer> products;

    static {
        products = new HashMap<>(){{
            put("mine", 1);
            put("antiaircraft", 30);
            put("airplane", 10);
            put("scanner", 9);
            put("invisible", 20);
        }};
    }

    public static void shopScanner(String username){
        user = User.getUserByUsername(username);
        String command;
        while (true){
            command = Scan.scanner.nextLine();
            if(command.matches("buy ([^ ]+) ([^ ]+)")){
                manageBuy(getMatcher(command, "buy ([^ ]+) ([^ ]+)"));
            }
            else if(command.equals("help")){
                help();
            }
            else if (command.equals("back")){
                return;
            }
            else if(command.equals("show-amount")){
                showAmount();
            }
            else {
                System.out.println(SomeOutputs.INVALID_COMMAND.getValue());
            }
        }
    }

    private static void manageBuy(Matcher matcher){
        matcher.find();

        String product = matcher.group(1);

        if(!doseThisProductExist(product)){
            System.out.println("there is no product with this name");
            return;
        }
        if(!isAmountValid(matcher.group(2))){
            System.out.println("invalid number");
            return;
        }

        int amount = Integer.parseInt(matcher.group(2));

        if(!doesUserHaveEnoughMoney(product, amount)){
            System.out.println("you don't have enough money");
            return;
        }

        buy(product, amount);
    }

    private static void buy(String product, int amount) {

        switch (product) {
            case "mine" -> user.setNumberOfMines(user.getNumberOfMines() + amount);
            case "antiaircraft" -> user.setNumberOfAntiAircrafts(user.getNumberOfAntiAircrafts() + amount);
            case "airplane" -> user.setNumberOfAirplanes(user.getNumberOfAirplanes() + amount);
            case "scanner" -> user.setNumberOfScanners(user.getNumberOfScanners() + amount);
            case "invisible" -> user.setNumberOfInvisibles(user.getNumberOfInvisibles() + amount);
        }

        user.setCredit(user.getCredit() - amount * products.get(product));
    }

    private static boolean doseThisProductExist(String product){
        return products.containsKey(product);
    }

    private static boolean isAmountValid(String amount){
        if(amount.matches("\\d+")){
            int tempAmount = Integer.parseInt(amount);
            return tempAmount != 0;
        }
        return false;
    }

    private static boolean doesUserHaveEnoughMoney(String product, int amout) {
        return user.getCredit() >= amout * products.get(product);
    }

    private static void showAmount(){
        System.out.println(user.getCredit());
    }

    private static void help() {
        System.out.println("buy [product] [number]\n" +
                "show-amount\n" +
                "help\n" +
                "back");
    }

    private static Matcher getMatcher(String matchingStr, String regex) {
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(matchingStr);
    }


}
