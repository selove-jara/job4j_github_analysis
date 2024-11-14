package ru.job4j.github.analysis.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommitResponseDTO {
    @JsonProperty("commit")
    private CommitDTO commitDTO;

    private String sha;
}
