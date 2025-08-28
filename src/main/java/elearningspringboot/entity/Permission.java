package elearningspringboot.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="permissions")
public class Permission extends BaseEntity {
    @Column(nullable = false, unique = true)
    private String name;
    private String description;
    @Column(nullable = false)
    private String module;

    @OneToMany(mappedBy = "permission")
    private List<RoleHasPermission> roleHasPermissions;
}
