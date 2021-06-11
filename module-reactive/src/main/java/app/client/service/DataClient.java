package app.client.service;

import app.controller.dto.RequestDTO;
import app.controller.dto.ResponseDTO;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class DataClient {

    private WebClient webClient;

    @Autowired
    public void setWebClient (WebClient webClient){
        this.webClient = webClient;

    }

    public Mono<ResponseDTO> getDataByIdAsync(final String endpoint) {
        return webClient
                .get()
                .uri(endpoint)
                .retrieve()
                .bodyToMono(ResponseDTO.class);
    }

    public ResponseDTO postDataByIdBlock(final String endpoint, RequestDTO requestDTO) {
        return webClient
                .post()
                .uri(endpoint)
                .body(Mono.just(requestDTO), RequestDTO.class)
                .retrieve()
                .bodyToMono(ResponseDTO.class)
                .block();
    }

    public Mono<ResponseDTO> postDataByIdAsync(final String endpoint, RequestDTO requestDTO) {
        return webClient
                .post()
                .uri(endpoint)
                .body(Mono.just(requestDTO), RequestDTO.class)
                .retrieve()
                .bodyToMono(ResponseDTO.class);
    }

    public ResponseDTO getDataByIdBlock(final String endpoint) {
        return webClient
                .get()
                .uri(endpoint)
                .retrieve()
                .bodyToMono(ResponseDTO.class)
                .block();
    }
}
