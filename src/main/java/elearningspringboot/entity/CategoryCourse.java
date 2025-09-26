package elearningspringboot.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "categories_course")
public class CategoryCourse extends BaseEntity {
    private String title;
    private String description;
    @Column(unique = true)
    private String slug;
}
