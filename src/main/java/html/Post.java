package html;

import java.time.LocalDateTime;

public class Post {
    private String name; // название вакансии
    private String link; // ссылка на вакансию
    private String text; // текст вакансии
    private LocalDateTime created; //дата первого поста

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