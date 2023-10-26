package ar.edu.itba.ss.models;

import java.util.Objects;

//TODO: ADAPTAR
public class Forces {
    public static final double K_NORMAL = 250;
    public static final double K_TAN = 2 * K_NORMAL;
    public static final double GAMMA = 2.5;
    public static final double MU = 0.7;
    public static final double GRAVITY = -100.0;

    public static double getRelativeVelocityX(Particle A, Particle B) {
        if(B == null) {
            return -A.getVelocityX();
        }
        return (A.getVelocityX() - B.getVelocityX());
    }

    public static double getRelativeVelocityY(Particle A, Particle B) {
        if(B == null){
            return -A.getVelocityY();
        }
        return (A.getVelocityY() - B.getVelocityY());
    }


    public static double getNormalForce(double superposition, Particle A, Particle B) {
        double relVelX = getRelativeVelocityX(A, B);
        double relVelY = getRelativeVelocityY(A, B);

        return -K_NORMAL * (superposition) - GAMMA * (relVelX + relVelY);

    }

    public static double getTangentialForceT3(double superposition, double relativeTangencialVelocity) {
        return -K_TAN * (superposition) * (relativeTangencialVelocity);
    }

    public static double getTangentialForceT1(double superposition, double relativeTangencialVelocity, Particle A, Particle B) {
        return -MU * Math.abs(getNormalForce(superposition, A, B)) * Math.signum(relativeTangencialVelocity);
    }

    public static double getTangentialForce(double superposition, double relativeTangencialVelocityX, double relativeTangencialVelocityY, double normalVersorX, double normalVersorY, Particle A, Particle B) {
        double forceT1 = getTangentialForceT1(superposition, relativeTangencialVelocityX * -normalVersorY + relativeTangencialVelocityY * normalVersorX, A, B);
        double forceT3 = getTangentialForceT3(superposition,relativeTangencialVelocityX * -normalVersorY + relativeTangencialVelocityY * normalVersorX);

        return Math.min(forceT1, forceT3);
    }


    public static Double getWallForce(String axis, double superposition, double relativeTangencialVelocityX, double relativeTangencialVelocityY, double normalVersorX, double normalVersorY, Particle A, Particle B) {
        double forceT = getTangentialForce(superposition, relativeTangencialVelocityX, relativeTangencialVelocityY, normalVersorX, normalVersorY, A, B);
        double forceN = getNormalForce(superposition, A, B);

        if(Objects.equals(axis, "x")) {
            return forceT * -normalVersorY + forceN * normalVersorX;
        } else {
            return forceT * normalVersorX + forceN * normalVersorY;

        }

    }

}
