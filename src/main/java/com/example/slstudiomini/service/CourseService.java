package com.example.slstudiomini.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.slstudiomini.model.Course;
import com.example.slstudiomini.repository.CourseRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public List<Course> findAllCourses() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Course> cq = cb.createQuery(Course.class);
        Root<Course> course = cq.from(Course.class);

        cq.select(course);
        cq.where(cb.isNull(course.get("deletedAt")));
        return entityManager.createQuery(cq).getResultList();
    }

    public Course findCourseById(Long id) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Course> cq = cb.createQuery(Course.class);
        Root<Course> course = cq.from(Course.class);

        cq.select(course);
        cq.where(
            cb.equal(course.get("id"), id)
        );
        return entityManager.createQuery(cq).getSingleResult();
    }

    @Transactional
    public Course save(Course course) {
        if (course != null) {
            course.setCreatedAt(LocalDateTime.now());
            courseRepository.save(course);
        }
        return course;
    }

    @Transactional
    public Course update(Course updateCourse) {
        Course course = findCourseById(updateCourse.getId());
        course.setName(updateCourse.getName());
        course.setDescription(updateCourse.getDescription());
        course.setUpdatedAt(LocalDateTime.now());

        return courseRepository.save(course);
    }

    @Transactional
    public void delete(Course deletedCourse) {
        Course course = findCourseById(deletedCourse.getId());
        // コースの削除はdeletedAtに日付を入れる
        course.setDeletedAt(LocalDateTime.now());
        course.getLessons().forEach(lesson -> lesson.setDeletedAt(LocalDateTime.now()));
        courseRepository.save(course);
    }

}
