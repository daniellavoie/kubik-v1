package com.cspinformatique.kubik.elasticsearch;

import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;

public abstract class NodeClientUtil {
	public static Node buildLocalNode(){
		return NodeBuilder.nodeBuilder().local(true).node();
	}
}
