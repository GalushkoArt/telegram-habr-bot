package tech.mtright.telegramhabrbot.controllers;

import org.springframework.web.bind.annotation.*;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import tech.mtright.telegramhabrbot.TelegramHabrBot;

@RestController
public class WebhookController {
    private final TelegramHabrBot telegramBot;

    public WebhookController (TelegramHabrBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    @PostMapping("/")
    public BotApiMethod<?> onUpdateReceived(@RequestBody Update update) {
        return telegramBot.onWebhookUpdateReceived(update);
    }
}
