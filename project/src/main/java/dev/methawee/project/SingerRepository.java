package dev.methawee.project;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SingerRepository extends JpaRepository<Singer, Long> {
    List<Singer> findByGenre(String genre);
}

