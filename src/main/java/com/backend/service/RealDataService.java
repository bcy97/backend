package com.backend.service;

import java.util.List;

public interface RealDataService {
    Object[] getRealData(int[] ids);

    List<String> getPicList();
}
