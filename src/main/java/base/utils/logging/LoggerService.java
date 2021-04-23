package base.utils.logging;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.annotation.Resource;

@Configuration
@Resource
public class LoggerService {

    private static final Logger log = LogManager.getLogger(LoggerService.class.getName());

    @Bean
    public LoggerService getLoggerService(){
        return new LoggerService();
    }

    public Logger log(){
        return log;
    }
}
