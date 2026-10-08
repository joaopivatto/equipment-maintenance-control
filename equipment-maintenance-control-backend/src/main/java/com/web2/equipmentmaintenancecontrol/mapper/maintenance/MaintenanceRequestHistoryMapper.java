package com.web2.equipmentmaintenancecontrol.mapper.maintenance;

import com.web2.equipmentmaintenancecontrol.mapper.BaseMapper;
import com.web2.equipmentmaintenancecontrol.mapper.profile.EmployeeMapper;
import com.web2.equipmentmaintenancecontrol.model.maintenance.MaintenanceRequest;
import com.web2.equipmentmaintenancecontrol.model.maintenance.MaintenanceRequestHistory;
import com.web2.equipmentmaintenancecontrol.model.maintenance.MaintenanceRequestStatus;
import com.web2.equipmentmaintenancecontrol.model.maintenance.Redirect;
import com.web2.equipmentmaintenancecontrol.model.maintenance.dto.HistoryEntryDetails;
import com.web2.equipmentmaintenancecontrol.model.profile.dtos.EmployeeResponse;
import jakarta.annotation.Nullable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class MaintenanceRequestHistoryMapper extends BaseMapper {

  private final Comparator<LocalDateTime> BY_DATE_TIME =
      Comparator.nullsFirst(Comparator.naturalOrder());

  private final EmployeeMapper employeeMapper;

  public MaintenanceRequestHistoryMapper(EmployeeMapper employeeMapper) {
    this.employeeMapper = employeeMapper;
  }

  public List<HistoryEntryDetails> toDetails(MaintenanceRequest request) {
    if (request == null || request.getHistory() == null) {
      return List.of();
    }

    Map<MaintenanceRequestHistory, Redirect> redirectsByEntry = resolveRedirects(request);

    return request.getHistory().stream()
        .map(entry -> toDetails(entry, redirectsByEntry.get(entry)))
        .toList();
  }

  private HistoryEntryDetails toDetails(
      MaintenanceRequestHistory entity, @Nullable Redirect redirect) {
    if (entity == null) {
      return null;
    }

    return new HistoryEntryDetails(
        entity.getStatus(),
        formatDateTime(entity.getUpdatedAt()),
        employeeMapper.toResponse(entity.getEmployee()),
        entity.getReason(),
        redirect != null ? employeeMapper.toResponse(redirect.getSourceEmployee()) : null,
        resolveToEmployee(entity, redirect));
  }

  private Map<MaintenanceRequestHistory, Redirect> resolveRedirects(MaintenanceRequest request) {
    List<Redirect> redirects = request.getRedirects();
    if (redirects.isEmpty()) {
      return Map.of();
    }

    List<Redirect> orderedRedirects = new ArrayList<>(redirects);
    orderedRedirects.sort(Comparator.comparing(Redirect::getCreatedAt, BY_DATE_TIME));
    Iterator<Redirect> pendingRedirects = orderedRedirects.iterator();

    Map<MaintenanceRequestHistory, Redirect> redirectsByEntry = new IdentityHashMap<>();
    for (MaintenanceRequestHistory entry : orderedHistory(request)) {
      if (entry.getStatus() != MaintenanceRequestStatus.REDIRECIONADA) {
        continue;
      }
      if (!pendingRedirects.hasNext()) {
        break;
      }
      redirectsByEntry.put(entry, pendingRedirects.next());
    }

    return redirectsByEntry;
  }

  private List<MaintenanceRequestHistory> orderedHistory(MaintenanceRequest request) {
    List<MaintenanceRequestHistory> entries = new ArrayList<>();
    for (MaintenanceRequestHistory entry : request.getHistory()) {
      if (entry != null) {
        entries.add(entry);
      }
    }
    entries.sort(Comparator.comparing(MaintenanceRequestHistory::getUpdatedAt, BY_DATE_TIME));
    return entries;
  }

  private EmployeeResponse resolveToEmployee(
      MaintenanceRequestHistory entity, @Nullable Redirect redirect) {
    if (redirect != null) {
      return employeeMapper.toResponse(redirect.getDestinationEmployee());
    }
    if (entity.getStatus() == MaintenanceRequestStatus.REDIRECIONADA) {
      return employeeMapper.toResponse(entity.getEmployee());
    }
    return null;
  }
}
