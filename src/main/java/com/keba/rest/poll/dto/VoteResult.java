package com.keba.rest.poll.dto;

import java.util.Collection;

/**
 * Created by alexp on 12/06/16.
 */
public class VoteResult {
        private int totalVotes;
        private Collection<OptionCount> results;

        public int getTotalVotes() {
            return totalVotes;
        }

        public void setTotalVotes(int totalVotes) {
            this.totalVotes = totalVotes;
        }

        public Collection<OptionCount> getResults() {
            return results;
        }

        public void setResults(Collection<OptionCount> results) {
            this.results = results;
        }
}
