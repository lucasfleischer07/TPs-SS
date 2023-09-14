package ar.edu.itba.ss.utils;

import ar.edu.itba.ss.models.Particle;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CollisionsMa {
    private final List<Particle> particles;
    private final double[][] timeToCollision;

    private final double mainPerimeterWidth;
    private final double mainPerimeterHeight;
    private final double minorPerimeterWidth;
    private final double minorPerimeterHeight;
    private final int maxStep;
    private double actualTime = 0;
    private int rep;
    private int lastMinI = -1, lastMinJ = -1;

    private final int NUMBER_OF_WALLS = 8;

    public CollisionsMa(List<Particle> particles, double mainPerimeterWidth, double mainPerimeterHeight,
                        double minorPerimeterWidth, double minorPerimeterHeight, int maxStep, int rep) {
        this.particles = particles;
        this.timeToCollision = new double[particles.size() + NUMBER_OF_WALLS][particles.size() + NUMBER_OF_WALLS];
        this.mainPerimeterWidth = mainPerimeterWidth;
        this.mainPerimeterHeight = mainPerimeterHeight;
        this.minorPerimeterWidth = minorPerimeterWidth;
        this.minorPerimeterHeight = minorPerimeterHeight;
        this.maxStep = maxStep;
        this.rep = rep;
    }

    private void collisionToWalls(int i) {
        Particle particle = particles.get(i);

        // Pared izquierda del cuadrado
        timeToCollision[i][particles.size()] = (0 + particle.getRadius() - particle.getX()) / particle.getVx();

        // Pared superior del cuadrado
        timeToCollision[i][particles.size() + 1] = (-0 - particle.getRadius() - particle.getY()) / particle.getVy();

        // Pared derecha del cuadrado, parte de arriba
        timeToCollision[i][particles.size() + 2] = (mainPerimeterWidth - particle.getRadius() - particle.getX()) / particle.getVx();

        // Pared superior del rectángulo
        timeToCollision[i][particles.size() + 3] = (-(mainPerimeterHeight - minorPerimeterHeight) / 2 - particle.getRadius() - particle.getY()) / particle.getVy();

        // Pared derecha del rectangulo
        timeToCollision[i][particles.size() + 4] = (mainPerimeterWidth + minorPerimeterWidth - particle.getRadius() - particle.getX()) / particle.getVx();

        // Pared inferior del rectángulo
        timeToCollision[i][particles.size() + 5] = (-((mainPerimeterHeight - minorPerimeterHeight) / 2 + minorPerimeterHeight) + particle.getRadius() - particle.getY()) / particle.getVy();

        // Pared derecha del cuadrado, parte de abajo
        timeToCollision[i][particles.size() + 6] = (mainPerimeterWidth - particle.getRadius() - particle.getX()) / particle.getVx();

        // Pared inferior del cuadrado
        timeToCollision[i][particles.size() + 7] = (-mainPerimeterHeight + particle.getRadius() - particle.getY()) / particle.getVy();


        for (int j = particles.size(); j < particles.size() + NUMBER_OF_WALLS; j++) {
            if (timeToCollision[i][j] < 0) {
                timeToCollision[i][j] = Double.MAX_VALUE;
            } else {
                // Calcula la posición futura de la partícula
                double futureX = particle.getX() + particle.getVx() * timeToCollision[i][j];
                double futureY = particle.getY() + particle.getVy() * timeToCollision[i][j];
                double radius = particle.getRadius();

                switch (j % particles.size()) {
                    case 0 -> // Pared izquierda del cuadrado
                            timeToCollision[i][j] = (-mainPerimeterHeight + radius <= futureY && futureY <= 0 - radius) ? timeToCollision[i][j] : Double.MAX_VALUE;
                    case 1, 7 -> // Pared superior e inferior del cuadrado
                            timeToCollision[i][j] = (0 + radius <= futureX && futureX <= mainPerimeterWidth - radius) ? timeToCollision[i][j] : Double.MAX_VALUE;
                    case 2 -> // Pared derecha del cuadrado, parte de arriba
                            timeToCollision[i][j] = (-(mainPerimeterHeight - minorPerimeterHeight) / 2 - radius <= futureY && futureY <= 0 - radius) ? timeToCollision[i][j] : Double.MAX_VALUE;
                    case 3, 5 -> // Pared superior e inferior del rectángulo
                            timeToCollision[i][j] = (mainPerimeterWidth - radius <= futureX && futureX <= mainPerimeterWidth + minorPerimeterWidth - radius) ? timeToCollision[i][j] : Double.MAX_VALUE;
                    case 4 -> // Pared derecha del rectángulo
                            timeToCollision[i][j] = (-((mainPerimeterHeight - minorPerimeterHeight) / 2 + minorPerimeterHeight) + radius <= futureY && futureY <= -(mainPerimeterHeight - minorPerimeterHeight) / 2 - radius)  ? timeToCollision[i][j] : Double.MAX_VALUE;
                    case 6 -> // Pared derecha del cuadrado, parte de abajo
                            timeToCollision[i][j] = (-mainPerimeterHeight + radius <= futureY && futureY <= -((mainPerimeterHeight - minorPerimeterHeight) / 2 + minorPerimeterHeight) + radius) ? timeToCollision[i][j] : Double.MAX_VALUE;
                }
            }
        }
    }

    private double collisionBetweenParticles(int i, int j) {
        Particle particleI = particles.get(i);
        Particle particleJ = particles.get(j);

        //deltaR vector (deltaX, deltaY)
        double[] deltaR = new double[2];
        deltaR[0] = particleI.getX() - particleJ.getX();
        deltaR[1] = particleI.getY() - particleJ.getY();

        //deltaV vector (deltaVx, deltaVy)
        double[] deltaV = new double[2];
        deltaV[0] = particleI.getVx() - particleJ.getVx();
        deltaV[1] = particleI.getVy() - particleJ.getVy();

        //deltaR dot deltaR
        double deltaR_deltaR = deltaR[0] * deltaR[0] + deltaR[1] * deltaR[1];

        //deltaV dot deltaV
        double deltaV_deltaV = deltaV[0] * deltaV[0] + deltaV[1] * deltaV[1];

        //deltaV dot deltaR
        double deltaV_deltaR = deltaV[0] * deltaR[0] + deltaV[1] * deltaR[1];

        //sigma = radius of particle i + radius of particle j
        double sigma = particleI.getRadius() + particleJ.getRadius();

        if (deltaV_deltaR >= 0) {
            return Double.MAX_VALUE;
        } else {
            double d = (deltaV_deltaR * deltaV_deltaR) - deltaV_deltaV * (deltaR_deltaR - sigma * sigma);
            if (d < 0) {
                return Double.MAX_VALUE;
            } else {
                return -(deltaV_deltaR + Math.sqrt(d)) / deltaV_deltaV;
            }
        }
    }

    private void doAStep() {
        // Calculate collision times
        for (int i = 0; i < particles.size(); i++) {
            for (int j = i + 1; j < particles.size(); j++) {
                this.timeToCollision[i][j] = collisionBetweenParticles(i, j);
            }
            collisionToWalls(i);
        }

        // Calculate the next collision
        double minTimeToCollision = Double.MAX_VALUE;
        int minI = -1;
        int minJ = -1;
        for (int i = 0; i < particles.size(); i++) {
            for (int j = i + 1; j < particles.size() + NUMBER_OF_WALLS; j++) {
                if (timeToCollision[i][j] < minTimeToCollision && !(i == lastMinI && j == lastMinJ)) {
                    minTimeToCollision = timeToCollision[i][j];
                    minI = i;
                    minJ = j;
                }
            }
        }

        lastMinI = minI;
        lastMinJ = minJ;

        actualTime += minTimeToCollision;

        // Update positions of particles considering the next collision
        for (Particle particle : particles) {
            particle.setX(particle.getX() + particle.getVx() * minTimeToCollision);
            particle.setY(particle.getY() + particle.getVy() * minTimeToCollision);
        }

        // Update velocities of the particles that collide,
        // if the collision is between one particle and one vertical wall -> (-vx, vy)
        // if the collision is between one particle and one horizontal wall -> (vx, -vy)
        if (minJ >= particles.size()) {
            Particle particle = particles.get(minI);
            if (minJ % 2 == 0) {
                particle.setVx(-particle.getVx());
            }
            else {
                particle.setVy(-particle.getVy());
            }
        } else {
            // if the collision is between two particles -> consider conservation of momentum (Jx, Jy),
            // elastic collision without friction or rotation, and the mass
            double[] deltaV = new double[2];
            double[] deltaR = new double[2];
            double sigma = particles.get(minI).getRadius() + particles.get(minJ).getRadius();

            deltaR[0] = particles.get(minI).getX() - particles.get(minJ).getX();
            deltaR[1] = particles.get(minI).getY() - particles.get(minJ).getY();
            deltaV[0] = particles.get(minI).getVx() - particles.get(minJ).getVx();
            deltaV[1] = particles.get(minI).getVy() - particles.get(minJ).getVy();
            double deltaV_deltaR = deltaV[0] * deltaR[0] + deltaV[1] * deltaR[1];

            double J = 2 * particles.get(minI).getMass() * particles.get(minJ).getMass() * deltaV_deltaR / ((particles.get(minI).getMass() + particles.get(minJ).getMass()) * sigma);
            double Jx = J * deltaR[0] / sigma;
            double Jy = J * deltaR[1] / sigma;

            particles.get(minI).setVx(particles.get(minI).getVx() - Jx / particles.get(minI).getMass());
            particles.get(minI).setVy(particles.get(minI).getVy() - Jy / particles.get(minI).getMass());
            particles.get(minJ).setVx(particles.get(minJ).getVx() + Jx / particles.get(minJ).getMass());
            particles.get(minJ).setVy(particles.get(minJ).getVy() + Jy / particles.get(minJ).getMass());
        }
    }

    private void writeOutputStep(int step) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(
                    "src/main/resources/output_" + minorPerimeterHeight + "_" + rep + ".txt", true));
            writer.write("STEP " + step + "\n");
            writer.write("TIME " + actualTime + "\n");
            writer.write("COLLISION " + lastMinI + " " + (lastMinJ >= particles.size() ? "P" + lastMinJ % particles.size() : lastMinJ) + "\n");

            for (Particle particle : particles) {
                writer.write(particle.getX() + " " + particle.getY() + " " +
                        particle.getVx() + " " + particle.getVy() + "\n");
            }
            writer.write("\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        //Write outputs step by step
        writeOutputStep(0);

        long startTime, endTime, elapsedTime = 0;
        int i;
        for (i = 1; i <= maxStep; i++) {
            startTime = System.currentTimeMillis();
            doAStep();
            endTime = System.currentTimeMillis();
            elapsedTime = elapsedTime + endTime - startTime;
            writeOutputStep(i);
        }

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(
                    "src/main/resources/output_" + minorPerimeterHeight + "_" + rep + ".txt", true));

            writer.write("N " + particles.size() + "\n");
            writer.write("MAXSTEP " + i + "\n");
            writer.write("ELAPSEDTIME " + elapsedTime + "\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
