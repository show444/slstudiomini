package com.example.slstudiomini.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.slstudiomini.exception.MyLessonNotFoundException;
import com.example.slstudiomini.model.Course;
import com.example.slstudiomini.model.Lesson;
import com.example.slstudiomini.repository.LessonRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;

@Service
public class LessonService {

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private CourseService courseService;

    @PersistenceContext
    private EntityManager entityManager;

    public List<Lesson> findAllLessons() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Lesson> cq = cb.createQuery(Lesson.class);
        Root<Lesson> lesson = cq.from(Lesson.class);

        cq.select(lesson);
        cq.where(cb.isNull(lesson.get("deletedAt")));
        return entityManager.createQuery(cq).getResultList();
    }

    public Lesson findLessonById(Long id) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Lesson> cq = cb.createQuery(Lesson.class);
        Root<Lesson> lesson = cq.from(Lesson.class);

        cq.select(lesson);
        cq.where(
            cb.equal(lesson.get("id"), id)
        );

        try{
            return entityManager.createQuery(cq).getSingleResult();
        }catch(NoResultException e){
            throw new MyLessonNotFoundException("存在しないレッスンです。Lesson ID = " + id);
        }
    }

    @Transactional
    public Lesson save(Lesson lesson) {
        lesson.setCreatedAt(LocalDateTime.now());
        return lessonRepository.save(lesson);
    }

    @Transactional
    public Lesson update(Lesson updateLesson) {
        Lesson lesson = findLessonById(updateLesson.getId());

        lesson.setUpdatedAt(LocalDateTime.now());
        lesson.setName(updateLesson.getName());
        lesson.setContent(updateLesson.getContent());
        lesson.setDescription(updateLesson.getDescription());
        lesson.setUpdatedAt(LocalDateTime.now());

        Course course = courseService.findCourseById(updateLesson.getCourse().getId());
        lesson.setCourse(course);
        return lessonRepository.save(lesson);
    }
}
