package ru.practicum.stats.client;

import ru.practicum.grpc.stat.action.ActionTypeProto;
import ru.practicum.grpc.stat.request.RecommendedEventProto;
import ru.practicum.stats.dto.HitDto;
import ru.practicum.stats.dto.StatsDto;
import ru.practicum.stats.dto.StatsRequestParamsDto;

import java.time.Instant;
import java.util.List;
import java.util.stream.Stream;

public interface StatClient {
    void hit(HitDto hitDto);

    List<StatsDto> get(StatsRequestParamsDto statsRequestParamsDto);

    void registerUserAction(long eventId, long userId, ActionTypeProto actionType, Instant instant);

    Stream<RecommendedEventProto> getSimilarEvents(long eventId, long userId, int maxResults);

    Stream<RecommendedEventProto> getRecommendationsForUser(long userId, int maxResults);

    Stream<RecommendedEventProto> getInteractionsCount(List<Long> eventIds);
}