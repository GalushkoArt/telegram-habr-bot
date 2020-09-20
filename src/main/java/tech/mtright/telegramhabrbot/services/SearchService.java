package tech.mtright.telegramhabrbot.services;

import tech.mtright.telegramhabrbot.models.Author;
import tech.mtright.telegramhabrbot.models.Company;
import tech.mtright.telegramhabrbot.models.Hub;
import tech.mtright.telegramhabrbot.models.Tag;

import java.util.List;
import java.util.Optional;

public interface SearchService {
    Optional<Tag> getIfRelevant(String tag);

    List<Company> searchCompaniesByName(String name);

    List<Hub> searchHubsByName(String name);

    List<Author> searchAuthorsByName(String name);
}
