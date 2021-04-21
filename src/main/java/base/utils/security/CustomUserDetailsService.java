package base.utils.security;

import base.datasource.DatabaseService;
import base.datasource.entity.UserDb;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Configuration
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger log = LogManager.getLogger(CustomUserDetailsService.class.getName());

    @Autowired
    private DatabaseService databaseService;

    @Autowired
    private BCryptPasswordEncoder encoder;

    final int length = 8;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

        UserDb connectUser = databaseService.findUserByLogin(userName);

        if (connectUser == null) {
            log.warn("Unknown user: "+userName);
            throw new UsernameNotFoundException("Unknown user: "+userName);
        }

        UserDetails user = User.builder()
                .username(connectUser.getLogin())
                //.password(encoder.encode(connectUser.getPassword()))
                .password(connectUser.getPassword())
                .roles(connectUser.getRole())
                .build();

        return user;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(length);  // 16 - нельзя, сборка + тесты 5 минут
    }

    public String encode(String password) {
        long start = System.currentTimeMillis();

        String res = encoder.encode(password);

        long executionTime = System.currentTimeMillis() - start;
        log.warn("encode: выполнен за " + executionTime + "мс" );
        return res;
    }
}
