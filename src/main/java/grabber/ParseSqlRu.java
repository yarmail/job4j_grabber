package grabber;

import java.util.List;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;


/**
 * Парсер sql.ru
 * Пример работы с Jsoup
 */
public class ParseSqlRu implements Parse {

    /** начальная ссылка*/
    private static final String LINK =  "https://www.sql.ru/forum/job-offers/";
    /** выбираем нужное количество страниц сайта - глубина обхода*/
    private static final int N = 2;
    private static DateTimeParserSqlRu dateTimeParserSqlRu = new DateTimeParserSqlRu();

    /**
     * Метод  берет начальную ссылку
     * и возвращает список ссылок на нужное количество страниц
     * Данный метод работает только для конкретного адреса
     * https://www.sql.ru/forum/job-offers/
     */
    private List<String> pageLinks() {
        List<String> result = new ArrayList<>();
        result.add(LINK);
        for (int i = 1; i <= N; i++) {
            if (i != 1) {
                result.add(LINK + i);
            }
        }
        return result;
    }

    /**
     * парсинг страницы
     * Element part = element.child(0);// кусок кода
     * String postLink = part.attr("href");// ссылка из него на пост
     * posts.add(detail(postLink)); // загружаем детали поста
     */
    public List<Post> list(String link) throws IOException {
        List<Post> posts = new ArrayList<>();
        List<String> pageLinks = pageLinks();
        for (int i = 0; i < pageLinks.size(); i++) {
            Document doc = Jsoup.connect(pageLinks.get(i)).get();
            Elements elements = doc.select(".postslisttopic");
            for (Element element: elements) {
                Element part = element.child(0);
                String postLink = part.attr("href");
                posts.add(detail(postLink));
            }
        }
        return posts;
    }

    /**
     * Парсим первый пост по ссылке объявления
     * Пример для парсинга:
     * https://www.sql.ru/forum/1335680/arhitektor-programmnyh-produktov-v-startup
     * ----
     * Ссылка - входящий параметр
     * ----
     * Текст ссылки:
     * Elements elements = doc.select(".msgBody");
     * post.setText(elements.get(1).text());
     * ----
     * Название объявления
     * elements = doc.select(".messageHeader");
     * post.setName(elements.get(0).text());
     * ----
     * Дата
     * Изначально дата приходит в виде
     * вчера, 18:11&nbsp;&nbsp;&nbsp;&nbsp;[
     * и её надо чистить перед использованием
     *
     */
    public Post detail(String link) throws IOException {
        var post = new Post();
        post.setLink(link);
        Document doc = Jsoup.connect(link).get();
        Elements elements = doc.select(".msgBody");
        post.setText(elements.get(1).text());
        elements = doc.select(".messageHeader");
        post.setName(elements.get(0).text());
        elements = doc.select(".msgFooter");
        String date = elements.get(0).childNode(0).toString().trim();
        date = date.substring(0, date.indexOf("&"));
        post.setCreated(dateTimeParserSqlRu.parse(date));
        return post;
    }
}