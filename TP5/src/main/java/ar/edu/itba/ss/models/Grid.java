package ar.edu.itba.ss.models;

import ar.edu.itba.ss.utils.Color;
import ar.edu.itba.ss.utils.Configuration;
import ar.edu.itba.ss.utils.ForcesUtils;
import ar.edu.itba.ss.utils.GenerateParticle;

import java.util.ArrayList;
import java.util.List;

import static ar.edu.itba.ss.utils.ForcesUtils.*;

public class Grid {
    private static final double A = Configuration.getA();
    private static final double ZERO_VALUE = 0.0;
    private static final double DIM_X = Configuration.getW();
    private static final double DIM_Y = Configuration.getL() + Configuration.getL()/10; // se tiene en cuenta el espacio fuera de la "caja"
    private static final int cols = 8;
    private static final int rowsInside = 30;
    private static final int rowsTotal = 33;
    private static final double CELL_DIMENSION_Y = DIM_Y / (double) rowsTotal;
    private static final double CELL_DIMENSION_X = DIM_X / (double) cols;
    private final Limit rightTopLimit;
    private final Limit leftBottomLimit;
    private final double rightTopLimitInitialY;
    private final double leftBottomLimitInitialY;
    private final double leftLimitHole;
    private final double rightLimitHole;
    private double movement;

    private static final Pair BottomNormalVersor = new Pair(ZERO_VALUE, -1.0);
    private static final Pair TopNormalVector = new Pair(ZERO_VALUE, 1.0);
    private static final Pair LeftNormalVector = new Pair(-1.0, ZERO_VALUE);
    private static final Pair RightNormalVector = new Pair(1.0, ZERO_VALUE);

    private final CellIndex[][] cells;

    public Grid(Limit topRightLimit, Limit bottomLeftLimit, double holeSize) {
        cells = new CellIndex[rowsTotal][cols];
        for (int row = 0; row < rowsTotal; row++) {
            for (int col = 0; col < cols; col++) {
                cells[row][col] = new CellIndex();
            }
        }
        this.leftBottomLimit = bottomLeftLimit;
        this.rightTopLimit = topRightLimit;
        this.leftBottomLimitInitialY = bottomLeftLimit.getY();
        this.rightTopLimitInitialY = topRightLimit.getY();
        leftLimitHole = topRightLimit.getPosition().getX() / 2 - holeSize / 2;
        rightLimitHole = topRightLimit.getPosition().getX() / 2 + holeSize / 2;
    }

    public void shake(double t, double w) {
        movement = A * Math.sin(w * t);
        leftBottomLimit.setY(leftBottomLimitInitialY + movement);
        rightTopLimit.setY(rightTopLimitInitialY + movement);
    }

    public void add(Particle particle) {
        CellIndex cell = getCell(particle.getPosition().getX(), particle.getPosition().getY());
        if (cell != null) {
            cell.addParticles(particle);
        } else {
            throw new IllegalStateException("Cell does not exists");
        }
    }

    public void addAll(List<Particle> particles) {
        particles.forEach(this::add);
    }


    private boolean outsideHole(Particle particle) {
        return particle.getPosition().getX() < leftLimitHole || particle.getPosition().getX() > rightLimitHole;
    }

