package com.axiom.mobilehandset.client;

import com.axiom.mobilehandset.model.MobileHandset;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class JsonAPIClient extends APIClient {

    @Autowired
    public JsonAPIClient(RestTemplateBuilder restTemplateBuilder,
                         @Value("${app.http.axiom.serviceUrl}") String serviceUrl,
                         @Value("${app.http.axiom.readTimeoutMs}") Integer readTimeoutMs,
                         @Value("${app.http.axiom.connectTimeoutMs}") Integer connectTimeoutMs) {
        super(restTemplateBuilder,
                serviceUrl,
                readTimeoutMs,
                connectTimeoutMs);
    }
    @Override
    @HystrixCommand(commandKey = "axiom")
    @Retryable(maxAttemptsExpression = "#{${app.http.axiom.maxAutoRetries}}",
            backoff = @Backoff(delayExpression = "#{${app.http.axiom.backoffDelayMs}}"),
            exceptionExpression = "#{#root instanceof T(org.springframework.web.client.HttpClientErrorException)")
    public List<MobileHandset> fetchDeviceList() {
        return super.fetchDeviceList();
    }
}