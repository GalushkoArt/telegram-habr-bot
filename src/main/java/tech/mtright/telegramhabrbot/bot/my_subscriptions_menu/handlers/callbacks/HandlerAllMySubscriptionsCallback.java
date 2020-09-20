package tech.mtright.telegramhabrbot.bot.my_subscriptions_menu.handlers.callbacks;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import tech.mtright.telegramhabrbot.TelegramHabrBot;
import tech.mtright.telegramhabrbot.bot.CallbackHandler;
import tech.mtright.telegramhabrbot.bot.my_subscriptions_menu.MySubscriptionsMenuPage;
import tech.mtright.telegramhabrbot.models.*;
import tech.mtright.telegramhabrbot.repositories.UserProfileDataRepository;
import tech.mtright.telegramhabrbot.services.ReplyMessagesService;

import java.util.List;
import java.util.Set;

import static tech.mtright.telegramhabrbot.bot.my_subscriptions_menu.MySubscriptionsCallbackButtons.ALL_MY_SUBSCRIPTIONS_BUTTON;

@Component
@Log4j2
public class HandlerAllMySubscriptionsCallback implements CallbackHandler {
    @Autowired
    private UserProfileDataRepository repository;
    @Autowired
    private ReplyMessagesService messagesService;
    @Autowired
    private MySubscriptionsMenuPage mySubscriptionsMenuPage;
    @Autowired
    private List<MySubscriptionCallbackHandler> mySubscriptionCallbackHandlers;
    @Autowired
    private TelegramHabrBot bot;

    @Override
    public String getHandledCallback() {
        return ALL_MY_SUBSCRIPTIONS_BUTTON;
    }

    @Override
    @Transactional
    public BotApiMethod<?> getAnswer(CallbackQuery callbackQuery) {
        Long chatId = callbackQuery.getMessage().getChatId();
        Integer messageId = callbackQuery.getMessage().getMessageId();
        int totalSubscriptionNumber = getTotalSubscriptionNumber(chatId);
        if (totalSubscriptionNumber == 0) {
            bot.sendMessage(chatId, messagesService.getReplyText("reply.youDontHaveAnySubscription"));
        } else {
            bot.sendMessage(chatId, messagesService.getReplyText("reply.showYourSubscription", totalSubscriptionNumber));
            mySubscriptionCallbackHandlers.forEach(handler -> handler.showSubscriptions(chatId));
        }
        bot.deleteMessage(chatId, messageId);
        return mySubscriptionsMenuPage.getPage(chatId);
    }

    private int getTotalSubscriptionNumber(long chatId) {
        UserProfileData userProfileData = repository.findByChatId(chatId);
        Set<Author> authors = userProfileData.getSubscribedAuthors();
        int numberOfAuthors = authors != null ? authors.size() : 0;
        Set<Tag> tags = userProfileData.getSubscribedTags();
        int numberOfTags = tags != null ? tags.size() : 0;
        Set<Company> companies = userProfileData.getSubscribedCompanies();
        int numberOfCompanies = companies != null ? companies.size() : 0;
        Set<Hub> hubs = userProfileData.getSubscribedHubs();
        int numberOfHubs = hubs != null ? hubs.size() : 0;
        return numberOfAuthors + numberOfCompanies + numberOfHubs + numberOfTags;
    }
}
