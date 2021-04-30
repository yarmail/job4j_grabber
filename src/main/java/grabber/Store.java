package grabber;


import java.util.List;

/**
 * Наш проект будет хранить данные в базе Postgresql.
 * Связь с базой будет осуществляться через интерфейс.
 *
 * Интерфейсы позволяют избавиться от прямой зависимости.
 * На первом этапе можно использовать
 * MemStore - хранение данных в памяти.
 *
 * Метод save() - сохраняет объявление в базе.
 * Метод getAll() - позволяет извлечь объявления из базы.
 * Метод findById(String id) - позволяет извлечь
 * объявление из базы по id.
 */
public interface Store {
    void save(Post post);

    List<Post> getAll();

    Post findById(String id);
}