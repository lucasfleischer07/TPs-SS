package ar.edu.itba.ss.utils;

import ar.edu.itba.ss.models.Event;
import ar.edu.itba.ss.models.Particle;
import ar.edu.itba.ss.models.ParticlePair;

import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

public class Collisions {

    private double totalTime;
    private final PriorityQueue<Event> priorityQueue;
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

        this.totalTime += event.getTime();

        for (Particle p : particles) {
            p.setX(p.getX() + p.getVx() * event.getTime());
            p.setY(p.getY() + p.getVy() * event.getTime());
        }

        Particle p1 = event.getP1();
        Particle p2 = event.getP2();


        // Significa que el choque sera con una pared
        if(p2 == null) {
            if(event.getWallToCollides() % 2 == 0) { //significa que es una pared vertical
                p1.impactXWall();
            } else {
                p1.impactYWall();
            }
        } else {
            p1.collideWithParticle(p2);
            System.out.println("Choque contra particula");
        }

        return event.getTime();

    }


    private Event particleEvent(Particle p2) {
        double tc;
        Event nextMinimalEvent = null;

        // Calculo de tc para choque con otras particulas
        for(Particle p1: particles) {
            if(p2.equals(p1)) continue;

//            boolean collisionAlreadyOccurred = checkCollisionAlreadyOccurred(p1, p2);

//            if(!collisionAlreadyOccurred){
                tc = p2.collideWithParticleTime(p1);
                if(nextMinimalEvent == null|| tc < nextMinimalEvent.getTime()) {
                    nextMinimalEvent = new Event(p2, p1, tc);
                }
//            }
        }

        // Calculo de tc para choque con paredes
        Object[] wallTc = p2.impactToWallTime();
        double timeToCollidesWall = (double) wallTc[0];

        if(timeToCollidesWall < nextMinimalEvent.getTime()) {
            nextMinimalEvent = new Event(p2, null, (double) wallTc[0], (int) wallTc[1]);
        }

        return nextMinimalEvent;
    }

    private boolean checkCollisionAlreadyOccurred(Particle p1, Particle p2) {
        // Crea un objeto ParticlePair para representar la pareja de partículas p1 y p2
        ParticlePair pair = new ParticlePair(p1, p2);

        // Verifica si la pareja de partículas ya está en el conjunto de colisiones previas
        return collidedPairs.contains(pair);
    }

    // Método para registrar una colisión entre dos partículas
    private void registerCollision(Particle p1, Particle p2) {
        // Crea un objeto ParticlePair para representar la pareja de partículas p1 y p2
        ParticlePair pair = new ParticlePair(p1, p2);

        // Agrega la pareja al conjunto de colisiones previas
        collidedPairs.add(pair);
    }

    public void setTotalTime(double totalTime) {
        this.totalTime = totalTime;
    }

    public double getTotalTime() {
        return totalTime;
    }
}
