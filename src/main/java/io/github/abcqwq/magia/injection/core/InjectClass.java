package io.github.abcqwq.magia.injection.core;

import io.github.abcqwq.magia.injection.annotation.Component;

@Component
public class InjectClass {

  public void printSmth(String a) {
    System.out.println(a + " from inject class");
  }
}
