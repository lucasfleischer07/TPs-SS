package ar.edu.itba.ss.models;

import ar.edu.itba.ss.utils.Color;
import ar.edu.itba.ss.utils.Configuration;
import ar.edu.itba.ss.utils.Forces;
import ar.edu.itba.ss.utils.GenerateParticle;

import java.util.ArrayList;
import java.util.List;

import static ar.edu.itba.ss.utils.Forces.*;

public class Simulation {
    private static final double A = Configuration.getA();
    private static final double ZERO_VALUE = 0.0, REINJECT_MIN_LIMIT = 40.0;
    private final CellIndex[][] cellIndexes;

    //con esta cantidad obtenemos la grilla mas optima posible
    private static final int COLS_TOTAL = 8;
    private static final int ROWS_SILO = 30;
    private static final int ROWS_TOTAL = 33;

    //usammos valores positivos pra el silo, para no trabajar con numeros negativos
    private static final double DIMENSION_X_AXES = Configuration.getW();
    private static final double DIMENSION_Y_AXES = Configuration.getL() + Configuration.getL() / 10;
    private static final double DIMENSION_Y_CELL = DIMENSION_Y_AXES / (double) ROWS_TOTAL;
    private static final double DIMENSION_X_CELL = DIMENSION_X_AXES / (double) COLS_TOTAL;
    private final Vertex upperRightVertex, downLeftVertex;
    private final double upperTopVertexY, downLeftVertexY, leftSplitVertex, rightSplitVertex;
    private double movement;
    private static final ParticlePair versorNormalIzqueirda = new ParticlePair(-1.0, ZERO_VALUE), versorNormalDerecha = new ParticlePair(1.0, ZERO_VALUE), versorNormalDown = new ParticlePair(ZERO_VALUE, -1.0), versorNormalUpper = new ParticlePair(ZERO_VALUE, 1.0);


    public Simulation(Vertex topRightVertex, Vertex bottomLeftVertex, double holeSize) {
        cellIndexes = new CellIndex[ROWS_TOTAL][COLS_TOTAL];
        for (int row = 0; row < ROWS_TOTAL; row++) {
            for (int col = 0; col < COLS_TOTAL; col++) {
                cellIndexes[row][col] = new CellIndex();
            }
        }
        this.downLeftVertex = bottomLeftVertex;
        this.upperRightVertex = topRightVertex;
        this.downLeftVertexY = bottomLeftVertex.getY();
        this.upperTopVertexY = topRightVertex.getY();
        leftSplitVertex = topRightVertex.getPosition().getX() / 2 - holeSize / 2;
        rightSplitVertex = topRightVertex.getPosition().getX() / 2 + holeSize / 2;
    }

    //addAll que implementa la clase Lists
    public void addAll(List<Particle> particles) {
        particles.forEach(this::add);
    }

    public void add(Particle particle) {
        CellIndex cell = getCell(particle.getPosition().getX(), particle.getPosition().getY());
        if (cell != null) {
            cell.addParticles(particle);
        } else {
            throw new IllegalStateException("La celda no existe");
        }
    }


    public void siloMovement(double t, double w) {
        movement = A * Math.sin(w * t);
        downLeftVertex.setY(downLeftVertexY + movement);
        upperRightVertex.setY(upperTopVertexY + movement);
    }

    private boolean isNotInSplit(Particle p) {
        // si no esta en la rendija
        return ( p.getPosition().getX() > rightSplitVertex ||
                p.getPosition().getX() < leftSplitVertex);
    }

