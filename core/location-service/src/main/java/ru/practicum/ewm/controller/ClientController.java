package ru.practicum.ewm.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.client.LocationClient;
import ru.practicum.ewm.dto.location.LocationDto;
import ru.practicum.ewm.dto.location.NewLocationDto;
import ru.practicum.ewm.service.LocationService;

import java.util.List;

@RestController
@RequestMapping(path = "/internal/api/locations")
@RequiredArgsConstructor
public class ClientController implements LocationClient {
    private final LocationService locationService;

    @Override
    @PostMapping
    public LocationDto addOrGetLocation(@RequestBody NewLocationDto newLocationDto) {
        return locationService.addOrGetLocation(newLocationDto);
    }

    @Override
    @GetMapping
    public List<LocationDto> getByRadius(@RequestParam(name = "latitude") Double lat,
                                         @RequestParam(name = "longitude") Double lon,
                                         @RequestParam(name = "radius") Double radius) {
        return locationService.getByRadius(lat, lon, radius);
    }

    @Override
    public LocationDto getById(Long locationId) {
        return locationService.getById(locationId);
    }
}
