package com.axiom.mobilehandset.client;

import com.axiom.mobilehandset.exception.InvalidResponseExceptionException;
import com.axiom.mobilehandset.model.MobileHandset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;

public abstract class APIClient {
    private static final Logger log = LoggerFactory.getLogger(APIClient.class);

    private final RestTemplate restTemplate;

    public APIClient(RestTemplateBuilder restTemplateBuilder,
                     String gatewayServiceUrl,
                     Integer readTimeoutMs,
                     Integer connectTimeoutMs) {
        this.restTemplate = restTemplateBuilder.rootUri(gatewayServiceUrl)
                .setReadTimeout(Duration.of(readTimeoutMs, ChronoUnit.MILLIS))
                .setConnectTimeout(Duration.of(connectTimeoutMs, ChronoUnit.MILLIS))
                .build();

        restTemplate.getInterceptors().add((request, body, execution) -> {
            ClientHttpResponse response = execution.execute(request, body);
            response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
            return response;
        });
    }

    public List<MobileHandset> fetchDeviceList() {
        try {
            ResponseEntity<List<MobileHandset>> exchange = restTemplate.exchange("/handsets/list",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<MobileHandset>>() {
                    });
            return exchange.getBody();
        } catch (RestClientResponseException e) {
            log.error("\n\n JSON API returned an Error {}  \n\n ", e.getMessage());
            throw new InvalidResponseExceptionException(e.getMessage());
        }
    }

}
