package ar.edu.itba.ss;

import ar.edu.itba.ss.models.Particle;
import ar.edu.itba.ss.utils.Configuration;
import ar.edu.itba.ss.utils.Forces;
import ar.edu.itba.ss.utils.GenerateParticle;
import ar.edu.itba.ss.utils.WriteFiles;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class App {
    public static void main( String[] args ) throws IOException, InterruptedException {
//        GenerateParticle.generateStaticFile(Configuration.getStaticFile() + ".txt");
        double bestFrequency;

// -------------------- Este es el item a que pide varias w ------------------------
        switch (Configuration.getExercise()) {
            case "a":
                double[] frequencies = {5, 10, 15, 20, 30, 50};
                List<GranularSystem> systems_a = new ArrayList<>();
                ExecutorService executor_a = Executors.newFixedThreadPool(frequencies.length);

                for (double frequency : frequencies) {
                    List<Particle> particles = GenerateParticle.generateParticles(Configuration.getStaticFile() + ".txt");
                    GranularSystem granularSystem = new GranularSystem(Configuration.getDt(),
                            Configuration.getD(),
                            Configuration.getIterations(),
                            frequency,
                            "src/main/resources/itemA/output" + "_frequency_" + frequency + "_D_" + Configuration.getD() + "_mhu_" + Forces.MU + ".txt",
                            particles
                    );

                    systems_a.add(granularSystem);
                    executor_a.execute(granularSystem);
                }

                executor_a.shutdown();
                if (!executor_a.awaitTermination(10, TimeUnit.HOURS)) {
                    throw new IllegalStateException("Threads timeout");
                }

                List<Double> caudals = systems_a.stream().map(GranularSystem::getCaudal).collect(Collectors.toList());
                bestFrequency = caudals.get(caudals.indexOf(caudals.stream().max(Double::compareTo).get()));
                WriteFiles.GenerateBestFrequencyFile("src/main/resources/itemA/best_frequency.txt", bestFrequency);
                WriteFiles.GenerateListFile("src/main/resources/itemA/caudal_frequency" + "_D_" + Configuration.getD() + "_mhu_" + Forces.MU + ".txt", caudals);

                int i = 1;
                for (GranularSystem system : systems_a) {
                    WriteFiles.GenerateListFile("src/main/resources/itemA/times_frequency_" + i + "_D_" + Configuration.getD() + "_mhu_" + Forces.MU + ".txt", system.getTimes());
                    i++;
                }

                break;
            case "b":
                double[] holeSizes = {3.0, 4.0, 5.0, 6.0};
                List<GranularSystem> systems_b = new ArrayList<>();
                ExecutorService executor_b = Executors.newFixedThreadPool(holeSizes.length);
                // TODO: CAMBIAR LA FRECUENCIA A LA QUE CORRESPONDA
                double frequency = 20.0;
                for (double holeSize : holeSizes) {
                    List<Particle> particles = GenerateParticle.generateParticles(Configuration.getStaticFile() + ".txt");
                    GranularSystem granularSystem = new GranularSystem(Configuration.getDt(),
                            holeSize,
                            Configuration.getIterations(),
                            frequency,
                            "src/main/resources/itemB/output" + "_hole_size_" + holeSize + "_frequency_" + frequency + "_mhu_" + Forces.MU + ".txt",
                            particles
                    );
                    systems_b.add(granularSystem);
                    executor_b.execute(granularSystem);
                }

                executor_b.shutdown();
                if(!executor_b.awaitTermination(10, TimeUnit.HOURS)) {
                    throw new IllegalStateException("Threads timeout");
                }

                WriteFiles.GenerateListFile("src/main/resources/itemB/caudals_hole_size" + "_mhu_" + Forces.MU + ".txt", systems_b.stream().map(GranularSystem::getCaudal).collect(Collectors.toList()));

                int k = 1;
                for (GranularSystem system : systems_b) {
                    WriteFiles.GenerateListFile("src/main/resources/itemB/times_hole_size_" + k + "_mhu_" + Forces.MU + ".txt", system.getTimes());
                    k++;
                }

                break;
            case "c":

                break;
            default:
                throw new IllegalArgumentException("Exercise item does not exist");
        }
    }
}
