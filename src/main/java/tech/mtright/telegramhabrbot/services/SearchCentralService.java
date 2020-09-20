package tech.mtright.telegramhabrbot.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import tech.mtright.telegramhabrbot.models.Author;
import tech.mtright.telegramhabrbot.models.Company;
import tech.mtright.telegramhabrbot.models.Hub;
import tech.mtright.telegramhabrbot.models.Tag;
import tech.mtright.telegramhabrbot.repositories.AuthorRepository;
import tech.mtright.telegramhabrbot.repositories.CompanyRepository;
import tech.mtright.telegramhabrbot.repositories.HubRepository;
import tech.mtright.telegramhabrbot.repositories.TagRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SearchCentralService implements SearchService {
    @Autowired
    @LoadBalanced
    private RestTemplate restTemplate;
    @Autowired
    private HubRepository hubRepository;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private CompanyRepository companyRepository;

    @Override
    @Transactional
    public Optional<Tag> getIfRelevant(String tag) {
        Tag repositoryTag = tagRepository.findByNameIgnoreCase(tag);
        if (repositoryTag != null) {
            return Optional.of(repositoryTag);
        }
        Boolean answer = restTemplate.getForObject("http://POSTCENTRAL/api/isTagRelevant?tag={tag}", Boolean.class, tag);
        if (answer != null && answer) {
            Tag newTag = Tag.builder().name(tag).build();
            tagRepository.save(newTag);
            return Optional.of(newTag);
        } else {
            return Optional.empty();
        }
    }

    @Override
    @Transactional
    public List<Company> searchCompaniesByName(String name) {
        Company company = companyRepository.findByNameIgnoreCase(name);
        if (company != null) {
            return List.of(company);
        } else {
            List<Company> body = restTemplate.exchange("http://POSTCENTRAL/api/getCompanies?name=" + name,
                    HttpMethod.GET, null, new ParameterizedTypeReference<List<Company>>() {
                    }).getBody();
            if (body == null) {
                return new ArrayList<>();
            } else {
                return body.stream().map(Company::getName)
                        .map(item -> {
                            Company repoCompany = companyRepository.findByName(item);
                            if (repoCompany != null) {
                                return repoCompany;
                            } else {
                                Company builtCompany = Company.builder().name(item).build();
                                companyRepository.save(builtCompany);
                                return builtCompany;
                            }
                        }).collect(Collectors.toList());
            }
        }
    }

    @Override
    @Transactional
    public List<Hub> searchHubsByName(String name) {
        Hub hub = hubRepository.findByNameIgnoreCase(name);
        if (hub != null) {
            return List.of(hub);
        } else {
            List<Hub> body = restTemplate.exchange("http://POSTCENTRAL/api/getHubs?name=" + name,
                    HttpMethod.GET, null, new ParameterizedTypeReference<List<Hub>>() {
                    }).getBody();
            if (body == null) {
                return new ArrayList<>();
            } else {
                return body.stream().map(Hub::getName)
                        .map(item -> {
                            Hub repoHub = hubRepository.findByName(item);
                            if (repoHub != null) {
                                return repoHub;
                            } else {
                                Hub builtHub = Hub.builder().name(item).build();
                                hubRepository.save(builtHub);
                                return builtHub;
                            }
                        }).collect(Collectors.toList());
            }
        }
    }

    @Override
    @Transactional
    public List<Author> searchAuthorsByName(String name) {
        Author author = authorRepository.findByNameIgnoreCaseOrFullNameIgnoreCase(name, name);
        if (author != null) {
            return List.of(author);
        } else {
            List<Author> body = restTemplate.exchange("http://POSTCENTRAL/api/findAuthors?name=" + name,
                    HttpMethod.GET, null, new ParameterizedTypeReference<List<Author>>() {
                    }).getBody();
            if (body == null) {
                return new ArrayList<>();
            } else {
                return body.stream()
                        .map(item -> {
                            Author repoAuthor = authorRepository.findByNameAndFullName(item.getName(), item.getFullName());
                            if (repoAuthor != null) {
                                return repoAuthor;
                            } else {
                                Author builtAuthor = Author.builder().name(item.getName()).fullName(item.getFullName()).build();
                                authorRepository.save(builtAuthor);
                                return builtAuthor;
                            }
                        }).collect(Collectors.toList());
            }
        }
    }
}
