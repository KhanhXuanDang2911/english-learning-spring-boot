package elearningspringboot.entity;

import elearningspringboot.enumeration.Gender;
import elearningspringboot.enumeration.UserRole;
import elearningspringboot.enumeration.Status;
import jakarta.persistence.*;
import lombok.*;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
//public class User extends BaseEntity implements UserDetails {
public class User extends BaseEntity {

    @Column(nullable = false)
    private String fullName;
    @Column(nullable = false)
    private String email;
    private String phoneNumber;
    private String password;
    private String avatarUrl;
    private String address;
    @Enumerated(EnumType.STRING)
    private Status status;
    private LocalDate birthDate;
    private Boolean noPassword;
    private Gender gender;
    @ManyToOne
    @JoinColumn(name="role_id")
    private Role role;

//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        Set<GrantedAuthority> authorities = new HashSet<>();
//        authorities.add(new SimpleGrantedAuthority(String.format("ROLE_%s", this.role.getRole().getName())));
//        for (RoleHasPermission r : this.role.getRoleHasPermissions()){
//            authorities.add(new SimpleGrantedAuthority(r.getPermission().getName()));
//        }
//        return authorities;
//    }
//
//    @Override
//    public String getUsername() {
//        return "";
//    }
//
//
//    @Override
//    public boolean isEnabled() {
//        return this.status.equals(Status.ACTIVE);
//    }
}
