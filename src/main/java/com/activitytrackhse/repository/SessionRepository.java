package com.activitytrackhse.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.sql.Date;
import java.util.List;
import java.util.Map;
import com.activitytrackhse.model.*;


public interface SessionRepository extends CrudRepository<Session, Integer> {
    @Query(value = "select * from sessions" +
            " where user_id = :user and date = :date and project_id = :project",
            nativeQuery = true)
    List<Session> findAllByUserAndProjectAndDate(User user, Project project, Date date);
    List<Session> findAllByUserAndProjectAndDateGreaterThanEqualAndDateLessThanEqual(User user, Project project, Date date, Date date2);
    List<Session> findAllByUserAndDate(User user, Date date);
    List<Session> findAllByUserAndDateGreaterThanEqualAndDateLessThanEqual(User user, Date date, Date date2);
    List<Session> findAllByProjectAndDate(Project project, Date date);
    List<Session> findAllByProjectAndDateGreaterThanEqualAndDateLessThanEqual(Project project, Date date, Date date2);

    @Query(value = "select avg(average_activity) from session_parts sp" +
            " where sp.user_id = :user and sp.date <= :second and sp.date >= :first",
    nativeQuery = true)
    Double getActivityByUserAndPeriod(int user, Date first, Date second);

    @Query(value = "select avg(average_activity) from session_parts sp" +
            " where sp.user_id = :user and sp.project_id = :project",
            nativeQuery = true)
    Double getActivityByUserAndProject(int user, int project);

    @Query(value = "select avg(average_activity) from session_parts sp" +
            " where sp.project_id = :project",
            nativeQuery = true)
    Double getActivityByProject(int project);

    @Query(value = "select avg(average_activity) from session_parts sp" +
            " where sp.project_id = :project and sp.date <= :second and sp.date >= :first",
            nativeQuery = true)
    Double getActivityByProjectAndPeriod(int project, Date first, Date second);

    @Query(value = "select avg(average_activity) from session_parts sp" +
            " where sp.user_id = :user and sp.project_id = :project and sp.date <= :second and sp.date >= :first",
            nativeQuery = true)
    Double getActivityByUserAndProjectAndPeriod(int user, int project, Date first, Date second);

    @Query(value = "select date, sum(duration) work_time, avg(average_activity) activity from session_parts \n" +
            "where user_id = :user " +
            "and date <= :second and date >= :first \n" +
            "group by date order by date",
            nativeQuery = true)
    List<Map<String, Object>> getWorkTimeByUser(int user, Date first, Date second);

    @Query(value = "select date, sum(duration) work_time, avg(average_activity) activity from session_parts \n" +
            "where project_id = :project " +
            "and date <= :second and date >= :first \n" +
            "group by date order by date",
            nativeQuery = true)
    List<Map<String, Object>> getWorkTimeByProject(int project, Date first, Date second);

    @Query(value = "select date, sum(duration) work_time, avg(average_activity) activity from session_parts \n" +
            "where user_id = :user and project_id = :project " +
            "and date <= :second and date >= :first \n" +
            "group by date order by date",
            nativeQuery = true)
    List<Map<String, Object>> getWorkTimeByUserAndProject(int user, int project, Date first, Date second);
}
