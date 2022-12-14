package cc.rits.membership.console.reminder.config;

import java.util.TimeZone;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * タイムゾーンの設定
 */
@Configuration
public class TimeZoneConfig {

    @Bean
    public TimeZone timeZone() {
        final var timeZone = TimeZone.getTimeZone("UTC");
        TimeZone.setDefault(timeZone);
        return timeZone;
    }

}
