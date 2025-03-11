package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Service;
import ru.practicum.config.KafkaConfig;
import ru.practicum.ewm.stat.avro.EventSimilarityAvro;
import ru.practicum.ewm.stat.avro.UserActionAvro;

import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class SimilarityServiceImpl implements SimilarityService {
    private final Producer<String, SpecificRecordBase> producer;
    private final KafkaConfig kafkaConfig;

    @Override
    public Optional<EventSimilarityAvro> updateSimilarity(UserActionAvro userAction) {
        return Optional.of(EventSimilarityAvro.newBuilder()
                .setEventA(1L)
                .setEventB(2L)
                .setScore(9D)
                .setTimestamp(Instant.now())
                .build());
    }

    @Override
    public void collectEventSimilarity(EventSimilarityAvro eventSimilarityAvro) {
        ProducerRecord<String, SpecificRecordBase> rec = new ProducerRecord<>(
                kafkaConfig.getKafkaProperties().getEventsSimilarityTopic(),
                null,
                eventSimilarityAvro.getTimestamp().toEpochMilli(),
                String.valueOf(eventSimilarityAvro.getEventA()),
                eventSimilarityAvro);
        producer.send(rec);
    }

    @Override
    public void close() {
        SimilarityService.super.close();
        if (producer != null) {
            producer.close();
        }
    }
}
