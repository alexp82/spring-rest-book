package com.keba.rest.poll.v1.controller;

import com.keba.rest.poll.domain.Poll;
import com.keba.rest.poll.dto.ErrorDetail;
import com.keba.rest.poll.exception.ResourceNotFoundException;
import com.keba.rest.poll.repository.PollRepository;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController("pollControllerV1")
@RequestMapping("/v1/")
@Api(value = "polls", description = "Poll API")
public class PollController {

    @Inject
    private PollRepository pollRepository;

    @RequestMapping(value = "/polls", method = RequestMethod.POST)
    @ApiOperation(value = "Creates a new Poll",
            notes = "The newly created poll Id will be sent in the location response header",
            response = Void.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Poll Created Successfully", response = Void.class),
            @ApiResponse(code = 500, message = "Error creating Poll", response = ErrorDetail.class)
    })
    public ResponseEntity<Void> createPoll(@Valid @RequestBody Poll poll) {
        poll = pollRepository.save(poll);

        // Set the location header for the newly created resource
        HttpHeaders responseHeaders = new HttpHeaders();
        URI newPollUri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(poll.getPollId()).toUri();
        responseHeaders.setLocation(newPollUri);

        return new ResponseEntity<>(null, responseHeaders, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/polls/{pollId}", method = RequestMethod.GET)
    @ApiOperation(value = "Retrieves given Poll", response = Poll.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "", response = Poll.class),
            @ApiResponse(code = 404, message = "Unable to find Poll", response = ErrorDetail.class)
    })
    public ResponseEntity<?> getPoll(@PathVariable Long pollId) {
        verifyPoll(pollId);
        Poll p = pollRepository.findOne(pollId);
        updatePollResourceWithLinks(p);
        return new ResponseEntity<>(p, HttpStatus.OK);
    }

    @RequestMapping(value = "/polls", method = RequestMethod.GET)
    @ApiOperation(value = "Retrieves all the polls", response = Poll.class, responseContainer = "List")
    public ResponseEntity<Iterable<Poll>> getAllPolls() {
        Iterable<Poll> allPolls = pollRepository.findAll();
        for (Poll p : allPolls) {
            updatePollResourceWithLinks(p);
        }
        return new ResponseEntity<>(allPolls, HttpStatus.OK);
    }

    @RequestMapping(value = "/polls/{pollId}", method = RequestMethod.PUT)
    @ApiOperation(value = "Updates given Poll", response = Void.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "", response = Void.class),
            @ApiResponse(code = 404, message = "Unable to find Poll", response = ErrorDetail.class)
    })
    public ResponseEntity<Void> updatePoll(@RequestBody Poll poll, @PathVariable Long pollId) {
        verifyPoll(pollId);
        pollRepository.save(poll);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/polls/{pollId}", method = RequestMethod.DELETE)
    @ApiOperation(value = "Deletes given Poll", response = Void.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "", response = Void.class),
            @ApiResponse(code = 404, message = "Unable to find Poll", response = ErrorDetail.class)
    })
    public ResponseEntity<Void> deletePoll(@PathVariable Long pollId) {
        verifyPoll(pollId);
        pollRepository.delete(pollId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    protected void verifyPoll(Long pollId) throws ResourceNotFoundException {
        Poll poll = pollRepository.findOne(pollId);
        if (poll == null) {
            throw new ResourceNotFoundException("Poll with id " + pollId + " not found");
        }
    }

    private void updatePollResourceWithLinks(Poll poll) {
        poll.add(linkTo(methodOn(PollController.class).getAllPolls()).slash(poll.
                getPollId()).withSelfRel());
        poll.add(linkTo(methodOn(VoteController.class).getAllVotes(poll.getPollId())).
                withRel("votes"));
        poll.add(linkTo(methodOn(ComputeResultController.class).computeResult(poll.
                getPollId())).withRel("compute-result"));
    }
}