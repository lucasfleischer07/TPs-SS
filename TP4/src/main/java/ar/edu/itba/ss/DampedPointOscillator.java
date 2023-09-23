package ar.edu.itba.ss;

import java.io.FileWriter;

public class DampedPointOscillator {

    public static double[] GearPredictorCorrectorAlgorithm(double x, double vel, double aceleration, double x3, double x4, double x5, double dt, double mass, double k, double gamma, FileWriter outputFileWriter) {
        //notacion: rp = dpr/dtq
        //la primer derivada de la posicion es la velocidad
        // la segunda derivada de la posicion es la aceletacion

        //fuerzas dependen de velocidad y posiciones
        double[] coefficients = {3/16.0, 251/360.0, 1, 11/18.0, 1/6.0, 1/60.0};

        //1)predecir: vamos hasta orden 5
        double xPredict = x + vel*dt + aceleration*Math.pow(dt, 2) / factorialNumber(2)+ x3 * Math.pow(dt, 3) / factorialNumber(3) + x4 * Math.pow(dt, 4) / factorialNumber(4) + x5 * Math.pow(dt, 5) / factorialNumber(5);
        double x1Predict = vel + aceleration * dt + x3 * Math.pow(dt, 2) / factorialNumber(2) + x4 * Math.pow(dt, 3) / factorialNumber(3) + x5 * Math.pow(dt, 4) / factorialNumber(4);
        double x2Predict = aceleration + x3 * dt + x4 * Math.pow(dt, 2) / factorialNumber(2) + x5 * Math.pow(dt, 3) / factorialNumber(3);
        double x3Predict = x3 + x4 * dt + x5 * Math.pow(dt, 2) / factorialNumber(2);
        double x4Predict = x4 + x5 * dt;
        double x5Predict = x5;

        //2) Evaluar
        //f = ma -> a = f/m -> a = (-kr - gammar1)/m  -> deltaA = a(t) - ap(t)
        double deltaA = (-k*xPredict - gamma*x1Predict)/mass - x2Predict;
        double deltaR2 = (deltaA*Math.pow(dt, 2)) / factorialNumber(2);

        //3)Obtengo variables corrregidas
        double xCorrected = xPredict + coefficients[0]*deltaR2;
        double velCorrected =  x1p + gearCoefficients[1] * deltaR2 / dt;







        return
    }


    private static int factorialNumber(int n) {
        if (n == 0)
            return 1;
        return n * factorialNumber(n-1);
    }

}
