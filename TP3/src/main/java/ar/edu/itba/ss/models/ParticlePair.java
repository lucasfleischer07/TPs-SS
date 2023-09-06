package ar.edu.itba.ss.models;

public class ParticlePair {
    private Particle particle1;
    private Particle particle2;

    public ParticlePair(Particle p1, Particle p2) {
        // Ordena las partículas de manera que la misma pareja tenga siempre el mismo orden
        if (p1.hashCode() < p2.hashCode()) {
            this.particle1 = p1;
            this.particle2 = p2;
        } else {
            this.particle1 = p2;
            this.particle2 = p1;
        }
    }

    @Override
    public int hashCode() {
        // Genera un valor hash con el XOR (^)
        return particle1.hashCode() ^ particle2.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        // Compara dos pares de partículas para determinar si son iguales
        if (this == obj) return true;
        if (!(obj instanceof ParticlePair)) return false;
        ParticlePair otherPair = (ParticlePair) obj;
        return (this.particle1.equals(otherPair.particle1) && this.particle2.equals(otherPair.particle2)) ||
                (this.particle1.equals(otherPair.particle2) && this.particle2.equals(otherPair.particle1));
    }
}