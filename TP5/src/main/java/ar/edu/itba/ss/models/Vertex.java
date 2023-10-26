package ar.edu.itba.ss.models;

public class Vertex {

    //TODO REFFF
    private final ParticlePair position;

    public Vertex(Double x, Double y) {
        this.position = new ParticlePair(x, y);
    }

    public void setY(Double Y){
        this.position.setY(Y);
    }

    public Double getY(){
        return this.position.getY();
    }

    public ParticlePair getPosition() {
        return position;
    }

    public String toString() {
        return position.getX() + " " + position.getY() + " " + 0.0 + " " + 0.0 + " " + 0.0001 + " " + 0 + " " + 0 + " " + 0;
    }
}
