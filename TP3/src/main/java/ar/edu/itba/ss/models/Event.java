package ar.edu.itba.ss.models;

public class Event {
    private Particle p1, p2;
    private double time;

    public Event(Particle p1, Particle p2, double time) {
        this.p1 = p1;
        this.p2 = p2;
        this.time = time;

    }
}
