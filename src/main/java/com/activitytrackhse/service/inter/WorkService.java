package com.activitytrackhse.service.inter;

import com.activitytrackhse.generic.service.inter.CrudService;
import com.activitytrackhse.model.*;

import java.sql.Date;
import java.util.List;

public interface WorkService extends CrudService<Work, Integer> {
    void insert(int user, int project, Date date);
    Work findByUserAndProject(User user, Project project);
    List<Work> findAllByProject(Project project);
    List<Work> findAllByUser(User user);
}
