package com.axiom.mobilehandset.repository;

import com.axiom.mobilehandset.model.MobileHandset;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MobileHandsetRepository extends MongoRepository<MobileHandset, Integer> {

}
