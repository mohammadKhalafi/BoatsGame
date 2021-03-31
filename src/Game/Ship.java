package Game;

import java.util.ArrayList;

public class Ship {

    int numberOfNotDestroyedPoints;
    int length;
    int xPointer;
    int yPointer;
    char direction;
    ArrayList<Integer> shipPoints = new ArrayList<>();

    public Ship(int length, int xPointer, int yPointer, char direction){

        setLength(length);
        setXPointer(xPointer);
        setYPointer(yPointer);
        setDirection(direction);
        setNumberOfNotDestroyedPoints();

        setShipPoints();
    }

    public void setNumberOfNotDestroyedPoints() {
        this.numberOfNotDestroyedPoints = length;
    }

    public void setLength(int length){
        this.length = length;
    }

    public void setXPointer(int xPointer){
        this.xPointer = xPointer;
    }

    public void setYPointer(int yPointer){
        this.yPointer = yPointer;
    }

    public void setDirection(char direction){
        this.direction = direction;
    }

    public void setShipPoints(){

        if(direction == 'v'){
            for(int i = 0; i < length; i++){
                shipPoints.add(xPointer);
                shipPoints.add(yPointer + i);
            }
        }
        else{
            for(int i = 0; i < length; i++){
                shipPoints.add(xPointer + i);
                shipPoints.add(yPointer);
            }
        }
    }

}