    public void updateForces() {
        for (int row = 0; row < ROWS_TOTAL; row++) {
            for (int col = 0; col < COLS_TOTAL; col++) {
                List<Particle> neighboursList = getNeighbours(row, col);
                List<Particle> currentList = cellIndexes[row][col].getParticlesList();

                for (Particle particle : currentList) {
                    particle.addToForce(ZERO_VALUE, particle.getParticleMass() * Forces.GRAVITY);

                    for (Particle other : currentList) {
                        double difference = particle.getPosition().mathModule(other.getPosition());
                        double sumRad = particle.getParticleRadius() + other.getParticleRadius();

                        if (difference < sumRad && !other.equals(particle)) {
                            ParticlePair normalVersor = other.getPosition().pairSubtract(particle.getPosition()).pairMultiply(1.0 / difference);
                            particle.addToForce(getNormalForce(sumRad - difference, normalVersor, particle, other));

                            ParticlePair relativeVelocity = particle.getVelocity().pairSubtract(other.getVelocity());
                            particle.addToForce(getTangencialForce(sumRad - difference, relativeVelocity, normalVersor, particle, other));
                        } else {
                            other.sumOfVelocitiesReset(particle);
                        }
                    }

                    for (Particle other : neighboursList) {
                        double difference = particle.getPosition().mathModule(other.getPosition());
                        double superposition = particle.getParticleRadius() + other.getParticleRadius() - difference;

                        if (superposition > ZERO_VALUE && !other.equals(particle)) {
                            ParticlePair normalVersor = other.getPosition().pairSubtract(particle.getPosition()).pairMultiply(1.0 / difference);
                            ParticlePair normalForce = getNormalForce(superposition, normalVersor, particle, other);

                            particle.addToForce(normalForce);
                            other.addToForce(normalForce.pairMultiply(-1.0));

                            ParticlePair relativeVelocity = particle.getVelocity().pairSubtract(other.getVelocity());
                            ParticlePair tangentialForce = getTangencialForce(superposition, relativeVelocity, normalVersor, particle, other);

                            particle.addToForce(tangentialForce);
                            other.addToForce(tangentialForce.pairMultiply(-1.0));
                        } else {
                            other.sumOfVelocitiesReset(particle);
                        }
                    }
                }

                if (row <= (ROWS_TOTAL - ROWS_SILO)) {
                    updateForceFloor(currentList);
                }

                if (row == ROWS_TOTAL - 1) {
                    updateForceTop(currentList);
                }

                if (col == 0) {
                    updateForceLeftWall(currentList);
                }

                if (col == COLS_TOTAL - 1) {
                    updateForceRightWall(currentList);
                }
            }
        }
    }

    private void updateForceFloor(List<Particle> particles) {
        for (Particle particle : particles) {
            if (isNotInSplit(particle) && !particle.isParticleSlit()) {
                double superposition = particle.getParticleRadius() - (particle.getPosition().getY() - downLeftVertex.getY());
                if (superposition > ZERO_VALUE) {
                    ParticlePair force = getWallForce(superposition, particle.getVelocity(), versorNormalDown, particle, null);
                    particle.addToForce(force);
                } else {
                    particle.sumOfVelocitiesInWallReset(0);
                }
            }
        }
    }

    private void updateForceTop(List<Particle> particles) {
        for (Particle particle : particles) {
            double superposition = particle.getParticleRadius() - (upperRightVertex.getPosition().getY() - particle.getPosition().getY());
            if (superposition > ZERO_VALUE) {
                ParticlePair force = getWallForce(superposition, particle.getVelocity(), versorNormalUpper, particle, null);
                particle.addToForce(force);
            } else {
                particle.sumOfVelocitiesInWallReset(1);
            }
        }
    }

    private void updateForceLeftWall(List<Particle> particles) {
        for (Particle particle : particles) {
            double superposition = particle.getParticleRadius() - (particle.getPosition().getX() - downLeftVertex.getPosition().getX());
            if (superposition > ZERO_VALUE) {
                ParticlePair force = getWallForce(superposition, particle.getVelocity(), versorNormalIzqueirda, particle, null);
                particle.addToForce(force);
            } else {
                particle.sumOfVelocitiesInWallReset(2);
            }
        }

    }

    private void updateForceRightWall(List<Particle> particles) {
        for (Particle particle : particles) {
            double superposition = particle.getParticleRadius() - (upperRightVertex.getPosition().getX() - particle.getPosition().getX());
            if (superposition > ZERO_VALUE) {
                ParticlePair force = getWallForce(superposition, particle.getVelocity(), versorNormalDerecha, particle, null);
                particle.addToForce(force);
            } else {
                particle.sumOfVelocitiesInWallReset(3);
            }
        }
    }


    private List<Particle> getNeighbours(int row, int col) {
        List<Particle> particleList = new ArrayList<>();
        if (row < ROWS_TOTAL - 1) {
            particleList.addAll(cellIndexes[row + 1][col].getParticlesList());
        }

        if (row < ROWS_TOTAL - 1 && col < COLS_TOTAL - 1) {
            particleList.addAll(cellIndexes[row + 1][col + 1].getParticlesList());
        }

        if (col < COLS_TOTAL - 1) {
            particleList.addAll(cellIndexes[row][col + 1].getParticlesList());
        }

        if (row > 0 && col < COLS_TOTAL - 1) {
            particleList.addAll(cellIndexes[row - 1][col + 1].getParticlesList());
        }

        return particleList;
    }

