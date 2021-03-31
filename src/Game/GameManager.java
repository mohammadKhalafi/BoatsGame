package Game;

import User.User;
import Scan.Scan;
import Enum.SomeOutputs;

import java.util.ArrayList;

import java.util.regex.*;


public class GameManager {

    String username1;
    String username2;

    Board board1;
    Board board2;

    private Board currentBoard;
    private User currentUser;

    public GameManager(String username1, String username2) {

        System.out.println("new game started successfully between " + username1 + " and " + username2);
        setUsernames(username1, username2);
        setBoards();
        setCurrentUser();
        setCurrentBoard();

    }

    private void setUsernames(String username1, String username2) {
        this.username1 = username1;
        this.username2 = username2;
    }

    private void setBoards() {
        board1 = new Board(username1);
        board1.arrangeTheBoard();
        System.out.println("turn completed");
        board2 = new Board(username2);
        board2.arrangeTheBoard();
        System.out.println("turn completed");
    }

    private void setCurrentBoard() {
        currentBoard = board2;
    }

    private void setCurrentUser() {
        currentUser = User.getUserByUsername(username1);
    }

    public void run() {

        gameMenuScanner();
    }

    private void gameMenuScanner() {
        String command;
        while (true) {

            /*System.out.println("its data:");

            System.out.println("turn");
            showTurn();

            System.out.println("my board :");
            showCurrentUserBoard();

            System.out.println("rival board :");
            showCurrentBoardForRival();

            System.out.println("board 1 : " + board1.score + "  board2 : " + board2.score);

            System.out.println("\n\n\n");*/

            command = Scan.scanner.nextLine();
            if (command.matches("bomb ([\\d]+),([\\d]+)")) {
                manageBomb(getMatcher(command, "bomb ([\\d]+),([\\d]+)"));

            } else if (command.equals("show-my-board")) {
                showCurrentUserBoard();

            } else if (command.equals("show-rival-board")) {
                showCurrentBoardForRival();

            } else if (command.equals("show-turn")) {
                showTurn();

            } else if (command.equals("help")) {
                help();

            } else if (command.matches("scanner ([\\d]+),([\\d]+)")) {
                managePutScanner(getMatcher(command, "scanner ([\\d]+),([\\d]+)"));

            } else if (command.matches("put-airplane ([\\d]+),([\\d]+) -([\\w])")) {
                managePutAirplane(getMatcher(command, "put-airplane ([\\d]+),([\\d]+) -([\\w])"));

            } else if (command.equals("forfeit")) {
                mangeForfeit();
                return;

            } else {
                System.out.println(SomeOutputs.INVALID_COMMAND.getValue());
            }

            if (isGameEnded()) {
                noteGameInUserData();
                printWinner();
                return;
            }
        }
    }

    private void managePutAirplane(Matcher matcher) {

        matcher.find();
        int airplaneCoordinationX = Integer.parseInt(matcher.group(1));
        int airplaneCoordinationY = Integer.parseInt(matcher.group(2));
        char direction = matcher.group(3).charAt(0);

        if (!isCoordinationValid(airplaneCoordinationX, airplaneCoordinationY)) {
            System.out.println(SomeOutputs.WORNG_COORDINATION.getValue());
            return;
        }
        if (!isDirectionValid(direction)) {
            System.out.println(SomeOutputs.INVALID_DIRECTION.getValue());
            return;
        }
        if (!isAirplaneTotallyInTheBoard(airplaneCoordinationX, airplaneCoordinationY, direction)) {
            System.out.println(SomeOutputs.OFF_THE_BOARD.getValue());
            return;
        }
        if (!doesUserHaveAnyAirplanes()) {
            System.out.println("you don't have airplane");
            return;
        }
        if (didAirplaneDestroyed(airplaneCoordinationX, airplaneCoordinationY, direction)) {
            System.out.println("the rival's antiaircraft destroyed your airplane");
            return;
        }
        putAirplane(airplaneCoordinationX, airplaneCoordinationY, direction);
    }

