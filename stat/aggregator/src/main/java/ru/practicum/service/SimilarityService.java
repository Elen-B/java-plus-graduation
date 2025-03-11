package ru.practicum.service;

import ru.practicum.ewm.stat.avro.EventSimilarityAvro;
import ru.practicum.ewm.stat.avro.UserActionAvro;

import java.util.Optional;

public interface SimilarityService {

    Optional<EventSimilarityAvro> updateSimilarity(UserActionAvro userAction);

    void collectEventSimilarity(EventSimilarityAvro eventSimilarityAvro);

    default void close() {}
}
