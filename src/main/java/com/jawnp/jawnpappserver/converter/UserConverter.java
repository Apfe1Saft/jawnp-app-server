package com.jawnp.jawnpappserver.converter;

import com.jawnp.jawnpappserver.dto.UserDTO;
import com.jawnp.jawnpappserver.model.HardSkill;
import com.jawnp.jawnpappserver.model.CareerGoal;
import com.jawnp.jawnpappserver.model.SoftSkill;
import com.jawnp.jawnpappserver.model.User;

import java.util.List;
import java.util.stream.Collectors;

public class UserConverter {

    public static UserDTO convertToDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setName(user.getName());
        userDTO.setSurname(user.getSurname());
        userDTO.setEmail(user.getEmail());
        userDTO.setFieldOfWork(user.getFieldOfWork());
        userDTO.setPassword(user.getPassword());
        userDTO.setLogin(user.getLogin());
        userDTO.setLinkedInLink(user.getLinkedInLink());
        userDTO.setSoftSkills(user.getSoftSkills().stream().map(SoftSkill::getName).collect(Collectors.toList()));
        userDTO.setHardSkills(user.getHardSkills().stream().map(HardSkill::getName).collect(Collectors.toList()));
        userDTO.setCareerGoals(user.getCareerGoals().stream().map(CareerGoal::getTitle).collect(Collectors.toList()));
        return userDTO;
    }

    public static User convertToEntity(UserDTO userDTO) {
        User user = new User();
        user.setName(userDTO.getName());
        user.setSurname(userDTO.getSurname());
        user.setEmail(userDTO.getEmail());
        user.setFieldOfWork(userDTO.getFieldOfWork());
        user.setPassword(userDTO.getPassword());
        user.setLogin(userDTO.getLogin());
        user.setLinkedInLink(userDTO.getLinkedInLink());

        List<SoftSkill> softSkills = userDTO.getSoftSkills().stream().map(name -> {
            SoftSkill softSkill = new SoftSkill();
            softSkill.setName(name);
            softSkill.setUser(user);
            return softSkill;
        }).collect(Collectors.toList());

        List<HardSkill> hardSkills = userDTO.getHardSkills().stream().map(name -> {
            HardSkill hardSkill = new HardSkill();
            hardSkill.setName(name);
            hardSkill.setUser(user);
            return hardSkill;
        }).collect(Collectors.toList());

        List<CareerGoal> careerGoals = userDTO.getCareerGoals().stream().map(title -> {
            CareerGoal careerGoal = new CareerGoal();
            careerGoal.setTitle(title);
            careerGoal.setUser(user);
            return careerGoal;
        }).collect(Collectors.toList());

        user.setSoftSkills(softSkills);
        user.setHardSkills(hardSkills);
        user.setCareerGoals(careerGoals);

        return user;
    }
}