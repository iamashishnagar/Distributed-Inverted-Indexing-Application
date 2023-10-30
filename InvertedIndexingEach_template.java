import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.hazelcast.config.IndexConfig;
import com.hazelcast.config.IndexType;
import com.hazelcast.query.Predicates;
import com.hazelcast.query.Predicate;

import java.util.Collection;
import java.util.Set;
import java.util.Iterator;
import java.util.Hashtable;
import java.util.Enumeration;

import com.hazelcast.core.HazelcastInstanceAware;

import java.util.concurrent.*;
import java.util.Date;
import java.io.Serializable;

public class InvertedIndexingEach_template implements Callable<String>, HazelcastInstanceAware, Serializable {

    String keyword;

    // default constructor
    public InvertedIndexingEach_template() {
    }

    // constructor used to receive a keyword for inverted indexing
    public InvertedIndexingEach_template(String keyword) {
        this.keyword = keyword;
    }

    private HazelcastInstance hz;

    @Override
    public void setHazelcastInstance(HazelcastInstance hz) {
        this.hz = hz;
    }

    @Override
    public String call() throws Exception {
        System.out.println("started");
        // define a collection of <filenames, count>
        Hashtable<String, Integer> local = new Hashtable<String, Integer>();

        // search for a collection of files
        IMap<String, String> map = hz.getMap("files");

        // examin each file in local
        Iterator<String> iter = map.localKeySet().iterator();
        while (iter.hasNext()) {
            String value = map.get(iter.next());
            String[] words = value.split(" ");
            // the same as InvertedIndexingLocal's logic:
            // prepare a word counter.
            // for each words[i], did you find keyword? if so increment the word counter.

            // if the counter is positive
            // put this file name with the counter value in local hashtable.
        }

        // convert local<String, Integer> indexs into a string like "filename count filename count ...."
        String retVal = "";
        Enumeration<String> keys = local.keys();
        while (keys.hasMoreElements()) {
            // get filename
            // get count
            // retVal += filename + " " + count + " ";
        }

        return retVal;
    }
}
