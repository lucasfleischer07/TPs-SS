package ar.edu.itba.ss;

import ar.edu.itba.ss.models.Particle;
import ar.edu.itba.ss.utils.Configuration;
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
        GenerateParticle.generateStaticFile(Configuration.getStaticFile() + ".txt");
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
                            Configuration.getOutputFile() + "_frequency_" + frequency + ".txt",
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
                WriteFiles.GenerateBestFrequencyFile("src/main/resources/best_frequency.txt", bestFrequency);
                WriteFiles.GenerateListFile("src/main/resources/caudal_frequency.txt", caudals);

                int i = 1, j = 1;
                for (GranularSystem system : systems_a) {
                    WriteFiles.GenerateListFile("src/main/resources/times_frequency_" + i + ".txt", system.getTimes());
                    i++;
                }

                for (GranularSystem system : systems_a) {
                    WriteFiles.GenerateListFile("src/main/resources/energy_frequency_" + j + ".txt", system.getEnergy());
                    j++;
                }

                break;
            case "b":
                double[] holeSizes = {4.0, 5.0, 6.0};
                List<GranularSystem> systems_b = new ArrayList<>();
                ExecutorService executor_b = Executors.newFixedThreadPool(holeSizes.length);

                for (double holeSize : holeSizes) {
                    List<Particle> particles = GenerateParticle.generateParticles(Configuration.getStaticFile() + ".txt");
                    // TODO: CAMBIAR LA FRECUENCIA A LA QUE CORRESPONDA
                    GranularSystem granularSystem = new GranularSystem(Configuration.getDt(),
                            holeSize,
                            Configuration.getIterations(),
                            20.0,
                            Configuration.getOutputFile() + "_hole_size_" + holeSize + ".txt",
                            particles
                    );
                    systems_b.add(granularSystem);
                    executor_b.execute(granularSystem);
                }

                executor_b.shutdown();
                if(!executor_b.awaitTermination(10, TimeUnit.HOURS)) {
                    throw new IllegalStateException("Threads timeout");
                }

                WriteFiles.GenerateListFile("src/main/resources/caudals_hole_size_.txt", systems_b.stream().map(GranularSystem::getCaudal).collect(Collectors.toList()));

                int k = 1, l = 1;
                for (GranularSystem system : systems_b) {
                    WriteFiles.GenerateListFile("src/main/resources/times_hole_size_" + k + ".txt", system.getTimes());
                    k++;
                }

                for (GranularSystem system : systems_b) {
                    WriteFiles.GenerateListFile("src/main/resources/energy_hole_size_" + l + ".txt", system.getTimes());
                    l++;
                }

                break;
            case "c":

                break;
            default:
                throw new IllegalArgumentException("Exercise item does not exist");
        }
    }
}
