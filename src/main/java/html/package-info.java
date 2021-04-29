package html;
/**
 *
 * 2. Парсинг HTML страницы.
 * Для парсинга страницы html используется библиотека jsoup
 * Она позволяет сделать запрос на сервер
 * и извлечь нужный текст из полученного HTML
 * jsoup позволяет извлечь текст из HTML по аттрибутам тегов HTML.
 * https://www.sql.ru/forum/job-offers
 *
 * ----
 *
 * Пример работы jsoup
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


