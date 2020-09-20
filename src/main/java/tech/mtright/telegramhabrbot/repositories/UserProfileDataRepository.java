package tech.mtright.telegramhabrbot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.mtright.telegramhabrbot.models.UserProfileData;

@Repository
public interface UserProfileDataRepository extends JpaRepository<UserProfileData, Integer> {
    UserProfileData findByChatId(long chatId);

    void deleteByChatId(long chatId);
}
