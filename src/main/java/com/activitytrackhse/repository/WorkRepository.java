package com.activitytrackhse.repository;

import com.activitytrackhse.model.*;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.util.List;

public interface WorkRepository extends CrudRepository<Work, Integer> {
    @Modifying
    @Query(value = "insert into works (user_id, project_id, entry_date) values (:user, :project, :date)", nativeQuery = true)
    void insert(@Param("user") int user, @Param("project") int project, @Param("date") Date date);

    Work findByUserAndProject(User user, Project project);

    List<Work> findAllByProject(Project project);

    List<Work> findAllByUser(User user);
}
