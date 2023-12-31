package ar.edu.itba.ss;

import ar.edu.itba.ss.models.Beeman;
import ar.edu.itba.ss.models.Euler;
import ar.edu.itba.ss.models.Verlet;
import ar.edu.itba.ss.utils.Configuration;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class DampedPointOscillator {

    public static void GearPredictorCorrectorAlgorithm(FileWriter outputFileWriter, double x, double velocity, double k, double gamma, double dt, double mass, double accelerationComp, boolean mseFileGraph) throws IOException {
        File timeF = new File("./src/main/resources/ex1/time_gear.txt");
        FileWriter timeOW = new FileWriter(timeF, true);
        long start = System.nanoTime();
        double initialTime = 0, finalTime = 5, error = 0;
        int i;

        //aceleracion = fuerza/masa = (-kr - gammav) /m PAGINA 30 TEORICA
        //paso a paso voy integrando la posicion por velocidad y la vel por aceleracion.
        double acceleration = (-k*x-gamma*velocity)/mass;
        double x3 = (-k*velocity - gamma*acceleration)/mass;
        double x4 = (-k*acceleration - gamma*x3)/mass;
        double x5 = (-k*x3 - gamma*x4)/mass;

        List<Double> errorList = new ArrayList<>();

        for (i = 0; initialTime < finalTime; i++) {
            error += Math.pow(analyticSolutionComparison(accelerationComp, gamma, mass, initialTime, k) - x, 2);
            errorList.add(error);

            //utilizamos el algoritmo para hacer la correccion de las variables.
            double[] gearResultList = GearPredictorCorrector(x, velocity, acceleration, x3, x4, x5, dt, mass, k, gamma);
            x = gearResultList[0];
            velocity = gearResultList[1];
            acceleration = gearResultList[2];
            x3 = gearResultList[3];
            x4 = gearResultList[4];
            x5 = gearResultList[5];

            if (Configuration.isDebug()) {
                System.out.printf("t=%.2f -> x=%.2f ; v=%.2f\n", initialTime, x, velocity);
            }
            if (i % Configuration.getOutputIntervalTime() == 0 && !mseFileGraph) {
                outputFileWriter.write(String.format(Locale.US, "%f\n%f %f\n", initialTime, x, velocity));
                outputFileWriter.flush();
            }

            initialTime += dt;

        }

        //calculamos el MEAN error
        double duration = (System.nanoTime()-start)/Math.pow(10,9);
        System.out.printf(Locale.US, "Gear Took %g seconds\n%n", duration);
        timeOW.write(String.format(Locale.US, "%g\n", duration));
        timeOW.close();
        error += Math.pow(analyticSolutionComparison(accelerationComp, gamma, mass, initialTime, k) - x, 2);
        double finalError = error/i;
        if(mseFileGraph) {
            PrintWriter printWriter = new PrintWriter(outputFileWriter);
            printWriter.printf("%.10e\n", finalError);
            printWriter.flush();
        }
        System.out.println(finalError);
    }

    public static void BeemanAlgorithm(FileWriter outputFileWriter, double x, double v, double k, double gamma, double dt, double mass, double acceleration, boolean mseFileGraph) throws IOException {
        File timeFile = new File("./src/main/resources/ex1/time_beeman.txt");
        FileWriter timeFileWriter = new FileWriter(timeFile, true);
        long start = System.nanoTime();

        //parametros de la filmina 36 teorica
        double initialTime = 0, finalTime = 5;
        double force = -k*x - gamma*v;
        double previousX = Euler.positionFromEuler(x, v, force, -dt, mass);
        double previousV = Euler.velocityFromEuler(v, force, -dt, mass);

        double prevA = (-k*previousX - gamma*previousV)/mass;
        double difference = 0;
        int i;

        for (i = 0; initialTime < finalTime; i++) {
            difference += Math.pow(analyticSolutionComparison(acceleration, gamma, mass, initialTime, k) - x, 2);
            force= -k*x - gamma * v;
            double a = force/mass;

            x = Beeman.positionFromBeeman(x, v, a, prevA, dt);
            v = Beeman.velocityFromBeeman(x, v, a, prevA, dt, mass, gamma, k);

            if (Configuration.isDebug()) {
                System.out.printf("t=%.2f -> x=%.2f ; v=%.2f\n", initialTime, x, v);
            }

            if (i % Configuration.getOutputIntervalTime() == 0 && !mseFileGraph) {
                outputFileWriter.write(String.format(Locale.US, "%f\n%f %f\n", initialTime, x, v));
                outputFileWriter.flush();
            }

            prevA = a;
            initialTime += dt;
        }

        double duration = (System.nanoTime()-start)/Math.pow(10,9);
        System.out.printf(Locale.US, "Beeman Took %g seconds\n%n", duration);
        timeFileWriter.write(String.format(Locale.US, "%g\n", duration));
        timeFileWriter.close();
        difference += Math.pow(analyticSolutionComparison(acceleration, gamma, mass, initialTime, k) - x, 2);
        double error = difference/i;
        if(mseFileGraph) {
            PrintWriter printWriter = new PrintWriter(outputFileWriter);
            printWriter.printf("%.10e\t", error);
            printWriter.flush();
        }

        System.out.println(error);
    }


    public static double[] GearPredictorCorrector(double x, double vel, double acceleration, double x3, double x4, double x5, double dt, double mass, double k, double gamma) {
        //notacion: rp = dpr/dtq
        //la primer derivada de la posicion es la velocidad
        // la segunda derivada de la posicion es la aceletacion

        //fuerzas dependen de velocidad y posiciones
        double[] coefficients = {3 / 16.0, 251 / 360.0, 1, 11 / 18.0, 1 / 6.0, 1 / 60.0};

        //1)predecir: vamos hasta orden 5
        double xPredict = x + vel * dt + acceleration * Math.pow(dt, 2) / factorialNumber(2) + x3 * Math.pow(dt, 3) / factorialNumber(3) + x4 * Math.pow(dt, 4) / factorialNumber(4) + x5 * Math.pow(dt, 5) / factorialNumber(5);
        double x1Predict = vel + acceleration * dt + x3 * Math.pow(dt, 2) / factorialNumber(2) + x4 * Math.pow(dt, 3) / factorialNumber(3) + x5 * Math.pow(dt, 4) / factorialNumber(4);
        double x2Predict = acceleration + x3 * dt + x4 * Math.pow(dt, 2) / factorialNumber(2) + x5 * Math.pow(dt, 3) / factorialNumber(3);
        double x3Predict = x3 + x4 * dt + x5 * Math.pow(dt, 2) / factorialNumber(2);
        double x4Predict = x4 + x5 * dt;
        double x5Predict = x5;

        //2) Evaluar
        //f = ma -> a = f/m -> a = (-kr - gammar1)/m  -> deltaA = a(t) - ap(t)
        double deltaA = (-k * xPredict - gamma * x1Predict) / mass - x2Predict;
        double deltaR2 = (deltaA * Math.pow(dt, 2)) / factorialNumber(2);

        //3)Obtengo variables corrregidas
        double xCorrected = xPredict + coefficients[0] * deltaR2;
        double velCorrected = x1Predict + coefficients[1] * deltaR2 / dt;
        double accelerationCorrected = x2Predict + coefficients[2] * deltaR2 * factorialNumber(2) / Math.pow(dt, 2);
        double x3Corrected = x3Predict + coefficients[3] * deltaR2 * factorialNumber(3) / Math.pow(dt, 3);
        double x4Corrected = x4Predict + coefficients[4] * deltaR2 * factorialNumber(4) / Math.pow(dt, 4);
        double x5Corrected = x5Predict + coefficients[5] * deltaR2 * factorialNumber(5) / Math.pow(dt, 5);

        return new double[]{xCorrected, velCorrected, accelerationCorrected, x3Corrected, x4Corrected, x5Corrected};
    }

    public static void VerletAlgorithm(FileWriter outputFileWriter, double x, double v, double k, double gamma, double dt, double mass, double acceleration, boolean mseFileGraph) throws IOException {
        File timeFile = new File("./src/main/resources/ex1/time_verlet.txt");
        FileWriter timeFileWriter = new FileWriter(timeFile, true);

        long startTime = System.nanoTime();
        double t = 0, tf = 5, error = 0;

        int i;
        double initialForce = -k*x - gamma*v;
        double previousX = Euler.positionFromEuler(x, v, initialForce, -dt, mass);

        for (i = 0; t < tf; i++) {
            error += Math.pow(analyticSolutionComparison(acceleration, gamma, mass, t, k) - x, 2);
            double force = -k*x - gamma * v;
            double auxX = x;

            // Buscamos las posicion con Verlet
            x = Verlet.positionFromVerlet2(x, force, previousX, dt, mass, gamma, k);

            // Buscamos la velocidad con Verlet
            v = Verlet.velocityFromVerlet(x, previousX, dt);

            if (Configuration.isDebug()) {
                System.out.printf("t=%.2f -> x=%.2f ; v=%.2f\n", t, x, v);
            }
            if (i % Configuration.getOutputIntervalTime() == 0 && !mseFileGraph) {
                outputFileWriter.write(String.format(Locale.US, "%.8f\n%.8f %.8f\n", t, x, v));
                outputFileWriter.flush();
            }

            previousX = auxX;
            t += dt;
        }

        double endTime = (System.nanoTime()-startTime)/Math.pow(10,9);
        System.out.printf(Locale.US, "Verlet Took %g seconds\n%n", endTime);
        timeFileWriter.write(String.format(Locale.US, "%g\n", endTime));
        timeFileWriter.close();
        error += Math.pow(analyticSolutionComparison(acceleration, gamma, mass, t, k) - x, 2);
        double finalError = error/i;
        if(mseFileGraph) {
            PrintWriter printWriter = new PrintWriter(outputFileWriter);
            printWriter.printf("%.10e\t", finalError);
            printWriter.flush();
        }

        System.out.println(finalError);
    }


    private static double analyticSolutionComparison(double acceleration, double gamma, double mass, double time, double k) {
        return acceleration*Math.exp(-time*(gamma/(2*mass)))*Math.cos(Math.pow(k/mass - Math.pow(gamma, 2)/(4*Math.pow(mass,2)),0.5)*time);
    }

    private static int factorialNumber(int n) {
        if (n == 0) {
            return 1;
        }
        return n * factorialNumber(n-1);
    }

}