    public void updateForces() {
        for (int row = 0; row < rowsTotal; row++) {
            for (int col = 0; col < cols; col++) {
                List<Particle> neighbours = getNeighbours(row, col);
                List<Particle> current = cells[row][col].getParticlesList();

                for (Particle particle : current) {
                    particle.addToForce(ZERO_VALUE, particle.getParticleMass() * ForcesUtils.GRAVITY);

                    for (Particle other : current) {
                        double difference = particle.getPosition().mathModule(other.getPosition());
                        double sumRad = particle.getParticleRadius() + other.getParticleRadius();

                        if (difference < sumRad && !other.equals(particle)) {
                            Pair normalVersor = other.getPosition().pairSubtract(particle.getPosition()).pairMultiply(1.0 / difference);
                            particle.addToForce(getNormalForce(sumRad - difference, normalVersor, particle, other));

                            Pair relativeVelocity = particle.getVelocity().pairSubtract(other.getVelocity());
                            particle.addToForce(getTangencialForce(sumRad - difference, relativeVelocity, normalVersor, particle, other));
                        }
                    }

                    // Add particle forces
                    for (Particle other : neighbours) {
                        double difference = particle.getPosition().mathModule(other.getPosition());
                        double superposition = particle.getParticleRadius() + other.getParticleRadius() - difference;

                        if (superposition > ZERO_VALUE && !other.equals(particle)) {
                            Pair normalVersor = other.getPosition().pairSubtract(particle.getPosition()).pairMultiply(1.0 / difference);
                            Pair normalForce = getNormalForce(superposition, normalVersor, particle, other);

                            particle.addToForce(normalForce);
                            other.addToForce(normalForce.pairMultiply(-1.0));

                            Pair relativeVelocity = particle.getVelocity().pairSubtract(other.getVelocity());
                            Pair tangentialForce = getTangencialForce(superposition, relativeVelocity, normalVersor, particle, other);

                            particle.addToForce(tangentialForce);
                            other.addToForce(tangentialForce.pairMultiply(-1.0));
                        }
                    }
                }

                if (row <= (rowsTotal - rowsInside)) {
                    updateForceFloor(current);
                }

                if (row == rowsTotal - 1) {
                    updateForceTop(current);
                }

                if (col == 0) {
                    updateForceLeftWall(current);
                }

                if (col == cols - 1) {
                    updateForceRightWall(current);
                }
            }
        }
    }

    private void updateForceFloor(List<Particle> particles) {
        for (Particle particle : particles) {
            if (outsideHole(particle) && !particle.isParticleSlit()) {
                floorForce(particle);
            }
        }
    }

    private void floorForce(Particle particle) {
        double superposition = particle.getParticleRadius() - (particle.getPosition().getY() - leftBottomLimit.getY());
        if (superposition > ZERO_VALUE) {
            Pair force = getWallForce(superposition, particle.getVelocity(), BottomNormalVersor, particle, null);
            particle.addToForce(force);
        }
    }

    private void updateForceTop(List<Particle> particles) {
        for (Particle particle : particles) {
            this.topForce(particle);
        }
    }

    private void topForce(Particle particle) {
        double superposition = particle.getParticleRadius() - (rightTopLimit.getPosition().getY() - particle.getPosition().getY());
        if (superposition > ZERO_VALUE) {
            Pair force = getWallForce(superposition, particle.getVelocity(), TopNormalVector, particle, null);
            particle.addToForce(force);
        }
    }

    private void updateForceLeftWall(List<Particle> particles) {
        for (Particle particle : particles) {
            this.leftForce(particle);
        }

    }

    private void leftForce(Particle particle) {
        double superposition = particle.getParticleRadius() - (particle.getPosition().getX() - leftBottomLimit.getPosition().getX());
        if (superposition > ZERO_VALUE) {
            Pair force = getWallForce(superposition, particle.getVelocity(), LeftNormalVector, particle, null);
            particle.addToForce(force);
        }
    }

    private void updateForceRightWall(List<Particle> particles) {
        for (Particle particle : particles) {
            this.rightForce(particle);
        }
    }

    private void rightForce(Particle particle) {
        double superposition = particle.getParticleRadius() - (rightTopLimit.getPosition().getX() - particle.getPosition().getX());
        if (superposition > ZERO_VALUE) {
            Pair force = getWallForce(superposition, particle.getVelocity(), RightNormalVector, particle, null);
            particle.addToForce(force);
        }
    }

    private List<Particle> getNeighbours(int row, int col) {
        List<Particle> particles = new ArrayList<>();

        if (row < rowsTotal - 1) {
            particles.addAll(cells[row + 1][col].getParticlesList());
        }

        if (row < rowsTotal - 1 && col < cols - 1) {
            particles.addAll(cells[row + 1][col + 1].getParticlesList());
        }

        if (col < cols - 1) {
            particles.addAll(cells[row][col + 1].getParticlesList());
        }

        if (row > 0 && col < cols - 1) {
            particles.addAll(cells[row - 1][col + 1].getParticlesList());
        }

        return particles;
    }

