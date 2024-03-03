package com.activitytrackhse.service.impl;


import com.activitytrackhse.generic.service.impl.CrudServiceImpl;
import com.activitytrackhse.model.*;
import com.activitytrackhse.repository.WorkRepository;
import com.activitytrackhse.service.inter.WorkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Date;
import java.util.List;

@Service
public class WorkServiceImpl extends CrudServiceImpl<Work, Integer, WorkRepository> implements WorkService {
    @Autowired
    public void setRepository(WorkRepository repository){
        this.repository = repository;
    }

    @Override
    @Transactional
    public void insert(int user, int project, Date date) {
        repository.insert(user, project, date);
    }

    @Override
    @Transactional
    public Work findByUserAndProject(User user, Project project) {
        return repository.findByUserAndProject(user, project);
    }

    @Override
    @Transactional
    public List<Work> findAllByProject(Project project) {
        return repository.findAllByProject(project);
    }

    @Override
    @Transactional
    public List<Work> findAllByUser(User user) {
        return repository.findAllByUser(user);
    }
}
