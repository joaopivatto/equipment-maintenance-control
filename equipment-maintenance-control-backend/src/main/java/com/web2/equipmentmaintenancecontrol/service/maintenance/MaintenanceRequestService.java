package com.web2.equipmentmaintenancecontrol.service.maintenance;

import com.web2.equipmentmaintenancecontrol.model.maintenance.MaintenanceRequest;
import com.web2.equipmentmaintenancecontrol.model.maintenance.MaintenanceRequestStatus;
import com.web2.equipmentmaintenancecontrol.model.maintenance.dtos.CreateBudgetDTO;
import com.web2.equipmentmaintenancecontrol.model.maintenance.dtos.MaintenanceRequestResponseDTO;
import com.web2.equipmentmaintenancecontrol.repository.maintenance.MaintenanceRequestRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MaintenanceRequestService {

    private final MaintenanceRequestRepository repository;

    // Construtor explícito em Java padrão
    public MaintenanceRequestService(MaintenanceRequestRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<MaintenanceRequestResponseDTO> findOpenRequests() {
        return repository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MaintenanceRequestResponseDTO> findAll() {
        return repository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public MaintenanceRequestResponseDTO findById(Integer id) {
        MaintenanceRequest entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Solicitação não encontrada com ID: " + id));
        return toDTO(entity);
    }

    @Transactional
    public MaintenanceRequestResponseDTO createBudget(Integer id, CreateBudgetDTO dto) {
        MaintenanceRequest request = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Solicitação não encontrada com ID: " + id));

        MaintenanceRequest saved = repository.save(request);
        return toDTO(saved);
    }

    // Mapeia a Entidade para o Record usando os métodos exatos (getId, getDefect,
    // etc.)
    private MaintenanceRequestResponseDTO toDTO(MaintenanceRequest entity) {
        Long idAsLong = entity.getId() != null ? entity.getId().longValue() : null;

        return new MaintenanceRequestResponseDTO(
                idAsLong,
                entity.getCreatedAt(),
                null, // customerName
                null, // customerId
                entity.getDefect(), // campo correto da sua entidade
                null, // categoryName
                MaintenanceRequestStatus.ABERTA, // enum correto do seu projeto
                null, // budgetValue
                null // justification
        );
    }
}
