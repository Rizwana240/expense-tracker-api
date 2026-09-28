package expense_tracker_api;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

@Service
public class CategorizerService {

    private final Map<String, Map<String, Integer>> wordCountsByCategory = new HashMap<>();
    private final Map<String, Integer> categoryCounts = new HashMap<>();

    private final Set<String> vocabulary = new HashSet<>();

    private int totalTrainingExamples = 0;

    @PostConstruct
    public void trainModel() {
        try {
            InputStream inputStream =
                    getClass().getClassLoader().getResourceAsStream("training-data.csv");

            if (inputStream == null) {
                throw new RuntimeException("training-data.csv not found");
            }

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(inputStream, StandardCharsets.UTF_8)
            );

            String line;

            while ((line = reader.readLine()) != null) {

                line = line.trim();

                if (line.isEmpty()) {
                    continue;
                }

                // Find the last comma so descriptions can contain commas
                int commaIndex = line.lastIndexOf(",");

                if (commaIndex == -1) {
                    continue;
                }

                String description = line.substring(0, commaIndex).trim();
                String category = line.substring(commaIndex + 1).trim();

                // Skip a CSV header if one exists
                if (description.equalsIgnoreCase("description")
                        && category.equalsIgnoreCase("category")) {
                    continue;
                }

                train(description, category);
            }

            reader.close();

            System.out.println(
                    "ML model trained successfully with "
                            + totalTrainingExamples
                            + " examples."
            );

        } catch (Exception e) {
            throw new RuntimeException("Could not train ML model", e);
        }
    }

    private void train(String description, String category) {

        categoryCounts.put(
                category,
                categoryCounts.getOrDefault(category, 0) + 1
        );

        totalTrainingExamples++;

        Map<String, Integer> wordCounts =
                wordCountsByCategory.computeIfAbsent(
                        category,
                        k -> new HashMap<>()
                );

        for (String word : tokenize(description)) {

            vocabulary.add(word);

            wordCounts.put(
                    word,
                    wordCounts.getOrDefault(word, 0) + 1
            );
        }
    }

    public String categorize(String description) {

        if (description == null || description.trim().isEmpty()) {
            return "Other";
        }

        List<String> words = tokenize(description);

        if (words.isEmpty()) {
            return "Other";
        }

        String bestCategory = "Other";
        double bestScore = Double.NEGATIVE_INFINITY;

        for (String category : categoryCounts.keySet()) {

            // Log of P(category)
            double score = Math.log(
                    (double) categoryCounts.get(category)
                            / totalTrainingExamples
            );

            Map<String, Integer> wordCounts =
                    wordCountsByCategory.get(category);

            int totalWordsInCategory = wordCounts.values()
                    .stream()
                    .mapToInt(Integer::intValue)
                    .sum();

            // Naive Bayes with Laplace smoothing
            for (String word : words) {

                int wordCount =
                        wordCounts.getOrDefault(word, 0);

                double probability =
                        (wordCount + 1.0)
                                / (totalWordsInCategory + vocabulary.size());

                score += Math.log(probability);
            }

            if (score > bestScore) {
                bestScore = score;
                bestCategory = category;
            }
        }

        return bestCategory;
    }

    private List<String> tokenize(String text) {

        return Arrays.stream(
                        text.toLowerCase()
                                .replaceAll("[^a-zA-Z0-9\\s]", " ")
                                .split("\\s+")
                )
                .filter(word -> !word.isBlank())
                .toList();
    }
}