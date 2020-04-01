# Java Checked Functions
When you've got a `java.lang.Runnable` that can throw checked exceptions, you have to handle them all explicitly since the `Runnable` interface doesn't have a `throws` clause. The same is true for other functional interfaces like `Consumer`, `Supplier` in `java.util.function`.

This little library gives you a twin interface for each of those. But the twin is declared with a `throws Throwable` clause. Through trickery ~~stolen~~ borrowed from [vavr](https://www.vavr.io/) these functional interfaces can be used where the JDK ones are expected.

Example:

```java
  private static void invokeRunnable(final Runnable runnable) {
    runnable.run();
  }

  private void foo() {
    final boolean state = true;
    invokeRunnable( () -> {
      if (state)
        throw new IllegalStateException();
      else
        System.out.println("Ran!");
    });
  }
```

In the example `invokeRunnable()` wants a `Runnable`. Syntactically, we've supplied it a `Runnable` down in `foo()`. But what we actually supplied was a `CheckedThunk` which is allowed to throw checked exceptions, so this compiles!
