package ar.edu.itba.ss.models;

public class Verlet {
    public static double positionFromVerlet(double x, double previousX, double dt, double mass, double f) {
        return 2*x - previousX + (Math.pow(dt,2)/mass)*f;
    }

    public static double positionFromVerlet2(double x, double force, double previousX, double dt, double mass, double gamma, double k){
        return (2*x - x*(Math.pow(dt, 2) * k) / mass + previousX * (dt*gamma / (2*mass) - 1))/(1 + gamma*dt/(2*mass));
    }

    public static double velocityFromVerlet(double nextPosition, double prevPosition, double dt) {
        return (nextPosition-prevPosition)/(2*dt);
    }

}
