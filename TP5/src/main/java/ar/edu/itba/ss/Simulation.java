package ar.edu.itba.ss;

import ar.edu.itba.ss.models.CellIndex;
import ar.edu.itba.ss.models.Forces;
import ar.edu.itba.ss.models.Particle;
import ar.edu.itba.ss.models.SimColors;
import ar.edu.itba.ss.utils.Configuration;
import ar.edu.itba.ss.utils.GenerateParticle;

import java.util.ArrayList;
import java.util.List;

import static ar.edu.itba.ss.models.Forces.*;

public class Simulation {
    private double movement;
    private static final double DIMENSION_Y_EXTRA_SPACE = Configuration.getL()/10;
    //Trabajando desde [0;77] para no caer en numeros negativos.
    private static final double DIMENSION_Y = Configuration.getL() + DIMENSION_Y_EXTRA_SPACE;
    private static final double DIMENSION_X = Configuration.getW();
    private final CellIndex[][] cellIndexes;

    private static final int ROWS_SILO = 30;
    private static final int ROWS = 33;
    private static final int COLS = 8;

    private static final double DIMENSION_Y_CELL = DIMENSION_Y/(double) ROWS;
    private static final double DIMENSION_X_CELL = DIMENSION_X/(double) COLS;

    private final double leftLimitHole;
    private final double rightLimitHole;
    private double topRightLimitX, bottomLeftLimitX, topRightLimitY, bottomLeftLimitY, bottomLeftLimitInitialY, topRightLimitInitialY;
    private static final double FloorNormalVersorX = 0.0;
    private static final double FloorNormalVersorY = -1.0;
    private static final double TopNormalVectorX = 0.0;
    private static final double TopNormalVectorY = 1.0;
    private static final double LeftNormalVectorX = -1.0;
    private static final double LeftNormalVectorY = 0.0;
    private static final double RightNormalVectorX = 1.0;
    private static final double RightNormalVectorY = 0.0;


