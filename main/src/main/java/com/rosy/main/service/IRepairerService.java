package com.rosy.main.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.entity.Repairer;

public interface IRepairerService extends IService<Repairer> {

    Repairer findBestAvailableRepairer(String deviceType);

    void incrementWorkload(Long repairerId);

    void decrementWorkload(Long repairerId);
}
