package ru.job4j.github.analysis.service;

import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import ru.job4j.github.analysis.dto.RepositoryCommits;
import ru.job4j.github.analysis.model.Commit;
import ru.job4j.github.analysis.model.Repository;
import ru.job4j.github.analysis.repository.CommitRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CommitService {
    private final GitHubService gitHubService;
    private final CommitRepository commitRepository;

    public List<RepositoryCommits> getCommitsByUserName(String userName) {
        return gitHubService.fetchRepositories(userName).stream()
                .map(repository -> {
                    List<Commit> commits = commitRepository.findByRepositoryName(repository.getName());

                    RepositoryCommits repositoryCommits = new RepositoryCommits();
                    repositoryCommits.setId(repository.getId());
                    repositoryCommits.setUserName(userName);
                    repositoryCommits.setName(repository.getName());
                    repositoryCommits.setUrl(repository.getUrl());
                    repositoryCommits.setList(commits);
                    return repositoryCommits;
                })
                .collect(Collectors.toList());
    }

    @Async
    public void save(Commit commit) {
        commitRepository.save(commit);
    }

    public Commit findLatsCommit(Repository repository) {
        return commitRepository.findTopByRepositoryOrderByDateDesc(repository);
    }
}
