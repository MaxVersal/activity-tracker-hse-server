package com.activitytrackhse.service.inter;

import com.activitytrackhse.model.SessionInfo;

import java.util.List;

public interface SessionInfoService {
    void saveSessionInfo(List<SessionInfo> info) throws IllegalArgumentException;
}
