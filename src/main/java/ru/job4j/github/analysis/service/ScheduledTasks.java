package ru.job4j.github.analysis.service;

import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.job4j.github.analysis.model.Commit;
import ru.job4j.github.analysis.model.Repository;

import java.util.List;

@AllArgsConstructor
@Service
public class ScheduledTasks {

    private final RepositoryService repositoryService;

    private final GitHubService gitHubService;

    private final CommitService commitService;

    @Scheduled(fixedRateString = "${scheduler.fixedRate}")
    public void fetchCommits() {
        List<Repository> repositories = repositoryService.findAll();
        for (Repository repository : repositories) {

            var latsCommit = commitService.findLatsCommit(repository);
            var lastCommitSha = latsCommit != null ? latsCommit.getSha() : "";
            List<Commit> commits = gitHubService.fetchCommits(
                    repository.getUserName(), repository.getName(), lastCommitSha);
                for (Commit commit : commits) {
                    commit.setRepository(repository);
                    commitService.save(commit);
            }
        }
    }
}
