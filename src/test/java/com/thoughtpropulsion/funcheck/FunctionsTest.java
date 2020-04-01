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
    final CheckedRunnable checked = CheckedRunnable.of(() -> flag.set(true));
    final Runnable unchecked = checked.unchecked();
    unchecked.run();
    assertThat(flag.get()).isTrue();
  }

  @Test
  public void checkedRunnableAsRunnableThrows() {
    final CheckedRunnable checked = CheckedRunnable.of(() -> {throw new IllegalArgumentException("bad");});
    final Runnable unchecked = checked.unchecked();
    assertThatThrownBy(unchecked::run)
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  public void checkedSupplierAsSupplierRuns() {
    final CheckedSupplier<Integer> checked = CheckedSupplier.of(() -> 1);
    final Supplier<Integer> unchecked = checked.unchecked();
    final Integer val = unchecked.get();
    assertThat(val).isEqualTo(1);
  }

  @Test
  public void checkedSupplierAsSupplierThrows() {
    final CheckedSupplier<Integer> checked = CheckedSupplier.of(() -> {throw new IllegalArgumentException("bad");});
    final Supplier<Integer> unchecked = checked.unchecked();
    assertThatThrownBy(unchecked::get)
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  public void checkedConsumerAsConsumerRuns() {
    final AtomicInteger val = new AtomicInteger(0);
    final CheckedConsumer<Integer> checked = CheckedConsumer.of(val::set);
    final Consumer<Integer> unchecked = checked.unchecked();
    unchecked.accept(1);
    assertThat(val.get()). isEqualTo(1);
  }

  @Test
  public void checkedConsumerAsConsumerThrows() {
    final CheckedConsumer<Integer> checked = CheckedConsumer.of((x) -> {throw new IllegalArgumentException("bad");});
    final Consumer<Integer> unchecked = checked.unchecked();
    assertThatThrownBy( () -> unchecked.accept(1))
        .isInstanceOf(IllegalArgumentException.class);
  }

}