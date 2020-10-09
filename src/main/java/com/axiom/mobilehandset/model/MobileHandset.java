package com.axiom.mobilehandset.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "handsets")
@Data
public class MobileHandset {

    @Id
    private Integer id;
    private String brand;
    private String phone;
    private String picture;
    private String sim;
    private String resolution;
    private Release release;
    private Hardware hardware;
}

