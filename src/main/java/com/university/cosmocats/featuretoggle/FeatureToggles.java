package com.university.cosmocats.featuretoggle;

import lombok.Getter;

@Getter
public enum FeatureToggles {
    COSMO_CATS("cosmo-cats"),;

    private final String featureName;

    FeatureToggles(String featureName) {
        this.featureName = featureName;
    }
}
