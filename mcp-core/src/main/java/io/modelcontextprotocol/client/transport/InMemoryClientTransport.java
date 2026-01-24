/*
 * Copyright 2024-2025 the original author or authors.
 */

package io.modelcontextprotocol.client.transport;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.Function;

import io.modelcontextprotocol.json.McpJsonMapper;
import io.modelcontextprotocol.json.TypeRef;
import io.modelcontextprotocol.server.transport.InMemoryServerTransport;
import io.modelcontextprotocol.spec.McpClientTransport;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.JSONRPCMessage;
import io.modelcontextprotocol.util.Assert;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

/**
 * In-memory client transport for in-process MCP communication.
 *
 * @author Worka
 */
public final class InMemoryClientTransport implements McpClientTransport {

	private final Sinks.Many<JSONRPCMessage> inboundSink;

	private final AtomicBoolean connected = new AtomicBoolean(false);

	private final McpJsonMapper jsonMapper;

	private InMemoryServerTransport serverTransport;

	private Consumer<Throwable> exceptionHandler;

	public InMemoryClientTransport(McpJsonMapper jsonMapper) {
		Assert.notNull(jsonMapper, "The JsonMapper can not be null");
		this.jsonMapper = jsonMapper;
		this.inboundSink = Sinks.many().unicast().onBackpressureBuffer();
	}

	void linkServer(InMemoryServerTransport serverTransport) {
		this.serverTransport = serverTransport;
	}

	void deliver(JSONRPCMessage message) {
		if (inboundSink.tryEmitNext(message).isFailure()) {
			throw new RuntimeException("Failed to enqueue message");
		}
	}

	@Override
	public Mono<Void> connect(Function<Mono<JSONRPCMessage>, Mono<JSONRPCMessage>> handler) {
		if (!connected.compareAndSet(false, true)) {
			return Mono.error(new IllegalStateException("Already connected"));
		}
		return inboundSink.asFlux()
			.flatMap(message -> Mono.just(message).transform(handler))
			.doOnError(e -> {
				if (exceptionHandler != null) {
					exceptionHandler.accept(e);
				}
			})
			.doFinally(signal -> connected.set(false))
			.then();
	}

	@Override
	public Mono<Void> sendMessage(JSONRPCMessage message) {
		if (serverTransport == null) {
			return Mono.error(new IllegalStateException("Not connected"));
		}
		return Mono.fromRunnable(() -> serverTransport.deliver(message));
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

	@Override
	public void setExceptionHandler(Consumer<Throwable> handler) {
		this.exceptionHandler = handler;
	}
}
