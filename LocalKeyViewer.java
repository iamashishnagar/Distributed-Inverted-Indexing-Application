import com.hazelcast.cluster.Member;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IExecutorService;
import com.hazelcast.core.MultiExecutionCallback;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

public class LocalKeyViewer {
    public static void main( String[] args ) throws Exception {
	HazelcastInstance hz = Hazelcast.newHazelcastInstance( );

	IExecutorService exec = hz.getExecutorService( "exec" );

	Callable<String> keyCallable = new LocalKeySearchCallable( );

	MultiExecutionCallback callback =
	    new MultiExecutionCallback( ) {

		@Override
		public void onResponse( Member member, Object msg ) {
		    System.err.println( "Received: " + msg );
		}

		@Override
		public void onComplete( Map<Member, Object> msgs ) {
		    for ( Object msg : msgs.values( ) ) {
			System.err.println( "Complete: " + msg );
		    }
		}
	    };

	// retrieve all cluster nodes.
	Set<Member> clusterMembers = hz.getCluster( ).getMembers( );

	// call LocalKeySearch.class at each cluster node
	exec.submitToMembers( keyCallable, clusterMembers, callback );
    }
}
