package mohammadali.fouladi.n01547173.mf;

public class Course {
    private String name;
    private String description;
    private String id;
//  MohammadAli n01547173
    public Course() {
    }

    public Course( String id,String name, String description) {
        this.id = id;

        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getDescription() {
        return description;
    }

    public String getId() {
        return id;
    }
}

