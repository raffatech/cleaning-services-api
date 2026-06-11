package com.example.cleaningservices.infrastructure.adapters.outbound.persistence;

import com.example.cleaningservices.application.ports.outbound.EmitterRepositoryPort;
import com.example.cleaningservices.domain.model.Emitter;
import com.example.cleaningservices.infrastructure.adapters.outbound.persistence.entity.EmitterEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class EmitterRepositoryAdapter implements EmitterRepositoryPort {

    private final EmitterJpaRepository emitterJpaRepository;

    public EmitterRepositoryAdapter(EmitterJpaRepository emitterJpaRepository) {
        this.emitterJpaRepository = emitterJpaRepository;
    }

    // converte domínio → entidade JPA
    private EmitterEntity convertToEntity(Emitter emitter) {
        EmitterEntity entity = new EmitterEntity();
        entity.setId(emitter.getId());
        entity.setCompanyName(emitter.getCompanyName());
        entity.setSignerName(emitter.getSignerName());
        entity.setCpfCnpj(emitter.getCpfCnpj());
        entity.setTemplatePath(emitter.getTemplatePath());
        entity.setMunicipalRegistration(emitter.getMunicipalRegistration());
        entity.setPhone(emitter.getPhone());
        entity.setEmail(emitter.getEmail());
        entity.setAddress(emitter.getAddress());
        entity.setLogoPath(emitter.getLogoPath());
        return entity;
    }

    private Emitter convertToDomain(EmitterEntity entity) {
        Emitter emitter = new Emitter(
                entity.getId(),
                entity.getCompanyName(),
                entity.getSignerName(),
                entity.getCpfCnpj(),
                entity.getTemplatePath(),
                entity.getMunicipalRegistration()
        );
        emitter.setPhone(entity.getPhone());
        emitter.setEmail(entity.getEmail());
        emitter.setAddress(entity.getAddress());
        emitter.setLogoPath(entity.getLogoPath());
        return emitter;
    }

    @Override
    public Emitter save(Emitter emitter) {
        EmitterEntity entity = convertToEntity(emitter);
        EmitterEntity saved = emitterJpaRepository.save(entity);
        return convertToDomain(saved);
    }

    @Override
    public List<Emitter> findAll() {
        return emitterJpaRepository.findAll()
                .stream()
                .map(this::convertToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Emitter> findById(Long id) {
        return emitterJpaRepository.findById(id).map(this::convertToDomain);
    }

    @Override
    public void deleteById(Long id) {
        emitterJpaRepository.deleteById(id);
    }

    @Override
    public boolean existsByCpfCnpj(String cpfCnpj) {
        return emitterJpaRepository.existsByCpfCnpj(cpfCnpj);
    }
}
