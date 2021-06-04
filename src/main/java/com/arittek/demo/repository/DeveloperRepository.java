package com.arittek.demo.repository;

import com.arittek.demo.model.Developer;
import org.springframework.data.repository.CrudRepository;

public interface DeveloperRepository extends CrudRepository<Developer, Long> {

}
