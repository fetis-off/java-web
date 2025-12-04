package com.university.cosmocats.controller;

import com.university.cosmocats.featuretoggle.FeatureToggleExtension;
import com.university.cosmocats.featuretoggle.FeatureToggles;
import com.university.cosmocats.featuretoggle.annotation.DisabledFeatureToggle;
import com.university.cosmocats.featuretoggle.annotation.EnabledFeatureToggle;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(FeatureToggleExtension.class)
class CosmoCatsControllerIT {

    @LocalServerPort
    private int port;

    @Test
    @DisplayName("Enabled cosmo-cats feature: should return 200 status")
    @EnabledFeatureToggle(FeatureToggles.COSMO_CATS)
    public void getCosmoCats_FeatureIsEnabled() {
        given()
                .port(port)
                .when()
                .get("/api/v1/cosmo-cats")
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("Disabled cosmo-cats feature: should return 409 status")
    @DisabledFeatureToggle(FeatureToggles.COSMO_CATS)
    public void getCosmoCats_FeatureIsDisabled() {
        given()
                .port(port)
                .when()
                .get("/api/v1/cosmo-cats")
                .then()
                .statusCode(HttpStatus.CONFLICT.value());
    }

}