package ru.job4j.github.analysis.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Entity
@Table(name = "repositories")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Repository {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("url")
    private String url;
    @Column(name = "user_name")
    @JsonProperty("user_name")
    private String userName;
}
