package cc.rits.membership.console.reminder.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatisの設定
 */
@MapperScan("cc.rits.membership.console.reminder.infrastructure.db.mapper")
@Configuration
public class MyBatisConfig {
}
