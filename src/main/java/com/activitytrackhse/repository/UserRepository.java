package com.activitytrackhse.repository;

import org.springframework.data.repository.CrudRepository;
import com.activitytrackhse.model.*;


public interface UserRepository extends CrudRepository<User, Integer> {
    User findByNickname(String nickname);
}
