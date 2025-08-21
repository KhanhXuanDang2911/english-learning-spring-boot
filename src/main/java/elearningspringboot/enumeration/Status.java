package elearningspringboot.enumeration;

public enum Status {
    ACTIVE("ACTIVE"),
    BANNED("BANNED");
    private final String name;

    Status(String name){
        this.name = name;
    }
    public Status getStatusFromName(String name){
        for (Status status : Status.values()){
            if (status.name.equalsIgnoreCase(name))
                return status;
        }
        throw new RuntimeException("Invalid name status enum");
    }

}
