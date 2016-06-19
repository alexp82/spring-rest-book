package com.keba.rest.poll.client;

import com.keba.rest.poll.domain.Poll;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alexp on 19/06/16.
 */
public class QuickPollClientV3OAuth {

    private static final String QUICK_POLL_URI_V3 = "http://localhost:8181/oauth2/v3/polls";

    public static void main(String[] args) {
        QuickPollClientV3OAuth client = new QuickPollClientV3OAuth();
        Poll poll = client.getPollById(1L);
        System.out.println("Poll: " + poll);
    }

    public Poll getPollById(Long pollId) {
        OAuth2RestTemplate restTemplate = restTemplate();
        return restTemplate.getForObject(QUICK_POLL_URI_V3 + "/{pollId}", Poll.class, pollId);
    }

    private OAuth2RestTemplate restTemplate() {
        ResourceOwnerPasswordResourceDetails resourceDetails = new ResourceOwnerPasswordResourceDetails();
        resourceDetails.setGrantType("password");
        resourceDetails.setAccessTokenUri("http://localhost:8181/oauth/token");
        resourceDetails.setClientId("quickpolliOSClient");
        resourceDetails.setClientSecret("top_secret");

        // Set scopes
        List<String> scopes = new ArrayList<>();
        scopes.add("read");
        scopes.add("write");
        resourceDetails.setScope(scopes);

        // Resource Owner details
        resourceDetails.setUsername("mickey");
        resourceDetails.setPassword("cheese");

        return new OAuth2RestTemplate(resourceDetails);
    }
}