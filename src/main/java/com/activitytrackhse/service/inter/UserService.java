package com.activitytrackhse.service.inter;

import com.activitytrackhse.generic.service.inter.CrudService;
import com.activitytrackhse.model.User;

public interface UserService extends CrudService<User, Integer> {
    User getByNickname(String nickname);
}