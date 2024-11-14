package ru.job4j.github.analysis.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommitDTO {
    @JsonProperty("message")
    private String message;

    @JsonProperty("author")
    private AuthorDTO authorDTO;
}
