package com.university.cosmocats.featuretoggle;

import com.university.cosmocats.featuretoggle.annotation.DisabledFeatureToggle;
import com.university.cosmocats.featuretoggle.annotation.EnabledFeatureToggle;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit.jupiter.SpringExtension;

public class FeatureToggleExtension implements BeforeEachCallback, AfterEachCallback {

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        context.getTestMethod().ifPresent(
                testMethod -> {
                    FeatureToggleService featureToggleService = getFeatureToggleService(context);

                    if (testMethod.isAnnotationPresent(EnabledFeatureToggle.class)) {
                        EnabledFeatureToggle enabledFeatureToggle = testMethod
                                .getAnnotation(EnabledFeatureToggle.class);
                        featureToggleService.enable(enabledFeatureToggle.value().getFeatureName());
                    } else if (testMethod.isAnnotationPresent(DisabledFeatureToggle.class)) {
                        DisabledFeatureToggle disabledFeatureToggle = testMethod
                                .getAnnotation(DisabledFeatureToggle.class);
                        featureToggleService.disable(disabledFeatureToggle.value().getFeatureName());
                    }

                });
    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        context.getTestMethod().ifPresent(
                testMethod -> {
                    String featureName = null;
                    if (testMethod.isAnnotationPresent(EnabledFeatureToggle.class)) {
                        EnabledFeatureToggle enabledFeatureToggle = testMethod
                                .getAnnotation(EnabledFeatureToggle.class);
                        featureName = enabledFeatureToggle.value().getFeatureName();
                    } else if (testMethod.isAnnotationPresent(DisabledFeatureToggle.class)) {
                        DisabledFeatureToggle disabledFeatureToggle = testMethod
                                .getAnnotation(DisabledFeatureToggle.class);
                        featureName = disabledFeatureToggle.value().getFeatureName();
                    }

                    if (featureName != null) {
                        FeatureToggleService featureToggleService = getFeatureToggleService(context);

                        if (getFeatureNameFromPropertiesAsBoolean(context, featureName)) {
                            featureToggleService.enable(featureName);
                        } else {
                            featureToggleService.disable(featureName);
                        }
                    }
                });
    }

    private boolean getFeatureNameFromPropertiesAsBoolean(ExtensionContext context, String featureName) {
        Environment environment = SpringExtension.getApplicationContext(context).getEnvironment();
        return environment.getProperty("spring.application.feature.toggles." + featureName,  Boolean.class, false);
    }

    private FeatureToggleService getFeatureToggleService(ExtensionContext context) {
        return SpringExtension.getApplicationContext(context).getBean(FeatureToggleService.class);
    }
}
