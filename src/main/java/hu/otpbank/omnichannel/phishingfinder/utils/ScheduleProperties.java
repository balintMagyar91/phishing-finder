package hu.otpbank.omnichannel.phishingfinder.utils;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "schedule")
public class ScheduleProperties {

    private int fixedRate;

    public int getFixedRate() {
        return fixedRate;
    }

    public void setFixedRate(int fixedRate) {
        this.fixedRate = fixedRate;
    }
}
