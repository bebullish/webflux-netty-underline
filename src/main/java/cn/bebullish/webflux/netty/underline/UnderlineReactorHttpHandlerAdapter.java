/*
 * Copyright 2002-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.bebullish.webflux.netty.underline;

import org.apache.commons.logging.Log;
import org.springframework.core.io.buffer.NettyDataBufferFactory;
import org.springframework.http.HttpLogging;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.http.server.reactive.HttpHeadResponseDecorator;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.util.Assert;

import java.net.URISyntaxException;
import java.util.function.BiFunction;

import io.netty.handler.codec.http.HttpResponseStatus;
import reactor.core.publisher.Mono;
import reactor.netty.http.server.HttpServerRequest;
import reactor.netty.http.server.HttpServerResponse;

/**
 * copy from ReactorHttpHandlerAdapter
 *
 * @author Marlon
 * @since 1.0.0
 */
public class UnderlineReactorHttpHandlerAdapter implements BiFunction<HttpServerRequest, HttpServerResponse, Mono<Void>> {

	private static final Log logger = HttpLogging.forLogName(UnderlineReactorHttpHandlerAdapter.class);


	private final HttpHandler httpHandler;


	public UnderlineReactorHttpHandlerAdapter(HttpHandler httpHandler) {
		Assert.notNull(httpHandler, "HttpHandler must not be null");
		this.httpHandler = httpHandler;
	}


	@Override
	public Mono<Void> apply(HttpServerRequest reactorRequest, HttpServerResponse reactorResponse) {
		NettyDataBufferFactory bufferFactory = new NettyDataBufferFactory(reactorResponse.alloc());
		try {
			UnderlineReactorServerHttpRequest request = new UnderlineReactorServerHttpRequest(reactorRequest, bufferFactory);
			ServerHttpResponse response = new ReactorServerHttpResponse(reactorResponse, bufferFactory);

			if (request.getMethod() == HttpMethod.HEAD) {
				response = new HttpHeadResponseDecorator(response);
			}

			return this.httpHandler.handle(request, response)
					.doOnError(ex -> logger.trace("[" + request.getId() + "] Failed to complete: " + ex.getMessage()))
					.doOnSuccess(aVoid -> logger.trace("[" + request.getId() + "] Handling completed"));
		}
		catch (URISyntaxException ex) {
			if (logger.isDebugEnabled()) {
				logger.debug("Failed to get request URI: " + ex.getMessage());
			}
			reactorResponse.status(HttpResponseStatus.BAD_REQUEST);
			return Mono.empty();
		}
	}

}
