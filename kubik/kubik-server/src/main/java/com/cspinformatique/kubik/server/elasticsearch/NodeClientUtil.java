package com.cspinformatique.kubik.server.elasticsearch;

import org.elasticsearch.client.Client;
import org.elasticsearch.node.NodeBuilder;

public abstract class NodeClientUtil {
	public static Client buildLocalNode(){
		return NodeBuilder.nodeBuilder().local(true).node().client();
	}
}
