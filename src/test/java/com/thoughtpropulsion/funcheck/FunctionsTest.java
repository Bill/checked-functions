package com.thoughtpropulsion.funcheck;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.junit.jupiter.api.Test;

class FunctionsTest {

  @Test
  public void checkedRunnableAsRunnableRuns() {
    final AtomicBoolean flag = new AtomicBoolean(false);
    invokeRunnable( () -> flag.set(true));
    assertThat(flag.get()).isTrue();
  }

  @Test
  public void checkedRunnableAsRunnableThrows() {
    assertThatThrownBy( () ->
        invokeRunnable( () -> {throw new IllegalArgumentException("bad");}))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  public void checkedSupplierAsSupplierRuns() {
    final Integer val = invokeSupplier(() -> 1);
    assertThat(val).isEqualTo(1);
  }

  @Test
  public void checkedSupplierAsSupplierThrows() {
    assertThatThrownBy( () ->
        invokeSupplier( () -> {throw new IllegalArgumentException("bad");}))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  public void checkedConsumerAsConsumerRuns() {
    final AtomicInteger val = new AtomicInteger(0);
    invokeConsumer( val2 -> {val.set(val2);}, 1);
    assertThat(val.get()). isEqualTo(1);
  }

  @Test
  public void checkedConsumerAsConsumerThrows() {
    assertThatThrownBy( () ->
        invokeConsumer( val -> {throw new IllegalArgumentException("bad");}, 1))
        .isInstanceOf(IllegalArgumentException.class);
  }

  private static void invokeRunnable(final Runnable runnable) {
    runnable.run();
  }
  private static <T> T invokeSupplier(final Supplier<T> supplier) {
    return supplier.get();
  }
  private static <T> void invokeConsumer(final Consumer<T> consumer, final T val) {
    consumer.accept(val);
  }
}