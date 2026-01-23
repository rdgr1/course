package com.ead.course.dtos;

import com.ead.course.models.CourseModel;
import com.ead.course.models.LessonModel;
import jakarta.validation.constraints.NotBlank;

import java.util.Set;
public record ModuleRecordDto(
        @NotBlank
        String name,
        @NotBlank
        String description
) {
}
