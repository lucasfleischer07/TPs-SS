package ar.edu.itba.ss.algorithms;

import ar.edu.itba.ss.models.Particle;

import java.util.*;

public class CellIndexMethod {
    private final Map<Long, List<Particle>> cellMap;
    private final int m;
    private final double l;
    private final double rc;
    private final boolean isPeriodic;
    double finalTime = 0;
    private final double cellSize;


    public CellIndexMethod(List<Particle> particles, boolean isPeriodic, double l) {
        this.cellMap = new HashMap<>();
        this.m = (int)Math.floor(l-1); //visto en clase, es el M optimo.
        this.rc = 1;
        this.l = l;
        this.isPeriodic = isPeriodic;
        this.cellSize = l/m;

        initializeCellMap(particles, l, m);

    }

    private void initializeCellMap(List<Particle> particles, double l, long m) {
           for (Particle p : particles) {
            long cellPosition = calculateCellNumber(p.getX(), p.getY());
               cellMap.putIfAbsent(cellPosition, new LinkedList<>());
               cellMap.get(cellPosition).add(p);
        }
    }

    private long calculateCellNumber(double posX, double posY) {
        long xCell = (long) (posX / cellSize) + 1;
        long yCell = (long) (posY / cellSize);
        return xCell + yCell * m;
    }

    private void addNeighbours(Map<Particle, List<Particle>> neighbors, long currentCell, long adjacentCell) {
        for(Particle particle : cellMap.get(currentCell)) {
            for(Particle adjacent : cellMap.get(adjacentCell)) {
                if(particle.getId() != adjacent.getId() && particle.distanceTo(adjacent, isPeriodic, l) < rc) {
                    neighbors.putIfAbsent(particle, new LinkedList<>());
                    neighbors.get(particle).add(adjacent);
                    if(currentCell != adjacentCell) {
                        neighbors.putIfAbsent(adjacent, new LinkedList<>());
                        neighbors.get(adjacent).add(particle);
                    }
                }
            }
        }
    }

    public Map<Particle, List<Particle>> generateNeighbors() {
        Map<Particle, List<Particle>> neighbors = new HashMap<>();
        //en este for se buscaran las particulas vecinas de cada celda siguiendo
        // la forma en L (arriba, arriba derecha, derecha, derecha abajo) ya que gracias a la simetria cumple Dij = Dji
        double startTime = System.currentTimeMillis();
        for (int x = 1; x <= m; x++) {
            for (int y = 0; y < m; y++) {
                long currentCell = x + (long) y *m;
                //por si rc es menor a la celda en si.
                if (cellMap.containsKey(currentCell)) {
                    addNeighbours(neighbors, currentCell, currentCell);
                }
                if (y > 0 || isPeriodic) {
                    long up;
                    if (y > 0) {
                        up = (long) (y - 1) * m + x;
                    } else {
                        up = (long) (m - 1) * m + x;
                    }
                    if (cellMap.containsKey(currentCell) && cellMap.containsKey(up)) {
                        addNeighbours(neighbors, currentCell, up);
                    }
                }

                if (x < m || isPeriodic) {
                    long upRight;
                    if (y > 0) {
                        upRight = ((x + 1) % m) + (long) (y - 1) * m;
                    } else {
                        upRight = ((x + 1) % m) + (long) (m - 1) * m;
                    }

                    long right = ((x + 1) % m) + (long) y * m;

                    long downRight;
                    if (y < (m - 1)) {
                        downRight = ((x + 1) % m) + (long) (y + 1) * m;
                    } else {
                        downRight = (x + 1) % m;
                    }

//                    Si la celda acutal no tiene una una particula ni la otra que quiero chequear, no verifico por vecinos
                    if (cellMap.containsKey(currentCell) && cellMap.containsKey(right)) {
                        addNeighbours(neighbors, currentCell, right);
                    }

                    if (y > 0 || isPeriodic) {
                        if (cellMap.containsKey(currentCell) && cellMap.containsKey(upRight)) {
                            addNeighbours(neighbors, currentCell, upRight);
                        }
                    }

                    if (y < m - 1 || isPeriodic) {
                        if (cellMap.containsKey(currentCell) && cellMap.containsKey(downRight)) {
                            addNeighbours(neighbors, currentCell, downRight);
                        }
                    }
                }

            }
        }
        finalTime = System.currentTimeMillis() - startTime;
        return neighbors;
    }


    public double getFinalTime() {
        return finalTime;
    }
}
