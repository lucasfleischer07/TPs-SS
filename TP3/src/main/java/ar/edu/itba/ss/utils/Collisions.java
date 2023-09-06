package ar.edu.itba.ss.utils;

import ar.edu.itba.ss.models.Event;
import ar.edu.itba.ss.models.Particle;
import ar.edu.itba.ss.models.ParticlePair;

import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

public class Collisions {

    private double tc;

    private final PriorityQueue<Event> priorityQueue;

    private final Set<ParticlePair> collidedPairs = new HashSet<>();

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

            boolean collisionAlreadyOccurred = checkCollisionAlreadyOccurred(p1, p2);

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


}
