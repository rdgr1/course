package com.ead.course.services.impl;

import com.ead.course.dtos.ModuleRecordDto;
import com.ead.course.exceptions.NotFoundException;
import com.ead.course.models.CourseModel;
import com.ead.course.models.ModuleModel;
import com.ead.course.repositories.LessonRepository;
import com.ead.course.repositories.ModuleRepository;
import com.ead.course.services.ModuleService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ModuleServiceImpl implements ModuleService {
    final ModuleRepository repo;
    final LessonRepository lessonRepo;
    final CourseServiceImpl courseService;


    public ModuleServiceImpl(ModuleRepository repo, LessonRepository lessonRepository, CourseServiceImpl courseService) {
        this.repo = repo;
        this.lessonRepo = lessonRepository;
        this.courseService = courseService;
    }


    @Transactional
    @Override
    public void delete(ModuleModel module) {
        var lessons = lessonRepo.findAllLessonIntoModule(module.getModuleId());
        if (!lessons.isEmpty()) {
            lessonRepo.deleteAll(lessons);
        }
        repo.delete(module);
    }

    @Override
    public ModuleModel save(CourseModel courseModel, ModuleRecordDto dto) {
        var moduleModel = new ModuleModel();
        BeanUtils.copyProperties(dto, moduleModel);
        moduleModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        moduleModel.setCourse(courseModel);
        return repo.save(moduleModel);
    }

    @Override
    public List<ModuleModel> findAllModuleIntoCourse(UUID courseId) {
        return repo.findAllModulesIntoCourse(courseId);
    }

    @Override
    public Optional<ModuleModel> findModuleIntoCourse(UUID courseId, UUID moduleId) {
        var optionalModule = repo.findModuleIntoCourse(courseId, moduleId);
        if (optionalModule.isEmpty()) {
            throw new NotFoundException("Error: Module not found.");
        }
        return optionalModule;
    }

    @Override
    public ModuleModel update(ModuleRecordDto dto, ModuleModel moduleIntoCourse) {
        BeanUtils.copyProperties(dto, moduleIntoCourse);
        return repo.save(moduleIntoCourse);
    }

    @Override
    public Optional<ModuleModel> findById(UUID moduleId) {
        var optionalModule = repo.findById(moduleId);
        if (optionalModule.isEmpty()) {
            throw new NotFoundException("Error: Module not found.");
        }
        return repo.findById(moduleId);
    }
}
