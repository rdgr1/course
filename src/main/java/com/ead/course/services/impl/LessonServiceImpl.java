package com.ead.course.services.impl;

import com.ead.course.repositories.LessonRepository;
import com.ead.course.services.LessonService;
import org.springframework.stereotype.Service;

@Service
public class LessonServiceImpl implements LessonService {
    final LessonRepository repo;

    public LessonServiceImpl(LessonRepository repo) {
        this.repo = repo;
    }
}
