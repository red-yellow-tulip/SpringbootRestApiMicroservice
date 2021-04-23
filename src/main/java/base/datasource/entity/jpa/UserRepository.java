package base.datasource.entity.jpa;

import base.datasource.entity.RemoveUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<RemoveUser, Long> {

    Optional<RemoveUser> findByLogin(String userName);

}
