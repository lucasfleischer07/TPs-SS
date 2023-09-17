package ar.edu.itba.ss.utils;

import ar.edu.itba.ss.models.Particle;

import java.util.List;

public class Collision {
    private final List<Particle> particles;
    private final double[][] tc;
    private final double tableWidth, l;
    private double actualTime = 0;
    private int collisionP1, collisionP2 = -1;
    private final int WALLS_AMOUNT = 8;

    public Collision(List<Particle> particles, double tableWidth, double l) {
        this.particles = particles;
        this.tc = new double[WALLS_AMOUNT + particles.size()][WALLS_AMOUNT + particles.size()];
        this.tableWidth = tableWidth;
        this.l = l;
    }

    public void setFuturePositions(Particle p, double minTc) {
        p.setX(p.getX() + p.getVx()* minTc);
        p.setY(p.getY() + p.getVy()* minTc);
    }

    public Object[] nextIteration() {
        for (int i = 0; i < particles.size(); i++) {
            for (int j = i + 1; j < particles.size(); j++) {
                this.tc[i][j] = particles.get(i).collideWithParticleTime(particles.get(j));
            }
            particles.get(i).impactToWallTime(i, tc, particles, tableWidth, l);
        }

        double minTc = Double.MAX_VALUE;
        int minTcP1 = -1, minTcP2 = -1;
        for (int i = 0; i < particles.size(); i++) {
            for (int j = i + 1; j < particles.size() + WALLS_AMOUNT; j++) {
                if (tc[i][j] < minTc && !(i == collisionP1 && j == collisionP2)) {
                    minTc = tc[i][j];
                    minTcP1 = i;
                    minTcP2 = j;
                }
            }
        }

        collisionP1 = minTcP1;
        collisionP2 = minTcP2;
        actualTime += minTc;

        for (Particle particle : particles) {
            setFuturePositions(particle, minTc);
        }

        if (minTcP2 >= particles.size()) {
            Particle particle = particles.get(minTcP1);
            if (minTcP2 % 2 == 0) {
                particle.impactXWall();
            }
            else {
                particle.impactYWall();
            }
        } else {
            particles.get(minTcP1).collideWithParticle(particles.get(minTcP2));
        }

        return new Object[]{actualTime, collisionP1, (collisionP2 >= particles.size() ? "W" + collisionP2 % particles.size() : collisionP2)};

    }

}
