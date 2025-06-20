package com.epam.rd.autocode.assessment.appliances.service.impl;

import com.epam.rd.autocode.assessment.appliances.model.Appliance;
import com.epam.rd.autocode.assessment.appliances.repository.ApplianceRepository;
import com.epam.rd.autocode.assessment.appliances.service.ApplianceService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApplianceServiceImpl implements ApplianceService {

    @Override
    public Appliance create(Appliance appliance) {
        return null;
    }

    @Override
    public Appliance update(Long id, Appliance appliance) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public Appliance findById(Long id) {
        return null;
    }

    @Override
    public List<Appliance> findAll() {
        return List.of();
    }
}