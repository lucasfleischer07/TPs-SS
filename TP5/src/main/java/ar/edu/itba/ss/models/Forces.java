package ar.edu.itba.ss.models;

import java.util.Objects;

//TODO: ADAPTAR
public class Forces {
    public static final double K_NORMAL = 250;
    public static final double K_TAN = 2 * K_NORMAL;
    public static final double GAMMA = 2.5;
    public static final double GRAVITY = -9.81;

    public static double getRelativeVelocityX(Particle A, Particle B) {
//        TODO: Ver si esto esta bien para choque contra la pared o no
        if(B == null){
            return A.getVelocityX();
        }
        return  (A.getVelocityX() - B.getVelocityX());
    }

    public static double getRelativeVelocityY(Particle A, Particle B) {
        if(B == null){
            return A.getVelocityY();
        }
        return (A.getVelocityY() - B.getVelocityY());
    }


    public static double getNormalForce(double superposition, Particle A, Particle B) {
        double relVelX = getRelativeVelocityX(A, B);
        double relVelY = getRelativeVelocityY(A, B);

        // TODO: Multiplicar por los versores correspondientes despues en el main
        return -K_NORMAL * (superposition / 100) - GAMMA * (relVelX + relVelY);

    }

    public static double getTangencialForceT3(double superposition, double relativeTangencialVelocity) {
        return -K_TAN * (superposition / 100) * (relativeTangencialVelocity / 100);
    }

    public static double getTangencialForceT1(double superposition, double relativeTangencialVelocity, Particle A, Particle B) {
        return -GAMMA * Math.abs(getNormalForce(superposition, A, B)) * Math.signum(relativeTangencialVelocity / 100);
    }

    public static double getTangencialForce(double superposition, double relativeTangencialVelocityX, double relativeTangencialVelocityY, double normalVersorX, double normalVersorY, Particle A, Particle B) {
        double forceT1 = getTangencialForceT1(superposition, relativeTangencialVelocityX * -normalVersorY + relativeTangencialVelocityY * normalVersorX, A, B);
        double forceT3 = getTangencialForceT3(superposition,relativeTangencialVelocityX * -normalVersorY + relativeTangencialVelocityY * normalVersorX);

        // TODO: Falta multiplicar por los vectores normales correspondientes a cada eje (hacerlo en el maul despues de llamara  esto)
        if(forceT1 < forceT3) {
            return forceT1;
        } else {
            return forceT3;
        }
    }


    public static Double getWallForce(String axis, double superposition, double relativeTangencialVelocityX, double relativeTangencialVelocityY, double normalVersorX, double normalVersorY, Particle A, Particle B) {
        double forceT = getTangencialForce(superposition, relativeTangencialVelocityX, relativeTangencialVelocityY, normalVersorX, normalVersorY, A, B);
        double forceN = getNormalForce(superposition, A, B);

        if(Objects.equals(axis, "x")) {
            return forceT * -normalVersorY + forceN * normalVersorX;
        } else {
            return forceT * normalVersorX + forceN * normalVersorY;

        }

    }

//    public static Double getWallForceY(double superposition, double relativeTangencialVelocityX, double relativeTangencialVelocityY, double normalVersorX, double normalVersorY, Particle A, Particle B) {
//        double forceT = getTangencialForce(superposition, relativeTangencialVelocityX, relativeTangencialVelocityY, normalVersorX, normalVersorY, A, B);
//        double forceN = getNormalForce(superposition, A, B);
//
//        return forceT * normalVersorX + forceN * normalVersorY;
//    }
}
