package edu.iis.mto.similarity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import edu.iis.mto.searcher.SearchResult;
import org.junit.jupiter.api.Test;

class SimilarityFinderTest {

    @Test
    void bothSequencesAreEmpty() {
        int[] sequence = {};
        int[] secondSequence = {};
        SimilarityFinder similarityFinder = new SimilarityFinder((elem, sequence1) -> SearchResult.builder().build());

        double result = similarityFinder.calculateJackardSimilarity(sequence,secondSequence);
        assertEquals(1,result);
    }

    @Test
    void oneSequenceIsEmpty(){
        int[] sequence = {1, 21, 32, 46, 58};
        int[] secondSequence = {};
        SimilarityFinder similarityFinder = new SimilarityFinder((elem, sequence1) ->
        {
            SearchResult searchResult = null;
            if(elem==1)
            {
                searchResult = SearchResult.builder().withPosition(0).withFound(false).build();
            }
            if (elem==21)
            {
                searchResult = SearchResult.builder().withPosition(1).withFound(false).build();
            }
            if (elem==32)
            {
                searchResult = SearchResult.builder().withPosition(2).withFound(false).build();
            }
            if (elem==46)
            {
                searchResult = SearchResult.builder().withPosition(3).withFound(false).build();
            }
            if (elem==58)
            {
                searchResult = SearchResult.builder().withPosition(4).withFound(false).build();
            }
            return searchResult;
        });
        double result = similarityFinder.calculateJackardSimilarity(sequence, secondSequence);
        assertEquals(0,result);
    }

    @Test
    void sequencesAreTheSame()
    {
        int[] sequence = {13, 61, 82, 41};
        SimilarityFinder similarityFinder = new SimilarityFinder((elem, sequence1) ->
        {
            SearchResult searchResult = null;
            if (elem==13)
                searchResult = SearchResult.builder().withPosition(0).withFound(true).build();
            if (elem==61)
                searchResult = SearchResult.builder().withPosition(1).withFound(true).build();
            if (elem==82)
                searchResult = SearchResult.builder().withPosition(2).withFound(true).build();
            if (elem==41)
                searchResult = SearchResult.builder().withPosition(3).withFound(true).build();
            return searchResult;
        });
        double result = similarityFinder.calculateJackardSimilarity(sequence, sequence);
        assertEquals(1,result);
    }

    @Test
    void sequencesHaveSomeSimilarities()
    {
        int[] sequence = {1, 14, 76, 21, 0, 43};
        int[] secondSequence = {1, 13, 33, 22, 12, 43};

        SimilarityFinder similarityFinder = new SimilarityFinder((elem, sequence1) ->
        {
            SearchResult searchResult = null;
            if (elem==1)
                searchResult = SearchResult.builder().withPosition(0).withFound(true).build();
            if (elem==14)
                searchResult = SearchResult.builder().withPosition(1).withFound(false).build();
            if (elem==76)
                searchResult = SearchResult.builder().withPosition(2).withFound(false).build();
            if (elem==21)
                searchResult = SearchResult.builder().withPosition(3).withFound(false).build();
            if (elem==0)
                searchResult = SearchResult.builder().withPosition(4).withFound(false).build();
            if (elem==43)
                searchResult = SearchResult.builder().withPosition(5).withFound(true).build();
            return searchResult;
        });
        double result = similarityFinder.calculateJackardSimilarity(sequence, secondSequence);
        assertEquals(2.0/(sequence.length+secondSequence.length-2),result);
    }

    @Test
    void sequencesHaveSimilaritiesButAreDifferentSize()
    {
        int[] sequence = {2, 13, 54, 22, 66,12};
        int[] secondSequence = {2, 13, 54, 22};

        SimilarityFinder similarityFinder = new SimilarityFinder((elem, sequence1) ->
        {
            SearchResult searchResult = null;
            if (elem==2)
                searchResult = SearchResult.builder().withPosition(0).withFound(true).build();
            if (elem==13)
                searchResult = SearchResult.builder().withPosition(1).withFound(true).build();
            if (elem==54)
                searchResult = SearchResult.builder().withPosition(2).withFound(true).build();
            if (elem==22)
                searchResult = SearchResult.builder().withPosition(3).withFound(true).build();
            if (elem==66)
                searchResult = SearchResult.builder().withPosition(4).withFound(false).build();
            if (elem==12)
                searchResult = SearchResult.builder().withPosition(5).withFound(false).build();
            return searchResult;
        });
        double result = similarityFinder.calculateJackardSimilarity(sequence, secondSequence);
        assertEquals(4.0/(sequence.length+secondSequence.length-4),result);
    }

}
