package base.datasource.entity.jpa;

import base.datasource.entity.RemoteUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<RemoteUser, Long> {

    Optional<RemoteUser> findByLogin(String userName);

}
