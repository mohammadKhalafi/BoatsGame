package Game;

import Scan.Scan;

import Enum.SomeOutputs;

import java.util.regex.*;

import java.lang.*;

import java.util.ArrayList;

import User.User;

public class Board {

    String username;

    String [][] boardMap;
    String [][] boardMapForRival;
    String [][] printingMap;

    ArrayList<Ship> s1Ships;
    ArrayList<Ship> s2Ships;
    ArrayList<Ship> s3Ships;
    ArrayList<Ship> s4Ships;

    ArrayList<String> antiAircrafts;
    int numberOfTotallyDestroyedShips = 0;
    int score = 0;


    {
        boardMap = new String[11][11];

        for(int i = 0; i < 11; i++){
            for(int j = 0; j < 11; j++){
                boardMap[i][j] = "  ";
            }
        }

        s1Ships = new ArrayList<>();
        s2Ships = new ArrayList<>();
        s3Ships = new ArrayList<>();
        s4Ships = new ArrayList<>();

        boardMapForRival = new String[11][11];
        printingMap = new String[11][11];

        for(int i = 0; i < 11; i++){
            for(int j = 0; j < 11; j++){
                boardMapForRival[i][j] = "  ";
            }
        }

        antiAircrafts = new ArrayList<>();
    }

    public Board(String username){
        this.username = username;
    }

    public void arrangeTheBoard(){
        while (true){
            String command = Scan.scanner.nextLine();
            if(command.matches("put S\\d+ \\d+,\\d+ -\\S")){
                managePutingBoat(getMatcher(command, "put S(\\d+) (\\d+),(\\d+) -(\\S)"));
            }
            else if (command.matches("put-mine (\\d+),(\\d+)")){
                managePutingMine(getMatcher(command, "^put-mine ([\\d]+),([\\d]+)$"));
            }
            else if(command.matches("show-my-board")){
                arrangePrintingMap();
                showMyBoard();
            }
            else if(command.matches("put-antiaircraft (\\d+) -(\\S)")){

                mangePutingAntiAircraft(getMatcher(command, "put-antiaircraft (\\d+) -(\\S)"));
            }
            else if (command.matches("invisible (\\d+),(\\d+)")){

                managePutingInvisible(getMatcher(command, "invisible (\\d+),(\\d+)"));
            }
            else if(command.compareTo("finish-arranging") == 0){
                if(canFinishArrange()){
                    return;
                }
            }
            else if(command.equals("help")){
                help();
            }
            else {
                System.out.println(SomeOutputs.INVALID_COMMAND.getValue());
            }

        }
    }

    private void help(){

        System.out.println("put S[number] [x],[y] [-h|-v]\n" +
                "put-mine [x],[y]\n" +
                "put-antiaircraft [s] [-h|-v]\n" +
                "invisible [x],[y]\n" +
                "show-my-board\n" +
                "help\n" +
                "finish-arranging");
    }

    private boolean canFinishArrange(){
        if(doesAnyBoatsInThisTypeRemain(1) || doesAnyBoatsInThisTypeRemain(2)
                || doesAnyBoatsInThisTypeRemain(3) || doesAnyBoatsInThisTypeRemain(4)){
            System.out.println("you must put all ships on the board");
            return false;
        }
        return true;
    }

    //put invisible

    private void managePutingInvisible(Matcher matcher) {
        matcher.find();
        int invisibleCoordinationX = Integer.parseInt(matcher.group(1));
        int invisibleCoordinationY = Integer.parseInt(matcher.group(2));

        if(!isCoordinationValid(invisibleCoordinationX, invisibleCoordinationY)){
            System.out.println(SomeOutputs.WORNG_COORDINATION.getValue());
            return;
        }
        if(!doesUserHaveAnyInvisibles()){
            System.out.println(SomeOutputs.DONT_HAVE_ENOUTH_INVISIBLE.getValue());
            return;
        }
        if(!isAnyShipsInThisPlace(invisibleCoordinationX, invisibleCoordinationY)){
            System.out.println(SomeOutputs.NO_SHIP_IN_THIS_PLACE.getValue());
            return;
        }
        if(hasThePlaceMadeInvisible(invisibleCoordinationX, invisibleCoordinationY)){
            System.out.println(SomeOutputs.ALREADY_MADE_INVISIBLE.getValue());
            return;
        }

        putInvisible(invisibleCoordinationX, invisibleCoordinationY);
        noteInvisibleInUserData(username);
    }

    private void noteInvisibleInUserData(String username) {
        int tempNumInvisible = User.getUserByUsername(username).getNumberOfInvisibles() - 1;
        User.getUserByUsername(username).setNumberOfInvisibles(tempNumInvisible);
    }

    private void putInvisible(int x, int y) {
        StringBuilder tempShipSign = new StringBuilder(boardMap[y][x]);
        tempShipSign.setCharAt(0, 'I');
        boardMap[y][x] = tempShipSign.toString();
    }

    private boolean hasThePlaceMadeInvisible(int invisibleCoordinationX, int invisibleCoordinationY) {
        return boardMap[invisibleCoordinationY][invisibleCoordinationX].startsWith("I");
    }

