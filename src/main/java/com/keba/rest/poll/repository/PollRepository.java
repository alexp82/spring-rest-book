package com.keba.rest.poll.repository;

import com.keba.rest.poll.domain.Poll;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by alexp on 12/06/16.
 */
public interface PollRepository extends PagingAndSortingRepository<Poll, Long> {
}