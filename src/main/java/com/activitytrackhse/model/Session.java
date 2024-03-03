package com.activitytrackhse.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Time;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Data
@Entity
@Table(name = "sessions")
public class Session {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @Basic
    @Column(name = "date", nullable = false)
    private Date date;

    @Basic
    @Column(name = "start_time", nullable = false)
    private Time startTime;

    @Basic
    @Column(name = "end_time", nullable = false)
    private Time endTime;

    @Basic
    @Column(name = "duration", nullable = false)
    private int duration;

    @Basic
    @Column(name = "average_activity", nullable = false, precision = 0)
    private BigInteger averageActivity;

    @OneToMany(mappedBy = "session")
    private List<Screenshot> screenshots;

    @OneToMany(mappedBy = "session")
    private List<SessionPart> sessionParts;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Session session = (Session) o;

        if (id != session.id) return false;
        if (!Objects.equals(user, session.user)) return false;
        if (!Objects.equals(project, session.project)) return false;
        if (duration != session.duration) return false;
        if (!Objects.equals(date, session.date)) return false;
        if (!Objects.equals(startTime, session.startTime)) return false;
        if (!Objects.equals(endTime, session.endTime)) return false;
        if (!Objects.equals(averageActivity, session.averageActivity))
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (user != null ? user.hashCode() : 0);
        result = 31 * result + (project != null ? project.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (startTime != null ? startTime.hashCode() : 0);
        result = 31 * result + (endTime != null ? endTime.hashCode() : 0);
        result = 31 * result + duration;
        result = 31 * result + (averageActivity != null ? averageActivity.hashCode() : 0);
        return result;
    }

    @Override
    public String toString(){
        return "session " + id;
    }
}