    private void putAirplane(int x, int y, char direction) {

        int numberOfDestroyedShips = 0;

        int xRange;
        int yRange;
        if(direction == 'v'){
            xRange = 2;
            yRange = 5;
        } else {
            xRange = 5;
            yRange = 2;
        }

        for(int i = x; i < x + xRange; i++){
            for(int j = y; j < y + yRange; j++){
                if(whatIsInThisPlace(i, j, currentBoard) == 1){
                numberOfDestroyedShips++;
                }
                destroyOnePlace(i, j, currentBoard);
            }
        }
        printNumberOfDestroyedShipsByAirplane(numberOfDestroyedShips);
    }

    private void printNumberOfDestroyedShipsByAirplane(int numberOfDestroyedShips) {
        if(numberOfDestroyedShips == 0){
            System.out.println("target not found");
        }
        else{
            System.out.println(numberOfDestroyedShips + " pieces of rival's ships was damaged");
        }
    }


    private boolean didAirplaneDestroyed(int x, int y, char direction) {

        ArrayList<String> antiAircrafts = currentBoard.antiAircrafts;
        int antiAirCraftIndex = 0;

        for (String antiAircraft : antiAircrafts) {

            char antiAircraftDirection = getAntiAircraftDirection(antiAircraft);
            int antiAircraftPointer = getAntiAircraftPointer(antiAircraft);
            boolean canDestroy = canAntiAircraftDestroyAirPlane(antiAircraftPointer,
                    antiAircraftDirection, x, y, direction);
            if (canDestroy) {
                currentBoard.antiAircrafts.remove(antiAirCraftIndex);
                return true;
            }
            antiAirCraftIndex++;
        }
        return false;
    }

    private boolean canAntiAircraftDestroyAirPlane(int antiAircraftPointer, char antiAircraftDirection,
                                                   int airplaneX, int airplaneY, char airplaneDirection){
        if(airplaneDirection == 'v'){
            if(antiAircraftDirection == 'v'){
                return airplaneX + 1 >= antiAircraftPointer && airplaneX < antiAircraftPointer + 3;
            }
            else{
                return airplaneY + 1>= antiAircraftPointer && airplaneY < antiAircraftPointer + 5;
            }
        }
        else{
            if(antiAircraftDirection == 'v'){
                return airplaneX + 1 >= antiAircraftPointer && airplaneX < antiAircraftPointer + 5;
            }
            else{
                return airplaneY + 1 >= antiAircraftPointer && airplaneY < antiAircraftPointer + 3;
            }
        }
    }

    private int getAntiAircraftPointer(String antiAircraft) {
        Matcher matcher = getMatcher(antiAircraft, "(\\d)");
        matcher.find();
        return Integer.parseInt(matcher.group(1));
    }

    private char getAntiAircraftDirection(String antiAircraft) {
        Matcher matcher = getMatcher(antiAircraft, "(v|h)");
        matcher.find();
        return matcher.group(1).charAt(0);
    }


    private boolean doesUserHaveAnyAirplanes() {
        return currentUser.getNumberOfAirplanes() > 0;
    }

    private boolean isAirplaneTotallyInTheBoard(int x, int y, char direction) {
        if (direction == 'v') {
            return x + 1 < 11 && y + 4 < 11;
        } else {
            return y + 1 < 11 && x + 4 < 11;
        }
    }

    private boolean isDirectionValid(char direction) {
        return direction == 'v' || direction == 'h';
    }

    private void managePutScanner(Matcher matcher) {

        matcher.find();
        int scannerCoordinationX = Integer.parseInt(matcher.group(1));
        int scannerCoordinationY = Integer.parseInt(matcher.group(2));

        if (!isCoordinationValid(scannerCoordinationX, scannerCoordinationY)) {
            System.out.println(SomeOutputs.WORNG_COORDINATION.getValue());
            return;
        } else if (!isScannerInTheBoard(scannerCoordinationX, scannerCoordinationY)) {
            System.out.println(SomeOutputs.OFF_THE_BOARD.getValue());
            return;
        } else if (!doesUserHaveAnyScanners()) {
            System.out.println("you don't have scanner");
            return;
        }

        printScanData(getScanData(scannerCoordinationX, scannerCoordinationY));
        reduceScannerInUserData();
    }

