package html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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
    public static void main(String[] args) throws Exception {
        Document doc = Jsoup.connect("https://www.sql.ru/forum/job-offers").get();
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
}