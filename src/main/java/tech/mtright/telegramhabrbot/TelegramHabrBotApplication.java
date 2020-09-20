package tech.mtright.telegramhabrbot;

import lombok.Setter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.ApiContext;
import tech.mtright.telegramhabrbot.bot.TelegramFacade;

@Setter
@SpringBootApplication
@ConfigurationProperties("telegram.bot")
public class TelegramHabrBotApplication {
    private String webHookPath;
    private String botUserName;
    private String botToken;

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean
    public TelegramHabrBot telegramHabrBot(@Lazy TelegramFacade facade) {
        DefaultBotOptions options = ApiContext.getInstance(DefaultBotOptions.class);

        TelegramHabrBot habrBot = new TelegramHabrBot(options, facade);
        habrBot.setBotUserName(botUserName);
        habrBot.setBotToken(botToken);
        habrBot.setWebHookPath(webHookPath);

        return habrBot;
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    public static void main(String[] args) {
        SpringApplication.run(TelegramHabrBotApplication.class, args);
    }

}
