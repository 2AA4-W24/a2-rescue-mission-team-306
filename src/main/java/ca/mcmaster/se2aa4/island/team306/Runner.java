package ca.mcmaster.se2aa4.island.team306;

import static eu.ace_design.island.runner.Runner.run;

import java.io.File;

public class Runner {

    /**
     * Main method to run the island exploration simulation.
     *
     * @param args Command-line arguments. Expects the filename of the island to explore.
     */
    public static void main(String[] args) {
        String filename = args[0];
        try {
            // Configure and run the exploration using the Runner API
            run(Explorer.class)
                    .exploring(new File(filename))
                    .withSeed(42L)
                    .startingAt(1, 1, "EAST")
                    .backBefore(70000) // Switch back to 7000 later
                    .withCrew(5)
                    .collecting(1000, "WOOD")
                    .storingInto("./outputs")
                    .withName("Island")
                    .fire();
        } catch(Exception e) {
            // Handle any exceptions that occur during the simulation
            System.err.println(e.getMessage());
            e.printStackTrace(System.err);
            System.exit(1); // Exit with error code 1
        }
    }

}
