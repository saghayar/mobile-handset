package com.axiom.mobilehandset.controller;


import com.axiom.mobilehandset.model.MobileHandset;
import com.axiom.mobilehandset.service.IMobileHandsetService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DeviceController {

    private IMobileHandsetService handsetService;

    public DeviceController(IMobileHandsetService handsetService) {
        this.handsetService = handsetService;
    }

    @GetMapping(value = "/mobile/search", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<List<MobileHandset>> search(@RequestParam MultiValueMap<String, Object> requestParams) {
        final List<MobileHandset> mobileHandsets = handsetService.search(requestParams);
        return ResponseEntity.ok(mobileHandsets);
    }

}
