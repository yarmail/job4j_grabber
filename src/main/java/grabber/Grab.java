package grabber;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;

/**
 * Периодический запуск.
 * В этом проекты мы будем использовать quartz
 * для запуска парсера.
 * Но напрямую мы не будем его использовать.
 * Абстрагируемся через интерфейс.
 */
public interface Grab {
    void init(Parse parse, Store store, Scheduler scheduler) throws SchedulerException;
}
