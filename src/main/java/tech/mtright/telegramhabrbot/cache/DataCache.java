package tech.mtright.telegramhabrbot.cache;


import tech.mtright.telegramhabrbot.bot.BotState;
import tech.mtright.telegramhabrbot.models.UserProfileData;

public interface DataCache {
    void setUsersCurrentBotState(long chatId, BotState botState);

    BotState getUsersCurrentBotState(long chatId);

    UserProfileData getUserProfileData(long chatId);

    void saveUserProfileData(long chatId, UserProfileData userProfileData);

    boolean hasUser(long chatId);

    void loadOrCreateUser(long chatId);
}
