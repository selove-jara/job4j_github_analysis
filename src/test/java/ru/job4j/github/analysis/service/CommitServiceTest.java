package ru.job4j.github.analysis.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.scheduling.annotation.EnableAsync;
import ru.job4j.github.analysis.dto.RepositoryCommits;
import ru.job4j.github.analysis.model.Commit;
import ru.job4j.github.analysis.model.Repository;
import ru.job4j.github.analysis.repository.CommitRepository;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@EnableAsync
public class CommitServiceTest {

    @Mock
    private CommitRepository commitRepository;

    @Mock
    private GitHubService gitHubService;

    @InjectMocks
    private CommitService commitService;

    private Repository repository;
    private Commit commit;

    @BeforeEach
    public void setUp() {
        repository = new Repository();
        repository.setName("test");
        repository.setUrl("https://github.com/user/test");

        commit = new Commit();
        commit.setMessage("Initial commit");
        commit.setAuthor("user");
        commit.setRepository(repository);
    }

    @Test
    void whenFindCommitsByRepositoryName() {
        when(commitRepository.findByRepositoryName("test")).thenReturn(Collections.singletonList(commit));

        var commits = commitRepository.findByRepositoryName("test");
        assertThat(commits).hasSize(1);
        assertThat(commits.get(0).getMessage()).isEqualTo("Initial commit");

        verify(commitRepository, times(1)).findByRepositoryName("test");
    }

    @Test
    void whenGetCommitsByUserName() {
        when(gitHubService.fetchRepositories("user")).thenReturn(Collections.singletonList(repository));
        when(commitRepository.findByRepositoryName("test")).thenReturn(Collections.singletonList(commit));

        List<RepositoryCommits> repositoryCommits = commitService.getCommitsByUserName("user");

        assertThat(repositoryCommits).hasSize(1);
        assertThat(repositoryCommits.get(0).getUserName()).isEqualTo("user");
        assertThat(repositoryCommits.get(0).getName()).isEqualTo("test");

        verify(gitHubService, times(1)).fetchRepositories("user");
        verify(commitRepository, times(1)).findByRepositoryName("test");
    }
}