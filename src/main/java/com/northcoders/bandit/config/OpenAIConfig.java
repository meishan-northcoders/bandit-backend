package com.northcoders.bandit.config;


import com.northcoders.bandit.service.OpenAIServiceImpl;
import io.github.sashirestela.openai.SimpleOpenAI;
import opennlp.tools.lemmatizer.LemmatizerME;
import opennlp.tools.lemmatizer.LemmatizerModel;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

@Configuration
public class OpenAIConfig {

    @Bean
    public SimpleOpenAI openAIClient() {

        return SimpleOpenAI.builder().apiKey(System.getenv("OPENAI_API_KEY"))
                .build();
    }

    @Bean
    public Set<String> stopWords() throws IOException {
        Set<String> stopWords = new HashSet<>();
        InputStream stopWordsStream = OpenAIConfig.class.getResourceAsStream("/stopwords-en.txt");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(stopWordsStream, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                stopWords.add(line.trim().toLowerCase());
            }
        }
        return stopWords;
    }

    @Bean
    public TokenizerME tokenizer() throws IOException {
        InputStream tokenizerModelStream = OpenAIServiceImpl.class.getResourceAsStream("/en-token.bin");
        TokenizerModel tokenizerModel = new TokenizerModel(tokenizerModelStream);
        return new TokenizerME(tokenizerModel);
    }


    @Bean
    public LemmatizerME lemmatizer() throws IOException {
        InputStream lemmatizerModelStream = OpenAIServiceImpl.class.getResourceAsStream("/en-lemmatizer.bin");
        LemmatizerModel lemmatizerModel = new LemmatizerModel(lemmatizerModelStream);
        return new LemmatizerME(lemmatizerModel);
    }


    @Bean
    public POSTaggerME posTagger() throws IOException {
        InputStream posModelStream = OpenAIServiceImpl.class.getResourceAsStream("/en-pos-maxent.bin");
        POSModel posModel = new POSModel(posModelStream);
        return new POSTaggerME(posModel);
    }


}
