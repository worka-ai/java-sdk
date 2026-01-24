/*
 * Copyright 2024-2025 the original author or authors.
 */

package io.modelcontextprotocol.server.transport;

import java.util.concurrent.atomic.AtomicBoolean;

import io.modelcontextprotocol.client.transport.InMemoryClientTransport;
import io.modelcontextprotocol.json.McpJsonMapper;
import io.modelcontextprotocol.json.TypeRef;
import io.modelcontextprotocol.spec.McpError;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.JSONRPCMessage;
import io.modelcontextprotocol.spec.McpServerSession;
import io.modelcontextprotocol.spec.McpServerTransport;
import io.modelcontextprotocol.util.Assert;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

/**
 * In-memory server transport for in-process MCP communication.
 *
 * @author Worka
 */
public final class InMemoryServerTransport implements McpServerTransport {

	private final Sinks.Many<JSONRPCMessage> inboundSink;

	private final AtomicBoolean connected = new AtomicBoolean(false);

	private final McpJsonMapper jsonMapper;

	private InMemoryClientTransport clientTransport;

	private McpServerSession session;

	public InMemoryServerTransport(McpJsonMapper jsonMapper) {
		Assert.notNull(jsonMapper, "The JsonMapper can not be null");
		this.jsonMapper = jsonMapper;
		this.inboundSink = Sinks.many().unicast().onBackpressureBuffer();
	}

	void linkClient(InMemoryClientTransport clientTransport) {
		this.clientTransport = clientTransport;
	}

	void deliver(JSONRPCMessage message) {
		if (inboundSink.tryEmitNext(message).isFailure()) {
			throw new RuntimeException("Failed to enqueue message");
		}
	}

	void attachSession(McpServerSession session) {
		this.session = session;
		if (connected.compareAndSet(false, true)) {
			inboundSink.asFlux()
				.flatMap(msg -> session.handle(msg))
				.doFinally(signal -> connected.set(false))
				.subscribe();
		}
	}

	@Override
	public Mono<Void> sendMessage(JSONRPCMessage message) {
		if (clientTransport == null) {
			return Mono.error(new McpError("No client connected"));
		}
		return Mono.fromRunnable(() -> clientTransport.deliver(message));
	}

	@Override
	public Mono<Void> closeGracefully() {
		return Mono.fromRunnable(() -> {
			connected.set(false);
			inboundSink.tryEmitComplete();
		});
	}

	@Override
	public <T> T unmarshalFrom(Object data, TypeRef<T> typeRef) {
		return jsonMapper.convertValue(data, typeRef);
	}
}
