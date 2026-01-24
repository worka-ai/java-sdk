/*
 * Copyright 2024-2025 the original author or authors.
 */

package io.modelcontextprotocol.server.transport;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;
import java.util.List;
import java.util.Map;

import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.server.McpServer;
import io.modelcontextprotocol.server.McpServerFeatures;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolRequest;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import io.modelcontextprotocol.spec.McpSchema.TextContent;
import org.junit.jupiter.api.Test;

public class InMemoryServerTransportProviderTests {

	@Test
	void roundTripToolCall() {
		var provider = new InMemoryServerTransportProvider();

		var tool = McpSchema.Tool.builder()
			.name("echo")
			.description("Echo tool")
			.build();

		McpServer.sync(provider)
			.serverInfo("in-memory", "0.0.1")
			.capabilities(McpSchema.ServerCapabilities.builder().tools(true).build())
			.tools(McpServerFeatures.SyncToolSpecification.builder()
				.tool(tool)
				.callHandler((exchange, req) -> CallToolResult.builder()
					.content(List.of(new TextContent("ok")))
					.isError(false)
					.build())
				.build())
			.build();

		McpSyncClient client = McpClient.sync(provider.createClientTransport())
			.requestTimeout(Duration.ofSeconds(5))
			.initializationTimeout(Duration.ofSeconds(2))
			.build();

		client.initialize();
		CallToolResult result = client.callTool(new CallToolRequest("echo", Map.of()));

		assertThat(result).isNotNull();
		assertThat(result.content()).hasSize(1);
		TextContent content = (TextContent) result.content().get(0);
		assertThat(content.text()).isEqualTo("ok");
		client.closeGracefully();
	}
}
