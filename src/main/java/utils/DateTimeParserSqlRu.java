package utils;

import java.util.HashMap;

import java.util.Locale;
import java.time.LocalDate;
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

    private static final Locale RUS_LOCALE = new Locale("ru", "RU");
    private static final HashMap<String, String> MAP = new HashMap<String, String>();


    /** статический блок инициализации
     * Также можно использовать анонимный класс
     * private static final HashMap<String, String> MAP = new HashMap<String, String>() {
     * put(...);
     * put(...);
     */
    static  {
        MAP.put("янв", "января");
        MAP.put("фев", "февраля");
        MAP.put("мар", "марта");
        MAP.put("апр", "апреля");
        MAP.put("июн", "июня");
        MAP.put("июл", "июля");
        MAP.put("авг", "августа");
        MAP.put("сен", "сентября");
        MAP.put("окт", "октября");
        MAP.put("ноя", "ноября");
        MAP.put("дек", "декабря");
    }

    @Override
    public LocalDateTime parse(String dateString) {
        LocalDateTime result;
        String checkedString = checkString(dateString);
        String datePattern = "d MMMM yy, HH:mm";
        final DateTimeFormatter dtf = new DateTimeFormatterBuilder()
                .parseCaseInsensitive()
              //.parseLenient() - для нумерованных месяцев
                .appendPattern(datePattern)
                .toFormatter(RUS_LOCALE);
        result = LocalDateTime.parse(checkedString, dtf);
        return result;
    }

    /**
     * Рассматриваем 2 варианта:
     * 1) Если входящая дата = сегодня или вчера - получаем
     * сегодняшнюю или вчерашнюю дату и подставляем
     * 2) Если в дате сокращение - меняем его на полный вариант
     */
    private static String checkString(String string) {
        String result = "проверка строки не сработала";
        if (string.contains("сегодня")  || string.contains("вчера")) {
            String datePattern = "d MMMM yy";
            final DateTimeFormatter dtf = new DateTimeFormatterBuilder()
                    .appendPattern(datePattern)
                    .toFormatter(RUS_LOCALE);
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

            for (String key: MAP.keySet()) {
                if (key.equals(partString)) {
                    result = string.replace(partString, MAP.get(key));
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