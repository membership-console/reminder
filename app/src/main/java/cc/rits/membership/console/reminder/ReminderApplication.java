package cc.rits.membership.console.reminder;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(nameGenerator = ReminderApplication.FQCNBeanNameGenerator.class)
public class ReminderApplication {

    public static void main(final String[] args) {
        SpringApplication.run(ReminderApplication.class, args);
    }

    static class FQCNBeanNameGenerator extends AnnotationBeanNameGenerator {

        @Override
        protected String buildDefaultBeanName(final BeanDefinition definition) {
            return definition.getBeanClassName();
        }

    }

}
