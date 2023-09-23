package ar.edu.itba.ss.models;

public class Beeman {

    public static double velocityFromBeeman(double nextX, double v, double acceleration, double previousAcceleration, double dt, double m, double gamma, double k) {
        return (v - (dt * k * nextX)/(3*m) + (5.0/6)*acceleration*dt - (1.0/6)*previousAcceleration*dt)/(1 + (dt * gamma) / (3*m));
    }

    public static double positionFromBeeman(double x, double v, double acceleration, double previousAcceleration, double dt) {
        return x + v*dt + (2.0/3)*acceleration*Math.pow(dt, 2) - (1.0/6)*previousAcceleration*Math.pow(dt, 2);
    }
}
