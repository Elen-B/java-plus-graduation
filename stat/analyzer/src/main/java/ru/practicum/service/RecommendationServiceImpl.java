package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.stat.avro.UserActionAvro;
import ru.practicum.grpc.stat.request.InteractionsCountRequestProto;
import ru.practicum.grpc.stat.request.RecommendedEventProto;
import ru.practicum.grpc.stat.request.SimilarEventsRequestProto;
import ru.practicum.grpc.stat.request.UserPredictionsRequestProto;
import ru.practicum.model.EventSimilarity;
import ru.practicum.model.RecommendedEvent;
import ru.practicum.model.UserAction;
import ru.practicum.repository.EventSimilarityRepository;
import ru.practicum.repository.UserActionRepository;
import ru.practicum.mapper.Mapper;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecommendationServiceImpl implements RecommendationService {
    private final EventSimilarityRepository eventSimilarityRepository;
    private final UserActionRepository userActionRepository;

    @Override
    public List<RecommendedEventProto> generateRecommendationsForUser(UserPredictionsRequestProto request) {
        return List.of(RecommendedEventProto.newBuilder().setEventId(1L).setScore(3.3D).build());
    }

    @Override
    public List<RecommendedEventProto> getSimilarEvents(SimilarEventsRequestProto request) {
        List<EventSimilarity> events = eventSimilarityRepository.findAllByEvent(request.getEventId());
        List<UserAction> actions = userActionRepository.findAllByUserId(request.getUserId());

        return events.stream()
                .filter(event -> actions.stream().noneMatch(action ->
                        Objects.equals(action.getEventId(), event.getAeventId()) ||
                                Objects.equals(action.getEventId(), event.getBeventId())))
                .sorted(Comparator.comparingDouble(EventSimilarity::getScore).reversed())
                .limit(request.getMaxResults())
                .map(event -> genRecommendedEventProtoFrom(event, request.getEventId()))
                .toList();
    }

    @Override
    public List<RecommendedEventProto> getInteractionsCount(InteractionsCountRequestProto request) {
        return userActionRepository.getSumWeightForEvents(request.getEventIdList())
                .stream()
                .map(Mapper::mapToRecommendedEventProto)
                .toList();
    }

    @Override
    public void saveUserAction(UserActionAvro userActionAvro) {
        UserAction userAction = Mapper.mapToUserAction(userActionAvro);
        Optional<UserAction> oldUserAction = userActionRepository.findByUserIdAndEventId(userAction.getUserId(), userAction.getEventId());
        if (oldUserAction.isPresent()) {
            userAction.setId(oldUserAction.get().getId());
            if (userAction.getWeight() > oldUserAction.get().getWeight()) {
                userAction.setWeight(oldUserAction.get().getWeight());
            }
        }
        userActionRepository.save(userAction);
    }

    private RecommendedEvent genRecommendedEventFrom(EventSimilarity eventSimilarity, Long eventId) {
        Long recommendedEventId = Objects.equals(eventSimilarity.getAeventId(), eventId) ?
                eventSimilarity.getBeventId() : eventSimilarity.getAeventId();

        return RecommendedEvent.builder()
                .eventId(recommendedEventId)
                .score(eventSimilarity.getScore())
                .build();
    }

    private RecommendedEventProto genRecommendedEventProtoFrom(EventSimilarity eventSimilarity, Long eventId) {
        Long recommendedEventId = Objects.equals(eventSimilarity.getAeventId(), eventId) ?
                eventSimilarity.getBeventId() : eventSimilarity.getAeventId();

        return RecommendedEventProto.newBuilder()
                .setEventId(recommendedEventId)
                .setScore(eventSimilarity.getScore())
                .build();
    }
}
