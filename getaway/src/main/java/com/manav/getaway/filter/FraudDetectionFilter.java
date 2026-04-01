package com.manav.getaway.filter;

import com.manav.getaway.dto.FraudRequest;
import com.manav.getaway.dto.FraudResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class FraudDetectionFilter implements GlobalFilter, Ordered {

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange,
                             GatewayFilterChain chain)
    {
        var request = exchange.getRequest();

        String userAgent = request.getHeaders().getFirst("User-Agent");
        String ip = request.getRemoteAddress()
                .getAddress()
                .getHostAddress();
        String path = request.getURI().getPath();

        FraudRequest fraudRequest = new FraudRequest(ip, userAgent, path);

        return webClientBuilder.build()
                .post()
                .uri("http://localhost:8082/fraud/check")
                .header("Content-Type", "application/json")
                .bodyValue(fraudRequest)
                .retrieve()
                .bodyToMono(FraudResponse.class)

                .flatMap(response ->{
                    System.out.println("fraud Decision:" + response.getStatus());
                    System.out.println("Risk Score:" + response.getRiskScore());

                    if("BLOCK".equals(response.getStatus())){
                        exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                        return exchange.getResponse().setComplete();
                    }

                    if("FLAG".equals(response.getStatus())){
                        ServerWebExchange mutatedExchange = exchange.mutate()
                                .request(builder -> builder.header("X-Fraud-Flag","true"))
                                .build();
                        return chain.filter(mutatedExchange);
                    }

                    return chain.filter(exchange);
                }).onErrorResume(error ->{
                    System.out.println("Fraud service failed:" + error.getMessage());

                    return chain.filter(exchange);
                });
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
