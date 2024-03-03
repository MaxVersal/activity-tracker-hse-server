package com.activitytrackhse.service.impl;

import com.activitytrackhse.generic.service.impl.CrudServiceImpl;
import com.activitytrackhse.model.*;
import com.activitytrackhse.repository.UserRepository;
import com.activitytrackhse.service.inter.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class UserServiceImpl extends CrudServiceImpl<User, Integer, UserRepository> implements UserService {
    @Autowired
    public void setRepository(UserRepository repository){
        this.repository = repository;
    }

    @Override
    @Transactional
    public User getByNickname(String nickname) {
        return repository.findByNickname(nickname);
    }
}
