package cn.bebullish.webflux.netty.underline;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.embedded.netty.NettyRouteProvider;
import org.springframework.boot.web.embedded.netty.NettyServerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorResourceFactory;

import java.util.stream.Collectors;

/**
 * copy from ReactiveWebServerFactoryConfiguration
 *
 * @author Marlon
 * @since 1.0.0
 */
@Configuration
public class UnderlineReactiveWebServerFactoryConfiguration {

    @Bean
    @ConditionalOnMissingBean
    ReactorResourceFactory reactorServerResourceFactory() {
        return new ReactorResourceFactory();
    }

    @Bean
    UnderlineNettyReactiveWebServerFactory nettyReactiveWebServerFactory(ReactorResourceFactory resourceFactory,
                                                                ObjectProvider<NettyRouteProvider> routes,
                                                                ObjectProvider<NettyServerCustomizer> serverCustomizers) {
        UnderlineNettyReactiveWebServerFactory serverFactory = new UnderlineNettyReactiveWebServerFactory();
        serverFactory.setResourceFactory(resourceFactory);
        routes.orderedStream().forEach(serverFactory::addRouteProviders);
        serverFactory.getServerCustomizers().addAll(serverCustomizers.orderedStream().collect(Collectors.toList()));
        return serverFactory;
    }

}
