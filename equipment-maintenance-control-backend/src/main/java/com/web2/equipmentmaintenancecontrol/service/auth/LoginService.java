package com.web2.equipmentmaintenancecontrol.service.auth;

import com.web2.equipmentmaintenancecontrol.exception.AppException;
import com.web2.equipmentmaintenancecontrol.exception.ErrorCode;
import com.web2.equipmentmaintenancecontrol.model.auth.dtos.LoginRequest;
import com.web2.equipmentmaintenancecontrol.model.auth.dtos.LoginResponse;
import com.web2.equipmentmaintenancecontrol.model.equipment.Equipment;
import com.web2.equipmentmaintenancecontrol.model.equipment.EquipmentType;
import com.web2.equipmentmaintenancecontrol.model.equipment.dtos.EquipmentRequest;
import com.web2.equipmentmaintenancecontrol.repository.equipment.EquipmentRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    public LoginResponse execute(LoginRequest request) {
        return new LoginResponse(true);
    }
}
