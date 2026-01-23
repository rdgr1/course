package com.ead.course.controllers;

import com.ead.course.dtos.ModuleRecordDto;
import com.ead.course.models.ModuleModel;
import com.ead.course.services.impl.CourseServiceImpl;
import com.ead.course.services.impl.ModuleServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class ModuleController {
    private final ModuleServiceImpl service;
    private final CourseServiceImpl courseService;

    public ModuleController(ModuleServiceImpl service, CourseServiceImpl courseService) {
        this.service = service;
        this.courseService = courseService;
    }

    @PostMapping("/courses/{courseId}/modules")
    public ResponseEntity<Object> saveModule(@PathVariable UUID courseId, @RequestBody @Valid ModuleRecordDto dto) {
        return ResponseEntity.status(HttpStatus.OK).body(service.save(courseService.findById(courseId).get(), dto));
    }

    @GetMapping("/courses/{courseId}/modules")
    public ResponseEntity<List<ModuleModel>> getAllModules(@PathVariable UUID courseId) {
        return ResponseEntity.status(HttpStatus.OK).body(service.findAllModuleIntoCourse(courseId));
    }

    @GetMapping("/courses/{courseId}/modules/{moduleId}")
    public ResponseEntity<Object> getOneModule(@PathVariable UUID courseId, @PathVariable UUID moduleId) {
        return ResponseEntity.status(HttpStatus.OK).body(service.findModuleIntoCourse(courseId, moduleId));
    }

    @DeleteMapping("/courses/{courseId}/modules/{moduleId}")
    public ResponseEntity<?> delete(@PathVariable UUID courseId, @PathVariable UUID moduleId) {
        service.delete(service.findModuleIntoCourse(courseId, moduleId).get());
        return ResponseEntity.status(HttpStatus.OK).body("Module Deleted Successfully!");
    }

    @PutMapping("/courses/{courseId}/modules/{moduleId}")
    public ResponseEntity<?> updateModule(@PathVariable UUID courseId, @PathVariable UUID moduleId, @RequestBody @Valid ModuleRecordDto dto) {
        return ResponseEntity.status(HttpStatus.OK).body(service.update(dto, service.findModuleIntoCourse(courseId, moduleId).get()));
    }
}
