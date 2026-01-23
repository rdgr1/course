package com.ead.course.controllers;

import com.ead.course.dtos.CourseRecordDto;
import com.ead.course.models.CourseModel;
import com.ead.course.services.impl.CourseServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin("*")
@RequestMapping("/courses")
public class CourseController {
    final CourseServiceImpl service;

    public CourseController(CourseServiceImpl service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> saveCourse(@RequestBody @Valid CourseRecordDto courseRecordDto) {
        if (service.existsByName(courseRecordDto.name())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: Course Name is Already Taken!");
        }
        var saved = service.save(courseRecordDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping
    public ResponseEntity<List<CourseModel>> getAllCourses() {
        return ResponseEntity.status(HttpStatus.OK).body(service.findAll());
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<Optional<CourseModel>> getOneCourse(@PathVariable UUID courseId) {
        return ResponseEntity.status(HttpStatus.OK).body(service.findById(courseId));
    }

    @DeleteMapping("/{courseId}")
    public ResponseEntity<?> delete(@PathVariable UUID courseId) {
        service.delete(service.findById(courseId).get());
        return ResponseEntity.status(HttpStatus.OK).body("Course Deleted Successfully!");
    }

    @PutMapping("/{courseId}")
    public ResponseEntity<CourseModel> updateCourse(@PathVariable UUID courseId, @RequestBody @Valid CourseRecordDto dto) {
        return ResponseEntity.status(HttpStatus.OK).body(service.update(dto, service.findById(courseId).get()));
    }
}
