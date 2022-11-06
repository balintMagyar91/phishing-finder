package hu.otpbank.omnichannel.phishingfinder.schedule;

import hu.otpbank.omnichannel.phishingfinder.component.ScraperTask;
import hu.otpbank.omnichannel.phishingfinder.utils.ScheduleProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.PeriodicTrigger;

import java.util.concurrent.TimeUnit;

@Configuration
public class Scheduler implements SchedulingConfigurer {

    @Autowired
    private ScheduleProperties scheduleProperties;

    @Value("${fixedDelay:}")
    private Integer fixedDelay;

    @Autowired
    private ScraperTask scraperTask;

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.addTriggerTask(scraperTask, trigger());
    }

    @Bean
    public Trigger trigger() {
        if (fixedDelay != null) {
            long periodInMilliseconds = TimeUnit.MINUTES.toMillis(fixedDelay);
            return new PeriodicTrigger(periodInMilliseconds);
        }
        return new PeriodicTrigger(TimeUnit.MINUTES.toMillis(1));
    }
}
