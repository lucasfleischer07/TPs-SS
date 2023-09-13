package ar.edu.itba.ss.utils;

import ar.edu.itba.ss.models.Event;
import ar.edu.itba.ss.models.Particle;
import ar.edu.itba.ss.models.ParticlePair;

import java.util.*;

public class Collisions {

    private double totalTime;
    private PriorityQueue<Event> priorityQueue;
    private final Set<ParticlePair> collidedPairs = new HashSet<>();
    private final List<Particle> particles;


    public Collisions(List<Particle> particles) {
        this.particles = particles;
        this.priorityQueue = new PriorityQueue<>();
        this.totalTime = 0;
    }

    public double nextEvent() {
        for (Particle particle: particles) {
            priorityQueue.add(particleEvent(particle));
        }

        Event event = priorityQueue.poll();

        Particle p1 = event.getP1();
        Particle p2 = event.getP2();

        Iterator<Event> iterator = priorityQueue.iterator();
        while (iterator.hasNext()) {
            Event e = iterator.next();
            if ((p1 != null && (p1.equals(e.getP1()) || p1.equals(e.getP2())) ) || (p2 != null && (p2.equals(e.getP2()) || p2.equals(e.getP1())))) {
                iterator.remove();
            }
        }

        for (Particle p : particles) {
            p.setX(p.getX() + p.getVx() * event.getTime());
            p.setY(p.getY() + p.getVy() * event.getTime());
        }


        // Significa que el choque sera con una pared
        if(p2 == null) {
            if(event.getWallToCollides() % 2 == 0) { //significa que es una pared vertical
                p1.impactXWall();
            } else {
                p1.impactYWall();
            }
        } else {
            p1.collideWithParticle(p2);
        }

        return event.getTime();

    }


    private Event particleEvent(Particle p2) {
        double tc;
        Event nextMinimalEvent = null;

        // Calculo de tc para choque con otras particulas
        for(Particle p1: particles) {
            if(p2.equals(p1)) continue;

            tc = p2.collideWithParticleTime(p1);
            if(tc > 0) {
                if(nextMinimalEvent == null|| tc < nextMinimalEvent.getTime()) {
                    nextMinimalEvent = new Event(p2, p1, tc);
                }
            }
        }

        // Calculo de tc para choque con paredes
        Object[] wallTc = p2.impactToWallTime();
        double timeToCollidesWall = (double) wallTc[0];

        if(timeToCollidesWall < nextMinimalEvent.getTime()) {
            nextMinimalEvent = new Event(p2, null, (double) wallTc[0], (int) wallTc[1]);
        }

        return nextMinimalEvent;
    }

    public void setTotalTime(double totalTime) {
        this.totalTime = totalTime;
    }

    public double getTotalTime() {
        return totalTime;
    }
}