    private String[][] getScanData(int x, int y) {

        String[][] scanData = new String[3][3];

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (currentBoard.boardMap[y + i][x + j].startsWith("S")) {
                    scanData[i][j] = "SX";
                } else {
                    scanData[i][j] = "  ";
                }
            }
        }
        return scanData;
    }

    private void printScanData(String[][] scanData) {

        StringBuilder scanDataStr = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                scanDataStr.append("|" + scanData[i][j]);
            }
            scanDataStr.append("|\n");
        }
        System.out.print(scanDataStr);
    }

    private void reduceScannerInUserData() {
        currentUser.setNumberOfScanners(currentUser.getNumberOfScanners() - 1);
    }

    private boolean doesUserHaveAnyScanners() {
        return currentUser.getNumberOfScanners() > 0;
    }

    private boolean isScannerInTheBoard(int x, int y) {
        return (x + 2 < 11) && (y + 2 < 11);
    }


    private void help() {
        System.out.println("bomb [x],[y]\n" +
                "put-airplane [x],[y] [-h|-v]\n" +
                "scanner [x],[y]\n" +
                "show-turn\n" +
                "show-my-board\n" +
                "show-rival-board\n" +
                "help\n" +
                "forfeit");
    }

    private void showTurn() {
        System.out.println(currentUser.getUsername() + "'s turn");
    }

    private void showCurrentBoardForRival() {
        currentBoard.showBoardForRival();
    }


    private void showCurrentUserBoard() {

        Board board = getUserBoard(currentUser);
        board.arrangePrintingMap();
        board.showMyBoard();
    }

    private void mangeForfeit() {

        setScore(currentUser, -1);
        setScore(getOtherUser(currentUser), 2);

        setCredit(getOtherUser(currentUser), 0);
        getUserBoard(currentUser).score = 0;
        setCredit(currentUser, -50);

        currentUser.setNumberOflosses(currentUser.getNumberOflosses() + 1);
        User otherUser = getOtherUser(currentUser);
        otherUser.setNumberOfWins(otherUser.getNumberOfWins() + 1);

        forfeitPrinter();
    }

    private void forfeitPrinter() {
        System.out.println(currentUser.getUsername() + " is forfeited");
        System.out.println(getOtherUser(currentUser).getUsername() + " is winner");
    }

    private void printWinner() {
        int winner = getWinner();
        if (winner == 1) {
            System.out.println(username1 + " is winner");
        } else if (winner == 2) {
            System.out.println(username2 + " is winner");
        } else if (winner == 4) {
            System.out.println("draw");
        }
    }

    private void noteGameInUserData() {

        User user1 = User.getUserByUsername(username1);
        User user2 = User.getUserByUsername(username2);

        int winner = getWinner();

        if (winner == 4) {

            setCredit(user1, 25);
            setScore(user1, 1);
            user1.setNumberOfDraws(user1.getNumberOfDraws() + 1);

            setCredit(user2, 25);
            setScore(user2, 1);
            user2.setNumberOfDraws(user2.getNumberOfDraws() + 1);

        } else if (winner == 1) {
            setCredit(user1, 50);
            setScore(user1, 3);
            user1.setNumberOfWins(user1.getNumberOfWins() + 1);
            user2.setNumberOflosses(user2.getNumberOflosses() + 1);

        } else if (winner == 2) {
            setCredit(user2, 50);
            setScore(user2, 3);
            user2.setNumberOfWins(user2.getNumberOfWins() + 1);
            user1.setNumberOflosses(user1.getNumberOflosses() + 1);
        }

    }

    private int getWinner() {

        boolean doesUser1Win = false;
        boolean doesUser2Win = false;

        if (board1.numberOfTotallyDestroyedShips == 10) {
            doesUser2Win = true;
        }
        if (board2.numberOfTotallyDestroyedShips == 10) {
            doesUser1Win = true;
        }

        if (doesUser1Win && doesUser2Win) {
            return 4;
        } else if (doesUser1Win) {
            return 1;
        } else if (doesUser2Win) {
            return 2;
        } else {
            return 0;
        }

    }

    private void setCredit(User user, int credit) {
        int tempCredit = user.getCredit();
        user.setCredit(tempCredit + credit + getOtherBoard(getUserBoard(user)).score);
    }

    private void setScore(User user, int score) {
        user.setScore(user.getScore() + score);
    }

    private boolean isGameEnded() {
        return board1.numberOfTotallyDestroyedShips == 10 || board2.numberOfTotallyDestroyedShips == 10;
    }

    private void manageBomb(Matcher matcher) {
        matcher.find();

        int bombCoordinationX = Integer.parseInt(matcher.group(1));
        int bombCoordinationY = Integer.parseInt(matcher.group(2));

        if (!isCoordinationValid(bombCoordinationX, bombCoordinationY)) {
            System.out.println(SomeOutputs.WORNG_COORDINATION.getValue());
            return;
        }
        if (isThisPlaceAlreadyDestryed(bombCoordinationX, bombCoordinationY, currentBoard)) {
            System.out.println("this place has already destroyed");
            return;
        }
        putBomb(bombCoordinationX, bombCoordinationY);
    }

    private void putBomb(int x, int y) {

        int placeInput = whatIsInThisPlace(x, y, currentBoard);

        if (placeInput == 0) {
            destroySee(x, y, currentBoard);
            System.out.println("the bomb fell into sea");
            changeTurn();
        } else if (placeInput == 1) {

            Ship ship = destroyShip(x, y, currentBoard);

            if (ship.numberOfNotDestroyedPoints != 0) {
                System.out.println("the rival's ship was damaged");
            } else {
                System.out.println("the rival's ship" +
                        ship.length +
                        " was destroyed");
            }
        } else if (placeInput == -1) {
            destroyMine(x, y, currentBoard);
            System.out.println("you destroyed the rival's mine");
            changeTurn();
        }
    }

    private void destroyOnePlace(int x, int y, Board board) {

        if (!isCoordinationValid(x, y)) {
            return;
        }

        if (isThisPlaceAlreadyDestryed(x, y, board)) {
            return;
        }

        int placeInput = whatIsInThisPlace(x, y, board);

        if (placeInput == 0) {
            destroySee(x, y, board);
        } else if (placeInput == 1) {
            destroyShip(x, y, board);
        } else if (placeInput == -1) {
            destroyMine(x, y, board);
        }
    }

    private void destroyMine(int x, int y, Board board) {
        noteDistroyedMine(x, y, board);
        noteScoreInBoard(board, -1);
        destroyOnePlace(x, y, getOtherBoard(board));
    }

    private Ship destroyShip(int x, int y, Board board) {
        ArrayList<Ship> ships = getTypeShipByLenght(getTypeShipInMap(board.boardMap[y][x]), board);
        Ship ship = returnDestroyedShip(ships, x, y);
        noteDestroyedShipInBoard(ship, x, y, board);
        noteScoreInBoard(board, 1);
        return ship;
    }

    private void noteScoreInBoard(Board board, int score) {
        board.score += score;
    }


    private void destroySee(int x, int y, Board board) {
        notBombThatFellIntoSee(x, y, board);
    }


    private void notBombThatFellIntoSee(int x, int y, Board board) {
        board.boardMap[y][x] = "XX";
        board.boardMapForRival[y][x] = "XX";
    }

    //see -> 0
    //ship -> 1
    //mine -> -1
    private int whatIsInThisPlace(int x, int y, Board board) {

        String tempMapData = board.boardMap[y][x];
        if (tempMapData.equals("  ") || tempMapData.equals("AA")) {
            return 0;
        } else if (tempMapData.startsWith("S") || tempMapData.startsWith("I")) {
            return 1;
        } else if (tempMapData.equals("Mm")) {
            return -1;
        }
        return 1000;
    }

    private void noteDistroyedMine(int x, int y, Board board) {
        board.boardMap[y][x] = "MX";
        board.boardMapForRival[y][x] = "MX";
    }

    private void noteDestroyedShipInBoard(Ship ship, int x, int y, Board board) {

        if (ship.direction == 'v') {

            ship.numberOfNotDestroyedPoints = ship.numberOfNotDestroyedPoints - 1;
            board.boardMap[y][x] = "D" + ship.length;

            if (ship.numberOfNotDestroyedPoints != 0) {
                board.boardMapForRival[y][x] = "DX";
            } else {
                board.numberOfTotallyDestroyedShips++;
                for (int i = 0; i < ship.length; i++) {
                    board.boardMapForRival[ship.yPointer + i][ship.xPointer] = "D" + ship.length;

                }
            }
        } else {

            ship.numberOfNotDestroyedPoints = ship.numberOfNotDestroyedPoints - 1;
            board.boardMap[y][x] = "D" + ship.length;

            if (ship.numberOfNotDestroyedPoints != 0) {
                board.boardMapForRival[y][x] = "DX";
            } else {
                board.numberOfTotallyDestroyedShips++;
                for (int i = 0; i < ship.length; i++) {
                    board.boardMapForRival[ship.yPointer][ship.xPointer + i] = "D" + ship.length;

                }
            }
        }

    }

    private Ship returnDestroyedShip(ArrayList<Ship> ships, int x, int y) {

        for (Ship ship : ships) {

            if (ship.direction == 'v') {
                if (x == ship.xPointer) {
                    if (y < ship.yPointer + ship.length && y >= ship.yPointer) {
                        return ship;
                    }
                }
            } else {
                if (y == ship.yPointer) {
                    if (x < ship.xPointer + ship.length && x >= ship.xPointer) {
                        return ship;
                    }
                }
            }
        }
        return null;
    }

    private int getTypeShipInMap(String tempMapData) {

        Matcher matcher = getMatcher(tempMapData, "(\\d+)");
        matcher.find();
        return Integer.parseInt(matcher.group(1));
    }


    private boolean isThisPlaceAlreadyDestryed(int x, int y, Board board) {
        return board.boardMap[y][x].equals("MX") ||
                board.boardMap[y][x].equals("XX") ||
                board.boardMap[y][x].startsWith("D");
    }


    private boolean isCoordinationValid(int x, int y) {
        return x > 0 && x < 11 && y > 0 && y < 11;
    }

    private void changeTurn() {
        if (currentUser.getUsername().equals(username1)) {
            currentUser = User.getUserByUsername(username2);
            currentBoard = board1;
        } else {
            currentUser = User.getUserByUsername(username1);
            currentBoard = board2;
        }
        System.out.println("turn completed");
    }

    private Board getUserBoard(User user) {
        if (user.getUsername().equals(username1)) {
            return board1;
        } else {
            return board2;
        }
    }

    private Board getOtherBoard(Board board) {
        if (board.username.equals(board1.username)) {
            return board2;
        } else {
            return board1;
        }
    }

    private User getOtherUser(User user) {
        if (user.getUsername().equals(username1)) {
            return User.getUserByUsername(username2);
        } else {
            return User.getUserByUsername(username1);
        }
    }

    private static Matcher getMatcher(String matchingStr, String regex) {
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(matchingStr);
    }

    private ArrayList<Ship> getTypeShipByLenght(int length, Board board) {

        switch (length) {
            case 1 -> {
                return board.s1Ships;
            }
            case 2 -> {
                return board.s2Ships;
            }
            case 3 -> {
                return board.s3Ships;
            }
            default -> {
                return board.s4Ships;
            }
        }
    }

}