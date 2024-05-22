package com.jawnp.jawnpappserver.model;

public interface Skill {
    Long getId();
    void setId(Long id);
    String getName();
    void setName(String name);
    User getUser();
    void setUser(User user);
}