package com.activitytrackhse.service.impl;

import com.activitytrackhse.generic.service.impl.CrudServiceImpl;
import com.activitytrackhse.model.Screenshot;
import com.activitytrackhse.repository.ScreenshotRepository;
import com.activitytrackhse.service.inter.ScreenshotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScreenshotServiceImpl extends CrudServiceImpl<Screenshot, Integer, ScreenshotRepository> implements ScreenshotService {
    @Autowired
    public void setRepository(ScreenshotRepository repository){
        this.repository = repository;
    }
}