    private boolean isAnyShipsInThisPlace(int invisibleCoordinationX, int invisibleCoordinationY) {
        return boardMap[invisibleCoordinationY][invisibleCoordinationX].startsWith("S") ||
                boardMap[invisibleCoordinationY][invisibleCoordinationX].startsWith("I");
    }

    private boolean doesUserHaveAnyInvisibles() {
        return User.getUserByUsername(username).getNumberOfInvisibles() > 0;
    }

    //end puting invisible

    //put anti aircraft

    private void mangePutingAntiAircraft(Matcher matcher) {
        matcher.find();
        int rowOrColumnPointer = Integer.parseInt(matcher.group(1));
        char direction = matcher.group(2).charAt(0);

        if(!isCoordinationValid(rowOrColumnPointer)){
            System.out.println(SomeOutputs.WORNG_COORDINATION.getValue());
            return;
        }
        if(!isAntAircraftTotallyInTheBoard(rowOrColumnPointer)){
            System.out.println(SomeOutputs.OFF_THE_BOARD.getValue());
            return;
        }
        if(!isDirectionValid(direction)){
            System.out.println(SomeOutputs.INVALID_DIRECTION.getValue());
            return;
        }
        if(!doesUserHaveAnyAntiAircrafts()){
            System.out.println(SomeOutputs.DONT_HAVE_ENOUTH_ANTI_AIRCRAFT.getValue());
            return;
        }

        putAntiAircraftInTheArrayList(rowOrColumnPointer, direction);
        noteAntiAircraftInUserData();

    }

    private void noteAntiAircraftInUserData() {
        int tempNumAntiAircraft = User.getUserByUsername(username).getNumberOfAntiAircrafts() - 1;
        User.getUserByUsername(username).setNumberOfAntiAircrafts(tempNumAntiAircraft);
    }

    private void putAntiAircraftInTheArrayList(int rowOrColumnPointer, char direction) {
        antiAircrafts.add(rowOrColumnPointer + " " + direction);
    }

    private boolean isAntAircraftTotallyInTheBoard(int pointer){
        return pointer + 2 < 11;
    }

    private boolean doesUserHaveAnyAntiAircrafts() {
        return User.getUserByUsername(username).getNumberOfAntiAircrafts() > 0;
    }

    //end put anti aircraft

    //mine

    private void managePutingMine(Matcher matcher) {
        matcher.find();
        int mineCoordinationX = Integer.parseInt(matcher.group(1));
        int mineCoordinationY = Integer.parseInt(matcher.group(2));

        if(!isCoordinationValid(mineCoordinationX, mineCoordinationY)){
            System.out.println(SomeOutputs.WORNG_COORDINATION.getValue());
            return;
        }
        if(!doesUserHaveAnyMines()){
            System.out.println(SomeOutputs.DONT_HAVE_ENOUTH_MINE.getValue());
            return;
        }
        if(doesHaveCollision(1, mineCoordinationX, mineCoordinationY,'v')){
            System.out.println(SomeOutputs.COLLISION_WITH_OTHER_THING.getValue());
            return;
        }
        putMineInTheMap(mineCoordinationX, mineCoordinationY);
        noteMineInUserData();
    }

    private void noteMineInUserData() {
        int tempNumMine = User.getUserByUsername(username).getNumberOfMines() - 1;
        User.getUserByUsername(username).setNumberOfMines(tempNumMine);
    }

    private void putMineInTheMap(int x, int y) {
        boardMap[y][x] = "Mm";
    }
    private boolean doesUserHaveAnyMines() {
        return User.getUserByUsername(username).getNumberOfMines() > 0;
    }

    //end mine

    //show board

    public void showMyBoard(){
        StringBuilder boardMapStr = new StringBuilder();
        for(int i = 1; i < 11; i++){
            boardMapStr.append('|');
            for(int j = 1; j < 11; j++){
                boardMapStr.append(printingMap[i][j] + "|");
            }
            boardMapStr.append("\n");
        }
        System.out.print(boardMapStr);
    }

    public void arrangePrintingMap(){
        copyMapToPrintingMap();

        char antiAircraftDirection;
        int antiAircraftPointer;

        for (String antiAircraft : antiAircrafts) {

            antiAircraftDirection = getAntiAircraftDirection(antiAircraft);
            antiAircraftPointer = getAntiAircraftPointer(antiAircraft);
            putAntiAircraftInTheMap(antiAircraftPointer, antiAircraftDirection);
        }
    }

    private void copyMapToPrintingMap(){
        for(int i = 1; i < 11; i++){
            for (int j = 1; j < 11; j++){
                printingMap[i][j] = boardMap[i][j];
            }
        }
    }

