package ar.edu.itba.ss.utils;

import ar.edu.itba.ss.models.Event;
import ar.edu.itba.ss.models.Particle;

import java.util.List;
import java.util.PriorityQueue;

public class Collisions {

    private double tc;

    private final PriorityQueue<Event> priorityQueue;

    Event nextMinimalEvent = null;

    private final List<Particle> particles;


    public Collisions(double tc, List<Particle> particles) {
        this.tc = tc;
        this.particles = particles;
        this.priorityQueue = new PriorityQueue<>();
    }

    public void nextEvent() {
        Event event = null;


    }


//    TODO: Si hay tiempo, hacer lo de no comparar o calcular 2 veces el miso tiempo
    private Event particleEvent(Particle p2) {

        // Calculo de tc para choque con otras particulas
        for(Particle p1: particles) {
            if(p2.equals(p1)) continue;
            tc = p2.collideWithParticleTime(p1);
            if(nextMinimalEvent == null || tc < nextMinimalEvent.getTime()) {
                nextMinimalEvent = new Event(p2, p1, tc);
            }
        }

        // Calculo de tc para choque con pared en x
        tc = p2.impactXWallTime();
        if(tc < nextMinimalEvent.getTime()) {
            nextMinimalEvent = new Event(p2, null, tc);
        }

        // Calculo de tc para choque con pared en y
        tc = p2.impactYWallTime();
        if(tc < nextMinimalEvent.getTime()) {
            nextMinimalEvent = new Event(p2, null, tc);
        }

        return nextMinimalEvent;

    }


}
