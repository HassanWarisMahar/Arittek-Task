package com.arittek.demo.repository;

import com.arittek.demo.model.Skill;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SkillRepository extends CrudRepository<Skill, Long> {

	public List<Skill> findByLabel(String label);

}
