package bean;

import java.io.Serializable;
import java.util.Objects;

public class Tour implements Serializable {
    private int id;
    private String description;
    private String title;
    private int count;

    public Tour() {
        id = -1;
        description = "Noname";
        title = "Noname";
        count = -1;
    }

    public Tour(int id, String description, String title, int count) {
        this.id = id;
        this.description = description;
        this.title = title;
        this.count = count;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof tour)) return false;
        Tour tour = (tour) o;
        return id == tour.id &&
                count == tour.count &&
                description.equals(tour.description) &&
                title.equals(tour.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, title, count);
    }

    @Override
    public String toString() {
        return "Tour{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", title='" + title + '\'' +
                ", count=" + count +
                '}';
    }
}
