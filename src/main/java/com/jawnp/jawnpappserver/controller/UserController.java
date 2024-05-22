package com.jawnp.jawnpappserver.controller;

import com.jawnp.jawnpappserver.converter.UserConverter;
import com.jawnp.jawnpappserver.dto.UserDTO;
import com.jawnp.jawnpappserver.model.CareerGoal;
import com.jawnp.jawnpappserver.model.LoginForm;
import com.jawnp.jawnpappserver.model.Skill;
import com.jawnp.jawnpappserver.model.User;
import com.jawnp.jawnpappserver.repo.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        try {
            logger.info("Fetching all users");
            List<UserDTO> userList = new ArrayList<>();
            userRepository.findAll().forEach(user -> userList.add(UserConverter.convertToDTO(user)));

            if (userList.isEmpty()) {
                logger.warn("No users found");
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            logger.info("Retrieved {} users", userList.size());
            return new ResponseEntity<>(userList, HttpStatus.OK);
        } catch (Exception exception) {
            logger.error("An error occurred while fetching users", exception);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        try {
            logger.info("Fetching user with ID: {}", id);
            Optional<User> userData = userRepository.findById(id);

            if (userData.isPresent()) {
                logger.info("User found with ID: {}", id);
                return new ResponseEntity<>(UserConverter.convertToDTO(userData.get()), HttpStatus.OK);
            }

            logger.warn("User not found with ID: {}", id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception exception) {
            logger.error("An error occurred while fetching user with ID: {}", id, exception);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/addUser")
    public ResponseEntity<UserDTO> addUser(@RequestBody UserDTO userDTO) {
        try {
            logger.info("Adding user: {}", userDTO);
            User user = UserConverter.convertToEntity(userDTO);
            User savedUser = userRepository.save(user);
            logger.info("User added successfully with ID: {}", savedUser.getId());
            return new ResponseEntity<>(UserConverter.convertToDTO(savedUser), HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Failed to add user", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Boolean> loginUser(@RequestBody LoginForm form) {
        try {
            logger.info("Logging in user with login: {}", form.toString());
            Optional<User> userOptional = userRepository.findByLogin(form.getLogin());
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                if (user.getPassword().equals(form.getPassword())) {
                    logger.info("Login successful for user: {}", user.getLogin());
                    return new ResponseEntity<>(true, HttpStatus.OK);
                } else {
                    logger.info("Incorrect password for user: {}", user.getLogin());
                    return new ResponseEntity<>(false, HttpStatus.UNAUTHORIZED);
                }
            } else {
                logger.info("User not found with login: {}", form.getLogin());
                return new ResponseEntity<>(false, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            logger.error("An error occurred while logging in user with login: {}", form.getLogin(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUserById(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        try {
            logger.info("Updating user with ID: {}", id);
            Optional<User> userData = userRepository.findById(id);

            if (userData.isPresent()) {
                User existingUser = userData.get();
                User updatedUser = UserConverter.convertToEntity(userDTO);

                existingUser.setEmail(updatedUser.getEmail());
                existingUser.setName(updatedUser.getName());
                existingUser.setSurname(updatedUser.getSurname());
                existingUser.setFieldOfWork(updatedUser.getFieldOfWork());
                existingUser.setLinkedInLink(updatedUser.getLinkedInLink());
                existingUser.setGithubLink(updatedUser.getGithubLink());
                existingUser.setLogin(updatedUser.getLogin());
                existingUser.setPassword(updatedUser.getPassword());

                updateSkills(existingUser.getSoftSkills(), updatedUser.getSoftSkills(), existingUser);
                updateSkills(existingUser.getHardSkills(), updatedUser.getHardSkills(), existingUser);
                updateCareerGoals(existingUser.getCareerGoals(), updatedUser.getCareerGoals(), existingUser);

                User savedUser = userRepository.save(existingUser);
                logger.info("User updated successfully with ID: {}", id);
                return new ResponseEntity<>(UserConverter.convertToDTO(savedUser), HttpStatus.OK);
            } else {
                logger.warn("User not found with ID: {}", id);
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception exception) {
            logger.error("An error occurred while updating user with ID: {}", id, exception);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private <T extends Skill> void updateSkills(List<T> existingSkills, List<T> newSkills, User user) {
        if (newSkills == null) {
            return;
        }

        existingSkills.removeIf(existingSkill ->
                newSkills.stream().noneMatch(newSkill ->
                        newSkill.getId() != null && newSkill.getId().equals(existingSkill.getId())));

        for (T newSkill : newSkills) {
            if (newSkill.getId() == null) {
                newSkill.setUser(user);
                existingSkills.add(newSkill);
            } else {
                for (T existingSkill : existingSkills) {
                    if (existingSkill.getId().equals(newSkill.getId())) {
                        existingSkill.setName(newSkill.getName());
                        break;
                    }
                }
            }
        }
    }

    private void updateCareerGoals(List<CareerGoal> existingGoals, List<CareerGoal> newGoals, User user) {
        if (newGoals == null) {
            return;
        }

        existingGoals.removeIf(existingGoal ->
                newGoals.stream().noneMatch(newGoal ->
                        newGoal.getId() != null && newGoal.getId().equals(existingGoal.getId())));

        for (CareerGoal newGoal : newGoals) {
            if (newGoal.getId() == null) {
                newGoal.setUser(user);
                existingGoals.add(newGoal);
            } else {
                for (CareerGoal existingGoal : existingGoals) {
                    if (existingGoal.getId().equals(newGoal.getId())) {
                        existingGoal.setTitle(newGoal.getTitle());
                        break;
                    }
                }
            }
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteUserById(@PathVariable Long id) {
        try {
            userRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}