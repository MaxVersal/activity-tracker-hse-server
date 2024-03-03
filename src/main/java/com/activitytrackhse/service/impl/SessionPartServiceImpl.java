package com.activitytrackhse.service.impl;

import com.activitytrackhse.generic.service.impl.CrudServiceImpl;
import com.activitytrackhse.repository.SessionPartRepository;
import com.activitytrackhse.service.inter.SessionPartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.activitytrackhse.model.*;


@Service
public class SessionPartServiceImpl extends CrudServiceImpl<SessionPart, Integer, SessionPartRepository> implements SessionPartService {
    @Autowired
    public void setRepository(SessionPartRepository repository){
        this.repository = repository;
    }
}
