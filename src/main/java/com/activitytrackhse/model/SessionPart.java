package com.activitytrackhse.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Time;
import java.util.Objects;

@Data
@Entity
@Table(name = "session_parts", schema = "public", catalog = "activitytracking")
public class SessionPart {

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id")
    @JsonIgnore
    private Session session;

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
    @Column(name = "mouse_click", nullable = false)
    private int mouseClick;

    @Basic
    @Column(name = "mouse_move", nullable = false)
    private int mouseMove;

    @Basic
    @Column(name = "key_click", nullable = false)
    private int keyClick;

    @Basic
    @Column(name = "average_activity", nullable = false, precision = 0)
    private BigInteger averageActivity;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SessionPart that = (SessionPart) o;

        if (id != that.id) return false;
        if (!Objects.equals(user, that.user)) return false;
        if (!Objects.equals(project, that.project)) return false;
        if (!Objects.equals(session, that.session)) return false;
        if (duration != that.duration) return false;
        if (mouseClick != that.mouseClick) return false;
        if (mouseMove != that.mouseMove) return false;
        if (keyClick != that.keyClick) return false;
        if (!Objects.equals(date, that.date)) return false;
        if (!Objects.equals(startTime, that.startTime)) return false;
        if (!Objects.equals(endTime, that.endTime)) return false;
        if (!Objects.equals(averageActivity, that.averageActivity))
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (user != null ? user.hashCode() : 0);
        result = 31 * result + (project != null ? project.hashCode() : 0);
        result = 31 * result + (session != null ? session.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (startTime != null ? startTime.hashCode() : 0);
        result = 31 * result + (endTime != null ? endTime.hashCode() : 0);
        result = 31 * result + duration;
        result = 31 * result + mouseClick;
        result = 31 * result + mouseMove;
        result = 31 * result + keyClick;
        result = 31 * result + (averageActivity != null ? averageActivity.hashCode() : 0);
        return result;
    }

    @Override
    public String toString(){
        return "session part " + id;
    }
}
