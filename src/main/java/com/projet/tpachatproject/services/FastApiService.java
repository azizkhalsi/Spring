package com.projet.tpachatproject.services;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.projet.tpachatproject.entities.PredictionRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class FastApiService {

    private final WebClient webClient;

    // URL de votre API FastAPI
    private final String FASTAPI_URL = "http://localhost:8000/predict";

    public FastApiService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    // Méthode pour envoyer une requête à l'API FastAPI
    public Mono<PredictionResponse> predict(PredictionRequest request) {
        return webClient.post()
                .uri(FASTAPI_URL)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(PredictionResponse.class);
    }




    // DTO pour mapper la réponse de l'API FastAPI
    public static class PredictionResponse {
        @JsonProperty("result")
        private String result;

        // Getters et Setters
        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }
    }
}