    private void putAntiAircraftInTheMap(int rowOrColumnPointer, char direction) {

        if (direction == 'v') {
            for(int i = rowOrColumnPointer; i < rowOrColumnPointer + 3; i++){
                for(int j = 1; j < 11; j++){
                    if(printingMap[j][i].compareTo("  ") == 0){
                        printingMap[j][i] = "AA";
                    }
                }
            }
        }
        else{
            for(int i = rowOrColumnPointer; i < rowOrColumnPointer + 3; i++){
                for(int j = 1; j < 11; j++){
                    if(printingMap[i][j].compareTo("  ") == 0){
                        printingMap[i][j] = "AA";
                    }
                }
            }
        }
    }

    private int getAntiAircraftPointer(String antiAircraft) {
        Matcher matcher = getMatcher(antiAircraft, "(\\d)");
        matcher.find();
        return Integer.parseInt(matcher.group(1));
    }

    private char getAntiAircraftDirection(String antiAircraft) {

        Matcher matcher = getMatcher(antiAircraft, "([v|h])");
        matcher.find();
        return matcher.group(1).charAt(0);
    }


    //end show board

    public void showBoardForRival(){
        StringBuilder boardMapStr = new StringBuilder();
        for(int i = 1; i < 11; i++){
            boardMapStr.append('|');
            for(int j = 1; j < 11; j++){
                boardMapStr.append(boardMapForRival[i][j] + "|");
            }
            boardMapStr.append("\n");
        }
        System.out.print(boardMapStr);
    }


    //put boat

    private void managePutingBoat(Matcher matcher){
        matcher.find();

        int shipNumber = Integer.parseInt(matcher.group(1));
        int shipCoordinationX = Integer.parseInt(matcher.group(2));
        int shipCoordinationY = Integer.parseInt(matcher.group(3));
        char shipDirection = matcher.group(4).charAt(0);

        if(!isShipNumberValid(shipNumber)){
            System.out.println(SomeOutputs.INVALID_SHIP_NUMBER.getValue());
            return;
        }
        if(!isCoordinationValid(shipCoordinationX, shipCoordinationY)){
            System.out.println(SomeOutputs.WORNG_COORDINATION.getValue());
            return;
        }
        if(!isDirectionValid(shipDirection)){
            System.out.println(SomeOutputs.INVALID_DIRECTION.getValue());
            return;
        }
        if(!isShipTotallyInTheBoard(shipNumber, shipCoordinationX, shipCoordinationY, shipDirection)){
            System.out.println(SomeOutputs.OFF_THE_BOARD.getValue());
            return;
        }
        if(!doesAnyBoatsInThisTypeRemain(shipNumber)){
            System.out.println(SomeOutputs.DONT_HAVE_THIS_TYPE_OF_SHIP.getValue());
            return;
        }
        if(doesHaveCollision(shipNumber, shipCoordinationX, shipCoordinationY, shipDirection)){
            System.out.println(SomeOutputs.COLLISION_WITH_OTHER_THING.getValue());
            return;
        }
        putBoatInBoard(shipNumber, shipCoordinationX, shipCoordinationY, shipDirection);
        putBoatInArraylist(shipNumber, shipCoordinationX, shipCoordinationY, shipDirection);

    }

    private void putBoatInArraylist (int length, int xPointer, int yPointer, char direction) {
        getTypeShipByLenght(length).add(new Ship(length, xPointer, yPointer, direction));
    }

    private void putBoatInBoard(int length, int xPointer, int yPointer, char direction) {

        if(direction == 'v'){
            for(int i = 0; i < length; i++){
                boardMap[yPointer + i][xPointer] = "S" + length;
            }
        }
        else{
            for(int i = 0; i < length; i++){
                boardMap[yPointer][xPointer + i] = "S" + length;
            }
        }

    }

    //end put boat

    private ArrayList<Ship> getTypeShipByLenght(int length){

        if(length == 1){
            return s1Ships;
        }
        else if (length == 2){
            return s2Ships;
        }
        else if (length == 3){
            return s3Ships;
        }
        else {
            return s4Ships;
        }
    }

    private boolean doesHaveCollision(int length, int x, int y, char direction) {

        if(direction == 'v'){
            for(int i = 0; i < length; i++){

                if(!(boardMap[y + i][x].equals("  "))){
                    return true;
                }
            }
        }
        else{
            for(int i = 0; i < length; i++){

                if(!(boardMap[y][x + i].equals("  "))){
                    return true;
                }
            }
        }

        return false;
    }

    private boolean doesAnyBoatsInThisTypeRemain(int length){
        return !(getTypeShipByLenght(length).size() == 5 - length);
    }

    private boolean isShipTotallyInTheBoard(int length, int x, int y, char direction){
        if(direction == 'v'){
            return y + length - 1 < 11;
        }
        else{
            return x + length - 1 < 11;
        }
    }

    private boolean isCoordinationValid(int x, int y){
        return x > 0 && x < 11 && y > 0 && y < 11;
    }

    private boolean isCoordinationValid(int x){
        return x > 0 && x < 11;
    }

    private boolean isShipNumberValid(int num){
        return num > 0 && num < 5;
    }

    private boolean isDirectionValid(char direction){
        return direction == 'v' || direction == 'h';
    }

    private Matcher getMatcher(String matchingStr, String regex) {
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(matchingStr);
    }

}
