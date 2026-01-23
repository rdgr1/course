package com.ead.course.services;

import com.ead.course.dtos.CourseRecordDto;
import com.ead.course.models.CourseModel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CourseService {
    void delete(CourseModel course);

    CourseModel save(CourseRecordDto dto);

    boolean existsByName(String name);

    List<CourseModel> findAll();

    Optional<CourseModel> findById(UUID courseId);

    CourseModel update(CourseRecordDto dto, CourseModel existing);
}