    public Simulation(double topRightLimitX, double topRightLimitY, double bottomLeftLimitX, double bottomLeftLimitY, double holeSize) {
        cellIndexes = new CellIndex[ROWS][COLS];
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                cellIndexes[row][col] = new CellIndex();
            }
        }

        this.bottomLeftLimitX = bottomLeftLimitX;
        this.bottomLeftLimitY = bottomLeftLimitY;
        this.topRightLimitX = topRightLimitX;
        this.topRightLimitY = topRightLimitY;
        this.bottomLeftLimitInitialY = bottomLeftLimitY;
        this.topRightLimitInitialY = topRightLimitY;
        leftLimitHole = topRightLimitX / 2 - holeSize / 2;
        rightLimitHole = topRightLimitX / 2 + holeSize / 2;
    }


    public void movement(double t, double w) {
        movement = Configuration.getA() * Math.sin(w * t);
        bottomLeftLimitY = bottomLeftLimitInitialY + movement;
        topRightLimitY = topRightLimitInitialY + movement;
    }

    //ver si el cell.add es el add de CellIndex o es recursivo
    public void add(Particle particle) {
        CellIndex cellIndex = getCellIndex(particle.getX(), particle.getY());
        if (cellIndex != null) {
            cellIndex.addParticle(particle);
        } else {
            throw new IllegalStateException("Celda inexistente");
        }
    }

    //ver si usar este y no el de List
    public void addAll(List<Particle> particles) {
        particles.forEach(this::add);
    }


    public int update() {
        int particlesTakenAway = 0;
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                for (int k = 0; k < cellIndexes[i][j].getParticles().size(); k++) {
                    if(!updateParticleCell(cellIndexes[i][j].getParticles().get(k), i, j))
                        particlesTakenAway += 1;

                }

            }

        }
        return particlesTakenAway;
    }

    private CellIndex getCellIndex(double x, double y) {
        if (x >= DIMENSION_X || x < 0 || y < 0 || y >= DIMENSION_Y)
            throw new IllegalStateException();
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


    //aproach sin try y catch
//    private Particle calculateNewCell(Particle particle, int col, int row, int newCol, int newRow) {
//        if (newRow < 0 || newRow >= cellIndexes.length || newCol < 0 || newCol >= cellIndexes[0].length) {
//            // Las coordenadas están fuera de los límites del arreglo
//            // Maneja la situación de manera apropiada, como lanzar una excepción o regresar un valor especial
//            // En este ejemplo, asumiré que se lanzará una excepción
//            throw new IllegalStateException("Coordenadas inválidas");
//        }
//
//        if (newRow < 0) {
//            particle.reInject();
//            cellIndexes[row][col].removeParticle(particle);
//            return particle;
//        } else {
//            cellIndexes[newRow][newCol].addParticle(particle);
//            cellIndexes[row][col].removeParticle(particle);
//            return null;
//        }
//    }

    private boolean moveFromCell(Particle particle, int row, int col, int newRow, int newCol) {
        try {
            if (newRow < 0) {
                particle.reInject();
                cellIndexes[row][col].removeParticle(particle);

                boolean overlap;
                int c, r;
                do {
                    overlap = false;
                    particle.setX(particle.getRadius() + Math.random() * (DIMENSION_X - 2.0 * particle.getRadius()));
                    particle.setY(40.0 + Configuration.getL() / 10 + Math.random() * ((Configuration.getL() - 40.0) - particle.getRadius()));
                    c = getIndexX(particle.getX());
                    r = getIndexY(particle.getY());


                    for (Particle existingParticle : getAllNeighbours(r, c)) {
                        if (GenerateParticle.overlap(particle, existingParticle))
                            overlap = true;
                    }
                } while (overlap);

                cellIndexes[r][c].addParticle(particle);
                particle.setTakenAway(false);

                return false;
            } else {
                cellIndexes[newRow][newCol].addParticle(particle);
                cellIndexes[row][col].removeParticle(particle);
                if (particle.isTakenAway() && particle.getColor().equals(SimColors.RED)) {
                    particle.setColor(SimColors.BLACK);
                }
                return true;
            }
        } catch (IndexOutOfBoundsException e) {
            return true;
        }
    }


    private boolean updateParticleCell(Particle particle, int row, int col) {

//        Pair inferiorLimit = new Pair(((double) col) * DIMENSION_X_CELL, ((double) row) * DIMENSION_Y_CELL + movement);
        double inferiorLimitX = ((double) col) * DIMENSION_X_CELL;
        double inferiorLimitY = ((double) row) * DIMENSION_Y_CELL + movement;

//        Pair superiorLimit = new Pair(((double) (col + 1)) * DIMENSION_X_CELL, ((double) (row + 1)) * DIMENSION_Y_CELL + movement);
        double superiorLimitX = ((double) (col + 1)) * DIMENSION_X_CELL;
        double superiorLimitY = ((double) (row + 1)) * DIMENSION_Y_CELL + movement;


        if(!particle.isTakenAway() && !outsideHole(particle) && particle.getY() < bottomLeftLimitY)
            particle.setTakenAway(true);

//        Pair inferiorDiff = particle.getPosition().subtract(inferiorLimit);
        double inferiorDiffX = particle.getX() - (inferiorLimitX);
        double inferiorDiffY = particle.getY() - (inferiorLimitY);

//        Pair superiorDiff = particle.getPosition().subtract(superiorLimit);
        double superiorDiffX = particle.getX() - (superiorLimitX);
        double superiorDiffY = particle.getY() - (superiorLimitY);

        return moveFromCell(particle, row, col,
                inferiorDiffY < 0 ? row - 1 : superiorDiffY >= 0 ? row + 1 : row,
                inferiorDiffX < 0 ? col - 1 : superiorDiffX >= 0 ? col + 1 : col
        );

    }

    public void updateForces() {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                List<Particle> neighbours = getNeighboursCellIndex(row, col);
                List<Particle> current = cellIndexes[row][col].getParticles();

                for (Particle p : current) {
                    // Add gravity
                    p.addToForce(0.0, p.getMass() * Forces.GRAVITY);

                    for (Particle n : current) {
                        if (n.equals(p)) continue;

                        double diff = Math.sqrt(Math.pow(p.getX() - n.getX(), 2) + Math.pow(p.getY() - n.getY(), 2));
                        double sumRadius = p.getRadius() + n.getRadius();

                        if (diff < sumRadius) {
                            double normalVersorX = (n.getX() - p.getX()) / diff;
                            double normalVersorY = (n.getY() - p.getY()) / diff;

                            double normalForce = getNormalForce(sumRadius - diff, p, n);
                            p.addToForce(normalForce * normalVersorX, normalForce * normalVersorY);

                            double relativeVelocityX = Forces.getRelativeVelocityX(p, n);
                            double relativeVelocityY = Forces.getRelativeVelocityY(p, n);
                            double tangentialForce = getTangentialForce(sumRadius - diff, relativeVelocityX, relativeVelocityY, normalVersorX, normalVersorY, p, n);
                            p.addToForce(tangentialForce * -normalVersorY, tangentialForce * normalVersorX);
                        }
                    }

                    // Add particle forces
                    for (Particle n : neighbours) {
                        double diff = Math.sqrt(Math.pow(p.getX() - n.getX(), 2) + Math.pow(p.getY() - n.getY(), 2));
                        double superposition = p.getRadius() + n.getRadius() - diff;

                        if (superposition > 0 && !n.equals(p)) {
                            double normalVersorX = (n.getX() - p.getX()) / diff;
                            double normalVersorY = (n.getY() - p.getY()) / diff;

                            double normalForce = getNormalForce(superposition, p, n);
                            p.addToForce(normalForce * normalVersorX, normalForce * normalVersorY);
                            n.addToForce(-1 * normalForce * normalVersorX, -1 * normalForce * normalVersorY);

                            double relativeVelocityX = Forces.getRelativeVelocityX(p, n);
                            double relativeVelocityY = Forces.getRelativeVelocityY(p, n);
                            double tangentialForce = Forces.getTangentialForce(superposition, relativeVelocityX, relativeVelocityY, normalVersorX, normalVersorY, p, n);

                            p.addToForce(tangentialForce * -normalVersorY, tangentialForce * normalVersorX);
                            n.addToForce(-tangentialForce * -normalVersorY, -tangentialForce * normalVersorX);
                        }
                    }

                    for (Particle n : neighbours) {
                        double diff = Math.sqrt(Math.pow(p.getX() - n.getX(), 2) + Math.pow(p.getY() - n.getY(), 2));
                        double superposition = p.getRadius() + n.getRadius() - diff;

                        if (superposition > 0 && !n.equals(p)) {
                            double normalVersorX = (n.getX() - p.getX()) / diff;
                            double normalVersorY = (n.getY() - p.getY()) / diff;

                            double normalForce = getNormalForce(superposition, p, n);
                            p.addToForce(normalForce * normalVersorX, normalForce * normalVersorY);
                            n.addToForce(-1 * normalForce * normalVersorX, -1 * normalForce * normalVersorY);

                            double relativeVelocityX = Forces.getRelativeVelocityX(p, n);
                            double relativeVelocityY = Forces.getRelativeVelocityY(p, n);
                            double tangentialForce = getTangentialForce(superposition, relativeVelocityX, relativeVelocityY, normalVersorX, normalVersorY, p, n);

                            p.addToForce(tangentialForce * -normalVersorY, tangentialForce * normalVersorX);
                            n.addToForce(-tangentialForce * -normalVersorY, -tangentialForce * normalVersorX);
                        }
                    }
                }


                if (row == (ROWS - ROWS_SILO)) {
                    updateForceFloor(current);
                }

                if (row == ROWS - 1) {
                    updateForceTop(current);
                }

                if (col == 0) {
                    updateForceLeftWall(current);
                }

                if (col == COLS - 1) {
                    updateForceRightWall(current);
                }


                // TODO: Cambiar esta sintaxis del for (para este y para las de abajo)
//                current.forEach(
//                        p -> {
//                            // Add gravity
//                            p.addToForce(0.0, p.getMass() * Forces.GRAVITY);
//
//                            current.forEach(n -> {
//                                double difference = Math.sqrt(Math.pow(p.getX() - n.getX(), 2) + Math.pow(p.getY() - n.getY(), 2));
//                                double sumRadius = p.getRadius() + n.getRadius();
//
//                                if (difference < sumRadius && !n.equals(p)) {
//                                    double normalVersorX = (n.getX() - p.getX()) / difference;
//                                    double normalVersorY = (n.getY() - p.getY()) / difference;
//
//                                    // TODO: Aca puede que sea en vez de la particula p, n sea n, p. Nose bien como diferenciarlo
//                                    // Asumo que tiene que coincidir con el que esta mas abajo
//                                    double normalForce = getNormalForce(sumRadius-difference, p, n);
//                                    p.addToForce(normalForce * normalVersorX,normalForce * normalVersorY);
//
//                                    double relativeVelocityX = Forces.getRelativeVelocityX(p, n);
//                                    double relativeVelocityY = Forces.getRelativeVelocityY(p, n);
//                                    double tangencialForce = getTangencialForce(sumRadius-difference, relativeVelocityX, relativeVelocityY, normalVersorX, normalVersorX, p, n);
//                                    p.addToForce(tangencialForce * -normalVersorY, tangencialForce * normalVersorX);
//                                }
//                            });
//
//                            // Add particle forces
//
//                            for(Particle n : neighbours) {
//                                double diff = Math.sqrt(Math.pow(p.getX() - n.getX(), 2) + Math.pow(p.getY() - n.getY(), 2));
//                                double superposition = p.getRadius() + n.getRadius() - diff;
//
//                                if (superposition > 0 && !n.equals(p)) {
//                                    double normalVersorX = (n.getX() - p.getX()) / diff;
//                                    double normalVersorY = (n.getY() - p.getY()) / diff;
//
//                                    // TODO: Aca puede que sea en vez de la particula p, n sea n, p. Nose bien como diferenciarlo
//                                    double normalForce = getNormalForce(superposition, p, n);
//                                    p.addToForce(normalForce * normalVersorX, normalForce * normalVersorY);
//                                    n.addToForce(-1 * normalForce * normalVersorX, -1 * normalForce * normalVersorY);
//
//                                    double relativeVelocityX = Forces.getRelativeVelocityX(p, n);
//                                    double relativeVelocityY = Forces.getRelativeVelocityY(p, n);
//                                    double tangencialForce = getTangencialForce(superposition, relativeVelocityX, relativeVelocityY, normalVersorX, normalVersorY, p, n);
//
//                                    p.addToForce(tangencialForce * -normalVersorY, tangencialForce * normalVersorX);
//                                    n.addToForce(-tangencialForce * -normalVersorY, -tangencialForce * normalVersorX);
//                                }
//                            }
//
//                            neighbours.forEach(
//                                    n -> {
//                                        double diff = Math.sqrt(Math.pow(p.getX() - n.getX(), 2) + Math.pow(p.getY() - n.getY(), 2));
//                                        double superposition = p.getRadius() + n.getRadius() - diff;
//
//                                        if (superposition > 0 && !n.equals(p)) {
//                                            double normalVersorX = (n.getX() - p.getX()) / diff;
//                                            double normalVersorY = (n.getY() - p.getY()) / diff;
//
//                                            // TODO: Aca puede que sea en vez de la particula p, n sea n, p. Nose bien como diferenciarlo
//                                            double normalForce = getNormalForce(superposition, p, n);
//                                            p.addToForce(normalForce * normalVersorX, normalForce * normalVersorY);
//                                            n.addToForce(-1 * normalForce * normalVersorX, -1 * normalForce * normalVersorY);
//
//                                            double relativeVelocityX = Forces.getRelativeVelocityX(p, n);
//                                            double relativeVelocityY = Forces.getRelativeVelocityY(p, n);
//                                            double tangencialForce = getTangencialForce(superposition, relativeVelocityX, relativeVelocityY, normalVersorX, normalVersorY, p, n);
//
//                                            p.addToForce(tangencialForce * -normalVersorY, tangencialForce * normalVersorX);
//                                            n.addToForce(-tangencialForce * -normalVersorY, -tangencialForce * normalVersorX);
//                                        }
//                                    }
//                            );
//                        }
//                );





            }
        }






    }

    private boolean outsideHole(Particle particle) {
        return particle.getX() < leftLimitHole || particle.getX() > rightLimitHole;
    }

    private void updateForceFloor(List<Particle> particles) {
        particles.forEach(p -> {
            if (outsideHole(p)) { //si pasa por el agujero, no choca con la pared
                double superposition = p.getRadius() - (p.getY() - bottomLeftLimitY);
                if (superposition > 0) {
                    p.addToForce(getWallForce("x", superposition, p.getVelocityX(), p.getVelocityY(), FloorNormalVersorX, FloorNormalVersorY, p, null), getWallForce("y", superposition, p.getVelocityX(), p.getVelocityY(), FloorNormalVersorX, FloorNormalVersorY, p, null));
                }
            }
        });
    }

    private void updateForceTop(List<Particle> particles) {
        particles.forEach(p -> {
            double superposition = p.getRadius() - (topRightLimitY - p.getY());
            if (superposition > 0) {
                p.addToForce(getWallForce("x", superposition, p.getVelocityX(), p.getVelocityX(), TopNormalVectorX, TopNormalVectorY, p, null), getWallForce("y", superposition, p.getVelocityX(), p.getVelocityY(), TopNormalVectorX, TopNormalVectorY, p, null));
            }
        });
    }

    private void updateForceLeftWall(List<Particle> particles) {
        for (Particle p : particles) {
            double superposition = p.getRadius() - (p.getX() - bottomLeftLimitX);
            if (superposition > 0) {
                double forceX = getWallForce("x", superposition, p.getVelocityX(), p.getVelocityX(), LeftNormalVectorX, LeftNormalVectorY, p, null);
                double forceY = getWallForce("y", superposition, p.getVelocityX(), p.getVelocityY(), LeftNormalVectorX, LeftNormalVectorY, p, null);
                p.addToForce(forceX, forceY);
            }
        }

    }

    private void updateForceRightWall(List<Particle> particles) {
        particles.forEach(p -> {
            double superposition = p.getRadius() - (topRightLimitX - p.getX());
            if (superposition > 0) {
                p.addToForce(getWallForce("x", superposition, p.getVelocityX(), p.getVelocityX(), RightNormalVectorX, RightNormalVectorY, p, null), getWallForce("y", superposition, p.getVelocityX(), p.getVelocityY(), RightNormalVectorX, RightNormalVectorY, p, null));
            }
        });
    }

    //if modificado para los vecinos
