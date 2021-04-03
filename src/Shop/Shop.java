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
        products = new HashMap<>();
        products.put("mine", 1);
        products.put("antiaircraft", 30);
        products.put("airplane", 10);
        products.put("scanner", 9);
        products.put("invisible", 20);
    }

    public static void shopScanner(String username){
        user = User.getUserByUsername(username);
        String command;
        while (true){
            command = Scan.scanner.nextLine();
            if(command.matches("buy ([^ ]+) ([\\d]+)")){
                manageBuy(getMatcher(command, "buy ([^ ]+) ([\\d]+)"));
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

        if(product.equals("mine")){
            user.setNumberOfMines(user.getNumberOfMines() + amount);
        }
        else if (product.equals("antiaircraft")){
            user.setNumberOfAntiAircrafts(user.getNumberOfAntiAircrafts() + amount);
        }
        else if(product.equals("airplane")){
            user.setNumberOfAirplanes(user.getNumberOfAirplanes() + amount);
        }
        else if(product.equals("scanner")){
            user.setNumberOfScanners(user.getNumberOfScanners() + amount);
        }
        else if(product.equals("invisible")){
            user.setNumberOfInvisibles(user.getNumberOfInvisibles() + amount);
        }

        user.setCredit(user.getCredit() - amount * products.get(product));
    }

    private static boolean doseThisProductExist(String product){
        return products.containsKey(product);
    }

    private static boolean isAmountValid(String amount){
        int tempAmount = Integer.parseInt(amount);
        if(tempAmount != 0)
            return true;
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
