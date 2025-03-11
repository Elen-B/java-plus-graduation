package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.model.EventSimilarity;

import java.util.Optional;

@Repository
public interface EventSimilarityRepository extends JpaRepository<EventSimilarity, Long> {

    Optional<EventSimilarity> findByAeventIdAndBeventId(Long aEventId, Long bEventId);
}
