package com.ead.course.services.impl;

import com.ead.course.models.ModuleModel;
import com.ead.course.repositories.LessonRepository;
import com.ead.course.repositories.ModuleRepository;
import com.ead.course.services.ModuleService;
import org.springframework.stereotype.Service;

@Service
public class ModuleServiceImpl implements ModuleService {
    final ModuleRepository repo;
    final LessonRepository lessonRepo;

    public ModuleServiceImpl(ModuleRepository repo, LessonRepository lessonRepository) {
        this.repo = repo;
        this.lessonRepo = lessonRepository;
    }

    @Override
    public void delete(ModuleModel module) {
        var lessons = lessonRepo.findAllLessonIntoModule(module.getModuleId());
        if (!lessons.isEmpty()){
            lessonRepo.deleteAll(lessons);
        }
        repo.delete(module);
    }
}
