package elearningspringboot.repository;

import elearningspringboot.entity.Course;
import elearningspringboot.enumeration.StatusCourse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

        @Query("select c from Course c where " +
                        "(:categoryId is null or c.category.id = :categoryId) and " +
                        "(:teacherId is null or c.teacher.id = :teacherId) and " +
                        "(:status is null or c.status = :status) and " +
                        "(lower(c.title) like %:keyword% or lower(c.shortDescription) like %:keyword% or lower(c.detailDescription) like %:keyword%)")
        Page<Course> searchCourses(@Param("keyword") String keyword,
                        @Param("categoryId") Long categoryId,
                        @Param("teacherId") Long teacherId,
                        @Param("status") StatusCourse status,
                        Pageable pageable);

        // New: fetch newest courses by status with limit via Pageable
        List<Course> findByStatusOrderByCreatedAtDesc(StatusCourse status, Pageable pageable);
}
