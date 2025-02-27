package ru.practicum.ewm.service;

import ru.practicum.ewm.dto.event.EventFullDto;
import ru.practicum.ewm.dto.request.ParticipationRequestCountDto;
import ru.practicum.ewm.dto.request.ParticipationRequestDto;
import ru.practicum.ewm.dto.request.ParticipationRequestStatus;
import ru.practicum.ewm.model.ParticipationRequest;

import java.util.List;

public interface ParticipationRequestService {
    ParticipationRequestDto create(Long userId, EventFullDto event);

    List<ParticipationRequestDto> get(Long userId);

    ParticipationRequestDto cancel(Long userId, Long requestId);

    List<ParticipationRequestDto> findAllByEventIdAndStatus(Long eventId, ParticipationRequestStatus status);

    List<ParticipationRequestDto> getByIds(List<Long> ids);

    List<ParticipationRequestDto> updateStatus(ParticipationRequestStatus status, List<Long> ids);

    List<ParticipationRequestCountDto> getConfirmedCount(List<Long> ids);
}