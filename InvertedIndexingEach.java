import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;
import com.hazelcast.map.IMap;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.concurrent.Callable;

/**
 * Represents a task for performing inverted indexing on a specific keyword. Implements the Callable interface to
 * support concurrent execution. Implements the HazelcastInstanceAware interface to receive the Hazelcast instance.
 * Implements the Serializable interface to support serialization.
 *
 * @author Ashish Nagar
 */
public class InvertedIndexingEach implements Callable<String>, HazelcastInstanceAware, Serializable {
    String keyword;
    private HazelcastInstance hazlecastInstance;

    /**
     * Constructs an InvertedIndexingEach object with an empty keyword.
     */
    public InvertedIndexingEach() {
    }

    /**
     * Constructs an InvertedIndexingEach object with the specified keyword.
     *
     * @param keyword the keyword to perform inverted indexing on
     */
    public InvertedIndexingEach(String keyword) {
        this.keyword = keyword;
    }

    /**
     * Sets the Hazelcast instance for this task.
     *
     * @param hazelcastInstance the Hazelcast instance
     */
    @Override
    public void setHazelcastInstance(HazelcastInstance hazelcastInstance) {
        this.hazlecastInstance = hazelcastInstance;
    }

    /**
     * Performs the inverted indexing task.
     *
     * @return a string representation of the inverted index result
     */
    public String call() {
        System.out.println("started");
        // prepare a local map
        Hashtable<String, Integer> local = new Hashtable<String, Integer>();
        IMap<String, String> map = hazlecastInstance.getMap("files");
        Iterator<String> iterator = map.localKeySet().iterator();

        while (iterator.hasNext()) {
            String name = iterator.next();
            String value = map.get(name);
            String[] words = value.split(" ");
            int counter = 0;
            for (int i = 0; i < words.length; i++) {
                if (words[i].equalsIgnoreCase(keyword)) {
                    counter++;
                }
            }
            if (counter > 0) {
                local.put(name, counter);
            }
        }

        StringBuilder result = new StringBuilder();
        for (String key : local.keySet()) {
            result.append(key).append(" ").append(local.get(key)).append(" ");
        }

        System.out.println(result);
        return result.toString();
    }
}
