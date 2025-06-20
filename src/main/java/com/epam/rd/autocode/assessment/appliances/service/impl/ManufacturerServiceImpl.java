package com.epam.rd.autocode.assessment.appliances.service.impl;

import com.epam.rd.autocode.assessment.appliances.model.Manufacturer;
import com.epam.rd.autocode.assessment.appliances.repository.ManufacturerRepository;
import com.epam.rd.autocode.assessment.appliances.service.ManufacturerService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ManufacturerServiceImpl implements ManufacturerService {

    @Override
    public List<Manufacturer> findAll() {
        return List.of();
    }

    @Override
    public Manufacturer findById(Long id) {
        return null;
    }

    @Override
    public Manufacturer save(Manufacturer manufacturer) {
        return null;
    }

    @Override
    public void deleteById(Long id) {

    }
}