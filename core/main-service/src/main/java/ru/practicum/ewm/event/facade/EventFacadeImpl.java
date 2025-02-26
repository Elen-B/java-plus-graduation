package ru.practicum.ewm.event.facade;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.client.LocationClient;
import ru.practicum.ewm.client.UserClient;
import ru.practicum.ewm.dto.location.LocationDto;
import ru.practicum.ewm.dto.location.NewLocationDto;
import ru.practicum.ewm.dto.user.UserShortDto;
import ru.practicum.ewm.error.exception.NotFoundException;
import ru.practicum.ewm.event.dto.*;
import ru.practicum.ewm.event.service.EventService;
import ru.practicum.ewm.participationrequest.dto.ParticipationRequestDto;
import ru.practicum.ewm.util.DateTimeUtil;
import ru.practicum.stats.client.StatClient;
import ru.practicum.stats.dto.HitDto;
import ru.practicum.stats.dto.StatsDto;
import ru.practicum.stats.dto.StatsRequestParamsDto;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventFacadeImpl implements EventFacade {
    private final UserClient userClient;
    private final StatClient statClient;
    private final LocationClient locationClient;

    private final EventService eventService;

    private static final String appNameForStat = "ewm-main-service";

    @Override
    public EventFullDto addEvent(Long id, NewEventDto newEventDto) {
        UserShortDto user = getUserById(id);
        LocationDto location = locationClient.addOrGetLocation(getLocation(newEventDto.getLocation()));

        return eventService.addEvent(user.getId(), newEventDto, location);
    }

    @Override
    public List<EventShortDto> getEventsByUserId(Long id, int from, int size) {
        List<EventShortDto> eventsDto = eventService.getEventsByUserId(id, from, size);
        populateWithStats(eventsDto);
        return eventsDto;
    }

    @Override
    public EventFullDto getEventById(Long userId, Long eventId) {
        EventFullDto eventDto = eventService.getEventById(userId, eventId);
        populateWithStats(List.of(eventDto));

        return eventDto;
    }

    @Override
    public EventFullDto updateEvent(Long userId, Long eventId, UpdateEventUserRequestDto eventUpdateDto) {
        LocationDto location = locationClient.addOrGetLocation(getLocation(eventUpdateDto.getLocation()));

        EventFullDto eventDto = eventService.updateEvent(userId, eventId, location, eventUpdateDto);
        populateWithStats(List.of(eventDto));

        return eventDto;
    }

    @Override
    public EventFullDto update(Long eventId, UpdateEventAdminRequestDto updateEventAdminRequestDto) {
        LocationDto location = locationClient.addOrGetLocation(getLocation(updateEventAdminRequestDto.getLocation()));

        EventFullDto eventDto = eventService.update(eventId, location, updateEventAdminRequestDto);
        populateWithStats(List.of(eventDto));

        return eventDto;
    }

    @Override
    public EventFullDto get(Long eventId, HttpServletRequest request) {
        EventFullDto eventDto = eventService.get(eventId, request);
        populateWithStats(List.of(eventDto));

        hitStat(request);
        return eventDto;
    }

    @Override
    public List<EventFullDto> get(EventAdminFilterParamsDto filters, int from, int size) {
        List<EventFullDto> eventsDto = eventService.get(filters, from, size);
        populateWithStats(eventsDto);

        return eventsDto;
    }

    @Override
    public List<EventShortDto> get(EventPublicFilterParamsDto filters, int from, int size, HttpServletRequest request) {
        List<LocationDto> locations = getLocationsByRadius(filters.getLat(), filters.getLon(), filters.getRadius());
        List<EventShortDto> eventsDto = eventService.get(filters, from, size, locations, request);
        populateWithStats(eventsDto);

        if (filters.getSort() != null && filters.getSort() == EventPublicFilterParamsDto.EventSort.VIEWS)
            eventsDto.sort(Comparator.comparing(EventShortDto::getViews).reversed());

        hitStat(request);
        return eventsDto;
    }

    @Override
    public List<ParticipationRequestDto> getEventAllParticipationRequests(Long eventId, Long userId) {
        return eventService.getEventAllParticipationRequests(eventId, userId);
    }

    @Override
    public EventRequestStatusUpdateResultDto changeEventState(
            Long userId,
            Long eventId,
            EventRequestStatusUpdateRequestDto requestStatusUpdateRequest) {
        UserShortDto user = getUserById(userId);
        return eventService.changeEventState(user.getId(), eventId, requestStatusUpdateRequest);
    }

    private UserShortDto getUserById(Long userId) {
        UserShortDto user = userClient.getById(userId);
        if (user == null) {
            throw new NotFoundException("Такого пользователя не существует: " + userId);
        }

        return user;
    }

    private void populateWithStats(List<? extends EventShortDto> eventsDto) {
        if (eventsDto.isEmpty()) return;

        Map<String, EventShortDto> uris = eventsDto.stream()
                .collect(Collectors.toMap(e -> String.format("/events/%s", e.getId()), e -> e));

        LocalDateTime currentDateTime = DateTimeUtil.currentDateTime();
        List<StatsDto> stats = statClient.get(StatsRequestParamsDto.builder()
                .start(currentDateTime.minusDays(1))
                .end(currentDateTime)
                .uris(uris.keySet().stream().toList())
                .unique(true)
                .build());

        stats.forEach(stat -> Optional.ofNullable(uris.get(stat.getUri()))
                .ifPresent(e -> e.setViews(stat.getHits())));
    }

    private void hitStat(HttpServletRequest request) {
        statClient.hit(HitDto.builder()
                .app(appNameForStat)
                .uri(request.getRequestURI())
                .ip(request.getRemoteAddr())
                .timestamp(DateTimeUtil.currentDateTime())
                .build());
    }

    private List<LocationDto> getLocationsByRadius(Double lat, Double lon, Double radius) {
        if (lat == null || lon == null) {
            return Collections.emptyList();
        }

        return locationClient.getByRadius(lat, lon, radius);
    }

    private NewLocationDto getLocation(NewLocationDto newLocationDto) {
        if (newLocationDto != null) {
            return newLocationDto;
        }
        return NewLocationDto.builder()
                .lat(0D)
                .lon(0D)
                .build();
    }
}
