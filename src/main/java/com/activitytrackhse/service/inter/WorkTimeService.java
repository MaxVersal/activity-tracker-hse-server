package com.activitytrackhse.service.inter;

import com.activitytrackhse.model.*;

import java.sql.Date;

public interface WorkTimeService {
    WorkTimeInfo getByUser(User user, Date first, Date second);
    WorkTimeInfo getByProject(Project project, Date first, Date second);
    WorkTimeInfo getByUserAndProject(User user, Project project, Date first, Date second);
}
