package ar.edu.itba.ss.algorithms;

import ar.edu.itba.ss.models.Particle;

import java.util.*;

public class CellIndexMethod {
    private final Map<Long, List<Particle>> cellMap = new HashMap<>();
    private final List<Particle> particles;
    private final long m;
    private final double l;
    private final double rc;
    private final boolean isPeriodic;
    private final double cellSize;
    double maxParticleRadius;

    public CellIndexMethod(List<Particle> particles, double l, int m, double rc, boolean isPeriodic, double maxParticleRadius) {
        if ((l / m) <= rc + 2*maxParticleRadius) {
            throw new RuntimeException("Invalid parameters");
        }

        this.particles = particles;
        this.m = m;
        this.l = l;
        this.rc = rc;
        this.isPeriodic = isPeriodic;
        this.cellSize = l/m;
        this.maxParticleRadius = maxParticleRadius;

        initializeCellMap(particles, l, m);

    }

    private void initializeCellMap(List<Particle> particles, double l, long m) {
        double cellSize = l / m;
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
        for (int x = 1; x <= m; x++) {
            for (int y = 0; y < m; y++) {
                long currentCell = x + y*m;
                //por si rc es menor a la celda en si.
                if (cellMap.containsKey(currentCell)) {
                    addNeighbours(neighbors, currentCell, currentCell);
                }
                if (y > 0 || isPeriodic) {
                    long up;
                    if (y > 0) {
                        up = (y - 1) * m + x;
                    } else {
                        up = (m - 1) * m + x;
                    }
                    if (cellMap.containsKey(currentCell) && cellMap.containsKey(up)) {
                        addNeighbours(neighbors, currentCell, up);
                    }
                }

                if (x < m || isPeriodic) {
                    long upRight;
                    if (y > 0) {
                        upRight = ((x + 1) % m) + (y - 1) * m;
                    } else {
                        upRight = ((x + 1) % m) + (m - 1) * m;
                    }

                    long right = ((x + 1) % m) + y * m;

                    long downRight;
                    if (y < (m - 1)) {
                        downRight = ((x + 1) % m) + (y + 1) * m;
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
        return neighbors;
    }

    public void printCellMap(){
        for(Map.Entry<Long, List<Particle>> e : cellMap.entrySet()){
            long cellNum = e.getKey();
            List<Particle> particles = e.getValue();
            System.out.println("Cell " + cellNum + ":");
            for (Particle particle : particles) {
                System.out.println("  Particle ID: " + particle.getId() + ", x: " + particle.getX() + ", y: " + particle.getY());
            }
        }
    }

    // algoritmo de fuerza bruta, a diferencia del anterior se buscan en todos los vecinos aunque sea ineficiente

    public Map<Particle, List<Particle>> generateNeighborsBruteForce() {
        Map<Particle, List<Particle>> neighbours = new HashMap<>();

        for(Particle p: particles) {
            List<Particle> particleNeighbours = new ArrayList<>();
            for(Particle p2: particles) {
                if(p.getId() != p2.getId() && p.distanceTo(p2, isPeriodic, l) < rc) {
                    particleNeighbours.add(p2);
                }
            }
            //si los vecinos estan vacios, agrega a p su lista de vecinos
            if (!particleNeighbours.isEmpty()) {
                neighbours.put(p, particleNeighbours);
            }
        }
    return neighbours;
    }

}
