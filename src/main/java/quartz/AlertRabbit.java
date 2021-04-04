package quartz;

import java.io.InputStream;
import java.util.Properties;
import java.sql.*;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;


/**
 * Библиотека http://www.quartz-scheduler.org/.
 * позволяет делать действия с периодичностью.
 */
public class AlertRabbit {

    private static int interval;
    private static Connection connection;

    private AlertRabbit() {
        init();
    }

    /**
     * В проекте агрегатор будет использоваться база данных.
     * Открыть и закрывать соединение с базой накладно.
     * Чтобы этого избежать коннект к базе будет
     * создаваться при старте.
     * Объект коннект будет передаваться в Job.
     *
     * + 1. Доработайте класс AlertRabbit.
     * Добавьте в файл rabbit.properties настройки для базы данных.
     */
    private void init() {
        try (InputStream is = AlertRabbit.class.getClassLoader().getResourceAsStream("rabbit.properties")) {
            Properties config = new Properties();
            config.load(is);
            interval = Integer.parseInt(config.getProperty("rabbit.interval"));
            Class.forName(config.getProperty("driver-class-name"));
            connection = DriverManager.getConnection(
                    config.getProperty("url"),
                    config.getProperty("username"),
                    config.getProperty("password")
            );
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Конфигурирование
     * Начало работы происходит с создания класса
     * управляющего всеми работами.
     * В объект Scheduler мы будем добавлять задачи,
     * которые хотим выполнять периодически.
     *
     * JobDataMap - для хранения данных для job на основе Map
     * Каждый раз, когда задание запускается, этот объект инициализируется
     * заново — т.е. вся информация, которая туда попадает во
     * время выполнения задания, пропадает. Если нам нужно
     * сохранить информацию между запусками одного и того
     * же типа задания, то вместо интерфейса Job нам следует
     * реализовать интерфейс StatefulJob
     *
     * Создание задачи.
     * quartz каждый раз создает объект с типом org.quartz.Job.
     * Вам нужно создать класс реализующий этот интерфейс.
     * Внутри этого класса нужно описать требуемые действия.
     * В нашем случае - это вывод на консоль текста.
     * JobDetail job = newJob(Rabbit.class).build();
     *
     * Создание расписания
     * Настраиваем периодичность запуска.
     * SimpleScheduleBuilder times = simpleSchedule()
     * interval задается при инициализации
     *
     * Задача выполняется через триггер.
     * Здесь можно указать, когда начинать запуск.
     * Мы хотим сделать это сразу.
     * Trigger trigger = newTrigger()
     *
     * + Задание 5 Весь main должен работать 10 секунд
     * Thread.sleep(10000);
     * scheduler.shutdown();
     *
     */
    public static void main(String[]args) {
        new AlertRabbit();
        try {
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();
            //JobDataMap data = new JobDataMap(Map.of("connection", connection));
            JobDataMap data = new JobDataMap();
            data.put("connection", connection);
            JobDetail job = newJob(Rabbit.class)
                    .usingJobData(data)
                    .build();
            SimpleScheduleBuilder times = simpleSchedule()
                    .withIntervalInSeconds(interval)
                    .repeatForever();
            Trigger trigger = newTrigger()
                    .startNow()
                    .withSchedule(times)
                    .build();
            scheduler.scheduleJob(job, trigger);
            Thread.sleep(10000);
            scheduler.shutdown();
        } catch (SchedulerException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * quartz каждый раз создает объект с типом org.quartz.Job.
     * Вам нужно создать класс реализующий этот интерфейс.
     * Внутри этого класса нужно описать требуемые действия.
     * В нашем случае - это вывод на консоль текста.
     *
     * Как я понимаю интерфейс Job имеет только один
     * переопределяемый метод execute который выполняет некоторую работу
     */
    public static class Rabbit implements Job {

        /**
         * Каждый запуск работы вызывает конструктор
         */
        public Rabbit() {
            System.out.println(hashCode());
        }

        /**
         * Чтобы в объект Job иметь общий ресурс
         * нужно использовать JobExecutionContext.
         * JobExecutionContext — это объект, который содержит информацию,
         * которая была передана заданию, перед тем как оно было запущено на исполнение.
         * Проще говоря, если нам нужно передать какие-то данные в задание,
         * перед тем как оно будет запущено, мы должны использовать JobExecutionContext.
         *
         * Как я понимаю сначала мы в JobDataMap засовывали объект
         * store (ArrayList), чтобы записывать данные в ArrayList,
         * а теперь засовываем в него объект Connection, чтобы иметь возможность работать
         * с базой данных (для записи)
         * А далее объекты мы обратно через
         * List<Long> store = (List<Long>) context.getJobDetail().getJobDataMap().get("store");
         * или
         * Connection connection = (Connection) context.getJobDetail().getJobDataMap().get("connection");
         * ? Объекты находящиеся в context (в JobDataMap) являются общими для каждого типа работы
         *
         * Старый вариант (с ArrayList)
         * List<Long> store = (List<Long>) context.getJobDetail().getJobDataMap().get("store");
         * store.add(System.currentTimeMillis());
         *
         */
        @Override
        public void execute(JobExecutionContext context) {
            System.out.println("Rabbit runs here ...");
            Connection connection = (Connection) context.getJobDetail().getJobDataMap().get("connection");
            try (PreparedStatement statement = connection.prepareStatement("insert into rabbit(created) values (?)")) {
                statement.setDate(1, new Date(System.currentTimeMillis()));
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}