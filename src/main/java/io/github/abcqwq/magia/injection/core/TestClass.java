package io.github.abcqwq.magia.injection.core;

import io.github.abcqwq.magia.injection.annotation.Component;
import io.github.abcqwq.magia.injection.annotation.Inject;

@Component
public class TestClass {

  @Inject
  private InjectClass injectClass;

  public void invoke(String s) {
    injectClass.printSmth(s);
  }
}