    private List<Particle> getAllNeighbours(int row, int col) {
        List<Particle> particles = new ArrayList<>();
        int[][] difference = {{0, 0}, {0, 1}, {0, -1}, {1, 0}, {1, 1}, {1, -1}, {-1, 0}, {-1, 1}, {-1, -1}};

        for (int[] value : difference) {
            try {
                particles.addAll(cells[row + value[0]][col + value[1]].getParticlesList());
            } catch (IndexOutOfBoundsException ignored) {}
        }

        return particles;
    }

    public int update() {
        int goneParticles = 0;
        for (int i = 0; i < rowsTotal; i++) {
            for (int j = 0; j < cols; j++) {
                for (int k = 0; k < cells[i][j].getParticlesList().size(); k++) {
                    if(!updateParticleCell(cells[i][j].getParticlesList().get(k), i, j)) {
                        goneParticles += 1;
                    }
                }
            }
        }
        return goneParticles;
    }


    private CellIndex getCell(double x, double y) {
        if (x >= DIM_X || x < 0 || y < 0 || y >= DIM_Y) {
            throw new IllegalStateException();
        }
        int row = getIndexY(y);
        int col = getIndexX(x);
        return cells[row][col];
    }

    private int getIndexX(double value) {
        return (int) (value / CELL_DIMENSION_X);
    }

    private int getIndexY(double value) {
        return (int) (value / CELL_DIMENSION_Y);
    }

    private boolean moveFromCell(Particle particle, int row, int col, int newRow, int newCol) {
        try {
            if (newRow < 0) {
                particle.attachedFromSlit();
                cells[row][col].removeParticles(particle);

                boolean overlap;
                int c, r;
                do {
                    overlap = false;
                    particle.getPosition().setX(particle.getParticleRadius() + Math.random() * (DIM_X - 2.0 * particle.getParticleRadius()));
                    particle.getPosition().setY(0.4 + 0.7 / 10 + Math.random() * ((0.7 - 0.4) - particle.getParticleRadius()));
                    c = getIndexX(particle.getPosition().getX());
                    r = getIndexY(particle.getPosition().getY());


                    for (Particle existingParticle : getAllNeighbours(r, c)) {
                        if (GenerateParticle.overlap(particle, existingParticle))
                            overlap = true;
                    }
                } while (overlap);

                cells[r][c].addParticles(particle);
                particle.setParticleSlit(false);

                return false;
            } else {
                cells[newRow][newCol].addParticles(particle);
                cells[row][col].removeParticles(particle);
                if (particle.isParticleSlit() && particle.getColor().equals(Color.RED))
                    particle.setColor(Color.BLUE);
                return true;
            }
        } catch (IndexOutOfBoundsException e) {
            return true;
        }
    }

    private boolean updateParticleCell(Particle particle, int row, int col) {

        Pair inferiorLimit = new Pair(((double) col) * CELL_DIMENSION_X, ((double) row) * CELL_DIMENSION_Y + movement);
        Pair superiorLimit = new Pair(((double) (col + 1)) * CELL_DIMENSION_X, ((double) (row + 1)) * CELL_DIMENSION_Y + movement);

        if(!particle.isParticleSlit() && !outsideHole(particle) && particle.getPosition().getY() < leftBottomLimit.getY())
            particle.setParticleSlit(true);

        Pair inferiorDiff = particle.getPosition().pairSubtract(inferiorLimit);
        Pair superiorDiff = particle.getPosition().pairSubtract(superiorLimit);

        return moveFromCell(particle, row, col,
                inferiorDiff.getY() < 0 ? row - 1 : superiorDiff.getY() >= 0 ? row + 1 : row,
                inferiorDiff.getX() < 0 ? col - 1 : superiorDiff.getX() >= 0 ? col + 1 : col
        );

    }
}
