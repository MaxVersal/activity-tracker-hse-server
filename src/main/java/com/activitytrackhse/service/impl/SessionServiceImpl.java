package com.activitytrackhse.service.impl;

import com.activitytrackhse.generic.service.impl.CrudServiceImpl;
import com.activitytrackhse.repository.SessionRepository;
import com.activitytrackhse.service.inter.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.activitytrackhse.model.*;


import javax.transaction.Transactional;
import java.sql.Date;
import java.util.List;

@Service
public class SessionServiceImpl extends CrudServiceImpl<Session, Integer, SessionRepository> implements SessionService {
    @Autowired
    public void setRepository(SessionRepository repository){
        this.repository = repository;
    }

    @Override
    @Transactional
    public List<Session> findAllByUserAndProjectAndDate(User user, Project project, Date date) {
        return repository.findAllByUserAndProjectAndDate(user, project, date);
    }

    @Override
    public List<Session> findAllByUserAndProjectAndPeriod(User user, Project project, Date first, Date second) {
        return repository.findAllByUserAndProjectAndDateGreaterThanEqualAndDateLessThanEqual(user, project, first, second);
    }

    @Override
    @Transactional
    public List<Session> findAllByUserAndDate(User user, Date date) {
        return repository.findAllByUserAndDate(user, date);
    }

    @Override
    public List<Session> findAllByUserAndPeriod(User user, Date first, Date second) {
        return repository.findAllByUserAndDateGreaterThanEqualAndDateLessThanEqual(user, first, second);
    }

    @Override
    @Transactional
    public List<Session> findAllByProjectAndDate(Project project, Date date) {
        return repository.findAllByProjectAndDate(project, date);
    }

    @Override
    public List<Session> findAllByProjectAndPeriod(Project project, Date first, Date second) {
        return repository.findAllByProjectAndDateGreaterThanEqualAndDateLessThanEqual(project, first, second);
    }

    @Override
    @Transactional
    public double getActivityByUserAndPeriod(int user, Date first, Date second) {
        Double res = repository.getActivityByUserAndPeriod(user, first, second);
        return res == null ? 0 : res;
    }

    @Override
    @Transactional
    public double getActivityByUserAndProject(int user, int project) {
        Double res = repository.getActivityByUserAndProject(user, project);
        return res == null ? 0 : res;
    }

    @Override
    @Transactional
    public double getActivityByProject(int project) {
        Double res = repository.getActivityByProject(project);
        return res == null ? 0 : res;
    }

    @Override
    @Transactional
    public double getActivityByUserAndProjectAndPeriod(int user, int project, Date first, Date second) {
        Double res = repository.getActivityByUserAndProjectAndPeriod(user, project, first, second);
        return res == null ? 0 : res;
    }
}
