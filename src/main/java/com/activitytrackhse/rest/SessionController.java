package com.activitytrackhse.rest;

import com.activitytrackhse.model.*;
import com.activitytrackhse.service.inter.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(value = "/sessions")
public class SessionController {
    private SessionInfoService sessionInfoService;
    private UserService userService;
    private ProjectService projectService;
    private SessionService sessionService;
    private WorkTimeService workTimeService;

    public SessionController(@Autowired SessionInfoService sessionInfoService,
                             @Autowired UserService userService,
                             @Autowired ProjectService projectService,
                             @Autowired SessionService sessionService,
                             @Autowired WorkTimeService workTimeService) {
        this.sessionInfoService = sessionInfoService;
        this.userService = userService;
        this.projectService = projectService;
        this.sessionService = sessionService;
        this.workTimeService = workTimeService;
    }

    @PostMapping
    public ResponseEntity<?> createSession(@RequestBody List<SessionInfo> sessionInfoList) {
        try {
            sessionInfoService.saveSessionInfo(sessionInfoList);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/get_by_upd")
    public ResponseEntity<?> getSessionsByUPD(@RequestParam(name = "user") int userId,
                                           @RequestParam(name = "project") int projectId,
                                           @RequestParam(name = "date") String d) {
        Date date;
        try {
            date = Date.valueOf(d);
            System.out.println("date " + date);
        }catch (Exception e){
            return new ResponseEntity<>("Incorrect date!", HttpStatus.BAD_REQUEST);
        }
        if(userId < 1){
            return new ResponseEntity<>("Incorrect user id = " + userId, HttpStatus.BAD_REQUEST);
        }
        if(projectId < 1){
            return new ResponseEntity<>("Incorrect project id = " + projectId, HttpStatus.BAD_REQUEST);
        }
        Project project = projectService.read(projectId);
        if(project == null){
            return new ResponseEntity<>("Project not found", HttpStatus.BAD_REQUEST);
        }
        User user = userService.read(userId);
        if(user == null){
            return new ResponseEntity<>("User not found", HttpStatus.BAD_REQUEST);
        }
        List<Session> res;
        res = sessionService.findAllByUserAndProjectAndDate(user, project, date);
        System.out.println(res.get(0).getSessionParts());
        if(res == null){
            res = new LinkedList<>();
        }
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping(value = "/get_by_upper")
    public ResponseEntity<?> getSessionsByUPPer(@RequestParam(name = "user") int userId,
                                                @RequestParam(name = "project") int projectId,
                                                @RequestParam(name = "first_day") String f,
                                                @RequestParam(name = "second_day") String s) {
        Date first;
        try {
            first = Date.valueOf(f);
        }catch (Exception e){
            return new ResponseEntity<>("Incorrect date!", HttpStatus.BAD_REQUEST);
        }
        Date second;
        try {
            second = Date.valueOf(s);
        }catch (Exception e){
            return new ResponseEntity<>("Incorrect date!", HttpStatus.BAD_REQUEST);
        }
        if(userId < 1){
            return new ResponseEntity<>("Incorrect user id = " + userId, HttpStatus.BAD_REQUEST);
        }
        if(projectId < 1){
            return new ResponseEntity<>("Incorrect project id = " + projectId, HttpStatus.BAD_REQUEST);
        }
        Project project = projectService.read(projectId);
        if(project == null){
            return new ResponseEntity<>("Project not found", HttpStatus.BAD_REQUEST);
        }
        User user = userService.read(userId);
        if(user == null){
            return new ResponseEntity<>("User not found", HttpStatus.BAD_REQUEST);
        }
        List<Session> res;
        res = sessionService.findAllByUserAndProjectAndPeriod(user, project, first, second);
        if(res == null){
            res = new LinkedList<>();
        }
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping(value = "/get_by_ud")
    public ResponseEntity<?> getSessionsByUD(@RequestParam(name = "user") int userId,
                                             @RequestParam(name = "date") String d) {
        Date date;
        try {
            date = Date.valueOf(d);
        }catch (Exception e){
            return new ResponseEntity<>("Incorrect date!", HttpStatus.BAD_REQUEST);
        }
        if(userId < 1){
            return new ResponseEntity<>("Incorrect user id = " + userId, HttpStatus.BAD_REQUEST);
        }
        User user = userService.read(userId);
        if(user == null){
            return new ResponseEntity<>("User not found", HttpStatus.BAD_REQUEST);
        }
        List<Session> res;
        res = sessionService.findAllByUserAndDate(user, date);
        if(res == null){
            res = new LinkedList<>();
        }
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping(value = "/get_by_uper")
    public ResponseEntity<?> getSessionsByUPer(@RequestParam(name = "user") int userId,
                                             @RequestParam(name = "first_day") String f,
                                             @RequestParam(name = "second_day") String s) {
        Date first;
        try {
            first = Date.valueOf(f);
        }catch (Exception e){
            return new ResponseEntity<>("Incorrect date!", HttpStatus.BAD_REQUEST);
        }
        Date second;
        try {
            second = Date.valueOf(s);
        }catch (Exception e){
            return new ResponseEntity<>("Incorrect date!", HttpStatus.BAD_REQUEST);
        }
        if(userId < 1){
            return new ResponseEntity<>("Incorrect user id = " + userId, HttpStatus.BAD_REQUEST);
        }
        User user = userService.read(userId);
        if(user == null){
            return new ResponseEntity<>("User not found", HttpStatus.BAD_REQUEST);
        }
        List<Session> res;
        res = sessionService.findAllByUserAndPeriod(user, first, second);
        if(res == null){
            res = new LinkedList<>();
        }
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping(value = "/get_by_pd")
    public ResponseEntity<?> getSessionsByPD(@RequestParam(name = "project") int projectId,
                                           @RequestParam(name = "date") String d) {
        Date date;
        try {
            date = Date.valueOf(d);
        }catch (Exception e){
            return new ResponseEntity<>("Incorrect date!", HttpStatus.BAD_REQUEST);
        }
        if(projectId < 1){
            return new ResponseEntity<>("Incorrect project id = " + projectId, HttpStatus.BAD_REQUEST);
        }
        Project project = projectService.read(projectId);
        if(project == null){
            return new ResponseEntity<>("Project not found", HttpStatus.BAD_REQUEST);
        }
        List<Session> res;
        res = sessionService.findAllByProjectAndDate(project, date);
        if(res == null){
            res = new LinkedList<>();
        }
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping(value = "/get_by_pper")
    public ResponseEntity<?> getSessionsByPPer(@RequestParam(name = "project") int projectId,
                                               @RequestParam(name = "first_day") String f,
                                               @RequestParam(name = "second_day") String s) {
        Date first;
        try {
            first = Date.valueOf(f);
        }catch (Exception e){
            return new ResponseEntity<>("Incorrect date!", HttpStatus.BAD_REQUEST);
        }
        Date second;
        try {
            second = Date.valueOf(s);
        }catch (Exception e){
            return new ResponseEntity<>("Incorrect date!", HttpStatus.BAD_REQUEST);
        }
        if(projectId < 1){
            return new ResponseEntity<>("Incorrect project id = " + projectId, HttpStatus.BAD_REQUEST);
        }
        Project project = projectService.read(projectId);
        if(project == null){
            return new ResponseEntity<>("Project not found", HttpStatus.BAD_REQUEST);
        }
        List<Session> res;
        res = sessionService.findAllByProjectAndPeriod(project, first, second);
        if(res == null){
            res = new LinkedList<>();
        }
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping(value = "/get_activity_by_uper")
    public ResponseEntity<?> getActivityByUserAndPeriod(@RequestParam(name = "user") int userId,
                                                        @RequestParam(name = "first_day") String f,
                                                        @RequestParam(name = "second_day") String s) {
        Date first, second;
        try {
            first = Date.valueOf(f);
            second = Date.valueOf(s);
        }catch (Exception e){
            return new ResponseEntity<>("Incorrect date!", HttpStatus.BAD_REQUEST);
        }
        if(first.after(second)){
            return new ResponseEntity<>("First date after second date!", HttpStatus.BAD_REQUEST);
        }
        if(userId < 1){
            return new ResponseEntity<>("Incorrect user id = " + userId, HttpStatus.BAD_REQUEST);
        }
        User user = userService.read(userId);
        if(user == null){
            return new ResponseEntity<>("User not found!", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(sessionService.getActivityByUserAndPeriod(userId, first, second), HttpStatus.OK);
    }

    @GetMapping(value = "/get_activity_by_upper")
    public ResponseEntity<?> getActivityByUserAndProjectAndPeriod(@RequestParam(name = "user") int userId,
                                                                  @RequestParam(name = "project") int projectId,
                                                                  @RequestParam(name = "first_day") String f,
                                                                  @RequestParam(name = "second_day") String s) {
        Date first, second;
        try {
            first = Date.valueOf(f);
            second = Date.valueOf(s);
        }catch (Exception e){
            return new ResponseEntity<>("Incorrect date!", HttpStatus.BAD_REQUEST);
        }
        if(first.after(second)){
            return new ResponseEntity<>("First date after second date!", HttpStatus.BAD_REQUEST);
        }
        if(userId < 1){
            return new ResponseEntity<>("Incorrect user id = " + userId, HttpStatus.BAD_REQUEST);
        }
        User user = userService.read(userId);
        if(user == null){
            return new ResponseEntity<>("User not found", HttpStatus.BAD_REQUEST);
        }
        if(projectId < 1){
            return new ResponseEntity<>("Incorrect project id = " + projectId, HttpStatus.BAD_REQUEST);
        }
        Project project = projectService.read(projectId);
        if(project == null){
            return new ResponseEntity<>("Project not found", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(sessionService.getActivityByUserAndProjectAndPeriod(userId, projectId, first, second), HttpStatus.OK);
    }

    @GetMapping(value = "/get_activity_by_up")
    public ResponseEntity<?> getActivityByUserAndProject(@RequestParam(name = "user") int userId,
                                                                  @RequestParam(name = "project") int projectId) {
        if(userId < 1){
            return new ResponseEntity<>("Incorrect user id = " + userId, HttpStatus.BAD_REQUEST);
        }
        User user = userService.read(userId);
        if(user == null){
            return new ResponseEntity<>("User not found", HttpStatus.BAD_REQUEST);
        }
        if(projectId < 1){
            return new ResponseEntity<>("Incorrect project id = " + projectId, HttpStatus.BAD_REQUEST);
        }
        Project project = projectService.read(projectId);
        if(project == null){
            return new ResponseEntity<>("Project not found", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(sessionService.getActivityByUserAndProject(userId, projectId), HttpStatus.OK);
    }

    @GetMapping(value = "/get_activity_by_p")
    public ResponseEntity<?> getActivityByProject(@RequestParam(name = "project") int projectId) {
        if(projectId < 1){
            return new ResponseEntity<>("Incorrect project id = " + projectId, HttpStatus.BAD_REQUEST);
        }
        Project project = projectService.read(projectId);
        if(project == null){
            return new ResponseEntity<>("Project not found", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(sessionService.getActivityByProject(projectId), HttpStatus.OK);
    }

    @GetMapping(value = "/get_work_time_by_uper")
    public ResponseEntity<?> getWorkTimeByUser(@RequestParam(name = "user") int userId,
                                               @RequestParam(name = "first_day") String f,
                                               @RequestParam(name = "second_day") String s) {
        Date first;
        try {
            first = Date.valueOf(f);
        }catch (Exception e){
            return new ResponseEntity<>("Incorrect date!", HttpStatus.BAD_REQUEST);
        }
        Date second;
        try {
            second = Date.valueOf(s);
        }catch (Exception e){
            return new ResponseEntity<>("Incorrect date!", HttpStatus.BAD_REQUEST);
        }
        if(userId < 1){
            return new ResponseEntity<>("Incorrect user id = " + userId, HttpStatus.BAD_REQUEST);
        }
        User user = userService.read(userId);
        if(user == null){
            return new ResponseEntity<>("User not found", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(workTimeService.getByUser(user, first, second), HttpStatus.OK);
    }

    @GetMapping(value = "/get_work_time_by_pper")
    public ResponseEntity<?> getWorkTimeByProject(@RequestParam(name = "project") int projectId,
                                                  @RequestParam(name = "first_day") String f,
                                                  @RequestParam(name = "second_day") String s) {
        Date first;
        try {
            first = Date.valueOf(f);
        }catch (Exception e){
            return new ResponseEntity<>("Incorrect date!", HttpStatus.BAD_REQUEST);
        }
        Date second;
        try {
            second = Date.valueOf(s);
        }catch (Exception e){
            return new ResponseEntity<>("Incorrect date!", HttpStatus.BAD_REQUEST);
        }
        if(projectId < 1){
            return new ResponseEntity<>("Incorrect project id = " + projectId, HttpStatus.BAD_REQUEST);
        }
        Project project = projectService.read(projectId);
        if(project == null){
            return new ResponseEntity<>("Project not found", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(workTimeService.getByProject(project, first, second), HttpStatus.OK);
    }

    @GetMapping(value = "/get_work_time_by_upper")
    public ResponseEntity<?> getWorkTimeByUserAndProject(@RequestParam(name = "user") int userId,
                                                         @RequestParam(name = "project") int projectId,
                                                         @RequestParam(name = "first_day") String f,
                                                         @RequestParam(name = "second_day") String s) {
        Date first;
        try {
            first = Date.valueOf(f);
        }catch (Exception e){
            return new ResponseEntity<>("Incorrect date!", HttpStatus.BAD_REQUEST);
        }
        Date second;
        try {
            second = Date.valueOf(s);
        }catch (Exception e){
            return new ResponseEntity<>("Incorrect date!", HttpStatus.BAD_REQUEST);
        }
        if(userId < 1){
            return new ResponseEntity<>("Incorrect user id = " + userId, HttpStatus.BAD_REQUEST);
        }
        if(projectId < 1){
            return new ResponseEntity<>("Incorrect project id = " + projectId, HttpStatus.BAD_REQUEST);
        }
        User user = userService.read(userId);
        if(user == null){
            return new ResponseEntity<>("User not found", HttpStatus.BAD_REQUEST);
        }
        Project project = projectService.read(projectId);
        if(project == null){
            return new ResponseEntity<>("Project not found", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(workTimeService.getByUserAndProject(user, project, first, second), HttpStatus.OK);
    }
}
