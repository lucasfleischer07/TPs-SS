package ar.edu.itba.ss.models;

public class Event implements Comparable<Event> {
    private Particle p1, p2;
    private double time;
    private int wallToCollides;

    public Event(Particle p1, Particle p2, double time) {
        this.p1 = p1;
        this.p2 = p2;
        this.time = time;
    }

    public Event(Particle p1, Particle p2, double time, int wallToCollides) {
        this.p1 = p1;
        this.p2 = p2;
        this.time = time;
        this.wallToCollides = wallToCollides;
    }

    public Particle getP1() {
        return p1;
    }

    public Particle getP2() {
        return p2;
    }

    public double getTime() {
        return time;
    }

    public void setP1(Particle p1) {
        this.p1 = p1;
    }

    public void setP2(Particle p2) {
        this.p2 = p2;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public int getWallToCollides() {
        return wallToCollides;
    }

    public void setWallToCollides(int wallToCollides) {
        this.wallToCollides = wallToCollides;
    }

    @Override
    public int compareTo(Event o) {
        int c = 0;

        if (this.time > o.time)
            c = 1;
        else if (this.time < o.time)
            c = -1;

        return c;
    }

}
