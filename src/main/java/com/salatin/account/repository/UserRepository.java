package com.salatin.account.repository;

import com.salatin.account.model.User;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findUserByEmail(String email);

    Optional<User> findUserByMobile(String mobile);
}
