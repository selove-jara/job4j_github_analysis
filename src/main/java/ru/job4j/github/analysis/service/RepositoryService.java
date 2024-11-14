package ru.job4j.github.analysis.service;

import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import ru.job4j.github.analysis.model.Repository;
import ru.job4j.github.analysis.repository.RepositoryRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class RepositoryService {

    private RepositoryRepository repositoryRepository;
    private GitHubService gitHubService;

    @Async
    public void create(String userName) {
        List<Repository> repositories = gitHubService.fetchRepositories(userName);
        for (Repository repository : repositories) {
            repository.setUserName(userName);
            repositoryRepository.save(repository);
        }
    }

    public List<Repository> findAll() {
        return repositoryRepository.findAll();
    }
}
