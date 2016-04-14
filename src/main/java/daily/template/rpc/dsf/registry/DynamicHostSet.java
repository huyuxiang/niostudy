package daily.template.rpc.dsf.registry;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import daily.template.rpc.dsf.common.ServerNode;

public class DynamicHostSet {

	
	private final Set<ServerNode> allNodes = new HashSet<ServerNode>();

	public Set<ServerNode> getAllNodes() {
		return allNodes;
	} 
	
	public synchronized void replaceWithList(Collection<ServerNode> hosts) {
        allNodes.clear();
        allNodes.addAll(hosts);
    }
	
	
	
}
