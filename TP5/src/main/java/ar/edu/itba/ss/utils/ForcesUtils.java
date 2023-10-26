package ar.edu.itba.ss.utils;

import ar.edu.itba.ss.models.Pair;
import ar.edu.itba.ss.models.Particle;

public class ForcesUtils {

    public static final double K_NORMAL = 0.25;
    public static final double GRAVITY = -0.05;
    public static final double GAMMA = 0.0025;
    public static final double MU = 0.7;
    public static final double K_TAN = 2 * K_NORMAL;


    public static double getNormalForce(double superposition, Particle A, Particle B) {
        Pair relativeVelocity;
        if(B == null) {
            relativeVelocity = A.getVelocity();
        } else {
            relativeVelocity = A.getVelocity().pairSubtract(B.getVelocity());
        }

        return -K_NORMAL * (superposition) - GAMMA * (relativeVelocity.getX() + relativeVelocity.getY());

    }


    public static Pair getNormalForce(double superposition, Pair versor, Particle A, Particle B) {
        double force = getNormalForce(superposition, A, B);

        return versor.pairMultiply(force);
    }


    public static double getTangencialForceT3(double superposition, double relativeTangencialVelocity) {
        return -K_TAN * (superposition) * (relativeTangencialVelocity);
    }

    public static double getTangencialForceT1(double superposition, double relativeTangencialVelocity, Particle A, Particle B) {
        return -MU * Math.abs(getNormalForce(superposition, A, B)) * Math.signum(relativeTangencialVelocity);
    }

    public static Pair getTangencialForce(double superposition, Pair relativeTangencialVelocity, Pair normalVersor, Particle A, Particle B) {
        Pair tan = new Pair(-normalVersor.getY(), normalVersor.getX());
        double forceT3 = getTangencialForceT3(superposition, relativeTangencialVelocity.dotProduct(tan));
        double forceT1 = getTangencialForceT1(superposition, relativeTangencialVelocity.dotProduct(tan), A, B);
        double force = Math.min(forceT1, forceT3);
        return tan.pairMultiply(force);
    }

    public static Pair getWallForce(double superposition, Pair relativeTangencialVelocity, Pair normalVersor, Particle A, Particle B) {
        Pair tan = new Pair(-normalVersor.getY(), normalVersor.getX());
        double forceT3 = getTangencialForceT3(superposition, relativeTangencialVelocity.dotProduct(tan));
        double forceT1 = getTangencialForceT1(superposition, relativeTangencialVelocity.dotProduct(tan), A, B);
        double forceT = Math.min(forceT1, forceT3);
        double forceN = getNormalForce(superposition, A, B);
        return normalVersor.pairMultiply(forceN).pairSummatory(tan.pairMultiply(forceT));
    }

}
