package com.web2.equipmentmaintenancecontrol.controller.profile;

import com.web2.equipmentmaintenancecontrol.config.AccessGuard;
import com.web2.equipmentmaintenancecontrol.config.RequiresProfile;
import com.web2.equipmentmaintenancecontrol.config.TokenInterceptor;
import com.web2.equipmentmaintenancecontrol.model.profile.ProfileType;
import com.web2.equipmentmaintenancecontrol.model.profile.dtos.EmployeeCreateRequest;
import com.web2.equipmentmaintenancecontrol.model.profile.dtos.EmployeeResponse;
import com.web2.equipmentmaintenancecontrol.model.profile.dtos.EmployeeUpdateRequest;
import com.web2.equipmentmaintenancecontrol.service.profile.EmployeeService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/employees")
@RequiresProfile(ProfileType.EMPLOYEE)
public class EmployeeController {

  private final EmployeeService service;

  public EmployeeController(EmployeeService service) {
    this.service = service;
  }

  @GetMapping
  public ResponseEntity<List<EmployeeResponse>> list() {
    return ResponseEntity.ok(service.findAll());
  }

  @GetMapping("/{id}")
  public ResponseEntity<EmployeeResponse> findById(@PathVariable Integer id) {
    return ResponseEntity.ok(service.findById(id));
  }

  @PostMapping
  public ResponseEntity<EmployeeResponse> create(
      @Valid @RequestBody EmployeeCreateRequest request) {
    EmployeeResponse employee = service.create(request);

    return ResponseEntity.status(HttpStatus.CREATED).body(employee);
  }

  @PutMapping("/{id}")
  public ResponseEntity<EmployeeResponse> update(
      @PathVariable Integer id, @Valid @RequestBody EmployeeUpdateRequest request) {
    return ResponseEntity.ok(service.update(id, request));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(
      @RequestAttribute(TokenInterceptor.USER_ID_ATTRIBUTE) Integer userId,
      @PathVariable Integer id,
      @RequestParam Integer currentEmployeeId) {
    AccessGuard.requireSameUser(userId, currentEmployeeId);

    service.delete(id, currentEmployeeId);

    return ResponseEntity.noContent().build();
  }
}
