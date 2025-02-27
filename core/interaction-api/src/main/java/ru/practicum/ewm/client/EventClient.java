package ru.practicum.ewm.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.practicum.ewm.dto.event.EventFullDto;

@FeignClient(name = "event-service", path = "/internal/api/events")
public interface EventClient {

    @GetMapping("/{eventId}")
    EventFullDto getById(@PathVariable Long eventId);
}
