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
        GenerateParticle.generateStaticFile(Configuration.getStaticFile());
        double bestFrequency;

// -------------------- Este es el item a que pide varias w ------------------------
        switch (Configuration.getExercise()) {
            case "a":
                double[] frequencies = {5, 10, 15, 20, 30, 50};
                List<GranularSystem> systems = new ArrayList<>();
                ExecutorService executor = Executors.newFixedThreadPool(frequencies.length);

                for (double frequency : frequencies) {
                    List<Particle> particles = GenerateParticle.generateParticles(Configuration.getStaticFile() + ".txt");
                    GranularSystem granularSystem = new GranularSystem(Configuration.getDt(),
                            Configuration.getIterations(),
                            frequency,
                            Configuration.getOutputFile() + ".txt",
                            particles
                    );

                    systems.add(granularSystem);
                    executor.execute(granularSystem);
                }

                executor.shutdown();
                if (!executor.awaitTermination(10, TimeUnit.HOURS)) {
                    throw new IllegalStateException("Threads timeout");
                }

                List<Double> caudals = systems.stream().map(GranularSystem::getCaudal).collect(Collectors.toList());
                bestFrequency = caudals.get(caudals.indexOf(caudals.stream().max(Double::compareTo).get()));
                WriteFiles.GenerateBestFrequencyFile("src/main/resources/best_frequency.txt", bestFrequency);

                WriteFiles.GenerateListFile("src/main/resources/caudal_frequency.txt", caudals);
                for (GranularSystem system : systems) {
                    WriteFiles.GenerateListFile("src/main/resources/times_frequency.txt", system.getTimes());
                }

                for (GranularSystem system : systems) {
                    WriteFiles.GenerateListFile("src/main/resources/energy_frequency.txt", system.getEnergy());
                }

                break;
            case "b":

                break;
            case "c":

                break;
            default:
                throw new IllegalArgumentException("Exercise item does not exist");
        }
    }
}
