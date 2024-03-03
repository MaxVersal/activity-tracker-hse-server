package com.activitytrackhse.service.inter;

import com.activitytrackhse.generic.service.inter.CrudService;
import com.activitytrackhse.model.*;

import java.sql.Date;
import java.util.List;

public interface SessionService extends CrudService<Session, Integer> {
    List<Session> findAllByUserAndProjectAndDate(User user, Project project, Date date);
    List<Session> findAllByUserAndProjectAndPeriod(User user, Project project, Date first, Date second);
    List<Session> findAllByUserAndDate(User user, Date date);
    List<Session> findAllByUserAndPeriod(User user, Date first, Date second);
    List<Session> findAllByProjectAndDate(Project project, Date date);
    List<Session> findAllByProjectAndPeriod(Project project, Date first, Date second);
    double getActivityByUserAndPeriod(int user, Date first, Date second);
    double getActivityByUserAndProject(int user, int project);
    double getActivityByProject(int project);
    double getActivityByUserAndProjectAndPeriod(int user, int project, Date first, Date second);
}
