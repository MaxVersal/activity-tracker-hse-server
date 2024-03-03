package com.activitytrackhse.service.impl;

import com.activitytrackhse.model.*;
import com.activitytrackhse.repository.SessionRepository;
import com.activitytrackhse.service.inter.WorkTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Date;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Service
public class WorkTimeServiceImpl implements WorkTimeService {
    private SessionRepository sessionRepository;

    public WorkTimeServiceImpl(@Autowired SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @Override
    @Transactional
    public WorkTimeInfo getByUser(User user, Date first, Date second) {
        List<Map<String, Object>> data = sessionRepository.getWorkTimeByUser(user.getId(), first, second);
        WorkTimeInfo workTimeInfo = getInfo(data, first, second);
        Double r = sessionRepository.getActivityByUserAndPeriod(user.getId(), first, second);
        workTimeInfo.setTotalActivity(r == null ? 0 : r);
        return workTimeInfo;
    }

    @Override
    @Transactional
    public WorkTimeInfo getByProject(Project project, Date first, Date second) {
        List<Map<String, Object>> data = sessionRepository.getWorkTimeByProject(project.getId(), first, second);
        WorkTimeInfo workTimeInfo = getInfo(data, first, second);
        Double r = sessionRepository.getActivityByProjectAndPeriod(project.getId(), first, second);
        workTimeInfo.setTotalActivity(r == null ? 0 : r);
        return workTimeInfo;
    }

    @Override
    @Transactional
    public WorkTimeInfo getByUserAndProject(User user, Project project, Date first, Date second) {
        List<Map<String, Object>> data = sessionRepository.getWorkTimeByUserAndProject(user.getId(), project.getId(), first, second);
        WorkTimeInfo workTimeInfo = getInfo(data, first, second);
        Double r = sessionRepository.getActivityByUserAndProjectAndPeriod(user.getId(), project.getId(), first, second);
        workTimeInfo.setTotalActivity(r == null ? 0 : r);
        return workTimeInfo;
    }

    private WorkTimeInfo getInfo(List<Map<String, Object>> data, Date first, Date second){
        WorkTimeInfo workTimeInfo = new WorkTimeInfo();
        List<DateTime> times = new LinkedList<>();
        LocalDate current = first.toLocalDate();
        for(Map<String, Object> d: data){
            DateTime dt = DateTime.get(d);
            while (current.isBefore(dt.getDate().toLocalDate())){
                times.add(new DateTime(Date.valueOf(current), 0, 0));
                current = current.plusDays(1);
            }
            times.add(dt);
            current = current.plusDays(1);
        }
        LocalDate end = second.toLocalDate();
        while (current.isBefore(end)){
            times.add(new DateTime(Date.valueOf(current), 0, 0));
            current = current.plusDays(1);
        }
        times.add(new DateTime(Date.valueOf(current), 0, 0));
        workTimeInfo.setAllDays(times);
        workTimeInfo.setTotal(times.stream().mapToInt(DateTime::getWorkTime).sum());
        LocalDate last = LocalDate.now().minusWeeks(1);
        while (!last.getDayOfWeek().equals(DayOfWeek.SUNDAY)){
            last = last.plusDays(1);
        }
        LocalDate l = last;
        workTimeInfo.setLastWeek(times.stream().filter(x -> x.getDate().toLocalDate().isAfter(l)).mapToInt(DateTime::getWorkTime).sum());
        Double r = times.stream().filter(x -> x.getDate().toLocalDate().isAfter(l)).mapToDouble(x -> x.getWorkTime() * x.getActivity()).sum() / workTimeInfo.getLastWeek();
        workTimeInfo.setLastWeekActivity(Double.isNaN(r) ? 0 : r);
        return workTimeInfo;
    }
}
