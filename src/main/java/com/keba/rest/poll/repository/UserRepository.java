package com.keba.rest.poll.repository;

import com.keba.rest.poll.domain.User;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by alexp on 19/06/16.
 */
public interface UserRepository extends CrudRepository<User, Long> {
    public User findByUsername(String username);
}
