package com.ead.course.services;

import com.ead.course.dtos.ModuleRecordDto;
import com.ead.course.models.CourseModel;
import com.ead.course.models.ModuleModel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ModuleService {

    void delete(ModuleModel module);

    ModuleModel save(CourseModel courseModel, ModuleRecordDto dto);

    List<ModuleModel> findAllModuleIntoCourse(UUID courseId);

    Optional<ModuleModel> findModuleIntoCourse(UUID courseId, UUID moduleId);

    ModuleModel update(ModuleRecordDto dto, ModuleModel moduleIntoCourse);

    Optional<ModuleModel> findById(UUID moduleId);
}
