import app.ApplicationRunner;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Predicate;

@SpringBootTest(classes = {ApplicationRunner.class})
public class TestFluxExample {

    private static final Logger log = LoggerFactory.getLogger(TestFluxExample.class);



    @Test
    public void Test1() {
        Flux.
                interval(Duration.ofSeconds(1)).
                doOnEach(
                        elem -> log.info(String.valueOf(elem.get())))
                .blockLast();
    }

    @Test
    public void Test2(){
        Flux.
                range(777,10)
                .skip(3)
                .take(4)
                .doOnEach(
                        elem -> log.info(String.valueOf(elem.get())))
                .blockLast();
    }

    @Test
    public void Test3()  {
        Flux<String> sequence = Flux.just("foo", "bar", "foobar");
                sequence
                .skip(1)
                .take(2)
                .doOnEach(
                        elem -> log.info(String.valueOf(elem.get())))
                .blockLast();
    }

    @Test
    public void Test4()  {
        List<String> iterable = Arrays.asList("A", "B", "C","D","E","F","G");
        Flux<String> sequence = Flux.fromIterable(iterable);

        sequence
                .skip(1)
                .take(4)
                .doOnEach(
                        elem -> log.info(String.valueOf(elem.get())))    //  <----
                .blockLast();
    }

    @Test
    public void Test5()  {
        List<String> iterable = Arrays.asList("A", "B", "C","D","E","F","G",null);
        Flux<String> sequence = Flux.fromIterable(iterable);

        sequence.subscribe(
                elem -> log.info(String.valueOf(elem)),
                error -> System.err.println("Error on subscribe: " + error)   //  <----
        );
    }

    @Test
    public void Test6()  {
        List<String> iterable = Arrays.asList("A", "B", "C","D","E","F","G");
        Flux<String> sequence = Flux.fromIterable(iterable);

        sequence.subscribe(
                elem -> log.info(String.valueOf(elem)),
                error -> System.err.println("Error on subscribe: " + error),
                () -> {System.out.println("Done");}     //  <----
        );
    }

    @Test
    public void Test6_1()  {
        List<String> iterable = Arrays.asList("A", "B", "C",null,"E","F","G");
        Flux<String> sequence = Flux.fromIterable(iterable);

        sequence
                .onErrorResume(error -> Mono.just("d"))   //  <----  A B C d
                .subscribe(
                    elem -> log.info(String.valueOf(elem))
        );
    }

    @Test
    public void Test6_2()  {
        List<String> iterable = Arrays.asList("A", "B", "C",null,"E","F","G");
        Flux<String> sequence = Flux.fromIterable(iterable);

        sequence
                .onErrorResume(error -> {
                            if (error instanceof NullPointerException)
                                return Mono.just("d");           //  <----  A B C d
                            else
                                return Flux.error(error);
                })
                .subscribe(
                        elem -> log.info(String.valueOf(elem))
                );
    }

    @Test
    public void Test6_3()  {
        List<String> iterable = Arrays.asList("A", "B", "C",null,"E","F","G");
        Flux<String> sequence = Flux.fromIterable(iterable);

        sequence
                .onErrorReturn(e -> e instanceof  NullPointerException,"d")    //  <----  A B C d
                .subscribe(
                        elem -> log.info(String.valueOf(elem))
                );
    }

    @Test
    public void Test6_4() throws InterruptedException {
        List<String> iterable = Arrays.asList("A", "B", "C","D","E","F","G");
        Flux<String> sequence = Flux.fromIterable(iterable);

        sequence
                .onErrorReturn(e -> e instanceof  NullPointerException,"d")
                .elapsed()
                .retry(2)         //  <----  A B C d
                .subscribe(
                        elem -> log.info(String.valueOf(elem))
                );

        TimeUnit.SECONDS.sleep(2);
    }


    @Test
    public void Test7() {
        Flux<Integer> sequence = Flux.range(0, 100).publishOn(Schedulers.newSingle("1")/*single()*/  /*parallel()*/);  //  <----
        //вызовы onNext, onComplete и onError будут происходить в шедулере single.
        sequence.subscribe(n -> {
            System.out.println("n = " + n);
            System.out.println("Thread.currentThread() = " + Thread.currentThread());  //  <----
        });
        sequence.blockLast();
    }

    @Test
    public void Test8()  {
        List<String> iterable = Arrays.asList("A", "B", "C","D","E","F","G");
        Flux<String> sequence = Flux.fromIterable(iterable);

        sequence
                .flatMap(r -> Mono.just(r.toLowerCase()))       //  <----
                .switchIfEmpty(Mono.just(UUID.randomUUID().toString()))     //  <----
                .take(15) // все равно вернет не больше чем size iterable
                .subscribe(
                    elem -> log.info(String.valueOf(elem))  );
    }

    @Test
    public void Test9()  {
        Flux<Integer> ints =  Flux.just(1, 2, 3, 4, 5)
                .map(i -> {
                    if (i != 4) return i;
                    throw new RuntimeException("Got to 4");
                })
                .map(i -> i + 100);
        ints
            .switchIfEmpty(Flux.just(7))
            .subscribe(
                System.out::println,
                error -> System.err.println("Error: " + error),
                () -> System.out.println("Successfull")
            );
    }

    @Test
    public void Test10()  {

        Mono<List<Integer>> listMono = Flux.just(1, 2, 3, 4)
                .filter(value -> value % 2 == 0)
                .collectList();
        log.info(listMono.block().toString());
    }

    @Test
    public void Test11()  {

        Flux<List<Integer>> listMono1 = Flux.just(Lists.list(1, 3, 5, 7));
        Flux<List<Integer>> listMono2 = Flux.just(Lists.list(0, 2, 4, 6,8,10));

       Flux.just(0)
                .mergeWith(listMono1.flatMapIterable( l -> l))  //  <----
                .mergeWith(listMono2.flatMapIterable( l -> l))  //  <----
                .sort()                                         //  <----
                .subscribe(
                    System.out::println,
                    error -> System.err.println("Error: " + error),
                    () -> System.out.println("Successfull")
                );

    }

}
