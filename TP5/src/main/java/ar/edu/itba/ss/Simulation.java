package ar.edu.itba.ss;

import ar.edu.itba.ss.models.CellIndex;
import ar.edu.itba.ss.models.Forces;
import ar.edu.itba.ss.models.Particle;
import ar.edu.itba.ss.utils.Configuration;

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
        // TODO: Falta meter lo de cambiar los limites para ovito y ver como lo hacemos con MathplotLib
    }

    //ver si el cell.add es el add de CellIndex o es recursivo
    public void add(Particle particle) {
        CellIndex cellIndex = getCellIndex(particle.getX(), particle.getY());
        if (cellIndex != null) {
            cellIndex.addParticle(particle);
        } else {
            throw new IllegalStateException("Cell does not exists");
        }
    }

    //ver si usar este y no el de List
    public void addAll(List<Particle> particles) {
        particles.forEach(this::add);
    }


    public List<Particle> update() {
        List<Particle> particleList = new ArrayList<>();
        Particle aux;

        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                for (int k = 0; k < cellIndexes[i][j].getParticles().size(); k++) {
                    aux = updateCellNumber(cellIndexes[i][j].getParticles().get(k), i, j);
                    if (aux != null) {
                        particleList.add(aux);
                    }
                }
            }
        }
        return particleList;
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
    private Particle calculateNewCell(Particle particle, int col, int row, int newCol, int newRow) {
        if (newRow < 0 || newRow >= cellIndexes.length || newCol < 0 || newCol >= cellIndexes[0].length) {
            // Las coordenadas están fuera de los límites del arreglo
            // Maneja la situación de manera apropiada, como lanzar una excepción o regresar un valor especial
            // En este ejemplo, asumiré que se lanzará una excepción
            throw new IllegalStateException("Coordenadas inválidas");
        }

        if (newRow < 0) {
            particle.reInject();
            cellIndexes[row][col].removeParticle(particle);
            return particle;
        } else {
            cellIndexes[newRow][newCol].addParticle(particle);
            cellIndexes[row][col].removeParticle(particle);
            return null;
        }
    }



    private Particle updateCellNumber(Particle particle, int col, int row) {
        double yNewRowInferior = row * DIMENSION_Y_CELL;
        double yNewRowSuperior = (row + 1) * DIMENSION_Y_CELL;
        double xNewColInferior = col * DIMENSION_X_CELL;
        double xNewColSuperior = (col + 1) * DIMENSION_X_CELL;

        //only movement on y axes
        double yCellInferior = yNewRowInferior + movement;
        double yCellSuperior = yNewRowSuperior + movement;
        double xCellInferior = xNewColInferior;
        double xCellSuperior = xNewColSuperior;

        double X = particle.getX();
        double Y = particle.getY();

        if (X >= xCellSuperior) { //movimientos a derecha +1cols
            if (Y >= yCellSuperior) { // movimiento para arriba. sumo +1 tanto a cols y rows
                return calculateNewCell(particle, row, col, row + 1, col + 1);
            } else if (Y < yCellInferior) { // movimiento para abajo. sumo +1 columna -1 fila
                return calculateNewCell(particle, row, col, row - 1, col + 1);
            } else { // +1 columna
                return calculateNewCell(particle, row, col, row, col + 1);
            }
        } else if (X < xCellInferior) { //movimientos a izquierda -1 cols
            if (Y >= yCellSuperior) { // se va para arriba +1 en rows y -1 cols.
                return calculateNewCell(particle, row, col, row + 1, col - 1);
            } else if (Y < yCellInferior) { // movimiento abajo e izquierda -1 en rows y cols
                return calculateNewCell(particle, row, col, row - 1, col - 1);
            } else { // -1 en rows
                return calculateNewCell(particle, row, col, row, col - 1);
            }
        } else { //movimientos en Y
            if (Y >= yCellSuperior) { // movimiento arriba, +1 rows
                return calculateNewCell(particle, row, col, row + 1, col);
            } else if (Y < yCellInferior) { // movimiento abajo -1 rows.
                return calculateNewCell(particle, row, col, row - 1, col);
            }
            return null;
        }
    }

    public void updateForces() {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                List<Particle> neighboursList = getNeighboursCellIndex(row, col);
                List<Particle> currentParticleList = cellIndexes[row][col].getParticles();

                // TODO: Cambiar esta sintaxis del for (para este y para las de abajo)
//                current.forEach(
//                        p -> {
//                            // Add gravity
//                            p.addToForce(0.0, p.getMass() * Forces.GRAVITY);
//
//                            current.forEach(n -> {
//                                double diff = Math.sqrt(Math.pow(p.getX() - n.getX(), 2) + Math.pow(p.getY() - n.getY(), 2));
//                                double superposition = p.getRadius() + n.getRadius() - diff;
//
//                                if (superposition > 0 && !n.equals(p)) {
//                                    double normalVersorX = (n.getX() - p.getX()) / diff;
//                                    double normalVersorY = (n.getY() - p.getY()) / diff;
//
//                                    // TODO: Aca puede que sea en vez de la particula p, n sea n, p. Nose bien como diferenciarlo
//                                    // Asumo que tiene que coincidir con el que esta mas abajo
//                                    double normalForce = getNormalForce(superposition, p, n);
//                                    p.addToForce(normalForce * normalVersorX,normalForce * normalVersorY);
//
//                                    double relativeVelocityX = Forces.getRelativeVelocityX(p, n);
//                                    double relativeVelocityY = Forces.getRelativeVelocityY(p, n);
//                                    double tangencialForce = getTangencialForce(superposition, relativeVelocityX, relativeVelocityY, normalVersorX, normalVersorX, p, n);
//                                    p.addToForce(tangencialForce * -normalVersorY, tangencialForce * normalVersorX);
//                                }
//                            });
//
//                            // Add particle forces
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

                for (Particle currentParticle : currentParticleList) {
                    // Add gravity
                    currentParticle.addToForce(0.0, currentParticle.getMass() * Forces.GRAVITY);

                    for (Particle neighbourParticle : currentParticleList) {
                        if (neighbourParticle.equals(currentParticle)) continue;

                        double diff = Math.sqrt(Math.pow(currentParticle.getX() - neighbourParticle.getX(), 2) + Math.pow(currentParticle.getY() - neighbourParticle.getY(), 2));
                        double superposition = currentParticle.getRadius() + neighbourParticle.getRadius() - diff;

                        if (superposition > 0) {
                            double normalVersorX = (neighbourParticle.getX() - currentParticle.getX()) / diff;
                            double normalVersorY = (neighbourParticle.getY() - currentParticle.getY()) / diff;

                            double normalForce = getNormalForce(superposition, currentParticle, neighbourParticle);
                            currentParticle.addToForce(normalForce * normalVersorX, normalForce * normalVersorY);

                            double relativeVelocityX = Forces.getRelativeVelocityX(currentParticle, neighbourParticle);
                            double relativeVelocityY = Forces.getRelativeVelocityY(currentParticle, neighbourParticle);
                            double tangentialForce = getTangentialForce(superposition, relativeVelocityX, relativeVelocityY, normalVersorX, normalVersorY, currentParticle, neighbourParticle);

                            currentParticle.addToForce(tangentialForce * -normalVersorY, tangentialForce * normalVersorX);
                        }
                    }

                    // Add particle forces
                    for (Particle neighbourParticle : neighboursList) {
                        if (neighbourParticle.equals(currentParticle)) continue;

                        double diff = Math.sqrt(Math.pow(currentParticle.getX() - neighbourParticle.getX(), 2) + Math.pow(currentParticle.getY() - neighbourParticle.getY(), 2));
                        double superposition = currentParticle.getRadius() + neighbourParticle.getRadius() - diff;

                        if (superposition > 0) {
                            double normalVersorX = (neighbourParticle.getX() - currentParticle.getX()) / diff;
                            double normalVersorY = (neighbourParticle.getY() - currentParticle.getY()) / diff;

                            double normalForce = getNormalForce(superposition, currentParticle, neighbourParticle);
                            currentParticle.addToForce(normalForce * normalVersorX, normalForce * normalVersorY);
                            neighbourParticle.addToForce(-1 * normalForce * normalVersorX, -1 * normalForce * normalVersorY);

                            double relativeVelocityX = Forces.getRelativeVelocityX(currentParticle, neighbourParticle);
                            double relativeVelocityY = Forces.getRelativeVelocityY(currentParticle, neighbourParticle);
                            double tangentialForce = getTangentialForce(superposition, relativeVelocityX, relativeVelocityY, normalVersorX, normalVersorY, currentParticle, neighbourParticle);

                            currentParticle.addToForce(tangentialForce * -normalVersorY, tangentialForce * normalVersorX);
                            neighbourParticle.addToForce(-tangentialForce * -normalVersorY, -tangentialForce * normalVersorX);
                        }
                    }
                }



                if (row == (ROWS - ROWS_SILO)) {
                    updateForceFloor(currentParticleList);
                }

                if (row == ROWS - 1) {
                    updateForceTop(currentParticleList);
                }

                if (col == 0) {
                    updateForceLeftWall(currentParticleList);
                }

                if (col == COLS - 1) {
                    updateForceRightWall(currentParticleList);
                }

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
        particles.forEach(p -> {
            double superposition = p.getRadius() - (p.getX() - bottomLeftLimitX);
            if (superposition > 0) {
                p.addToForce(getWallForce("x", superposition, p.getVelocityX(), p.getVelocityX(), LeftNormalVectorX, LeftNormalVectorY, p, null), getWallForce("y", superposition, p.getVelocityX(), p.getVelocityY(), LeftNormalVectorX, LeftNormalVectorY, p, null));
            }
        });
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
    private List<Particle> getNeighboursCellIndex(int col, int row) {
        List<Particle> particlesList = new ArrayList<>();

        if (row < ROWS - 1) {
            particlesList.addAll(cellIndexes[row + 1][col].getParticles());
            if (col < COLS - 1) {
                particlesList.addAll(cellIndexes[row + 1][col + 1].getParticles());
            }
        }

        if (col < COLS - 1) {
            particlesList.addAll(cellIndexes[row][col + 1].getParticles());
            if (row > 0) {
                particlesList.addAll(cellIndexes[row - 1][col + 1].getParticles());
            }
        }

        return particlesList;
    }

    public double getTopRightLimitY() {
        return topRightLimitY;
    }

    public double getBottomLeftLimitY() {
        return bottomLeftLimitY;
    }
}
