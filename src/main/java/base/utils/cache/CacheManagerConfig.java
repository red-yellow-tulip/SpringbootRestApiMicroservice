package base.utils.cache;

import com.google.common.cache.CacheBuilder;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Configuration
public class CacheManagerConfig {

    private final TimeUnit unit = TimeUnit.MINUTES;
    private final Integer peroid = 2;
    private final Integer size = 65536;
    private final List<String> topics = Arrays.asList("Student","Student.id","Student.fio","Student.flag","RemoveUser");

   @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager simpleCacheManager = new SimpleCacheManager();
        simpleCacheManager.setCaches(topics.stream().map(this::createConcurrentMapCache).collect(Collectors.toList()));
        return simpleCacheManager;
    }

    private Cache createConcurrentMapCache(final String name) {
        return new ConcurrentMapCache(name,
                CacheBuilder.newBuilder()
                        .expireAfterWrite(peroid, unit)
                        .initialCapacity(size/2)
                        .maximumSize(size)
                        .build()
                        .asMap(),
                false);
    }
}
