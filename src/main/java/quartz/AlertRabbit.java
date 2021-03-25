package quartz;

import java.io.InputStream;
import java.util.Properties;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import static org.quartz.JobBuilder.*;
import static org.quartz.TriggerBuilder.*;
import static org.quartz.SimpleScheduleBuilder.*;

/**
 * Библиотека http://www.quartz-scheduler.org/.
 * позволяет делать действия с периодичностью.
 */
public class AlertRabbit {

    private static int interval;

    private AlertRabbit() {
        init();
    }

    /**
     * Задание
     * При запуске программы нужно читать файл rabbit.properties.
     */
    private void init() {
        try (InputStream is = AlertRabbit.class.getClassLoader().getResourceAsStream("rabbit.properties")) {
            Properties config = new Properties();
            config.load(is);
            interval = Integer.parseInt(config.getProperty("rabbit.interval"));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public static void main(String[]args) {
        new AlertRabbit();

        try {
            /*
            1. Конфигурирование
            Начало работы происходит с создания класса
            управляющего всеми работами.
            В объект Scheduler мы будем добавлять задачи,
            которые хотим выполнять периодически.
             */
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();

            /*
            2. Создание задачи.
            quartz каждый раз создает объект с типом org.quartz.Job.
            Вам нужно создать класс реализующий этот интерфейс.
            Внутри этого класса нужно описать требуемые действия.
            В нашем случае - это вывод на консоль текста.
             */
            JobDetail job = newJob(Rabbit.class).build();

            /*
            3.Создание расписания
            Настраиваем периодичность запуска. В нашем случае, мы будем
            запускать задачу через 10 секунд и делать это бесконечно.
             */
            SimpleScheduleBuilder times = simpleSchedule()
                    .withIntervalInSeconds(interval)
                    .repeatForever();

            /*
            4. Задача выполняется через триггер.
            Здесь можно указать, когда начинать запуск.
            Мы хотим сделать это сразу.
             */
            Trigger trigger = newTrigger()
                    .startNow()
                    .withSchedule(times)
                    .build();
            scheduler.scheduleJob(job, trigger);
        } catch (SchedulerException se) {
            se.printStackTrace();
        }
    }

    /**
     * quartz каждый раз создает объект с типом org.quartz.Job.
     * Вам нужно создать класс реализующий этот интерфейс.
     * Внутри этого класса нужно описать требуемые действия.
     * В нашем случае - это вывод на консоль текста.
     */
    public static class Rabbit implements Job {
        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {
            System.out.println("Rabbit runs here ...");
        }
    }
}