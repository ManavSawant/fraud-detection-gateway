package com.manav.getaway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class FraudDetectionFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange,
                             GatewayFilterChain chain)
    {
        var request = exchange.getRequest();

        String userAgent = request.getHeaders().getFirst("User-Agent");

        String ip = request.getRemoteAddress()
                .getAddress()
                .getHostAddress();

        System.out.println("Incoming Request:");
        System.out.println("IP: " + ip);
        System.out.println("User-Agent: " + userAgent);
        System.out.println("Path: " + request.getURI());

        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            System.out.println("Response sent back to client");
        }));
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
