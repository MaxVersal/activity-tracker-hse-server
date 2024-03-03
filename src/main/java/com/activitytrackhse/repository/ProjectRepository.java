package com.activitytrackhse.repository;

import com.activitytrackhse.model.*;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProjectRepository extends CrudRepository<Project, Integer> {
    List<Project> findAllByOwner(User owner);
}
