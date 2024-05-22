package com.jawnp.jawnpappserver.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserDTO {
    private String name;
    private String surname;
    private String email;
    private String fieldOfWork;
    private String password;
    private String login;
    private String linkedInLink;
    private List<String> softSkills;
    private List<String> hardSkills;
    private List<String> careerGoals;
}