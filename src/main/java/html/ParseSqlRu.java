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
public class ParseSqlRu {

    /** начальная ссылка*/
    private static final String LINK =  "https://www.sql.ru/forum/job-offers/";
    /** выбираем нужное количество страниц*/
    private static final int N = 1;
    private static List posts = new ArrayList<>();
    private static DateTimeParserSqlRu dateTimeParserSqlRu = new DateTimeParserSqlRu();

    /**
     * Метод  берет начальную ссылку
     * и по ней углубляется на нужное количество страниц
     */
    private static void cycle() throws IOException {
        String resultLink = "";
        for (int i = 1; i <= N; i++) {
            if (i == 1) {
                resultLink = LINK;
            }
            if (i != 1) {
                resultLink = LINK + i;
            }
            list(resultLink);
        }
    }

    /** парсинг страницы*/
    private static void list(String link) throws IOException {
        Document doc = Jsoup.connect(link).get();
        Elements rows = doc.select(".postslisttopic");
        for (Element row : rows) {
            Post post = new Post();
            Element href = row.child(0); //весь кусок
            post.setLink((href.attr("href"))); //ссылка
            post.setName(href.text()); //имя, название объявления
            Element date = row.parent().child(5); //дата весь кусок
            post.setCreated(dateTimeParserSqlRu.parse(date.text())); //дата создания
            //теперь берем новую ссылку и по ней тянем тело
            post.setText(detail(post.getLink()));
            posts.add(post);
        }
        System.out.println(posts.size()); //для проверки
    }

    /**
     * Загружаем последняюю деталь поста,
     * а именно тело, текст по сылке поста,
     * первое сообщение
     */
    private static String detail(String link) throws IOException {
        Document doc = Jsoup.connect(link).get();
        Elements elements = doc.select(".msgBody");
        String resultText = elements.get(1).text();
        return resultText;
    }

    public static void main(String[] args) throws IOException {
        cycle();
    }
}