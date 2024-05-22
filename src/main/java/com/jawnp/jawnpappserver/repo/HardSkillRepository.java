package com.jawnp.jawnpappserver.repo;

import com.jawnp.jawnpappserver.model.HardSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HardSkillRepository extends JpaRepository<HardSkill,Long> {

}
