package com.jawnp.jawnpappserver.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "HardSkills")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class HardSkill implements Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}