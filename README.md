# Java Checked Functions
When you've got a `java.lang.Runnable` that can throw checked exceptions, you have to handle them all explicitly since the `Runnable` interface doesn't have a `throws` clause. The same is true for other functional interfaces like `Consumer`, `Supplier` in `java.util.function`.

This little library gives you a twin interface for each of those. But the twin is declared with a `throws Throwable` clause. Through trickery ~~stolen~~ borrowed from [vavr](https://www.vavr.io/) these functional interfaces can be used where the JDK ones are expected.

Example:

```java
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
  }
}
```

In the example `invokeRunnable()` wants a `Runnable`. But this won't compile because the lambda we are passing throws a checked exception.

We could try this instead:

```java
public class ExampleTest {
  @Test
  public void compilesAndThrows() {
    final boolean state = true;

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
  }
}
```

While this compiles, it leaves us wondering what to do in the `catch` block?

`CheckedRunnable` disguises a checked-exception-throwing lambda as a `Runnable` like this:

```java
public class ExampleTest {
  @Test
  public void compilesAndThrows() {
    final boolean state = true;
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
```

We used `CheckedRunnable.of()` to construct a `CheckedRunnable` from our lambda and then we used `unchecked()` to convert that into a `Runnable`. But as you can see, not only does this compile, but also the checked exception propagates at runtime.