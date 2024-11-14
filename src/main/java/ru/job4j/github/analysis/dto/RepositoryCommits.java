package ru.job4j.github.analysis.dto;

import lombok.*;
import ru.job4j.github.analysis.model.Commit;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RepositoryCommits {
    private Long id;
    private String userName;
    private String name;
    private String url;
    private List<Commit> list = new ArrayList<>();
}
