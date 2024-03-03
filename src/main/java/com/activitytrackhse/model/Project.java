package com.activitytrackhse.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;
import java.util.Objects;

@Data
@Entity
@Table(name="projects")
public class Project {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private int id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "owner_id")
    private User owner;

    @Basic
    @Column(name = "creation_date", nullable = true)
    private Date creationDate;

    @Basic
    @Column(name = "name", nullable = false, length = -1)
    private String name;

    @Basic
    @Column(name = "description", nullable = true, length = -1)
    private String description;

    @Basic
    @Column(name = "session_part_interval", nullable = false)
    private int sessionPartInterval;

    @Basic
    @Column(name = "screenshot_interval", nullable = false)
    private int screenshotInterval;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "project")
    @JsonIgnore
    private List<Screenshot> screenshots;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "project")
    @JsonIgnore
    private List<Session> sessions;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "project")
    @JsonIgnore
    private List<SessionPart> sessionParts;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "project")
    @JsonIgnore
    private List<Work> works;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Project project = (Project) o;

        if (id != project.id) return false;
        if (sessionPartInterval != project.sessionPartInterval) return false;
        if (screenshotInterval != project.screenshotInterval) return false;
        if (!Objects.equals(owner, project.owner)) return false;
        if (!Objects.equals(creationDate, project.creationDate))
            return false;
        if (!Objects.equals(name, project.name)) return false;
        if (!Objects.equals(description, project.description))
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (owner != null ? owner.hashCode() : 0);
        result = 31 * result + (creationDate != null ? creationDate.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + sessionPartInterval;
        result = 31 * result + screenshotInterval;
        return result;
    }

    public void setNewInfo(Project another){
        setName(another.getName());
        setDescription(another.getDescription());
        setScreenshotInterval(another.getScreenshotInterval());
        setSessionPartInterval(another.getSessionPartInterval());
    }

    @Override
    public String toString(){
        return "project " + id;
    }
}
