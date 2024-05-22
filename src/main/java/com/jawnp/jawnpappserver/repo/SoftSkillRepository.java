package com.jawnp.jawnpappserver.repo;

import com.jawnp.jawnpappserver.model.SoftSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SoftSkillRepository extends JpaRepository<SoftSkill,Long> {
}
