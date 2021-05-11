package grabber;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import org.quartz.*;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

public class Grabber implements Grab {
    private final Properties config = new Properties();

    public Store store() {
        return new StorePsql(config);
    }

    public Scheduler scheduler() throws SchedulerException {
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        scheduler.start();
        return scheduler;
    }

    public void cfg() throws IOException {
        InputStream in = Grabber.class.getClassLoader()
                .getResourceAsStream("grabber.properties");
        config.load(in);
    }

    @Override
    public void init(Parse parse, Store store, Scheduler scheduler) throws SchedulerException {
        JobDataMap data = new JobDataMap();
        data.put("store", store);
        data.put("parse", parse);
        JobDetail job = newJob(GrabJob.class)
                .usingJobData(data)
                .build();
        SimpleScheduleBuilder times = simpleSchedule()
                .withIntervalInSeconds(Integer.parseInt(config.getProperty("interval")))
                .repeatForever();
        Trigger trigger = newTrigger()
                .startNow()
                .withSchedule(times)
                .build();
        scheduler.scheduleJob(job, trigger);
    }

    public static class GrabJob implements Job {

        /**
         * Какую работу нужно выполнять?
         * Контекст в котором надо выполнять работу?
         */
        @Override
        public void execute(JobExecutionContext context) {
            JobDataMap map = context.getJobDetail().getJobDataMap();
            Store store = (Store) map.get("store");
            Parse parse = (Parse) map.get("parse");
            List<Post> posts = parse.list("https://www.sql.ru/forum/job-offers/");
            posts.forEach(store::save);
        }
    }

    public static void main(String[] args) throws Exception {
        Grabber grabber = new Grabber();
        grabber.cfg();
        Scheduler scheduler = grabber.scheduler();
        Store store = grabber.store();
        grabber.init(new ParseSqlRu(), store, scheduler);
    }
}