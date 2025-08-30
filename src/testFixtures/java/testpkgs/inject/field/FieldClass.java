package testpkgs.inject.field;

import io.github.abcqwq.magia.injection.annotation.Component;
import io.github.abcqwq.magia.injection.annotation.Inject;
import testpkgs.inject.DependencyClass;

@Component
public class FieldClass {

    @Inject
    private DependencyClass dependencyClass;

    public int invokeTimesTwo(int n) {
        return dependencyClass.timesTwo(n);
    }
}
