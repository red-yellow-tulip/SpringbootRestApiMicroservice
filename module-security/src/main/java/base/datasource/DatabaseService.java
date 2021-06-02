package base.datasource;

import base.datasource.entity.RemoteUser;
import base.datasource.entity.jpa.UserRepository;
import base.utils.security.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Optional;

@Service
@Transactional
public class DatabaseService {

    @Resource  // таблица - user
    private UserRepository userRepository;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;


    //------------------------------------------------------------------------------------------------------------------
    //RemoveUser
    @Transactional (propagation = Propagation.REQUIRED )
    public RemoteUser AddRemoveUser(RemoteUser user){
        user.setPassword(customUserDetailsService.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Transactional (readOnly = true)
    public Optional<RemoteUser> findRemoveByLogin(String userName) {
        return userRepository.findByLogin(userName);
    }

    @Transactional (readOnly = true)
    public void deleteRemoteUser(RemoteUser user){
        userRepository.delete(user);
    }


    //------------------------------------------------------------------------------------------------------------------
    //DemoData

    public void createDemoData() {

        clearTable();

        AddRemoveUser(new RemoteUser("USER1","pswd","USER", "userName1"));
        AddRemoveUser(new RemoteUser("USER2","pswd","USER","userName2"));
        AddRemoveUser(new RemoteUser("ADMIN","pswd","ADMIN","ADMIN"));
    }

    @Transactional (propagation = Propagation.REQUIRED )
    public void clearTable() {

        userRepository.deleteAll();
    }

}