package edu.iis.mto.similarity;

import edu.iis.mto.searcher.SearchResult;
import edu.iis.mto.searcher.SequenceSearcher;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class SimilarityFinderBehaviorTest {
    @Test
    void methodShouldInvokeSixTimes() throws IllegalAccessException, NoSuchFieldException {
        int[] sequence = {13, 12, 11, 99, 201, -12};
        int[] secondSequence = {1, 2, 3, 4, 5, 6, 7};
        SequenceSearcher sequenceSearcher = new SequenceSearcher() {
            int counter =   0;
            @Override
            public SearchResult search(int elem, int[] sequence) {
                SearchResult searchResult = null;
                counter++;
                if (elem == 13)
                    searchResult = SearchResult.builder().withPosition(0).withFound(false).build();
                if (elem == 12)
                    searchResult = SearchResult.builder().withPosition(1).withFound(false).build();
                if (elem == 11)
                    searchResult = SearchResult.builder().withPosition(2).withFound(false).build();
                if (elem == 99)
                    searchResult = SearchResult.builder().withPosition(3).withFound(false).build();
                if (elem == 201)
                    searchResult = SearchResult.builder().withPosition(4).withFound(false).build();
                if (elem == -12)
                    searchResult = SearchResult.builder().withPosition(5).withFound(false).build();
                return searchResult;
            }
        };
        SimilarityFinder similarityFinder = new SimilarityFinder(sequenceSearcher);
        similarityFinder.calculateJackardSimilarity(sequence,secondSequence);

        Field field = sequenceSearcher.getClass().getDeclaredField("counter");
        int amountOfSearches = field.getInt(sequenceSearcher);
        assertEquals(6,amountOfSearches);
    }

    @Test
    void methodShouldInvokeZeroTimes() throws IllegalAccessException, NoSuchFieldException {
        int[] sequence = {};
        int[] secondSequence = {-12, -5, -1, -15};
        SequenceSearcher sequenceSearcher = new SequenceSearcher() {
            int counter =   0;
            @Override
            public SearchResult search(int elem, int[] sequence) {
                counter++;
                return SearchResult.builder().build();
            }
        };

        SimilarityFinder similarityFinder = new SimilarityFinder(sequenceSearcher);
        similarityFinder.calculateJackardSimilarity(sequence,secondSequence);

        Field field = sequenceSearcher.getClass().getDeclaredField("counter");
        int amountOfSearches = field.getInt(sequenceSearcher);
        assertEquals(0,amountOfSearches);
    }

    @Test
    void methodShouldNotChangeSequences() {
        int[] sequence = {98, -100, 13, 1, 7};
        int[] secondSequence = {0, -7, -5, 15, 231};

        ArrayList<Integer> secondSequenceChecker = new ArrayList<>();
        ArrayList<Integer> firstSequenceChecker = new ArrayList<>();
        SequenceSearcher sequenceSearcher = (elem, seq) -> {
            firstSequenceChecker.add(elem);
            if(secondSequenceChecker.isEmpty())
                secondSequenceChecker.addAll(Arrays.stream(seq).boxed().collect(Collectors.toList()));

            SearchResult searchResult = null;
            if (elem == 98)
                searchResult = SearchResult.builder().withPosition(0).withFound(false).build();
            if (elem == -100)
                searchResult = SearchResult.builder().withPosition(1).withFound(false).build();
            if (elem == 13)
                searchResult = SearchResult.builder().withPosition(2).withFound(false).build();
            if (elem == 1)
                searchResult = SearchResult.builder().withPosition(3).withFound(false).build();
            if (elem == 7)
                searchResult = SearchResult.builder().withPosition(4).withFound(false).build();
            return searchResult;
        };
        SimilarityFinder similarityFinder = new SimilarityFinder(sequenceSearcher);
        similarityFinder.calculateJackardSimilarity(sequence,secondSequence);

        assertArrayEquals(firstSequenceChecker.stream().mapToInt(Integer::intValue).toArray(),sequence);
        assertArrayEquals(secondSequenceChecker.stream().mapToInt(Integer::intValue).toArray(),secondSequence);
    }
}
