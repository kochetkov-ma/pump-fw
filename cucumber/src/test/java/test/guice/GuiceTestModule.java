package test.guice;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

import static java.lang.Math.PI;

public class GuiceTestModule extends AbstractModule {

    @Override
    protected void configure() {
        bindConstant().annotatedWith(Names.named("Pi number")).to(PI);
    }
}
