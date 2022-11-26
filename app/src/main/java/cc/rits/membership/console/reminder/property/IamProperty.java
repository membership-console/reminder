package cc.rits.membership.console.reminder.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

/**
 * IAMプロパティ
 */
@Data
@Configuration
@ConfigurationProperties("iam")
public class IamProperty {

    /**
     * URL
     */
    String url;

    /**
     * クライアントID
     */
    String clientId;

    /**
     * クライアントシークレット
     */
    String clientSecret;

}
