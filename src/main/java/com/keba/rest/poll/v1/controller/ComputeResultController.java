package com.keba.rest.poll.v1.controller;

import com.keba.rest.poll.domain.Vote;
import com.keba.rest.poll.dto.OptionCount;
import com.keba.rest.poll.dto.VoteResult;
import com.keba.rest.poll.repository.VoteRepository;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

@RestController("computeResultControllerV1")
@RequestMapping("/v1/")
@Api(value = "computeresult", description = "Compute Results API")
public class ComputeResultController {

    @Inject
    private VoteRepository voteRepository;

    @RequestMapping(value = "/computeresult", method = RequestMethod.GET)
    @ApiOperation(value = "Computes the results of a given Poll", response = VoteResult.class)
    public ResponseEntity<?> computeResult(@RequestParam Long pollId) {
        VoteResult voteResult = new VoteResult();
        Iterable<Vote> allVotes = voteRepository.findByPoll(pollId);

        // Algorithm to count votes
        int totalVotes = 0;
        Map<Long, OptionCount> tempMap = new HashMap<Long, OptionCount>();
        for (Vote v : allVotes) {
            totalVotes++;
            // Get the OptionCount corresponding to this Option
            OptionCount optionCount = tempMap.get(v.getOption().getId());
            if (optionCount == null) {
                optionCount = new OptionCount();
                optionCount.setOptionId(v.getOption().getId());
                tempMap.put(v.getOption().getId(), optionCount);
            }
            optionCount.setCount(optionCount.getCount() + 1);
        }

        voteResult.setTotalVotes(totalVotes);
        voteResult.setResults(tempMap.values());

        return new ResponseEntity<VoteResult>(voteResult, HttpStatus.OK);
    }

}