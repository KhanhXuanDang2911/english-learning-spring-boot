package elearningspringboot.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "role_has_permission")
public class RoleHasPermission extends BaseEntity{
    @ManyToOne
    @JoinColumn(name="role_id", nullable = false)
    private Role role;

    @ManyToOne
    @JoinColumn(name="permission_id", nullable = false)
    private Permission permission;

    private String description;

}
