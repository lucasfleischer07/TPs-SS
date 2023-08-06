package ar.edu.itba.ss;

import ar.edu.itba.ss.models.Particle;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;

public class App {
    public static void main( String[] args ) {
        final String staticPath = "./resources/Static100.txt";
        try {
            File file = new File(staticPath);
            Scanner scanner = new Scanner(file);
            int n = scanner.nextInt();
            int l = scanner.nextInt();
            Random randomCoordinates = new Random();

            for(int i = 0; i < n; i++) {
                Particle particle = new Particle(i, randomCoordinates.nextDouble(), randomCoordinates.nextDouble(), scanner.nextDouble(), scanner.nextDouble());
            }


            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("The file was not found: " + e.getMessage());
        }

    }
}
