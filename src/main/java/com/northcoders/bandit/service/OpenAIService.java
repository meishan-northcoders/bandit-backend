package com.northcoders.bandit.service;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

public interface OpenAIService {

    List<String> tokenize(List<String> tokenList);

    List<String> tokenize(String text);

    String buildTsQuery(String query);
}