    private List<Particle> getAllNeighbours(int row, int col) {
        List<Particle> particles = new ArrayList<>();
        int[][] difference = {{0, 0}, {0, 1}, {0, -1}, {1, 0}, {1, 1}, {1, -1}, {-1, 0}, {-1, 1}, {-1, -1}};

        for (int[] value : difference) {
            try {
                particles.addAll(cellIndexes[row + value[0]][col + value[1]].getParticlesList());
            } catch (IndexOutOfBoundsException ignored) {}
        }

        return particles;
    }

    public int update() {
        int goneParticles = 0;
        for (int i = 0; i < ROWS_TOTAL; i++) {
            for (int j = 0; j < COLS_TOTAL; j++) {
                for (int k = 0; k < cellIndexes[i][j].getParticlesList().size(); k++) {
                    if(!updateParticleCell(cellIndexes[i][j].getParticlesList().get(k), i, j)) {
                        goneParticles += 1;
                    }
                }
            }
        }
        return goneParticles;
    }


    private CellIndex getCell(double x, double y) {
        if (x >= DIMENSION_X_AXES || x < 0 || y < 0 || y >= DIMENSION_Y_AXES) {
            throw new IllegalStateException();
        }
        int row = getIndexY(y);
        int col = getIndexX(x);
        return cellIndexes[row][col];
    }

    private int getIndexX(double value) {
        return (int) (value / DIMENSION_X_CELL);
    }

    private int getIndexY(double value) {
        return (int) (value / DIMENSION_Y_CELL);
    }

    private boolean moveFromCell(Particle particle, int row, int col, int newRow, int newCol) {
        try {
            if (newRow < 0) {
                particle.attachedFromSlit();
                cellIndexes[row][col].removeParticles(particle);

                boolean overlap;
                int auxCol, auxRow;
                do {
                    overlap = false;

                    double newX = particle.getParticleRadius() + Math.random() * (DIMENSION_X_AXES - 2.0 * particle.getParticleRadius());
                    particle.getPosition().setX(newX);

                    double newY = REINJECT_MIN_LIMIT + Configuration.getL() / 10 + Math.random() * ((Configuration.getL() - REINJECT_MIN_LIMIT) - particle.getParticleRadius());
                    particle.getPosition().setY(newY);

                    auxCol = getIndexX(particle.getPosition().getX());
                    auxRow = getIndexY(particle.getPosition().getY());

                    for (Particle existingParticle : getAllNeighbours(auxRow, auxCol)) {
                        if (GenerateParticle.overlap(particle, existingParticle)) {
                            overlap = true;
                        }
                    }
                } while (overlap);

                cellIndexes[auxRow][auxCol].addParticles(particle);
                particle.setParticleSlit(false);

                return false;

            } else {
                cellIndexes[newRow][newCol].addParticles(particle);
                cellIndexes[row][col].removeParticles(particle);
                if (particle.isParticleSlit() && particle.getColor().equals(Color.RED)) {
                    particle.setColor(Color.BLUE);
                }
                return true;
            }
        } catch (IndexOutOfBoundsException e) {
            return true;
        }
    }

    private boolean updateParticleCell(Particle particle, int row, int col) {
        double inferiorLimitX = ((double) col) * DIMENSION_X_CELL;
        double inferiorLimitY = ((double) row) * DIMENSION_Y_CELL + movement;
        ParticlePair inferiorLimit = new ParticlePair(inferiorLimitX, inferiorLimitY);

        double superiorLimitX = ((double) (col + 1)) * DIMENSION_X_CELL;
        double superiorLimitY = ((double) (row + 1)) * DIMENSION_Y_CELL + movement;
        ParticlePair superiorLimit = new ParticlePair(superiorLimitX, superiorLimitY);

        if(!particle.isParticleSlit() && !isNotInSplit(particle) && particle.getPosition().getY() < downLeftVertex.getY()) {
            particle.setParticleSlit(true);
        }

        ParticlePair inferiorDiff = particle.getPosition().pairSubtract(inferiorLimit);
        ParticlePair superiorDiff = particle.getPosition().pairSubtract(superiorLimit);

        return moveFromCell(particle, row, col,
                inferiorDiff.getY() < 0 ? row - 1 : superiorDiff.getY() >= 0 ? row + 1 : row,
                inferiorDiff.getX() < 0 ? col - 1 : superiorDiff.getX() >= 0 ? col + 1 : col
        );

    }
}
