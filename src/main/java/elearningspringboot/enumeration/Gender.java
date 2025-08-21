package elearningspringboot.enumeration;

public enum Gender {
    MALE("MALE"),
    FEMALE("FEMALE");

    private final String name;
    Gender(String name){
        this.name = name;
    }

    public Gender getGenderFromName(String name){
        for (Gender gender : Gender.values()){
            if (gender.name.equalsIgnoreCase(name))
                return gender;
        }
        throw new RuntimeException("Invalid name gender enum");
    }

}
