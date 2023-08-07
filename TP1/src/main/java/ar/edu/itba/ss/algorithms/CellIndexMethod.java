package ar.edu.itba.ss.algorithms;

import ar.edu.itba.ss.models.Particle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CellIndexMethod {
    private final Map<Long, List<Particle>> cellMap = new HashMap<>();
    private final List<Particle> particles;
    private final long m;
    private final double l;
    private final double rc;
//    private final boolean isPeriodic;

    public CellIndexMethod(List<Particle> particles, double l, long m, double rc, boolean isPeriodic) {
        if (l / m <= rc) {
            throw new RuntimeException("Invalid parameters");
        }

        this.particles = particles;
        this.m = m;
        this.l = l;
        this.rc = rc;
//        this.isPeriodic = isPeriodic;

        initializeCellMap(particles, l, m);

    }

    private void initializeCellMap(List<Particle> particles, double l, long m) {
        double cellSize = l / m;
        for (Particle p : particles) {
            long cellPosition = calculateCellNumber(p, cellSize, m);
            cellMap.putIfAbsent(cellPosition, new ArrayList<>());
            cellMap.get(cellPosition).add(p);
        }
    }

    private long calculateCellNumber(Particle p, double cellSize, long m) {
        long xCell = (long) (p.getX() / cellSize) + 1;
        long yCell = (long) (p.getY() / cellSize);
        return xCell + yCell * m;
    }
}
