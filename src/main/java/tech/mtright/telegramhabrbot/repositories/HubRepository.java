package tech.mtright.telegramhabrbot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.mtright.telegramhabrbot.models.Hub;

@Repository
public interface HubRepository extends JpaRepository<Hub, Integer> {
    Hub findByNameIgnoreCase(String name);

    Hub findByName(String name);
}
