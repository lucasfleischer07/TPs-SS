package ar.edu.itba.ss;

import ar.edu.itba.ss.models.Parameters;
import ar.edu.itba.ss.utils.Configuration;
import ar.edu.itba.ss.utils.GenerateParticle;
import ar.edu.itba.ss.utils.WriteFiles;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidParameterException;
import java.util.Locale;

public class App {
    private static double DT_MAX = 0.1;
    public static void main(String[] args) throws IOException {
        //  Lectura del archivo JSON
        InputStream inputStream = Files.newInputStream(Paths.get("config.json"));
        InputStreamReader reader = new InputStreamReader(inputStream);
        JsonParser jsonParser = new JsonParser();
        JsonObject configObject = jsonParser.parse(reader).getAsJsonObject();

        if(Configuration.getExercise() == 1) {
            String outputFileNameEx1 = Configuration.getOutputFileNameEx1();
            Files.deleteIfExists(Paths.get(outputFileNameEx1));
            File outputFile = new File(outputFileNameEx1);
            FileWriter outputFileWriterEx1 = new FileWriter(outputFile, true);

            // Parameters
            double m = 70;
            double k = Math.pow(10, 4);
            double gamma = 100;
            double A = 1;

            // Initial conditions
            double x = 1;
            double v = -A*gamma / (2*m);
            double dt =  Configuration.getSimulationTime();


            if(!Configuration.isMseEx1Graph()) {
                DampedPointOscillator.VerletAlgorithm(outputFileWriterEx1, x, v, k, gamma, dt, m, A, Configuration.isMseEx1Graph());
                DampedPointOscillator.BeemanAlgorithm(outputFileWriterEx1, x, v, k, gamma, dt, m, A, Configuration.isMseEx1Graph());
                DampedPointOscillator.GearPredictorCorrectorAlgorithm(outputFileWriterEx1, x, v, k, gamma, dt, m, A, Configuration.isMseEx1Graph());
            } else {
                double[] dtTimes = {0.000001, 0.00001, 0.0001, 0.001, 0.01};
                String mscErrorFileNameEx1 = Configuration.getMscErrorFileNameEx1();

                Files.deleteIfExists(Paths.get(mscErrorFileNameEx1));
                for(double auxDt : dtTimes) {
                    File outputMSEDtTimesFile = new File(mscErrorFileNameEx1);
                    FileWriter outputFileWriterMSEDtTimesEx1 = new FileWriter(outputMSEDtTimesFile, true);
//                    Formato del archivo:
//                    dt1
//                    error1Verlet    error1Beeman   error1Gear
//                    dt2
//                    error2Verlet    error2Beeman   error2Gear
//                    dt3
//                    error3Verlet    error3Beeman   error3Gear
                    outputFileWriterMSEDtTimesEx1.write(String.format(Locale.US, "%f\n", auxDt));
                    DampedPointOscillator.VerletAlgorithm(outputFileWriterMSEDtTimesEx1, x, v, k, gamma, auxDt, m, A, Configuration.isMseEx1Graph());
                    DampedPointOscillator.BeemanAlgorithm(outputFileWriterMSEDtTimesEx1, x, v, k, gamma, auxDt, m, A, Configuration.isMseEx1Graph());
                    DampedPointOscillator.GearPredictorCorrectorAlgorithm(outputFileWriterMSEDtTimesEx1, x, v, k, gamma, auxDt, m, A, Configuration.isMseEx1Graph());
                }

            }

        } else if(Configuration.getExercise() == 2) {
            String staticFileNameEx2 = Configuration.getStaticFileNameEx2();
            String outputFileNameEx2 = Configuration.getOutputFileNameEx2();

            int n = Configuration.getN();
            int iterations = Configuration.getIterations();
            double mass = Configuration.getMass();
            double particleRadius = Configuration.getParticleRadius();
            double lineLength = Configuration.getLineLength();
            double tf = 180;

            WriteFiles writeFiles = new WriteFiles();

            double[] dtValues = {1.0E-1, 1.0E-2, 1.0E-3, 1.0E-4, 1.0E-5};
            for(double dt : dtValues) {
                System.out.println("------------------ STARTING WITH DT = " + dt + "------------------");
                // * Cambiar generateStaticFile por generateStaticFile2 si se quieren hacer 25 o mas particulas
                writeFiles.generateStaticFile(staticFileNameEx2 + "_" + n + "_" + dt + ".txt", particleRadius, n, mass, lineLength);
                Parameters parameters = GenerateParticle.generateParticles(staticFileNameEx2 + "_" + n + "_" + dt + ".txt");
                writeFiles.generateOutputFile(outputFileNameEx2 + "_" + n + "_" + dt + ".txt", parameters.getParticles(), 0.0);

                Collision collision = new Collision(parameters.getParticles(), dt);
                double currentTime = 0.0;
                for(double i = 0; i < tf; i += dt) {
                    collision.nextCollision();
                    if(currentTime > DT_MAX) {
                        System.out.println("DT value = " + dt + ", i value = " + i + ", from " + tf);
                        writeFiles.generateOutputFile(outputFileNameEx2 + "_" + n + "_" + dt + ".txt", collision.getParticles(), collision.getTotalTime());
                        currentTime = 0.0;
                    } else {
                        currentTime += dt;
                    }
//                    System.out.println(i);
                }
                System.out.println("------------------ FINISHING WITH DT = " + dt + "------------------");
            }


//            String staticFileNameEx2 = Configuration.getStaticFileNameEx2();
//            String outputFileNameEx2 = Configuration.getOutputFileNameEx2();
//
//            int n = Configuration.getN();
//            int iterations = Configuration.getIterations();
//            double mass = Configuration.getMass();
//            double particleRadius = Configuration.getParticleRadius();
//            double lineLength = Configuration.getLineLength();
//            double tf = 180;
//
//            WriteFiles writeFiles = new WriteFiles();
//
//            double[] dtValues = {1.0E-1, 1.0E-2, 1.0E-3, 1.0E-4, 1.0E-5};
//            for(double dt : dtValues) {
//                // * Cambiar generateStaticFile por generateStaticFile2 si se quieren hacer 25 o mas particulas
//                writeFiles.generateStaticFile(staticFileNameEx2 + "_" + n + "_" + dt + ".txt", particleRadius, n, mass, lineLength);
//                System.out.println("-----------------------Particle generation finished-----------------------");
//                Parameters parameters = GenerateParticle.generateParticles(staticFileNameEx2 + "_" + n + "_" + dt + ".txt");
////                writeFiles.generateOutputFile(outputFileNameEx2 + "_" + n + "_" + dt + ".txt", parameters.getParticles(), 0.0);
//                final File outFile = new File(outputFileNameEx2 + "_" + n + "_" + dt + ".txt");
//
//                System.out.println("-----------------------Collision started-----------------------");
//                Collision2.run(parameters.getParticles(), lineLength,  tf, dt, 0.1, outFile);
//
//                System.out.println("-----------------------Collision finished-----------------------");
//            }

        } else {
            throw new InvalidParameterException("Invalid exercise number");
        }

    }
}
