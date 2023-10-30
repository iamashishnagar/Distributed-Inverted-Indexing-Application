import Mobile.Agent;

import java.util.Arrays;
import java.util.Date;
import java.util.Hashtable;
import java.util.stream.Collectors;

/**
 * TestAgent is a test mobile agent that is injected into the 1st Mobile.Place platform to print a breath message,
 * migrates to the 2nd platform to say "Hello!", and even moves to the 3rd platform to say "Oi!".
 * <p>
 * The agent performs inverted indexing on the files present in each platform and collects the count of occurrences of a
 * specific keyword.
 * <p>
 * The agent uses a hashtable to store the local inverted index counts and provides functionality to print the inverted
 * index results and elapsed time.
 * <p>
 * The agent extends the Mobile.Agent class.
 *
 * @author Ashish Nagar
 * @version 1.0
 */
public class TestAgent extends Agent {

    /**
     * The number of hops the agent has made.
     */
    public int hopCount = -1;

    /**
     * The array of destination platforms for the agent to migrate.
     */
    public String[] destination = null;

    /**
     * A hashtable to store the local inverted index counts.
     */
    Hashtable<String, Integer> localTable = new Hashtable<>();

    /**
     * The start time of the agent.
     */
    Date startTime;

    /**
     * Constructs a new TestAgent with the provided arguments and initial source.
     *
     * @param args          the arguments to be passed to the agent
     * @param initialSource the initial source platform for the agent
     */
    public TestAgent(String[] args, String initialSource) {
        startTime = new Date();
        destination = Arrays.copyOf(args, args.length + 1);
        destination[destination.length - 1] = initialSource;
        System.out.println("Destinations: " + Arrays.toString(destination));
    }

    /**
     * The default method called upon agent injection. If the hopCount is less than 0, the agent hops to the next
     * destination. If the hopCount is within the range of destinations, the agent performs inverted indexing and hops
     * to the next destination if necessary. Otherwise, it prints the inverted index results and elapsed time.
     */
    public void init() {
        if (hopCount < 0) {
            hopCount++;
            hop(destination[hopCount], "init");
        }
        else if (hopCount < destination.length) {
            call(destination[hopCount]);
            if (!destination[hopCount].equals(getSpawnedHostName())) {
                hop(destination[++hopCount], "init");
            }
            else {
                System.out.println("Inverted indexing completed.");

                localTable.forEach((name, count) -> System.out.println("File[" + name + "] has " + count));

                System.out.println("\nElapsed time = " + (new Date().getTime() - startTime.getTime()));
            }
        }
    }

    /**
     * Performs inverted indexing on the files present in the specified hostname.
     *
     * @param hostname the hostname of the platform to perform inverted indexing
     *
     * @return a string representation of the inverted index results
     */
    public String call(String hostname) {
        System.out.println("Local inverted indexing started for " + hostname);

        Hashtable<String, String> map = getMap(hostname);

        map.forEach((name, value) -> {
            String[] words = value.split(" ");
            int counter = (int) Arrays.stream(words)
                    .filter(getKeyword()::equals)
                    .count();

            if (counter > 0) {
                localTable.put(name, counter);
            }
        });

        return localTable.entrySet().stream()
                .map(entry -> entry.getKey() + " " + entry.getValue())
                .collect(Collectors.joining(" "));
    }
}