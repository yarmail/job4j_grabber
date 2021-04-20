package html;

public class Post {
    private String name; // название вакансии
    private String link;
    private String author;
    private String message; // тело вакансии
    private String createDate;

    public Post(String name, String link, String author, String message, String createDate) {
        this.name = name;
        this.link = link;
        this.author = author;
        this.message = message;
        this.createDate = createDate;
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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }
}