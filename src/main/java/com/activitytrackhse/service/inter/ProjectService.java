package com.activitytrackhse.service.inter;

import com.activitytrackhse.generic.service.inter.CrudService;
import com.activitytrackhse.model.Project;
import com.activitytrackhse.model.User;

import java.util.List;

public interface ProjectService extends CrudService<Project, Integer> {
    List<Project> findAllByOwner(User owner);
}
