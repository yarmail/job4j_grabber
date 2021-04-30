package grabber;

import java.time.LocalDateTime;

/**
 * name; // название вакансии
 * link; // ссылка на вакансию
 * text; // текст вакансии
 * created; //дата первого поста
 */
public class Post {
    private String name;
    private String link;
    private String text;
    private LocalDateTime created;

    public Post() {
    }

    public Post(String name, String link, String text, LocalDateTime created) {
        this.name = name;
        this.link = link;
        this.text = text;
        this.created = created;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    @Override
    public String toString() {
        return "Post{"
                + "name = '" + name + '\''
                + ", link = '" + link + '\''
                + ", text = '" + text + '\''
                + ", created = " + created
                + '}';
    }
}