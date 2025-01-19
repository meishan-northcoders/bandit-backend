package com.northcoders.bandit.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.northcoders.bandit.config.OpenAIConfig;
import com.northcoders.bandit.model.ExtractTermsResponseDTO;
import io.github.sashirestela.openai.SimpleOpenAI;
import io.github.sashirestela.openai.common.ResponseFormat;
import io.github.sashirestela.openai.domain.chat.Chat;
import io.github.sashirestela.openai.domain.chat.ChatMessage;
import io.github.sashirestela.openai.domain.chat.ChatRequest;
import opennlp.tools.lemmatizer.LemmatizerME;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.TokenizerME;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Component
public class OpenAIServiceImpl implements OpenAIService {

    @Autowired
    private SimpleOpenAI openAI;

    @Autowired
    private Set<String> stopWords;

    @Autowired
    private TokenizerME tokenizer;

    @Autowired
    private LemmatizerME lemmatizer;

    @Autowired
    private POSTaggerME posTagger;


    public static void main(String[] args) throws IOException {
        OpenAIServiceImpl openAIService = new OpenAIServiceImpl();
        openAIService.openAI = SimpleOpenAI.builder()
                .apiKey("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")
                .build();
        openAIService.stopWords = new OpenAIConfig().stopWords();
        openAIService.tokenizer = new OpenAIConfig().tokenizer();
        openAIService.posTagger = new OpenAIConfig().posTagger();
        openAIService.lemmatizer = new OpenAIConfig().lemmatizer();
        String s = openAIService.buildTsQuery("I am looking for an Saxophone player within Manchester. We specialise in POP genre. No Metal bands please. We have upcoming concerts in London and Edinburgh. Please don't send invite if you are not Saxophone player");
        System.out.println(s);

        //Vocalist & (London | Manchester | (Glasgow | Ruchill)) & Guitar

//        String query = "&Vocalist&(London | Manchester |(Glasgow Ruchill)) & Guitar|";
//        System.out.println(query);
//        String fixedQuery = fixIrregularTsQuery(query);
//        System.out.println(fixedQuery);
    }


    @Override
    public List<String> tokenize(List<String> tokenList) {
        String[] tokens = tokenList.toArray(new String[0]);
        System.out.println("Tokens: " + tokenList);
        return tokenize(tokens);
    }

    @Override
    public List<String> tokenize(String text) {
        //String text = "I am a Energetic Casual Beginner band player.";
        String[] tokens = tokenizer.tokenize(text);
        System.out.println("Tokens: " + Arrays.toString(tokens));
        return tokenize(tokens);
    }

    private List<String> tokenize(String[] tokens) {
        String[] posTags = posTagger.tag(tokens);
        String[] lemmas = lemmatizer.lemmatize(tokens, posTags);
        System.out.println("Lemmas: " + Arrays.toString(lemmas));
        List<String> filteredTokens = Arrays.stream(tokens)
                .map(token -> token.replaceAll("\\p{Punct}", "").toLowerCase())
                .filter(token -> !stopWords.contains(token))
                .filter(str -> !str.isBlank())
                .collect(Collectors.toList());
        System.out.println("Filtered Tokens: " + filteredTokens);
        return filteredTokens;
    }


