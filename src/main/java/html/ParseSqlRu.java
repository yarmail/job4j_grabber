package html;

import java.util.List;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

import utils.DateTimeParserSqlRu;


/**
 * Jsoup
 * https://www.sql.ru/forum/job-offers
 * Ячейка с именем имеет аттрибут class=postslisttopic.
 * jsoup может извлечь все элементы с этим аттрибутом.
 * Elements rows = doc.select(".postslisttopic");
 *
 * Element href = row.child(0);
 * Нам нужен первый элемент. Это ссылка.
 * У нее мы получаем адрес и текст.
 *
 * System.out.println(href.attr("href"));
 * System.out.println(href.text());
 *
 * Пример получаемого вывода:
 * https://www.sql.ru/forum/1334832/trebuetsya-analitik-1s-usloviya-v-soobshheniyah
 * Требуется аналитик 1С. Условия в сообщениях :)
 * 31 мар 21, 11:06
 */
public class ParseSqlRu implements Parse {

    /** начальная ссылка*/
    private static final String LINK =  "https://www.sql.ru/forum/job-offers/";
    /** выбираем нужное количество страниц*/
    private static final int N = 1;
    private static DateTimeParserSqlRu dateTimeParserSqlRu = new DateTimeParserSqlRu();

    /**
     * Метод  берет начальную ссылку
     * и по ней углубляется на нужное количество страниц
     * Данный метод работает только для конкретного сайта
     * https://www.sql.ru/forum/job-offers/
     */
    private void cycle() {
        String resultLink = "";
        for (int i = 1; i <= N; i++) {
            if (i == 1) {
                resultLink = LINK;
            }
            if (i != 1) {
                resultLink = LINK + i;
            }
        }
    }

    /**
     * парсинг страницы
     * Element href = row.child(0); весь кусок кода
     * (href.attr("href")) - ссылка из него
     * href.text()); имя, название объявления
     * Element date = row.parent().child(5); //дата весь кусок
     * post.setCreated(dateTimeParserSqlRu.parse(date.text())); //дата создания
     * ---
     * Чтобы взять текст ссылки парсим документ по ссылке
     * Document linkDoc = Jsoup.connect(post.getLink()).get();
     * Пример для парсинга:
     * https://www.sql.ru/forum/job-offers/
     *
     */
    public List<Post>  list(String link) throws IOException {
        List posts = new ArrayList<>();
        Document doc = Jsoup.connect(link).get();
        Elements rows = doc.select(".postslisttopic");
        for (Element row : rows) {
            var post = new Post();
            Element href = row.child(0);
            post.setLink((href.attr("href")));
            post.setName(href.text());
            Element date = row.parent().child(5);
            post.setCreated(dateTimeParserSqlRu.parse(date.text()));
            Document linkDoc = Jsoup.connect(post.getLink()).get();
            Elements elements = linkDoc.select(".msgBody");
            post.setText(elements.get(1).text());
            posts.add(post);
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

    // проверка работы методов
    public static void main(String[] args) throws IOException {
        System.out.println(new ParseSqlRu()
        .detail("https://www.sql.ru/forum/1335680/arhitektor-programmnyh-produktov-v-startup"));
    }
}