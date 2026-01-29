package com.ead.course.services.impl;

import com.ead.course.dtos.LessonRecordDto;
import com.ead.course.exceptions.NotFoundException;
import com.ead.course.models.LessonModel;
import com.ead.course.models.ModuleModel;
import com.ead.course.repositories.LessonRepository;
import com.ead.course.services.LessonService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class LessonServiceImpl implements LessonService {
    final LessonRepository repo;

    public LessonServiceImpl(LessonRepository repo) {
        this.repo = repo;
    }

    @Override
    public void delete(LessonModel lesson) {
        repo.delete(lesson);
    }

    @Override
    public LessonModel save(LessonRecordDto dto, ModuleModel moduleModel) {
        var lessonModel = new LessonModel();
        BeanUtils.copyProperties(dto, lessonModel);
        lessonModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        lessonModel.setModule(moduleModel);
        return repo.save(lessonModel);
    }

    @Override
    public List<LessonModel> findAllLessonsIntoModule(UUID moduleId) {
        return repo.findAllLessonIntoModule(moduleId);
    }

    @Override
    public Optional<LessonModel> findLessonIntoModule(UUID moduleId, UUID lessonId) {
        var optionalLesson = repo.findLessonIntoModule(moduleId, lessonId);
        if (optionalLesson.isEmpty()){
            throw new NotFoundException("Error: Lesson not found for this module.");
        }
        return optionalLesson;
    }

    @Override
    public LessonModel updateLesson(LessonRecordDto dto, LessonModel lessonModel) {
        BeanUtils.copyProperties(dto, lessonModel);
        return repo.save(lessonModel);
    }
}
