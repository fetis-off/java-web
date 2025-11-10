package com.university.cosmocats.controller;

import com.university.cosmocats.featuretoggle.FeatureToggles;
import com.university.cosmocats.featuretoggle.annotation.FeatureToggle;
import com.university.cosmocats.service.cosmo.CosmoCatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/cosmo-cats")
public class CosmoCatsController {
    private final CosmoCatService cosmoCatService;

    @GetMapping
    @FeatureToggle(FeatureToggles.COSMO_CATS)
    public ResponseEntity<List<String>> getCosmoCats() {
        return ResponseEntity.ok(cosmoCatService.getCosmoCats());
    }

}
