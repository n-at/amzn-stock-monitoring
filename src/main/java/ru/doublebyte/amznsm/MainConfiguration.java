package ru.doublebyte.amznsm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.context.WebApplicationContext;
import ru.doublebyte.amznsm.services.*;

@Configuration
public class MainConfiguration {

    private final JavaMailSender javaMailSender;

    @Autowired
    public MainConfiguration(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Value("${stock-monitoring.mail-from}")
    private String mailFrom;

    @Value("${stock-monitoring.mail-to}")
    private String mailTo;

    @Value("${stock-monitoring.mail-subject}")
    private String mailSubject;

    @Bean
    public TaskScheduler taskScheduler() {
        return new ThreadPoolTaskScheduler();
    }

    @Bean
    public ItemStorage itemStorage() {
        return new ItemStorage();
    }

    @Bean
    public MailRenderer mailRenderer() {
        return new MailRenderer();
    }

    @Bean
    public MailMessageSender mailMessageSender() {
        return new MailMessageSender(javaMailSender, mailRenderer(), mailFrom, mailTo, mailSubject);
    }

    @Bean
    public StockInfo stockInfo() {
        return new StockInfo();
    }

    @Bean
    public StockMonitor stockMonitor() {
        return new StockMonitor(itemStorage(), stockInfo(), mailMessageSender());
    }

    @Bean
    @Scope(
            value = WebApplicationContext.SCOPE_SESSION,
            proxyMode = ScopedProxyMode.TARGET_CLASS
    )
    public SessionStorage sessionStorage() {
        return new SessionStorage();
    }

}
