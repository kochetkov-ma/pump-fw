package ru.mk.pump.commons.utils;

import groovy.lang.GroovyShell;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.customizers.ImportCustomizer;

@SuppressWarnings("WeakerAccess")
public final class Groovy {

    private static String[] DEFAULT_GROOVY_IMPORTS = {"java.time.LocalDate", "java.time.format.DateTimeFormatter"};

    private final String[] groovyImports;

    private Groovy(String[] imports) {
        this.groovyImports = imports;
    }

    public static Groovy of() {
        return new Groovy(DEFAULT_GROOVY_IMPORTS);
    }

    public static Groovy of(String[] imports) {
        return new Groovy(imports);
    }

    public Object evalGroovy(String groovyExpression) {
        final ImportCustomizer imports = new ImportCustomizer();
        imports.addImports(groovyImports);
        final CompilerConfiguration compilerConfiguration = new CompilerConfiguration();
        compilerConfiguration.addCompilationCustomizers(imports);
        GroovyShell groovy = new GroovyShell(compilerConfiguration);

        return groovy.evaluate(groovyExpression);
    }
}