package tech.mtright.telegramhabrbot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.mtright.telegramhabrbot.models.Tag;

@Repository
public interface TagRepository extends JpaRepository<Tag, Integer> {
    Tag findByNameIgnoreCase(String name);
}
