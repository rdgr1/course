package com.ead.course.controllers;

import com.ead.course.dtos.LessonRecordDto;
import com.ead.course.services.impl.LessonServiceImpl;
import com.ead.course.services.impl.ModuleServiceImpl;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class LessonController {
    private final LessonServiceImpl service;

    private final ModuleServiceImpl moduleService;

    public LessonController(LessonServiceImpl service, ModuleServiceImpl moduleService) {
        this.service = service;
        this.moduleService = moduleService;
    }

    @PostMapping("/modules/{moduleId}/lessons")
    public ResponseEntity<?> save(@RequestBody @Valid LessonRecordDto dto, @PathVariable UUID moduleId){
        return ResponseEntity.status(HttpStatus.OK).body(service.save(dto, moduleService.findById(moduleId).get()));
    }

    @GetMapping("/modules/{moduleId}/lessons")
    public ResponseEntity<?> getAllLessonIntoModule(@PathVariable UUID moduleId){
        return ResponseEntity.status(HttpStatus.OK).body(service.findAllLessonsIntoModule(moduleId));
    }

    @GetMapping("/modules/{moduleId}/lessons/{lessonId}")
    public ResponseEntity<?> getOneLesson(@PathVariable UUID moduleId, @PathVariable UUID lessonId){
        return ResponseEntity.status(HttpStatus.OK).body(service.findLessonIntoModule(moduleId,lessonId));
    }

    @DeleteMapping("/modules/{moduleId}/lessons/{lessonId}")
    public ResponseEntity<?> delete(@PathVariable UUID moduleId, @PathVariable UUID lessonId){
        service.delete(service.findLessonIntoModule(moduleId,lessonId).get());
        return ResponseEntity.status(HttpStatus.OK).body("Lesson Deleted Successfully!");
    }

    @PutMapping("/modules/{moduleId}/lessons/{lessonId}")
    public ResponseEntity<?> updateLesson(@PathVariable UUID moduleId, @PathVariable UUID lessonId, @RequestBody @Valid LessonRecordDto dto){
        return ResponseEntity.status(HttpStatus.OK).body(service.updateLesson(dto,service.findLessonIntoModule(moduleId,lessonId).get()));
    }

}
