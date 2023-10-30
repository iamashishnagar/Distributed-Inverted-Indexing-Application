import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;
import com.hazelcast.map.IMap;

import java.io.Serializable;
import java.util.Iterator;
import java.util.concurrent.Callable;

public class LocalKeySearchCallable
    implements Callable<String>, HazelcastInstanceAware, Serializable {

    private HazelcastInstance hz;

    @Override
    public void setHazelcastInstance( HazelcastInstance hz ) {
	this.hz = hz;
    }

    @Override
    public String call( ) throws Exception {
	IMap<String, String> map = hz.getMap( "files" );
	//Iterator<String> iter = map.keySet( ).iterator( );
	Iterator<String> iter = map.localKeySet( ).iterator( );

	String fileNames = "";
	int fileCount = 0;
	while ( iter.hasNext( ) ) {
	    String name = iter.next( );
	    System.out.println( name );
	    fileNames += name + "\n";
	    fileCount++;
	}
	return hz.getCluster( ).getLocalMember( ).toString( ) + " - " + "local file count = " + fileCount + "\n" + fileNames;
    }
}

