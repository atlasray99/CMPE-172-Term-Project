package com.advisingscheduler.service;

import com.advisingscheduler.repository.ServiceRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ServiceService {

    private final ServiceRepository serviceRepository;

    public ServiceService(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    public List<com.advisingscheduler.model.Service> getAllServices() {
        return serviceRepository.findAll();
    }
}
