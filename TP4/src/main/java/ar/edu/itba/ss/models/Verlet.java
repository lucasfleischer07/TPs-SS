package ar.edu.itba.ss.models;

public class Verlet {

    public static double positionFromVerlet(double x, double prevX, double dt, double m, double f) {
        return 2*x - prevX + (Math.pow(dt,2)/m)*f;
    }

    public static double positionFromVerlet2(double x, double f, double prevX, double dt, double m, double gamma, double k){
        double numerator = 2*x - x*(Math.pow(dt, 2) * k) / m + prevX * (dt*gamma / (2*m) - 1);
        double denominator = 1 + gamma*dt/(2*m);
        return numerator/denominator;
    }
    public static double velocityFromVerlet(double nextPosition, double prevPosition, double dt) {
        return (nextPosition-prevPosition)/(2*dt);
    }


}
