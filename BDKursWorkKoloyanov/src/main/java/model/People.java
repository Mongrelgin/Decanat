package model;

public class People {
    private Integer id;
    private String name;
    private String lastName;
    private String partherName;
    private Character type;
    private Group group;

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPartherName(String partherName) {
        this.partherName = partherName;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public void setType(Character type) {
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPartherName() {
        return partherName;
    }

    public Character getType() {
        return type;
    }

    public Group getGroup() { return group; }

    public People(Integer id, String name, String lastName, String partherName, Character type, Group group) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.partherName = partherName;
        this.type = type;
        this.group = group;
    }

    public People(String name, String lastName, String partherName, Character type, Group group) {
        this.name = name;
        this.lastName = lastName;
        this.partherName = partherName;
        this.type = type;
        this.group = group;
    }

}
