package com.epam.rd.autocode.assessment.appliances.service.impl;

import com.epam.rd.autocode.assessment.appliances.model.Appliance;
import com.epam.rd.autocode.assessment.appliances.repository.ApplianceRepository;
import com.epam.rd.autocode.assessment.appliances.service.ApplianceService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApplianceServiceImpl implements ApplianceService {

    private final ApplianceRepository repository;

    public ApplianceServiceImpl(ApplianceRepository repository) {
        this.repository = repository;
    }

    @Override
    public Appliance create(Appliance appliance) {
        return repository.save(appliance);
    }

    @Override
    public Appliance update(Long id, Appliance appliance) {
        Appliance existing = repository.findById(id).orElseThrow();
        appliance.setId(id);
        return repository.save(appliance);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Appliance findById(Long id) {
        return repository.findById(id).orElseThrow();
    }

    @Override
    public List<Appliance> findAll() {
        return repository.findAll();
    }
}