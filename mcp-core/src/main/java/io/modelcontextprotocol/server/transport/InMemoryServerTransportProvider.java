/*
 * Copyright 2024-2025 the original author or authors.
 */

package io.modelcontextprotocol.server.transport;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import io.modelcontextprotocol.client.transport.InMemoryClientTransport;
import io.modelcontextprotocol.json.McpJsonMapper;
import io.modelcontextprotocol.spec.McpError;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpServerSession;
import io.modelcontextprotocol.spec.McpServerTransportProvider;
import io.modelcontextprotocol.spec.ProtocolVersions;
import io.modelcontextprotocol.util.Assert;
import reactor.core.publisher.Mono;

/**
 * In-memory server transport provider for in-process MCP communication.
 *
 * @author Worka
 */
public final class InMemoryServerTransportProvider implements McpServerTransportProvider {

	private final McpJsonMapper jsonMapper;

	private final List<McpServerSession> sessions = new CopyOnWriteArrayList<>();

	private McpServerSession.Factory sessionFactory;

	public InMemoryServerTransportProvider() {
		this(McpJsonMapper.getDefault());
	}

	public InMemoryServerTransportProvider(McpJsonMapper jsonMapper) {
		Assert.notNull(jsonMapper, "The JsonMapper can not be null");
		this.jsonMapper = jsonMapper;
	}

	@Override
	public List<String> protocolVersions() {
		return List.of(ProtocolVersions.MCP_2024_11_05);
	}

	@Override
	public void setSessionFactory(McpServerSession.Factory sessionFactory) {
		Assert.notNull(sessionFactory, "Session factory must not be null");
		this.sessionFactory = sessionFactory;
	}

	/**
	 * Creates a client transport linked to a new in-memory server session.
	 * @return a linked {@link InMemoryClientTransport}
	 */
	public InMemoryClientTransport createClientTransport() {
		if (sessionFactory == null) {
			throw new IllegalStateException("Session factory not set");
		}

		var serverTransport = new InMemoryServerTransport(jsonMapper);
		var clientTransport = new InMemoryClientTransport(jsonMapper);
		serverTransport.linkClient(clientTransport);
		clientTransport.linkServer(serverTransport);

		var session = sessionFactory.create(serverTransport);
		serverTransport.attachSession(session);
		sessions.add(session);

		return clientTransport;
	}

	@Override
	public Mono<Void> notifyClients(String method, Object params) {
		if (sessions.isEmpty()) {
			return Mono.error(new McpError("No sessions available"));
		}
		return Mono.when(sessions.stream().map(s -> s.sendNotification(method, params)).toList());
	}

	@Override
	public Mono<Void> closeGracefully() {
		return Mono.when(sessions.stream().map(McpServerSession::closeGracefully).toList());
	}
}
