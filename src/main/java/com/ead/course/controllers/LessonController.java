package com.ead.course.controllers;

import com.ead.course.dtos.LessonRecordDto;
import com.ead.course.services.impl.LessonServiceImpl;
import com.ead.course.services.impl.ModuleServiceImpl;
import com.ead.course.specifications.SpecificationTemplate;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class LessonController {
    private final LessonServiceImpl service;
    private final ModuleServiceImpl moduleService;

    Logger logger = LogManager.getLogger(LessonController.class);

    public LessonController(LessonServiceImpl service, ModuleServiceImpl moduleService) {
        this.service = service;
        this.moduleService = moduleService;
    }

    @PostMapping("/modules/{moduleId}/lessons")
    public ResponseEntity<?> save(@RequestBody @Valid LessonRecordDto dto, @PathVariable UUID moduleId){
        return ResponseEntity.status(HttpStatus.OK).body(service.save(dto, moduleService.findById(moduleId).get()));
    }

    @GetMapping("/modules/{moduleId}/lessons")
    public ResponseEntity<?> getAllLessonIntoModule(@PathVariable UUID moduleId, SpecificationTemplate.LessonSpec spec, Pageable pageable){
        return ResponseEntity.status(HttpStatus.OK).body(service.findAllLessonsIntoModule(SpecificationTemplate.lessonModuleId(moduleId).and(spec), pageable));
    }

    @GetMapping("/modules/{moduleId}/lessons/{lessonId}")
    public ResponseEntity<?> getOneLesson(@PathVariable UUID moduleId, @PathVariable UUID lessonId){
        return ResponseEntity.status(HttpStatus.OK).body(service.findLessonIntoModule(moduleId,lessonId));
    }

    @DeleteMapping("/modules/{moduleId}/lessons/{lessonId}")
    public ResponseEntity<?> delete(@PathVariable UUID moduleId, @PathVariable UUID lessonId){
        logger.debug("DELETE deleteLesson moduleId: {}, lessonId: {}", moduleId, lessonId);
        service.delete(service.findLessonIntoModule(moduleId,lessonId).get());
        return ResponseEntity.status(HttpStatus.OK).body("Lesson Deleted Successfully!");
    }

    @PutMapping("/modules/{moduleId}/lessons/{lessonId}")
    public ResponseEntity<?> updateLesson(@PathVariable UUID moduleId, @PathVariable UUID lessonId, @RequestBody @Valid LessonRecordDto dto){
        logger.debug("PUT updateLesson moduleId: {}, lessonId: {}, LessonRecordDto: {}",moduleId,lessonId,dto);
        return ResponseEntity.status(HttpStatus.OK).body(service.updateLesson(dto,service.findLessonIntoModule(moduleId,lessonId).get()));
    }

}