//    private List<Particle> getNeighboursCellIndex(int col, int row) {
//        List<Particle> particlesList = new ArrayList<>();
//
//        if (row < ROWS - 1) {
//            particlesList.addAll(cellIndexes[row + 1][col].getParticles());
//            if (col < COLS - 1) {
//                particlesList.addAll(cellIndexes[row + 1][col + 1].getParticles());
//            }
//        }
//
//        if (col < COLS - 1) {
//            particlesList.addAll(cellIndexes[row][col + 1].getParticles());
//            if (row > 0) {
//                particlesList.addAll(cellIndexes[row - 1][col + 1].getParticles());
//            }
//        }
//
//        return particlesList;
//    }

    private List<Particle> getNeighboursCellIndex(int row, int col) {
        List<Particle> particles = new ArrayList<>();

        if (row < ROWS - 1)
            particles.addAll(cellIndexes[row + 1][col].getParticles());

        if (row < ROWS - 1 && col < COLS - 1)
            particles.addAll(cellIndexes[row + 1][col + 1].getParticles());

        if (col < COLS - 1)
            particles.addAll(cellIndexes[row][col + 1].getParticles());

        if (row > 0 && col < COLS - 1)
            particles.addAll(cellIndexes[row - 1][col + 1].getParticles());


        return particles;
    }

    private List<Particle> getAllNeighbours(int row, int col) {
        List<Particle> particles = new ArrayList<>();

        int numRows = cellIndexes.length;
        int numCols = cellIndexes[0].length;

        //Esto se hace para representar los movimientos en las direcciones hacia arriba, abajo, izquierda, derecha y las diagonales.

        int[][] difference = {{0, 0}, {0, 1}, {0, -1}, {1, 0}, {1, 1}, {1, -1}, {-1, 0}, {-1, 1}, {-1, -1}};

        for (int[] d : difference) {
            int newRow = row + d[0];
            int newCol = col + d[1];

            if (newRow >= 0 && newRow < numRows && newCol >= 0 && newCol < numCols) {
                particles.addAll(cellIndexes[newRow][newCol].getParticles());
            }
        }

        return particles;
    }


    public double getTopRightLimitY() {
        return topRightLimitY;
    }

    public double getBottomLeftLimitY() {
        return bottomLeftLimitY;
    }
}
