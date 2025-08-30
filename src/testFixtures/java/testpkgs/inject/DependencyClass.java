package testpkgs.inject;

import io.github.abcqwq.magia.injection.annotation.Component;

@Component
public class DependencyClass {
  public int timesTwo(int n) {
    return n * 2;
  }
}
