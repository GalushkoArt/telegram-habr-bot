package tech.mtright.telegramhabrbot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.mtright.telegramhabrbot.models.Company;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Integer> {
    Company findByNameIgnoreCase(String name);

    Company findByName(String name);
}
