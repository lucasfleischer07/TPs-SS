package ar.edu.itba.ss.utils;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

public class WriteFiles {

    public void writeFiles(String staticFileName, String dynamicFileName, double particleRadiusMin, double particleRadiusMax, double property, double l, int n, int times, boolean isStatistics) throws IOException {
//        Creo los archivos para poder escribirlos
        PrintWriter staticWriter = new PrintWriter(new FileWriter(staticFileName));
        PrintWriter dynamicWriter = new PrintWriter(new FileWriter(dynamicFileName));

        staticWriter.printf("%d\n%f\n", n, l);

        Random random = new Random();
//        Para mas adelante vamos a tener que generar movimiento de las particulas, entonces ya lo dejamos asi listo
        for(int i = 0; i < times; i++) {
            dynamicWriter.printf("%d\n", i);
            for(int j = 0; j < n; j++) {
                if(i == 0) {
                    staticWriter.printf("%f\t%f\n", particleRadiusMin + Math.random() * (particleRadiusMax - particleRadiusMin), property);
                }
                dynamicWriter.printf("%.2f\t%.2f\n", random.nextDouble() * l, random.nextDouble() * l);
            }
        }

        staticWriter.close();
        dynamicWriter.close();

    }
}
