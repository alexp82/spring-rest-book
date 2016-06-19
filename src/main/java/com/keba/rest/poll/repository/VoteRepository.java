package com.keba.rest.poll.repository;

import com.keba.rest.poll.domain.Poll;
import com.keba.rest.poll.domain.Vote;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by alexp on 12/06/16.
 */
public interface VoteRepository extends CrudRepository<Vote, Long> {

    @Query(value="select v.* from Option o, Vote v where o.POLL_ID = ?1 and v.OPTION_ID = o.OPTION_ID", nativeQuery = true)
    Iterable<Vote> findByPoll(Long pollId);
}