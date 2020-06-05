package com.thoughtpropulsion.funcheck;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * test the README example
 */
public class ExampleTest {

  // a checked exception
  static class Bad extends Exception {
  }

  // a method that needs a Runnable. Runnables can't throw checked exceptions
  private static void invokeRunnable(final Runnable runnable) {
    runnable.run();
  }

  @Test
  public void compilesAndThrows() {
    final boolean state = true;

    /*

    // This is what we want to do but it won't compile:

    assertThatThrownBy(
        () ->
            invokeRunnable(
                () -> {
                  if (state) {
                    throw new Bad();
                  } else {
                    System.out.println("Ran!");
                  }
                })).isInstanceOf(Bad.class);

    // We could do this instead but what should we do in the catch clause?

    assertThatThrownBy(
        () ->
            invokeRunnable(
                () -> {
                  if (state) {
                    try {
                      throw new Bad();
                    } catch (Bad bad) {
                      // !! NOW WHAT?!?
                    }
                  } else {
                    System.out.println("Ran!");
                  }
                })).isInstanceOf(Bad.class);
     */

    assertThatThrownBy(
        () ->
    invokeRunnable(
        CheckedRunnable.of(() -> {
          if (state) {
            throw new Bad();
          } else {
            System.out.println("Ran!");
          }
        }).unchecked())).isInstanceOf(Bad.class);
  }
}
