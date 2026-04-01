package com.manav.getaway.filter;

import com.manav.getaway.dto.FraudRequest;
import com.manav.getaway.dto.FraudResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import java.time.Duration;
import java.util.concurrent.TimeoutException;

@Component
public class FraudDetectionFilter implements GlobalFilter, Ordered {

    private static final Logger log = LoggerFactory.getLogger(FraudDetectionFilter.class);

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
                .exchangeToMono(clientResponse -> {
                    if(clientResponse.statusCode().is2xxSuccessful()){
                        return clientResponse.bodyToMono(FraudResponse.class);
                    }else{
                        return clientResponse.bodyToMono(String.class)
                                .flatMap(errorBody -> {
                                    log.error("Fraud service error response {}", errorBody);
                                    return Mono.error(new RuntimeException("Fraud service error"));
                                });
                    }
                })
                .timeout(Duration.ofSeconds(2))

                .flatMap(response ->{
                    log.info("Fraud Decision: {}", response.getStatus());
                    log.info("Risk Score: {}", response.getRiskScore());

                    if("BLOCK".equalsIgnoreCase(response.getStatus())){
                        exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                        return exchange.getResponse().setComplete();
                    }

                    if("FLAG".equalsIgnoreCase(response.getStatus())){
                        ServerWebExchange mutatedExchange = exchange.mutate()
                                .request(builder -> builder.header("X-Fraud-Flag","true"))
                                .build();
                        return chain.filter(mutatedExchange);
                    }

                    return chain.filter(exchange);
                })
                .onErrorResume(error -> {
                    String pathValue = exchange.getRequest().getURI().getPath();

                    if(error instanceof TimeoutException){
                        log.warn("Fraud service timeout after 2 seconds for path: {}", pathValue);
                    } else{
                        log.error("Fraud service failed for path {} : {}", pathValue, error.getMessage());
                    }

                    if(pathValue.contains("admin") || pathValue.contains("payment")){
                        log.warn("Fail-Closed triggered for high-risk path: {}", pathValue);

                        exchange.getResponse().setStatusCode(HttpStatus.SERVICE_UNAVAILABLE);
                        return exchange.getResponse().setComplete();
                    }
                    log.warn("Fail-Open Triggered for path: {}", pathValue);

                    return chain.filter(exchange);
                });
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
