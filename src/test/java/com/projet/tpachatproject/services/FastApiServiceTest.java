package com.projet.tpachatproject.services;

import com.projet.tpachatproject.entities.PredictionRequest;

import org.junit.jupiter.api.Test;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Mono;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test") // Pour utiliser un profil de test, si nécessaire
public class FastApiServiceTest {

    @MockBean
    private FastApiService fastApiService; // Mock du service

    //@Test
    public void testPredict() {
        // ARRANGE
        PredictionRequest request = new PredictionRequest();
        request.setFeature1("0");
        request.setFeature2("1");
        FastApiService.PredictionResponse response = new FastApiService.PredictionResponse();

        response.setResult("prediction fais avec succes");

        when(fastApiService.predict(any(PredictionRequest.class))).thenReturn(Mono.just(response));

        // ACT & ASSERT
        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE) // Définit le type de contenu
                .body(request)
                .when()
                .post("http://localhost:8000/predict")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("result", equalTo("prediction fais avec succes"));
    }
}

