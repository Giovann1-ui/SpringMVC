package app.repository;

import org.springframework.stereotype.Repository;
import app.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface UserRepository extends JpaRepository<UserModel, Long> {
    UserModel findFirstByUsername(String username);
}