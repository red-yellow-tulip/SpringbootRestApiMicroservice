package app.route;

import app.mapper.Mapper;
import app.service.DataService;
import app.utils.Request;
import app.utils.Response;
import app.utils.dto.RequestDTO;
import app.utils.dto.ResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.BodyInserters.fromPublisher;

@Component
public class DataHandler {

    private static final Logger log = LoggerFactory.getLogger(DataHandler.class);

    private Mapper mapper = null;

    private DataService dataService;

    @Autowired
    public void setStrategyFactory(DataService dataService) {
        this.dataService = dataService;
    }

    @Autowired
    public void setStrategyFactory(Mapper mapper) {
        this.mapper = mapper;
    }


    public Mono<ServerResponse> getById(ServerRequest serverRequest) {

        final String id = serverRequest.pathVariable("id");
        log.info("method GET: /route-data/{id}? id = " + id );

        if (id.isEmpty())
            return ServerResponse.badRequest().contentType(APPLICATION_JSON).bodyValue("id is bad");

        return ServerResponse.ok().contentType(APPLICATION_JSON)
                .body(fromPublisher(
                        Mono.just(mapper.toResponseDTO(dataService.execute(id))),
                        ResponseDTO.class)
                );
    }

    public Mono<ServerResponse> getAll(ServerRequest serverRequest) {

        final int count = Integer.parseInt(serverRequest.pathVariable("count"));

        log.info("method GET: /route-data/all/{count}" );

        Flux<ResponseDTO> response = dataService.getAll(count);

        return  ServerResponse
                .ok()
                .contentType(APPLICATION_JSON)
                .body(response, ResponseDTO.class);
    }

    public Mono<ServerResponse> post(ServerRequest serverRequest) {

        final String id = serverRequest.pathVariable("id");
        final Mono<RequestDTO> requestTO = serverRequest.bodyToMono(RequestDTO.class);

        log.info("method POST: /route-data/{id}? id = " + id );

        return requestTO.flatMap( r -> ServerResponse.ok()
                .contentType(APPLICATION_JSON)
                .body(  Mono.just(mapper.toResponseDTO(dataService.execute(mapper.toRequest(r)))),
                        ResponseDTO.class
                ));
    }
}
