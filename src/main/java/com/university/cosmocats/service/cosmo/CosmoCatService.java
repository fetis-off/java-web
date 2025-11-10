package com.university.cosmocats.service.cosmo;


import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CosmoCatService {
    public List<String> getCosmoCats() {
        return List.of("Cosmo Cat One", "Cosmo Cat Two", "Cosmo Cat Three");
    }
}
