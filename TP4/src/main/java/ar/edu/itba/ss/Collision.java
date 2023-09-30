package ar.edu.itba.ss;

import ar.edu.itba.ss.models.Euler;
import ar.edu.itba.ss.models.Parameters;
import ar.edu.itba.ss.models.Particle;
import ar.edu.itba.ss.models.Verlet;
import ar.edu.itba.ss.utils.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Collision {
    private List<Particle> particles;
    private final double dt;
    private double totalTime;
//    datos de consigna

//    private double L = 135;
    private double L = Configuration.getLineLength();

//    private double r = 2.25;
    private double r = Configuration.getParticleRadius();

//    private double k = 2500;
    private double k = 2.5;
    private double colisionForceSum = 0;


    public Collision(List<Particle> particles, double dt) {
        this.particles = particles;
        this.dt = dt;
        this.totalTime = 0;
    }

    public void nextCollision() {
        if(!Configuration.getIntegrationMethod().equals("gear")) {
            for(Particle p1 : particles) {
                double nextX = newPosition(p1.getX(), p1.getVelX(), p1.getMass(), 0.0, 0.0);
                double nextVx = newVelocity(nextX, p1.getVelX(), p1.getMass(), 0.0, 0.0);
                p1.setX(nextX);
                p1.setVelX(nextVx);
            }
        } else {

            particles = gearNextCollision();
        }

//        for(Particle p1 : particles) {
//            double force = 0;
//            for(Particle p2 : particles) {
//                if(p1 == p2) {
//                    continue;
//                }
//                force += getCollisionForce(p1, p2);
//            }
//
//        }

        totalTime += dt;
    }

    private double newPosition(double x, double vx, double mass, double force, double prevX) {
        if(Configuration.getIntegrationMethod().equals("verlet")) {
            return Verlet.positionFromVerlet(x, prevX, dt, mass, force);
        } else if(Configuration.getIntegrationMethod().equals("euler")) {
            return Euler.positionFromEuler(x, vx, force, dt, mass);
        } else {
            throw new RuntimeException("Invalid integration method");
        }
    }

    private double newVelocity(double x, double vx, double mass, double force, double prevX) {
        if(Configuration.getIntegrationMethod().equals("verlet")) {
            return Verlet.velocityFromVerlet(x, prevX, dt);
        } else if(Configuration.getIntegrationMethod().equals("euler")) {
            return Euler.velocityFromEuler(vx, force, dt, mass);
        } else {
            throw new RuntimeException("Invalid integration method");
        }
    }

    //fuerza entre choque de particulas
    private double getCollisionForce(Particle p1, Particle p2) {
        return k * (Math.abs(p1.getX()-p2.getX()) - (2*r)) * (Math.signum(p1.getX()-p2.getX()));
    }

    private double getPropulsionForce(Particle p) {
//        return (p.getU()-p.getVelX())/1;
        return p.getU()-p.getVelX();
    }

    //va a devolver a aceleración por eso se divide la fuerza por la masa al retornar
    //va a ser la sumatoria de la propulsion de la partícula más la sumatoria de la fuerza de colision
    private double particleMovementEquation(Particle p, List<Particle> particles) {
        for(Particle otherParticle: particles) {
            //chequeo que no sea la misma partícula
            if(otherParticle != p) {
                double xDistance = Math.abs(otherParticle.getX() - p.getX()) % L;
                double radiusSum = p.getRadius() + otherParticle.getRadius();

                if (Math.sqrt(Math.pow(xDistance, 2)) <= radiusSum) {
                    if (xDistance != 0)
                        colisionForceSum += getCollisionForce(p, otherParticle);
                    else
                        colisionForceSum = 0;
                }

//                colisionForceSum += getCollisionForce(p, otherParticle);
            }
        }
        return (getPropulsionForce(p) + colisionForceSum)/p.getMass();
    }


    private List<Particle> gearNextCollision() {
        List<Particle> newParticles = new ArrayList<>();
        for(Particle p1 : particles) {
            Particle newParticle = new Particle(p1.getX(), p1.getY(), p1.getVelX(), p1.getVelY(), p1.getU(), p1.getRadius(), p1.getMass(), p1.getForceX(), p1.getForceY(), p1.getX2(), p1.getX3(), p1.getX4(), p1.getX5());

            //predecimos las nuevas derivadas hasta orden 5
            double[] predictionPositionX = getPredictions(newParticle.getX(), p1.getVelX(), p1.getX2(), p1.getX3(), p1.getX4(), p1.getX5());

            //evaluamos en a(t+dt)
            newParticle.setX(predictionPositionX[0]);
            newParticle.setVelX(predictionPositionX[1]);

            //busco la fuerza para calcular aceleracion y R2
//            newParticle.setA(particleMovementEquation(newParticle, particles));
            double deltaA = particleMovementEquation(newParticle, particles) - predictionPositionX[2];
            double deltaR2 = deltaA * Math.pow(dt, 2) / factorialNumber(2);

            //llamar al corrector
            double[] gearCoefficients = {3/20.0, 251/360.0, 1, 11/18.0, 1/6.0, 1/60.0};
            newParticle.setX(predictionPositionX[0] + gearCoefficients[0] * deltaR2);
            newParticle.setVelX(predictionPositionX[1] + gearCoefficients[1] * deltaR2 / dt);
            newParticle.setX2(predictionPositionX[2] + gearCoefficients[2] * deltaR2 * factorialNumber(2) / Math.pow(dt, 2));
            newParticle.setX3(predictionPositionX[3] + gearCoefficients[3] * deltaR2 * factorialNumber(3) / Math.pow(dt, 3));
            newParticle.setX4(predictionPositionX[4] + gearCoefficients[4] * deltaR2 * factorialNumber(4) / Math.pow(dt, 4));
            newParticle.setX5(predictionPositionX[5] + gearCoefficients[5] * deltaR2 * factorialNumber(5) / Math.pow(dt, 5));

            newParticles.add(newParticle);
        }

        return newParticles;
    }


    private double[] getPredictions(double r, double r1, double r2, double r3, double r4, double r5) {
        double rp = r + r1 * dt + r2 * Math.pow(dt, 2) / factorialNumber(2) + r3 * Math.pow(dt, 3) / factorialNumber(3) + r4 * Math.pow(dt, 4) / factorialNumber(4) + r5 * Math.pow(dt, 5) / factorialNumber(5);
        double r1p = r1 + r2 * dt + r3 * Math.pow(dt, 2) / factorialNumber(2) + r4 * Math.pow(dt, 3) / factorialNumber(3) + r5 * Math.pow(dt, 4) / factorialNumber(4);
        double r2p = r2 + r3 * dt + r4 * Math.pow(dt, 2) / factorialNumber(2) + r5 * Math.pow(dt, 3) / factorialNumber(3);
        double r3p = r3 + r4 * dt + r5 * Math.pow(dt, 2) / factorialNumber(2);
        double r4p = r4 + r5 * dt;
        double r5p = r5;

        return new double[]{rp, r1p, r2p, r3p, r4p, r5p};
    }

    private static int factorialNumber(int n) {
        if (n == 0)
            return 1;
        return n * factorialNumber(n-1);
    }


    public List<Particle> getParticles() {
        return particles;
    }

    public double getTotalTime() {
        return totalTime;
    }
}
