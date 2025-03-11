package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.model.EventSimilarity;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventSimilarityRepository extends JpaRepository<EventSimilarity, Long> {

    Optional<EventSimilarity> findByAeventIdAndBeventId(Long aEventId, Long bEventId);

    @Query("select es from EventSimilarity es where es.aeventId = :id or es.beventId = :id")
    List<EventSimilarity> findAllByEvent(@Param("id") long eventId);
}
