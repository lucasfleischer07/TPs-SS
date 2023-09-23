package ar.edu.itba.ss.models;

public class Euler {
    public static double positionFromEuler(double x, double vel, double f, double dt, double m) {
        return x + dt*vel + (Math.pow(dt, 2) / (2*m)) * f;
    }

    public static double velocityFromEuler(double v, double f, double dt, double m) {
        return v + (dt / m) * f;
    }

}
