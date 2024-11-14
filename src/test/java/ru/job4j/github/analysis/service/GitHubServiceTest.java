package ru.job4j.github.analysis.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import ru.job4j.github.analysis.dto.AuthorDTO;
import ru.job4j.github.analysis.dto.CommitDTO;
import ru.job4j.github.analysis.dto.CommitResponseDTO;
import ru.job4j.github.analysis.model.Commit;
import ru.job4j.github.analysis.model.Repository;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.any;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class GitHubServiceTest {
    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private GitHubService gitHubService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void whenFetchRepositories() {
        String username = "user";
        String url = "https://api.github.com/users/" + username + "/repos";
        Repository repository = new Repository();
        repository.setName("test");
        List<Repository> mockResponse = Collections.singletonList(repository);
        when(restTemplate.exchange(
                eq(url),
                eq(HttpMethod.GET),
                any(),
                any(ParameterizedTypeReference.class)
        )).thenReturn(new ResponseEntity<>(mockResponse, HttpStatus.OK));
        var repositories = gitHubService.fetchRepositories(username);
        assertThat(repositories).hasSize(1);
        assertThat(repositories.get(0).getName()).isEqualTo("test");
    }

    @Test
    void whenFetchCommits_thenReturnsCommitList() {
        // Данные для мока
        String owner = "user";
        String repoName = "test-repo";
        String sha = "123456abcdef";
        String url = String.format("https://api.github.com/repos/%s/%s/commits?sha=%s", owner, repoName, sha);

        CommitResponseDTO commitResponseDTO = new CommitResponseDTO();
        CommitDTO commitDTO = new CommitDTO();
        commitDTO.setMessage("Initial commit");
        commitDTO.setAuthorDTO(new AuthorDTO("user", "user@example.com", LocalDateTime.parse("2024-01-01T00:00:00")));
        commitResponseDTO.setCommitDTO(commitDTO);

        List<CommitResponseDTO> mockResponse = Collections.singletonList(commitResponseDTO);

        when(restTemplate.exchange(
                eq(url),
                eq(HttpMethod.GET),
                any(),
                eq(new ParameterizedTypeReference<List<CommitResponseDTO>>() {})
        )).thenReturn(ResponseEntity.ok(mockResponse));

        List<Commit> commits = gitHubService.fetchCommits(owner, repoName, sha);

        assertNotNull(commits);
        assertEquals(1, commits.size());
        assertEquals("Initial commit", commits.get(0).getMessage());
        assertEquals("user", commits.get(0).getAuthor());
        assertEquals(LocalDateTime.parse("2024-01-01T00:00:00"), commits.get(0).getDate());
    }

    @Test
    void whenFetchCommitsRepoNotFoundThenReturnsEmptyList() {
        String owner = "user";
        String sha = "123456abcdef";
        String repoName = "non-existing-repo";
        String url = String.format("https://api.github.com/repos/%s/%s/commits?sha=%s", owner, repoName, sha);
        when(restTemplate.exchange(
                eq(url),
                eq(HttpMethod.GET),
                any(),
                eq(new ParameterizedTypeReference<List<CommitResponseDTO>>() {
                })
        )).thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        List<Commit> commits = gitHubService.fetchCommits(owner, repoName, sha);

        assertEquals(Collections.emptyList(), commits);
    }
}

