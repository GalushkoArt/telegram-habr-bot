package tech.mtright.telegramhabrbot.bot.my_subscriptions_menu.handlers.callbacks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tech.mtright.telegramhabrbot.models.Subscribable;
import tech.mtright.telegramhabrbot.services.ReplyMessagesService;
import tech.mtright.telegramhabrbot.services.UsersProfileDataService;

import java.util.Set;

import static tech.mtright.telegramhabrbot.bot.my_subscriptions_menu.MySubscriptionsCallbackButtons.MY_AUTHOR_SUBSCRIPTIONS_BUTTON;
import static tech.mtright.telegramhabrbot.bot.my_subscriptions_menu.MySubscriptionsCallbackButtons.UNSUBSCRIBE_ME_FROM_AUTHOR_BUTTON;

@Component
public class HandlerMyAuthorSubscriptionsCallback extends AbstractHandlerMySubscriptionsCallback {
    @Autowired
    private ReplyMessagesService messagesService;
    @Autowired
    private UsersProfileDataService dataService;

    @Override
    @Transactional
    public Set<? extends Subscribable> getSet(long chatId) {
        return dataService.getUserProfileData(chatId).getSubscribedAuthors();
    }

    @Override
    public String getTarget() {
        return "Автор";
    }

    @Override
    public String getCallBackForItem() {
        return UNSUBSCRIBE_ME_FROM_AUTHOR_BUTTON;
    }

    @Override
    public String getHandledCallback() {
        return MY_AUTHOR_SUBSCRIPTIONS_BUTTON;
    }

    @Override
    public String getNoItemsAnswer() {
        return messagesService.getReplyText("reply.youDontHaveAuthorsSubscriptions");
    }

    @Override
    public String getAnswerBeforeItems() {
        return messagesService.getReplyText("reply.yourAuthorsSubscriptions");
    }

}
