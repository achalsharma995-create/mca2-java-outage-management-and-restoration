package com.omrs.service;

import com.omrs.dto.progress.ProgressUpdateRequest;
import com.omrs.dto.progress.ProgressUpdateResponse;

import java.util.List;

public interface RestorationProgressService {
    ProgressUpdateResponse addProgressUpdate(ProgressUpdateRequest request, Long engineerUserId);
    List<ProgressUpdateResponse> getTimelineForOutage(Long outageId);
    List<ProgressUpdateResponse> getProgressForWorkOrder(Long workOrderId);
}
