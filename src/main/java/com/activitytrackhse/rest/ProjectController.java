package com.activitytrackhse.rest;

import com.activitytrackhse.model.Project;
import com.activitytrackhse.model.*;
import com.activitytrackhse.service.inter.ProjectService;
import com.activitytrackhse.service.inter.UserService;
import com.activitytrackhse.service.inter.WorkService;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@RestController
@CrossOrigin
@RequestMapping(value = "/projects")
public class ProjectController {
    private ProjectService projectService;
    private UserService userService;
    private WorkService workService;

    public ProjectController(@Autowired ProjectService projectService,
                             @Autowired UserService userService,
                             @Autowired WorkService workService) {
        this.projectService = projectService;
        this.userService = userService;
        this.workService = workService;
    }

    @GetMapping
    public ResponseEntity<?> readProjects() {
        final List<Project> projects = projectService.readAll();
        return projects != null &&  !projects.isEmpty()
                ? new ResponseEntity<>(projects, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> readProjectById(@PathVariable int id) {
        if(id < 1){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }else {
            Project project = projectService.read(id);
            return project != null
                    ? new ResponseEntity<>(project, HttpStatus.OK)
                    : new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/{project}/users")
    public ResponseEntity<?> readUsersInProject(@PathVariable int project) {
        if(project < 1) {
            return new ResponseEntity<>("Id can not be less then 1!", HttpStatus.BAD_REQUEST);
        }
        Project p = projectService.read(project);
        if(p == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<Work> works = workService.findAllByProject(p);
        if(works == null || works.size() == 0){
            return new ResponseEntity<>(null, HttpStatus.OK);
        }
        List<User> users_1 = (works.stream().map(Work::getUser)).toList();
        List<User> users = new LinkedList<>(users_1);
        users.removeIf((user -> Objects.equals(user.getRole(), "Admin") || Objects.equals(user.getRole(), "SuperAdmin")));
        return users != null
                ? new ResponseEntity<>(users, HttpStatus.OK)
                : new ResponseEntity<>(null, HttpStatus.OK);
    }

    @GetMapping(value = "/{project}/free_users")
    public ResponseEntity<?> readUsersNotInProject(@PathVariable int project) {
        if(project < 1) {
            return new ResponseEntity<>("Id can not be less then 1!", HttpStatus.BAD_REQUEST);
        }
        Project p = projectService.read(project);
        if(p == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<Work> works = workService.findAllByProject(p);
        if(works == null || works.size() == 0) {
            List<User> all = userService.readAll();
            all.removeIf(user -> Objects.equals(user.getRole(), "Admin") || Objects.equals(user.getRole(), "SuperAdmin"));
            return new ResponseEntity<>(all, HttpStatus.OK);
        }
        List<User> users = works.stream().map(Work::getUser).toList();
        List<User> all = userService.readAll();
        all.removeAll(users);
        all.removeIf(user -> Objects.equals(user.getRole(), "Admin") || Objects.equals(user.getRole(), "SuperAdmin"));
        return all != null
                ? new ResponseEntity<>(all, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/i_have")
    public ResponseEntity<?> readProjectIHave(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.getByNickname(userDetails.getUsername());
        if(user == null){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        List<Project> projects = projectService.findAllByOwner(user);
        return projects != null
                ? new ResponseEntity<>(projects, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/i_consist_in")
    public ResponseEntity<?> readProjectIConsistIn(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.getByNickname(userDetails.getUsername());
        if(user == null){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        if(Objects.equals(user.getRole(), "SuperAdmin") || Objects.equals(user.getRole(), "Admin")) {
            final List<Project> projects = projectService.readAll();
            return projects != null &&  !projects.isEmpty()
                    ? new ResponseEntity<>(projects, HttpStatus.OK)
                    : new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<Project> projects = workService.findAllByUser(user).stream().map(Work::getProject).toList();
        //List<Project> projects = user.getProjects();
        return projects != null
                ? new ResponseEntity<>(projects, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<?> createProject(@AuthenticationPrincipal UserDetails userDetails, @RequestBody Project project) {
        User user = userService.getByNickname(userDetails.getUsername());
        if(user == null){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        project.setOwner(user);
        project.setCreationDate(Date.valueOf(LocalDate.now()));
        try{
            projectService.create(project);
        } catch(DataIntegrityViolationException ex) {
            ConstraintViolationException e = (ConstraintViolationException) ex.getCause();
            String message;
            if (e.getConstraintName().equals("name")) {
                message = "Name can not be null!";
            }else if(e.getConstraintName().equals("project_name_key")){
                message = "A project with this name has already been created!";
            }else if(e.getConstraintName().equals("screenshot_interval_check")){
                message = "Incorrect screenshot interval!";
            }else if(e.getConstraintName().equals("session_part_interval_check")){
                message = "Incorrect session part!";
            }else {
                message = e.getSQLException().getMessage();
            }
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteProject(@AuthenticationPrincipal UserDetails userDetails, @PathVariable int id) {
        if(id < 1){
            return new ResponseEntity<>("Incorrect id = " + id, HttpStatus.BAD_REQUEST);
        }
        User me = userService.getByNickname(userDetails.getUsername());
        if(me == null || !(me.getRole().equals("Admin")) || !(me.getRole().equals("SuperAdmin"))) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        Project project = projectService.read(id);
        if(project == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
//        if(!(me == project.getOwner())) {
//            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
//        }
        projectService.delete(project);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<?> updateProject(@AuthenticationPrincipal UserDetails userDetails, @RequestBody Project project,
                                           @PathVariable int id) {
        if(id < 1){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        User me = userService.getByNickname(userDetails.getUsername());
        if(me == null || !(me.getRole().equals("Admin") || me.getRole().equals("Manager"))){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        Project p = projectService.read(id);
        if(p == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if(me.getRole().equals("Manager") && !(me == p.getOwner())){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        p.setNewInfo(project);
        try{
            projectService.save(p);
        } catch (DataIntegrityViolationException ex) {
            ConstraintViolationException e = (ConstraintViolationException) ex.getCause();
            String message;
            if (e.getConstraintName().equals("name")) {
                message = "Name can not be null!";
            }else if(e.getConstraintName().equals("project_name_key")){
                message = "A project with this name has already been created!";
            }else if(e.getConstraintName().equals("screenshot_interval_check")){
                message = "Incorrect screenshot interval!";
            }else if(e.getConstraintName().equals("session_part_interval_check")){
                message = "Incorrect session part!";
            }else {
                message = e.getSQLException().getMessage();
            }
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/{project}/add_user")
    public ResponseEntity<?> addPerson(@AuthenticationPrincipal UserDetails userDetails,
                                       @RequestParam int user, @PathVariable int project) {
        if(user < 1){
            return new ResponseEntity<>("Incorrect user id = " + user, HttpStatus.BAD_REQUEST);
        }
        if(project < 1){
            return new ResponseEntity<>("Incorrect project id = " + project, HttpStatus.BAD_REQUEST);
        }
        User me = userService.getByNickname(userDetails.getUsername());
        if(me == null || !(me.getRole().equals("Admin") || me.getRole().equals("Manager"))){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        Project p = projectService.read(project);
        if(p == null){
            return new ResponseEntity<>("Project not found", HttpStatus.NOT_FOUND);
        }
        User u = userService.read(user);
        if(u == null){
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
        if(me.getRole().equals("Manager") && !(me == p.getOwner())){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        try {
            workService.insert(user, project, Date.valueOf(LocalDate.now()));
        }catch (DataIntegrityViolationException ex){
            ConstraintViolationException e = (ConstraintViolationException) ex.getCause();
            String message;
            if (e.getConstraintName().equals("works_user_project_unique")) {
                message = "This user has already added to this project!";
            }else {
                message = e.getSQLException().getMessage();
            }
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(value = "/{project}/delete_user")
    public ResponseEntity<?> deletePerson(@AuthenticationPrincipal UserDetails userDetails,
                                       @RequestParam int user, @PathVariable int project) {
        if(user < 1){
            return new ResponseEntity<>("Incorrect user id = " + user, HttpStatus.BAD_REQUEST);
        }
        if(project < 1){
            return new ResponseEntity<>("Incorrect project id = " + project, HttpStatus.BAD_REQUEST);
        }
        User me = userService.getByNickname(userDetails.getUsername());
        if(me == null || !(me.getRole().equals("Admin") || me.getRole().equals("Manager"))){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        Project p = projectService.read(project);
        if(p == null){
            return new ResponseEntity<>("Project not found!", HttpStatus.NOT_FOUND);
        }
        User u = userService.read(user);
        if(u == null){
            return new ResponseEntity<>("User not found!", HttpStatus.NOT_FOUND);
        }
        if(me.getRole().equals("Manager") && !(me == p.getOwner())){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        Work work = workService.findByUserAndProject(u, p);
        if(work == null){
            return new ResponseEntity<>("This user not work on this project!", HttpStatus.NOT_FOUND);
        }
        workService.delete(work);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
