package testpkgs.inject.method;

import io.github.abcqwq.magia.injection.annotation.Component;
import io.github.abcqwq.magia.injection.annotation.Inject;
import testpkgs.inject.DependencyClass;

@Component
public class MethodClass {

    private DependencyClass dependencyClass;

    @Inject
    public void setDepClass(DependencyClass dependencyClass) {
        this.dependencyClass = dependencyClass;
    }

    public int invokeTimesTwo(int n) {
        return dependencyClass.timesTwo(n);
    }
}
