package ru.job4j.github.analysis.service;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import ru.job4j.github.analysis.dto.CommitResponseDTO;
import ru.job4j.github.analysis.model.Commit;
import ru.job4j.github.analysis.model.Repository;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class GitHubService {
    @Autowired
    private RestTemplate restTemplate;
    private static final Logger LOGGER = LoggerFactory.getLogger(GitHubService.class);

    public List<Repository> fetchRepositories(String username) {
        String url = "https://api.github.com/users/" + username + "/repos";
        ResponseEntity<List<Repository>> response = restTemplate.exchange(
                url, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Repository>>() {
                });
        return response.getBody();
    }

    public List<Commit> fetchCommits(String owner, String repoName, String sha) {
        String url = String.format("https://api.github.com/repos/%s/%s/commits?sha=%s", owner, repoName, sha);
        try {
            ResponseEntity<List<CommitResponseDTO>> response = restTemplate.exchange(
                    url, HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<CommitResponseDTO>>() {
                    }
            );
            List<CommitResponseDTO> commitDTOs = response.getBody();
            if (commitDTOs != null && !commitDTOs.isEmpty()) {
                return commitDTOs.stream().map(commitResponseDTO -> {
                    Commit commit = new Commit();
                    commit.setMessage(commitResponseDTO.getCommitDTO().getMessage());
                    commit.setAuthor(commitResponseDTO.getCommitDTO().getAuthorDTO().getName());
                    commit.setDate(commitResponseDTO.getCommitDTO().getAuthorDTO().getDate());
                    commit.setSha(commitResponseDTO.getSha());
                    return commit;
                }).collect(Collectors.toList());
            }
        } catch (HttpClientErrorException e) {
            LOGGER.warn("Репозиторий '{}' пуст, коммиты не найдены.", repoName);
        }
        return Collections.emptyList();
    }
}