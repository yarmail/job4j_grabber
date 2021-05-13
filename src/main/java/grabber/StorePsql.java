package grabber;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import static java.lang.Integer.valueOf;

public class StorePsql implements Store, AutoCloseable {
    private static Connection connection;

    public StorePsql(Properties config) {
        try {
            Class.forName(config.getProperty("driver-class-name"));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            connection = DriverManager.getConnection(
                    config.getProperty("url"),
                    config.getProperty("username"),
                    config.getProperty("password"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Вероятно это аналог add
     * В таблице post у нас 5 столбцов
     * id(заполняется автоматически), name, text, link, created
     * Поле created в модели LocalDateTime, а в таблице Timestamp
     * on conflict do nothing - ничего не делать, если есть конфликты
     */
    @Override
    public void save(Post post) {
        String query = "insert into post(name, text, link, created) values (?, ?, ?, ?) on conflict do nothing";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, post.getName());
            ps.setString(2, post.getText());
            ps.setString(3, post.getLink());
            ps.setTimestamp(4, Timestamp.valueOf(post.getCreated()));
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Вероятно это аналог findAll
     *
     */
    @Override
    public List<Post> getAll() {
        List<Post> list = new ArrayList<>();
        String query = "select * from post";
        try (Statement st = connection.createStatement()) {
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                list.add(new Post(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("text"),
                        rs.getString("link"),
                        rs.getTimestamp("created").toLocalDateTime()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public Post findById(String id) {
        Post post = null;
        String query = "select * from post where id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, (int) valueOf(id));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                post = new Post(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("text"),
                        rs.getString("link"),
                        rs.getTimestamp("created").toLocalDateTime());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return post;
    }

    @Override
    public void close() throws Exception {
        if (connection != null) {
            connection.close();
        }
    }

/*
    //Проверка методов
    //Поле link должно быть уникальным для каждой строки
    public static void main(String[] args) {
       //Post post = new Post("name", "link2", "text", LocalDateTime.now());
        //new StorePsql().save(post);
        //System.out.println(new StorePsql().getAll());
        //System.out.println(new StorePsql().findById("1"));
    }
*/
}