package com.axiom.mobilehandset.swagger;

import org.springframework.stereotype.Component;

@Component
public class MySwaggerConfiguration extends SwaggerConfiguration {
    @Override
    public String apiTitle() {
        return "Axiom Mobile handset service documentation";
    }

}
