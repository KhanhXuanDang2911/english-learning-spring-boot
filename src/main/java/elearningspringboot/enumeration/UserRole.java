package elearningspringboot.enumeration;

import lombok.Getter;

@Getter
public enum UserRole {
    USER("USER"),
    ADMIN("ADMIN"),
    TEACHER("TEACHER");

    private final String name;

    UserRole(String name){
        this.name = name;
    }

    public UserRole getRoleFromName(String name){
        for (UserRole role : UserRole.values()){
            if (role.name.equalsIgnoreCase(name))
                return role;
        }
        throw new RuntimeException("Invalid name role enum");
    }
}
