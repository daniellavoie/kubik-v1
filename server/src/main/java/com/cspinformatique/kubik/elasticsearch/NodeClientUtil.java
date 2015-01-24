package com.cspinformatique.kubik.elasticsearch;

import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;
import org.springframework.core.env.Environment;

public abstract class NodeClientUtil {
	public static Node buildNode(Environment env) {
		return NodeBuilder
				.nodeBuilder()
				.settings(
						ImmutableSettings
								.settingsBuilder()
								.put("cluster-nodes",
										env.getRequiredProperty("kubik.elasticsearch.urls"))
								.build()).node();
	}
}
