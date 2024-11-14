package ru.job4j.github.analysis.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.job4j.github.analysis.model.Commit;
import ru.job4j.github.analysis.model.Repository;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ScheduledTasksTest {

    @Mock
    private RepositoryService repositoryService;

    @Mock
    private GitHubService gitHubService;

    @Mock
    private CommitService commitService;

    @InjectMocks
    private ScheduledTasks scheduledTasks;

    @Mock
    private Repository repository;

    @Mock
    private Commit commit;

    @BeforeEach
    public void setUp() {
        repository = new Repository();
        repository.setName("test");
        repository.setUrl("https://github.com/user/test");
        repository.setUserName("user");

        commit = new Commit();
        commit.setMessage("Commit");
        commit.setAuthor("user");
        commit.setRepository(repository);
    }

    @Test
    void whenFetchCommitsWithLatestCommit() {
        Commit commit1 = new Commit();
        commit1.setRepository(repository);

        List<Commit> newCommits = Collections.singletonList(commit);
        when(repositoryService.findAll()).thenReturn(Collections.singletonList(repository));
        lenient().when(gitHubService.fetchCommits(eq("user"), eq("test"), anyString())).thenReturn(newCommits);

        scheduledTasks.fetchCommits();

        verify(repositoryService, times(1)).findAll();
        verify(gitHubService, times(1)).fetchCommits(eq("user"), eq("test"), anyString());
        verify(commitService, times(1)).save(commit);
        assertThat(repository).isEqualTo(commit.getRepository());
    }
}