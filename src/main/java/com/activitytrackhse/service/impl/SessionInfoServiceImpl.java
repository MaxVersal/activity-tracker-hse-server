package com.activitytrackhse.service.impl;

import com.activitytrackhse.model.*;
import com.activitytrackhse.repository.*;
import com.activitytrackhse.service.inter.SessionInfoService;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class SessionInfoServiceImpl implements SessionInfoService {
    private SessionRepository sessionRepository;
    private SessionPartRepository sessionPartRepository;
    private ScreenshotRepository screenshotRepository;
    private UserRepository userRepository;
    private ProjectRepository projectRepository;

    public SessionInfoServiceImpl(@Autowired SessionRepository sessionRepository,
                                  @Autowired SessionPartRepository sessionPartRepository,
                                  @Autowired ScreenshotRepository screenshotRepository,
                                  @Autowired UserRepository userRepository,
                                  @Autowired ProjectRepository projectRepository) {
        this.sessionRepository = sessionRepository;
        this.sessionPartRepository = sessionPartRepository;
        this.screenshotRepository = screenshotRepository;
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
    }

    @Override
    @Transactional
    public void saveSessionInfo(List<SessionInfo> sessionInfoList) throws IllegalArgumentException {
        for(SessionInfo info: sessionInfoList){
            Optional<User> user = userRepository.findById(info.getUserId());
            if(user.isEmpty()){
                throw new IllegalArgumentException("User with id = " + info.getUserId() + " not found!");
            }
            Optional<Project> project = projectRepository.findById(info.getProjectId());
            if(project.isEmpty()){
                throw new IllegalArgumentException("Project with id = " + info.getProjectId() + " not found!");
            }
            Session s = info.getSession();
            s.setUser((user.get()));
            s.setProject((project.get()));
            s.setId(0);
            Session session;
            try{
                session = sessionRepository.save(s);
            }catch(DataIntegrityViolationException ex) {
                ConstraintViolationException e = (ConstraintViolationException) ex.getCause();
                String message;
                if (e.getConstraintName().equals("session_time_check")) {
                    message = "End time less then start time for session!";
                } else if (e.getConstraintName().equals("session_duration_check")) {
                    message = "Duration less then zero for session!";
                } else if (e.getConstraintName().equals("session_average_activity_check")) {
                    message = "Incorrect average activity for session!";
                } else {
                    message = e.getSQLException().getMessage();
                }
                throw new IllegalArgumentException(message);
            }
            List<SessionPart> parts = info.getSessionParts();
            parts.forEach(x -> {
                x.setUser((user.get()));
                x.setProject((project.get()));
                x.setSession(session);
                x.setId(0);
            });
            try {
                sessionPartRepository.saveAll(parts);
            } catch(DataIntegrityViolationException ex) {
                ConstraintViolationException e = (ConstraintViolationException) ex.getCause();
                String message;
                if (e.getConstraintName().equals("session_part_time_check")) {
                    message = "End time less then start time for session part!";
                } else if (e.getConstraintName().equals("session_part_duration_check")) {
                    message = "Duration less then zero for session part!";
                } else if (e.getConstraintName().equals("session_part_average_activity_check")) {
                    message = "Incorrect average activity for session part!";
                } else if (e.getConstraintName().equals("session_part_key_click_check")) {
                    message = "Key click number less then zero for session part!";
                } else if (e.getConstraintName().equals("session_part_mouse_click_check")) {
                    message = "Mouse click number less then zero for session part!";
                } else if (e.getConstraintName().equals("session_part_mouse_move_check")) {
                    message = "Mouse move number less then zero for session part!";
                } else {
                    message = e.getSQLException().getMessage();
                }
                throw new IllegalArgumentException(message);
            }
            List<Screenshot> screenshots = info.getScreenshots();
            screenshots.forEach(x -> {
                x.setUser(user.get());
                x.setProject(project.get());
                x.setSession(session);
                x.setId(0);
            });
            try{
                screenshotRepository.saveAll(screenshots);
            }catch(DataIntegrityViolationException ex) {
                ConstraintViolationException e = (ConstraintViolationException) ex.getCause();
                String message = e.getSQLException().getMessage();
                throw new IllegalArgumentException(message);
            }
        }
    }
}
