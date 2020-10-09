package com.axiom.mobilehandset.service;

import com.axiom.mobilehandset.model.MobileHandset;
import org.springframework.util.MultiValueMap;

import java.util.List;

public interface IMobileHandsetService {

    /**
     * @param requestParams in put params form user
     * @return filtered mobile handsets list based on input parameters
     */
    List<MobileHandset> search(MultiValueMap<String, Object> requestParams);
}
