package com.jawnp.jawnpappserver.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "CareerGoals")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class CareerGoal {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String title;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
