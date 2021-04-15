package utils;

import java.time.LocalDate;
import java.util.HashMap;

import java.util.Locale;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;


/*
Примеры дат:
12 мар 21, 14:23
2 дек 19, 22:29
22 янв 16, 10:56
сегодня, 16:33
вчера, 16:09
 */

public class DateTimeParserSqlRu implements DateTimeParser {

    private static Locale rusLocale = new Locale("ru", "RU");


    @Override
    public LocalDateTime parse(String dateString) {
        LocalDateTime result;
        String checkedString = checkString(dateString);
        String datePattern = "d MMMM yy, HH:mm";
        DateTimeFormatter dtf = new DateTimeFormatterBuilder()
                .parseCaseInsensitive()
              //.parseLenient() - для нумерованных месяцев
                .appendPattern(datePattern)
                .toFormatter(rusLocale);
        result = LocalDateTime.parse(checkedString, dtf);
        return result;
    }

    /**
     * Рассматриваем 2 варианта
     * 1) Если входящая дата = сегодня или вчера - получаем
     * сегодняшнюю или вчерашнюю дату и подставляем
     * 2) Если в дате сокращение - меняем его на полный вариант
     */
    private static String checkString(String string) {
        String result = "проверка строки не сработала";
        if (string.contains("сегодня")  | string.contains("вчера")) {
            String datePattern = "d MMMM yy";
            DateTimeFormatter dtf = new DateTimeFormatterBuilder()
                    .appendPattern(datePattern)
                    .toFormatter(rusLocale);
            String today = LocalDate.now().format(dtf);
            String yesterday = LocalDate.now().minusDays(1).format(dtf);
            if (string.contains("сегодня")) {
                result = string.replace("сегодня", today);
            }
            if (string.contains("вчера")) {
                result = string.replace("вчера", yesterday);
            }
        } else {
            String[] arrayString = string.split(" ");
            String partString = arrayString[1];

            var map = new HashMap<String, String>();
            map.put("янв", "января");
            map.put("фев", "февраля");
            map.put("мар", "марта");
            map.put("апр", "апреля");
            map.put("июн", "июня");
            map.put("июл", "июля");
            map.put("авг", "августа");
            map.put("сен", "сентября");
            map.put("окт", "октября");
            map.put("ноя", "ноября");
            map.put("дек", "декабря");

            for (String key: map.keySet()) {
                if (key.equals(partString)) {
                    result = string.replace(partString, map.get(key));
                    break;
                }
            }
        }
        return result;
    }

    // проверка работы парсера
    public static void main(String[] args) {
        String dateString = "вчера, 14:23";
        String dateString2 = "2 дек 19, 22:29";
        System.out.println(new DateTimeParserSqlRu().parse(dateString));
    }
}