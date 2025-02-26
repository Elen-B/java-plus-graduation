package ru.practicum.ewm.event.service;

import jakarta.servlet.http.HttpServletRequest;
import ru.practicum.ewm.dto.location.LocationDto;
import ru.practicum.ewm.event.dto.*;
import ru.practicum.ewm.participationrequest.dto.ParticipationRequestDto;

import java.util.List;

public interface EventService {
    EventFullDto addEvent(Long id, NewEventDto newEventDto, LocationDto location);

    List<EventShortDto> getEventsByUserId(Long id, int from, int size);

    EventFullDto getEventById(Long userId, Long eventId);

    EventFullDto updateEvent(Long userId, Long eventId, LocationDto location, UpdateEventUserRequestDto eventUpdateDto);

    EventFullDto update(Long eventId, LocationDto location, UpdateEventAdminRequestDto updateEventAdminRequestDto);

    EventFullDto get(Long eventId, HttpServletRequest request);

    List<EventFullDto> get(EventAdminFilterParamsDto filters, int from, int size);

    List<EventShortDto> get(EventPublicFilterParamsDto filters, int from, int size, List<LocationDto> locations,
                            HttpServletRequest request);

    List<ParticipationRequestDto> getEventAllParticipationRequests(Long eventId, Long userId);

    EventRequestStatusUpdateResultDto changeEventState(Long userId, Long eventId,
                                                       EventRequestStatusUpdateRequestDto requestStatusUpdateRequest);
}