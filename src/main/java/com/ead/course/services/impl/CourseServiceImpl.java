package com.ead.course.services.impl;

import com.ead.course.dtos.CourseRecordDto;
import com.ead.course.exceptions.NotFoundException;
import com.ead.course.models.CourseModel;
import com.ead.course.repositories.CourseRepository;
import com.ead.course.repositories.LessonRepository;
import com.ead.course.repositories.ModuleRepository;
import com.ead.course.services.CourseService;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

@Service
public class CourseServiceImpl implements CourseService {
    final CourseRepository repo;
    final ModuleRepository moduleRepository;
    final LessonRepository lessonRepository;

    public CourseServiceImpl(CourseRepository repo,  ModuleRepository moduleRepository,  LessonRepository lessonRepository) {
        this.repo = repo;
        this.moduleRepository = moduleRepository;
        this.lessonRepository = lessonRepository;
    }

    @Transactional
    @Override
    public void delete(CourseModel course) {
        var modules = moduleRepository.findAllModulesIntoCourse(course.getCourseId());
        if (!modules.isEmpty()) {
            modules.forEach(modulesModel -> {
                var lessons = lessonRepository.findAllLessonIntoModule(modulesModel.getModuleId());
                if (!lessons.isEmpty()) {
                    lessonRepository.deleteAll(lessons);
                }
            });
            moduleRepository.deleteAll(modules);
        }
        repo.delete(course);
    }

    @Override
    public CourseModel save(CourseRecordDto dto) {
        var courseModel = new CourseModel();
        BeanUtils.copyProperties(dto, courseModel);
        courseModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        courseModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        return repo.save(courseModel);
    }

    @Override
    public boolean existsByName(String name) {
        return repo.existsByName(name);
    }

    @Override
    public Page<CourseModel> findAll(Specification<CourseModel> spec, Pageable pageable) {
        return repo.findAll(spec,pageable);
    }

    @Override
    public Optional<CourseModel> findById(UUID courseId) {
        var course = repo.findById(courseId);
        if (course.isEmpty()) {
            throw new NotFoundException("Error: Course not found.");
        }
        return course;
    }

    @Override
    public CourseModel update(CourseRecordDto dto, CourseModel existing) {
        BeanUtils.copyProperties(dto, existing);
        existing.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        return repo.save(existing);
    }
}
