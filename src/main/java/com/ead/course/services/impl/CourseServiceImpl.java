package com.ead.course.services.impl;

import com.ead.course.models.CourseModel;
import com.ead.course.repositories.CourseRepository;
import com.ead.course.repositories.LessonRepository;
import com.ead.course.repositories.ModuleRepository;
import com.ead.course.services.CourseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CourseServiceImpl implements CourseService {
    final CourseRepository repo;
    final ModuleRepository moduleRepository;

    final LessonRepository lessonRepository;

    public CourseServiceImpl(CourseRepository repo, ModuleRepository moduleRepository, LessonRepository lessonRepository) {
        this.repo = repo;
        this.moduleRepository = moduleRepository;
        this.lessonRepository = lessonRepository;
    }

    @Transactional
    @Override
    public void delete(CourseModel courseModel) {
        var modules = moduleRepository.findAllModulesIntoCourse(courseModel.getCourseId());
        if (!modules.isEmpty()){
            modules.forEach(
                    modulesModel -> {
                        var lessons = lessonRepository.findAllLessonIntoModule(modulesModel.getModuleId());
                        if (!lessons.isEmpty()){
                            lessonRepository.deleteAll(lessons);
                        }
                    }
            );
            moduleRepository.deleteAll(modules);
        }
        repo.delete(courseModel);
    }
}
