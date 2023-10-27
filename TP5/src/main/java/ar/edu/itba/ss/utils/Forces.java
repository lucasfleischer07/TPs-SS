package ar.edu.itba.ss.utils;

import ar.edu.itba.ss.models.ParticlePair;
import ar.edu.itba.ss.models.Particle;

public class Forces {
    public static final double K_NORMAL = 250;
    public static final double GRAVITY = -5;
    public static final double GAMMA = 2.5;
    public static final double MU = 0.5;
    public static final double K_TAN = 2 * K_NORMAL;
    private static final double ZERO_VALUE = 0.0;

    private static final ParticlePair versorNormalIzqueirda = new ParticlePair(-1.0, ZERO_VALUE), versorNormalDerecha = new ParticlePair(1.0, ZERO_VALUE), versorNormalDown = new ParticlePair(ZERO_VALUE, -1.0), versorNormalUpper = new ParticlePair(ZERO_VALUE, 1.0);


    public static double getNormalForce(double superposition, Particle A, Particle B) {
        ParticlePair relativeVelocity;
        if(B == null) {
            relativeVelocity = A.getVelocity();
        } else {
            relativeVelocity = A.getVelocity().pairSubtract(B.getVelocity());
        }

        return -K_NORMAL * (superposition) - GAMMA * (relativeVelocity.getX() + relativeVelocity.getY());

    }


    public static ParticlePair getNormalForce(double superposition, ParticlePair versor, Particle A, Particle B) {
        double force = getNormalForce(superposition, A, B);

        return versor.pairMultiply(force);
    }


    public static double getTangencialForceT3(double superposition, double relativeTangencialVelocity) {
        return -K_TAN * (relativeTangencialVelocity) * Configuration.getDt();
    }

    public static double getTangencialForceT1(double superposition, double relativeTangencialVelocity, Particle A, Particle B) {
        return -MU * Math.abs(getNormalForce(superposition, A, B)) * Math.signum(relativeTangencialVelocity);
    }

    public static ParticlePair getTangencialForce(double superposition, ParticlePair relativeTangencialVelocity, ParticlePair normalVersor, Particle A, Particle B) {
        ParticlePair tan = new ParticlePair(-normalVersor.getY(), normalVersor.getX());
        A.addSumOfVelocities(B, relativeTangencialVelocity.dotProduct(tan));
        double forceT3 = getTangencialForceT3(superposition, A.sumOfVelocities(B));
        double forceT1 = getTangencialForceT1(superposition, relativeTangencialVelocity.dotProduct(tan), A, B);
        double force = Math.min(forceT1, forceT3);
        return tan.pairMultiply(force);
    }

    public static int determineWall(ParticlePair normalVersor) {
        if (normalVersor.equals(versorNormalDown)) {
            return 0;
        } else if (normalVersor.equals(versorNormalUpper)) {
            return 1;
        } else if (normalVersor.equals(versorNormalIzqueirda)) {
            return 2;
        } else {
            return 3;
        }
    }


    public static ParticlePair getWallForce(double superposition, ParticlePair relativeTangencialVelocity, ParticlePair normalVersor, Particle A, Particle B) {

        int wall = determineWall(normalVersor);

        ParticlePair tan = new ParticlePair(-normalVersor.getY(), normalVersor.getX());
        A.addAcumVelWall(wall, relativeTangencialVelocity.dotProduct(tan));

        double forceT3 = getTangencialForceT3(superposition, A.sumOfVelocitiesInWall(wall));
        double forceT1 = getTangencialForceT1(superposition, relativeTangencialVelocity.dotProduct(tan), A, B);
        double forceT = Math.min(forceT1, forceT3);
        double forceN = getNormalForce(superposition, A, B);
        return normalVersor.pairMultiply(forceN).pairSummatory(tan.pairMultiply(forceT));
    }

}
