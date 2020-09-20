package tech.mtright.telegramhabrbot.bot.my_subscriptions_menu;


public interface MySubscriptionsCallbackButtons {
    String ALL_MY_SUBSCRIPTIONS_BUTTON = "getAllMySubscriptions";
    String MY_HUB_SUBSCRIPTIONS_BUTTON = "getMyHubSubscriptions";
    String MY_TAG_SUBSCRIPTIONS_BUTTON = "getMyTagSubscriptions";
    String MY_AUTHOR_SUBSCRIPTIONS_BUTTON = "getMyAuthorSubscriptions";
    String MY_COMPANY_SUBSCRIPTIONS_BUTTON = "getMyCompanySubscriptions";
    String BACK_OFF_MY_SUBSCRIPTIONS_BUTTON = "backOffMySubscriptions";
    String UNSUBSCRIBE_ME_FROM_HUB_BUTTON = "unsubscribeMe:FromHub:";
    String UNSUBSCRIBE_ME_FROM_TAG_BUTTON = "unsubscribeMe:FromTag:";
    String UNSUBSCRIBE_ME_FROM_AUTHOR_BUTTON = "unsubscribeMe:FromAuthor:";
    String UNSUBSCRIBE_ME_FROM_COMPANY_BUTTON = "unsubscribeMe:FromCompany:";
}
