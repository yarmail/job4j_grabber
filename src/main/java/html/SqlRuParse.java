package html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

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
public class SqlRuParse {

//2.1.1. Парсинг https://www.sql.ru/forum/job-offers/3 [#293734]
// Задание: доработать метод main из предыдущего задания,
// чтобы он парсил первые 5 страниц.

    /** выбираем нужное количество страниц*/
    private static int n = 5;

    /** начальная ссылка*/
    private static final String LINK =  "https://www.sql.ru/forum/job-offers/";

    /** Работа со ссылкой и передача её в парсинг*/
    private static void cycle() throws IOException {
        String resultLink = "";
        for (int i = 1; i <= n; i++) {
            if (i == 1) {
                resultLink = LINK;
            }
            if (i != 1) {
                resultLink = LINK + i;
            }
            parse(resultLink);
        }
    }

    /** процесс парсинга*/
    private static void parse(String link) throws IOException {
        Document doc = Jsoup.connect(link).get();
        Elements rows = doc.select(".postslisttopic");
        for (Element row : rows) {
            Element href = row.child(0);
            System.out.println(href.attr("href"));
            System.out.println(href.text());
            Element date = row.parent().child(5);
            System.out.println(date.text());
            System.out.println();
        }
    }

    public static void main(String[] args) throws IOException {
        cycle();
    }
}