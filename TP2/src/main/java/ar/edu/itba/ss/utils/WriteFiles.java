package ar.edu.itba.ss.utils;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

public class WriteFiles {
    public void writeFiles(String staticFileName, double particleRadius, double l, int n, double rc, double velocity) throws IOException {
        // Creo los archivos para poder escribirlos
        PrintWriter staticWriter = new PrintWriter(new FileWriter(staticFileName));

        staticWriter.printf("%f\n", rc);

        Random random = new Random();
        for(int j = 0; j < n; j++) {
            staticWriter.printf("%f\t%f\t%f\t%f\t%f\n", particleRadius, random.nextDouble() * l, random.nextDouble() * l, velocity, random.nextDouble() * 2 * Math.PI - Math.PI);
        }

        staticWriter.close();

    }
}
