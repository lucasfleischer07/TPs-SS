package ar.edu.itba.ss.models;

import java.util.Objects;

public class Event implements Comparable<Event> {
    private Particle p1, p2;
    private double time;
    private int wallToCollides;
    private int p1CollisionCount = 0, p2CollisionCount = 0;

//    public Event(Particle p1, Particle p2, double time) {
//        this.p1 = p1;
//        this.p2 = p2;
//        this.time = time;
//        if (p1 != null)
//            this.p1CollisionCount = p1.getCollisionCount();
//        if (p2 != null)
//            this.p2CollisionCount = p2.getCollisionCount();
//    }
//
//    public Event(Particle p1, Particle p2, double time, int wallToCollides) {
//        this.p1 = p1;
//        this.p2 = p2;
//        this.time = time;
//        this.wallToCollides = wallToCollides;
//        if (p1 != null)
//            this.p1CollisionCount = p1.getCollisionCount();
//        if (p2 != null)
//            this.p2CollisionCount = p2.getCollisionCount();
//    }

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

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        Event event = (Event) o;
//        return Double.compare(time, event.time) == 0 && wallToCollides == event.wallToCollides && Objects.equals(p2, event.p1) && Objects.equals(p1, event.p2) && Objects.equals(p1, event.p1) && Objects.equals(p2, event.p2);
//    }

    @Override
    public int hashCode() {
        return Objects.hash(p1, p2, time, wallToCollides);
    }

//    public boolean wasSuperveningEvent() {
//        return (p1 != null && p1CollisionCount != p1.getCollisionCount()) || (p2 != null && p2CollisionCount != p2.getCollisionCount());
//    }
}
