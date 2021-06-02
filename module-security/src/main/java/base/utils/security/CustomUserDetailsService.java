package base.utils.security;

import base.datasource.DatabaseService;
import base.datasource.entity.RemoteUser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

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

        Optional<RemoteUser> opm = databaseService.findRemoveByLogin(userName);

        if (opm.isEmpty()) {
            log.warn("Unknown user: "+userName);
            throw new UsernameNotFoundException("Unknown user: "+userName);
        }
        RemoteUser connectUser = opm.get();
        return  User.builder()
                .username(connectUser.getLogin())
                //.password(encoder.encode(connectUser.getPassword()))
                .password(connectUser.getPassword())
                .roles(connectUser.getRole())
                .build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(length);  // 16 - нельзя, сборка + тесты 5 минут
    }

    public String encode(String password) {

        long start = System.currentTimeMillis();

        String res = encoder.encode(password);

        long executionTime = System.currentTimeMillis() - start;
        log.trace("encode: выполнен за " + executionTime + "мс" );
        return res;
    }
}
