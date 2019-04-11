package com.backend.dao;

import java.util.List;

public interface RemoteDao {
    void remoteControl(String ptName,byte state, String companyId);
    void remoteControl(List<String> ptNames,byte state, String companyId);
}
