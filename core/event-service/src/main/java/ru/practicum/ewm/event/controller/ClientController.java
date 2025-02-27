package ru.practicum.ewm.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.client.EventClient;
import ru.practicum.ewm.dto.event.EventFullDto;
import ru.practicum.ewm.event.facade.EventFacade;

@RestController
@RequestMapping(path = "/internal/api/events")
@RequiredArgsConstructor
public class ClientController implements EventClient {
    private final EventFacade eventFacade;

    @Override
    public EventFullDto getById(Long eventId) {
        return eventFacade.getEventById(eventId);
    }
}
