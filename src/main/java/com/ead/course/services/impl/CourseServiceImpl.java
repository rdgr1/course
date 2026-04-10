package com.ead.course.services.impl;

import com.ead.course.client.AuthUserClient;
import com.ead.course.dtos.CourseRecordDto;
import com.ead.course.exceptions.NotFoundException;
import com.ead.course.models.CourseModel;
import com.ead.course.models.CourseUserModel;
import com.ead.course.repositories.CourseRepository;
import com.ead.course.repositories.CourseUserRepository;
import com.ead.course.repositories.LessonRepository;
import com.ead.course.repositories.ModuleRepository;
import com.ead.course.services.CourseService;
import com.ead.course.specifications.SpecificationTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CourseServiceImpl implements CourseService {
    final CourseRepository repo;
    final AuthUserClient authUserClient;
    final ModuleRepository moduleRepository;
    final CourseUserRepository courseUserRepository;
    final LessonRepository lessonRepository;

    public CourseServiceImpl(CourseRepository repo, AuthUserClient authUserClient, ModuleRepository moduleRepository, CourseUserRepository courseUserRepository, LessonRepository lessonRepository) {
        this.repo = repo;
        this.authUserClient = authUserClient;
        this.moduleRepository = moduleRepository;
        this.courseUserRepository = courseUserRepository;
        this.lessonRepository = lessonRepository;
    }

    @Transactional
    @Override
    public void delete(CourseModel course) {
        boolean deleteCourseUserInAuthUser = false;
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
        var courseUserModelList = courseUserRepository.findAllCourseUserIntoCourse(course.getCourseId());
        if (!courseUserModelList.isEmpty()){
            courseUserRepository.deleteAll(courseUserModelList);
            deleteCourseUserInAuthUser = true;
        }
        repo.delete(course);
        if (deleteCourseUserInAuthUser){
            authUserClient.deleteUserCourseByCourse(course.getCourseId());
        }
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
        List<CourseUserModel> courseUserModelList = courseUserRepository.findAllCourseUserIntoCourse(courseId);
        if (!courseUserModelList.isEmpty()){
            courseUserRepository.deleteAll(courseUserModelList);
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
