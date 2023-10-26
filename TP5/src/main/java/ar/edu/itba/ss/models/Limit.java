package ar.edu.itba.ss.models;

public class Limit {

    //TODO REFFF
    private final Pair position;

    public Limit(Double x, Double y) {
        this.position = new Pair(x, y);
    }

    public void setY(Double Y){
        this.position.setY(Y);
    }

    public Double getY(){
        return this.position.getY();
    }

    public Pair getPosition() {
        return position;
    }

    public String toString() {
        return position.getX() + " " + position.getY() + " " + 0.0 + " " + 0.0 + " " + 0.0001 + " " + 0 + " " + 0 + " " + 0;
    }
}
