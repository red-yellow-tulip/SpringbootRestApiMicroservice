package app.client.service;

import app.utils.dto.RequestDTO;
import app.utils.dto.ResponseDTO;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;
import java.time.Duration;

@Service
@AllArgsConstructor
public class DataClient {

    private static final Logger log = LoggerFactory.getLogger(DataClient.class);

    private WebClient webClient;

    @Autowired
    public void setWebClient (WebClient webClient){
        this.webClient = webClient;

    }

    public Flux<ResponseDTO> getDataByIdAsync(final String endpoint) {
        return webClient
                .get()
                .uri(endpoint)
                .retrieve()
                .bodyToFlux(ResponseDTO.class);
    }

    public ResponseDTO postDataByIdBlock(final String endpoint, RequestDTO requestDTO) {
        return webClient
                .post()
                .uri(endpoint)
                .body(Mono.just(requestDTO), RequestDTO.class)
                .retrieve()
                .bodyToMono(ResponseDTO.class)
                .doOnError(error -> log.error("An error has occurred {}", error.getMessage()))
                .block();
    }

    public Mono<ResponseDTO> postDataById(final String endpoint, RequestDTO requestDTO) {
        return webClient
                .post()
                .uri(endpoint)
                .body(Mono.just(requestDTO), RequestDTO.class)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError,
                        error -> Mono.error(new RuntimeException("API not found")))
                .onStatus(HttpStatus::is5xxServerError,
                        error -> Mono.error(new RuntimeException("Server is not responding")))
                .bodyToMono(ResponseDTO.class)
                .doOnError(error -> log.error("An error has occurred {}", error.getMessage()))
                ;
    }


    public Mono<ResponseDTO> postDataByIdAsync(final String endpoint, RequestDTO requestDTO) {
        return webClient
                .post()
                .uri(endpoint)
                .body(Mono.just(requestDTO), RequestDTO.class)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError,
                        error -> Mono.error(new RuntimeException("API not found")))
                .onStatus(HttpStatus::is5xxServerError,
                        error -> Mono.error(new RuntimeException("Server is not responding")))
                .bodyToMono(ResponseDTO.class)
                .doOnError(error -> log.error("An error has occurred {}", error.getMessage()))
                .retryWhen(Retry.fixedDelay(3, Duration.ofMillis(100)));
    }

    public ResponseDTO getDataByIdBlock(final String endpoint) {
        return webClient
                .get()
                .uri(endpoint)
                .retrieve()
                .bodyToMono(ResponseDTO.class)
                .doOnError(error -> log.error("An error has occurred {}", error.getMessage()))
                .block();
    }

    public Flux<ResponseDTO> getAllDataAsync(final String endpoint) {
        return webClient
                .get()
                .uri(endpoint)
                .retrieve()
                .bodyToFlux(ResponseDTO.class)
                .doOnError(error -> log.error("An error has occurred {}", error.getMessage()));
    }
}
