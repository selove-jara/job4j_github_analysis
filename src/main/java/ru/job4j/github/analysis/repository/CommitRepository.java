package ru.job4j.github.analysis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.job4j.github.analysis.model.Commit;
import ru.job4j.github.analysis.model.Repository;

import java.util.List;

public interface CommitRepository  extends JpaRepository<Commit, Long> {

    @Query("SELECT c FROM Commit c JOIN c.repository r WHERE r.name = :name")
    List<Commit> findByRepositoryName(@Param("name") String name);

    Commit findTopByRepositoryOrderByDateDesc(Repository repository);
}
