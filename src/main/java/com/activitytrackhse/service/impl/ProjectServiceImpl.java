package com.activitytrackhse.service.impl;

import com.activitytrackhse.generic.service.impl.CrudServiceImpl;
import com.activitytrackhse.model.*;
import com.activitytrackhse.repository.ProjectRepository;
import com.activitytrackhse.service.inter.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class ProjectServiceImpl extends CrudServiceImpl<Project, Integer, ProjectRepository> implements ProjectService {
    @Autowired
    public void setRepository(ProjectRepository repository){
        this.repository = repository;
    }

    @Override
    @Transactional
    public List<Project> findAllByOwner(User owner) {
        return repository.findAllByOwner(owner);
    }
}
