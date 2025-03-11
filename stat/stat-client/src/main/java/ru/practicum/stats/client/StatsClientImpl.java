package ru.practicum.stats.client;

import com.google.protobuf.Timestamp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;
import ru.practicum.grpc.stat.action.ActionTypeProto;
import ru.practicum.grpc.stat.action.UserActionProto;
import ru.practicum.grpc.stat.collector.UserActionControllerGrpc;
import ru.practicum.stats.dto.HitDto;
import ru.practicum.stats.dto.StatsDto;
import ru.practicum.stats.dto.StatsRequestParamsDto;

import java.time.Instant;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class StatsClientImpl implements StatClient {

    @GrpcClient("collector")
    private UserActionControllerGrpc.UserActionControllerBlockingStub userClient;
/*
    @Autowired
    public StatsClientImpl(DiscoveryClient discoveryClient,
                           @Value("${discovery.services.stats-server-id}") String statsServiceId,
                           RestTemplateBuilder builder) {
        this.discoveryClient = discoveryClient;
        this.statsServiceId = statsServiceId;
        this.rest = builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(""))
                .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                .build();

        this.retryTemplate = new RetryTemplate();
        FixedBackOffPolicy fixedBackOffPolicy = new FixedBackOffPolicy();
        fixedBackOffPolicy.setBackOffPeriod(3000L);
        retryTemplate.setBackOffPolicy(fixedBackOffPolicy);

        MaxAttemptsRetryPolicy retryPolicy = new MaxAttemptsRetryPolicy();
        retryPolicy.setMaxAttempts(3);
        retryTemplate.setRetryPolicy(retryPolicy);
    }*/

    @Override
    public void hit(HitDto hitDto) {/*
        HttpEntity<HitDto> requestEntity = new HttpEntity<>(hitDto, defaultHeaders());
        try {
            rest.exchange(makeUri("/hit"), HttpMethod.POST, requestEntity, Object.class);
        } catch (HttpStatusCodeException e) {
            log.error("Hit stats was not successful with code {} and message {}", e.getStatusCode(), e.getMessage(), e);
        } catch (Exception e) {
            log.error("Hit stats was not successful with exception {} and message {}", e.getClass().getName(), e.getMessage(), e);
        }*/
    }

    @Override
    public List<StatsDto> get(StatsRequestParamsDto statsRequestParamsDto) {/*
        if (!checkValidRequestParamsDto(statsRequestParamsDto)) {
            log.error("Get stats was not successful because of incorrect parameters {}", statsRequestParamsDto);
            return List.of();
        }

        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromPath("/stats")
                .queryParam("start", statsRequestParamsDto.getStart().format(DateTimeUtil.DATE_TIME_FORMATTER))
                .queryParam("end", statsRequestParamsDto.getEnd().format(DateTimeUtil.DATE_TIME_FORMATTER));

        if (statsRequestParamsDto.getUris() != null && !statsRequestParamsDto.getUris().isEmpty()) {
            uriComponentsBuilder.queryParam("uris", statsRequestParamsDto.getUris());
        }
        if (statsRequestParamsDto.getUnique() != null) {
            uriComponentsBuilder.queryParam("unique", statsRequestParamsDto.getUnique());
        }
        String uri = uriComponentsBuilder.build(false)
                .encode()
                .toUriString();

        HttpEntity<String> requestEntity = new HttpEntity<>(defaultHeaders());
        ResponseEntity<StatsDto[]> statServerResponse;
        try {
            statServerResponse = rest.exchange(makeUri(uri), HttpMethod.GET, requestEntity, StatsDto[].class);
        } catch (HttpStatusCodeException e) {
            log.error("Get stats was not successful with code {} and message {}", e.getStatusCode(), e.getMessage(), e);
            return List.of();
        } catch (Exception e) {
            log.error("Get stats was not successful with exception {} and message {}", e.getClass().getName(), e.getMessage(), e);
            return List.of();
        }
        statServerResponse.getBody();
        return List.of(Objects.requireNonNull(statServerResponse.getBody()));*/
        return List.of();
    }

    @Override
    public void registerUserAction(long eventId, long userId, ActionTypeProto actionType, Instant instant) {
        log.info("statsClientImpl registerUserAction");
        Timestamp timestamp = Timestamp.newBuilder().setNanos(instant.getNano()).build();
        UserActionProto request = UserActionProto.newBuilder()
                .setEventId(eventId)
                .setUserId(userId)
                .setActionType(actionType)
                .setTimestamp(timestamp)
                .build();
        log.info("statsClientImpl registerUserAction request = {}", request);
        userClient.collectUserAction(request);
    }
/*
    private boolean checkValidRequestParamsDto(StatsRequestParamsDto statsRequestParamsDto) {
        if (statsRequestParamsDto.getStart() == null || statsRequestParamsDto.getEnd() == null
                || statsRequestParamsDto.getStart().isAfter(statsRequestParamsDto.getEnd())) {
            return false;
        }

        if (statsRequestParamsDto.getUris() != null && statsRequestParamsDto.getUris().isEmpty()) {
            return false;
        }

        return true;
    }

    private HttpHeaders defaultHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        return headers;
    }

    private URI makeUri(String path) {
        ServiceInstance instance = retryTemplate.execute(cxt -> getInstance(statsServiceId));
        log.info("Host() = {} Port() = {}", instance.getHost(), instance.getPort());
        return URI.create("http://" + instance.getHost() + ":" + instance.getPort() + path);
    }

    private ServiceInstance getInstance(String serviceId) {
        try {
            return discoveryClient
                    .getInstances(serviceId)
                    .getFirst();
        } catch (Exception exception) {
            throw new RuntimeException(
                    "Ошибка обнаружения адреса сервиса статистики с id: " + serviceId,
                    exception
            );
        }
    }*/
}