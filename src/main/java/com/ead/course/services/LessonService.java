package com.ead.course.services;

import com.ead.course.dtos.LessonRecordDto;
import com.ead.course.models.LessonModel;
import com.ead.course.models.ModuleModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LessonService {

    void delete(LessonModel lesson);

    LessonModel save(LessonRecordDto dto, ModuleModel moduleModel);

    Optional<LessonModel> findLessonIntoModule(UUID moduleId, UUID lessonId);

    LessonModel updateLesson(LessonRecordDto dto, LessonModel lessonModel);

    Page<LessonModel> findAllLessonsIntoModule(Specification<LessonModel> spec, Pageable pageable);
}
