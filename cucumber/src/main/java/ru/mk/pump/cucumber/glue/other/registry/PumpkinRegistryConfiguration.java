package ru.mk.pump.cucumber.glue.other.registry;

import static java.util.Locale.ENGLISH;

import cucumber.api.TypeRegistry;
import cucumber.api.TypeRegistryConfigurer;
import io.cucumber.cucumberexpressions.ParameterByTypeTransformer;
import lombok.extern.slf4j.Slf4j;

import java.util.Locale;

@Slf4j
public class PumpkinRegistryConfiguration implements TypeRegistryConfigurer {

    private final ParameterByTypeTransformer transformer = new PumpkinTransformer();

    @Override
    public Locale locale() {
        return ENGLISH;
    }

    @Override
    public void configureTypeRegistry(TypeRegistry typeRegistry) {
        typeRegistry.setDefaultParameterTransformer(transformer);
    }
}