    @Override
    public String buildTsQuery(String query) {
        ChatRequest chatRequest = ChatRequest.builder().model("gpt-4o-mini").temperature(0.0).maxCompletionTokens(2048)
                .topP(1.0).frequencyPenalty(0.0).presencePenalty(0.0)
                .message(ChatMessage.SystemMessage.of("Transform an input search sentence into a structured query using association operators: & (And), | (Or), and ! (Not). The generated query string should be compliant with Postgres TsQuery format.\n" +
                        "# Steps\n" +
                        "\n" +
                        "1. **Identify Keywords**: Extract key components from the input sentence.\n" +
                        "2.**Filter Keywords**: Filter unimportant such as band, lead etc. Only include Genre, Location, Instrument type and some adjectives.  \n" +
                        "3. **Determine Associations**: Analyze the sentence for relationships among the keywords:\n" +
                        "   - Use '&' for conjunction relationships (e.g., \"and\").\n" +
                        "   - Use '|' for disjunction relationships (e.g., \"or\").\n" +
                        "   - Use '!' for negation (e.g., \"but not\").\n" +
                        "4.**Split Keywords**: Make sure all keywords are split using association symbols.\n" +
                        "4. **Build the Query**: Construct the query string using the identified associations.\n" +
                        "\n" +
                        "# Output Format\n" +
                        "\n" +
                        "Output the transformed sentence as a single string demonstrating the query structure.\n" +
                        "\n" +
                        "# Examples\n" +
                        "\n" +
                        "**Input:** \"Vocalists and Guitarists in Glasgow and UK but not in Manchester\"\n" +
                        "\n" +
                        "**Reasoning:**\n" +
                        "- Identify keywords: Vocalists, Guitarists, Glasgow, UK, Manchester\n" +
                        "- Filter out any stop words and unimportant words.\n" +
                        "- Determine Associations:\n" +
                        "  - \"and\" implies '&' between Vocalists and Guitarists.\n" +
                        "  - \"in\" implies &(between locations).\n" +
                        "  - \"but not\" implies '!' with Manchester.\n" +
                        "- Constructed structure using operators: Vocalists & Guitarists & (Glasgow | UK) & !Manchester\n" +
                        "\n" +
                        "**Output:** Vocalists & Guitarists & (Glasgow | UK) & !Manchester\n" +
                        "\n" +
                        "\n" +
                        "\n" +
                        "**Input:** \"I am looking for an energetic guitarist within UK but not in Glasgow don't want anybody who is also a vocalist.\"\n" +
                        "\n" +
                        "**Reasoning:**\n" +
                        "- Identify keywords: Energetic, Guitarist, Glasgow, UK, vocalist\n" +
                        "- Filter out any stop words and unimportant words such as looking, anybody etc.\n" +
                        "- Determine Associations:\n" +
                        "  - \"and\" implies '&' between Energetic and Guitarist.\n" +
                        "  - \"within\" implies &(between locations).\n" +
                        "  - \"but not\" implies '!' with Glasgow.\n" +
                        "  - \"don't want\" implies '!' with vocalist.\n" +
                        "- Constructed structure using operators: Energetic & Guitarist &  UK & ! Glasgow & ! vocalist\n" +
                        "\n" +
                        "**Output:** Energetic & Guitarist &  UK & ! Glasgow & ! vocalist\n" +
                        "\n" +
                        "\n" +
                        "\n" +
                        "**Input:** \"I am looking for an Saxophone player within Manchester. We specialise in POP genre. No Metal bands please. We have upcoming concerts in London and Edinburgh. Please don't send invite if you are not Saxophone player\"\n" +
                        "\n" +
                        "**Output:** Saxophone & Manchester &  POP & London & Edinburgh\n" +
                        "\n" +
                        "\n" +
                        "**Input:** \"I am looking for a lead Vocalist for my band. I'm based of London. Search for anyone in Manchester or Glasgow as well. Also should have some Guitar playing skills.\"\n" +
                        "\n" +
                        "**Output:**  Vocalist & (London | Manchester | Glasgow) &  Guitar\n" +
                        "\n" +
                        "**Input:** \"Looking for a lead Guitarist in Leeds. Also suggest someone else who can play drums and is from London.\"\n" +
                        "\n" +
                        "**Output:**  Guitarist & Leeds | (Drums & London)\n" +
                        "\n" +
                        "**Input:** \"I am looking for an Saxophone player within Manchester. We specialise in POP genre. No Metal bands please. We have upcoming concerts in London and Edinburgh. Please don't send invite if you are not Saxophone player\"\n" +
                        "\n" +
                        "**Output:**  Saxophone & POP & !Metal & (Manchester | London | Edinburgh)\n" +
                        "\n" +
                        "\n" +
                        "# Notes\n" +
                        "\n" +
                        "- Ensure the logical order of operations is preserved in the constructed query.\n" +
                        "- Handle multiple conjunctions and disjunctions by appropriately nesting or sequencing them with parentheses.\n" +
                        "- Make sure all keywords are split using appropriate associations"))
                .responseFormat(ResponseFormat.jsonSchema(ResponseFormat.JsonSchema.builder()
                        .name("ExtractedTerms")
                        .schemaClass(ExtractTermsResponseDTO.class)
                        .build()))
                .message(ChatMessage.UserMessage.of(query)).build();
        CompletableFuture<Chat> chatCompletableFuture = openAI.chatCompletions().create(chatRequest);
        Chat resp = chatCompletableFuture.join();
        String s = resp.firstContent();
        ExtractTermsResponseDTO termsResponseDTO = null;
        try {
            termsResponseDTO = new ObjectMapper().readValue(s, ExtractTermsResponseDTO.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        System.out.println(termsResponseDTO);

//        termsResponseDTO.setPositiveTerms(tokenize(termsResponseDTO.getPositiveTerms()));
//        termsResponseDTO.setNegativeTerms(tokenize(termsResponseDTO.getNegativeTerms()));
//        termsResponseDTO.setLocationsToExclude(tokenize(termsResponseDTO.getLocationsToExclude()));
//        termsResponseDTO.setLocationsToInclude(tokenize(termsResponseDTO.getLocationsToInclude()));

//        String finalTsQuery = String.join(" | ", termsResponseDTO.getPositiveTerms());
//        String negativeQuery = termsResponseDTO.getNegativeTerms().stream()
//                .map(term -> "!" + term)
//                .collect(Collectors.joining(" & "));
//        if (!negativeQuery.isEmpty()) {
//            finalTsQuery = finalTsQuery + " & " + negativeQuery;
//        }
//
//        String locationsToInclude = String.join(" & ", termsResponseDTO.getLocationsToInclude());
//        if (!locationsToInclude.isEmpty()) {
//            finalTsQuery = finalTsQuery + " & " + locationsToInclude;
//        }
//        String locationsToExclude = termsResponseDTO.getLocationsToExclude().stream()
//                .map(term -> "!" + term)
//                .collect(Collectors.joining(" & "));
//
//        if (!locationsToExclude.isEmpty()) {
//            finalTsQuery = finalTsQuery + " & " + locationsToExclude;
//        }


        String tsQuery = termsResponseDTO.getResponse();
        return fixIrregularTsQuery(tsQuery);
    }

    private String fixIrregularTsQuery(String queryStr) {
        String wordsStr = Arrays.stream(queryStr.split("&")).map(String::trim).collect(Collectors.joining(" & "));
        wordsStr = Arrays.stream(wordsStr.split("\\|")).map(String::trim).collect(Collectors.joining(" | "));

        String[] s = wordsStr.split(" ");

        if (s[0].matches("[&|]")) {
            s = Arrays.copyOfRange(s, 1, s.length);
        }
        if (s[s.length - 1].matches("[&|]")) {
            s = Arrays.copyOfRange(s, 0, s.length - 1);
        }
        boolean valid = true;
        boolean flip = false;
        List<String> words = new ArrayList<>();
        List<Integer> addFixingPipeSymbolAt = new ArrayList<>();
        for (int i = 0; i < s.length; i++) {
            if (s[i].matches("[&|]")) {
                valid = flip;
            } else {
                valid = !flip;
            }
            flip = !flip;

            if (valid) {
                words.add(s[i]);
            } else {
                String s1 = words.get(i - 1);
                if (!s1.startsWith("(")) {
                    words.set(i - 1, "(" + s1);
                }
                //words.add("|"); //Add at end of loop instead of here to not disturb size of array list
                addFixingPipeSymbolAt.add(i);
                if (!s[i].endsWith(")")) {
                    words.add(s[i] + ")");
                } else {
                    words.add(s[i]);
                }
                flip = !flip;
            }
        }
        for (Integer i : addFixingPipeSymbolAt) {
            words.add(i, "|");
        }

        return String.join(" ", words);
    }


}
