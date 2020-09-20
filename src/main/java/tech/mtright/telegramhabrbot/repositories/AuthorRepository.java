package tech.mtright.telegramhabrbot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.mtright.telegramhabrbot.models.Author;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Integer> {
    Author findByNameIgnoreCaseOrFullNameIgnoreCase(String name, String fullName);

    Author findByNameAndFullName(String name, String fullName);
}
