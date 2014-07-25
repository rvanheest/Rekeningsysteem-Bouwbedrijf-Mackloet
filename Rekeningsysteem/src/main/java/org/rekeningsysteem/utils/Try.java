package org.rekeningsysteem.utils;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * A container class which may either contain a value or an {@link Throwable}. If a value is
 * present, {@code isSuccess()} will return {@code true} and {@code get()} will return the value. If
 * an exception is present, {@code isFailure()} will return {@code true} and
 * {@code throwException()} will throw the exception.
 * 
 * <p>
 * Additional methods that depend on the presence or absence of a contained value are provided, such
 * as {@link #orElse(java.lang.Object) orElse()} (return a default value if value not present) and
 * {@link #ifPresent(java.util.function.Consumer) ifPresent()} (execute a block of code if the value
 * is present).
 *
 * <p>
 * This is a <a href="../lang/doc-files/ValueBased.html">value-based</a> class; use of
 * identity-sensitive operations (including reference equality ({@code ==}), identity hash code, or
 * synchronization) on instances of {@code Try} may have unpredictable results and should be
 * avoided.
 * 
 * @author Richard van Heest
 */
public abstract class Try<T> {

	/**
	 * Returns a {@code Try} with the specified present value.
	 * 
	 * @param value the value to be present
	 * @return a {@code Try} with the present value
	 */
	public static <T> Try<T> of(T value) {
		return new Success<>(value);
	}

	/**
	 * Returns a {@code Try} with the specified present exception.
	 * 
	 * @param e the exception to be present
	 * @return a {@code Try} with the present exception
	 */
	public static <T> Try<T> failure(Throwable e) {
		return new Failure<>(e);
	}

	/**
	 * Constructs an instance of {@code Try}. Makes sure that this class can't be instantiated
	 * itself.
	 */
	private Try() {
	}

	/**
	 * Returns {@code true} if there is a value present, otherwise {@code false}.
	 * 
	 * @return {@code true} if there is a value present, otherwise {@code false}
	 */
	public abstract boolean isSuccess();

	/**
	 * Returns {@code true} if there is an exception present, otherwise {@code false}.
	 * 
	 * @return {@code true} if there is an exception present, otherwise {@code false}
	 */
	public abstract boolean isFailure();

	/**
	 * If a value is present in this {@code Try}, returns the value, otherwise throws the exception.
	 *
	 * @return the non-null value held by this {@code Optional}
	 * @see Try#isPresent()
	 */
	public abstract T get();

	/**
	 * If an exception is present in this {@code Try}, throws the exception, otherwise does nothing.
	 */
	public abstract void throwException();

	/**
	 * If a value is present, invoke the specified consumer with the value, otherwise do nothing.
	 *
	 * @param consumer block to be executed if a value is present
	 */
	public abstract void ifPresent(Consumer<? super T> consumer);

	/**
	 * If a value is present, and the value matches the given predicate, return an {@code Try}
	 * describing the value, otherwise return a {@code Try} with an exception. If a value is present
	 * and {@code predicate} is null, a {@code Try} with an exception will be returned.
	 *
	 * @param predicate a predicate to apply to the value, if present
	 * @return an {@code Try} describing the value of this {@code Try} if a value is present and the
	 *         value matches the given predicate, otherwise a {@code Try} with an exception
	 */
	public abstract Try<T> filter(Predicate<? super T> predicate);

	/**
	 * If a value is present, apply the provided mapping function to it, and if the result is
	 * non-null, return an {@code Try} describing the result. Otherwise return a {@code Try} with an
	 * exception. If a value is present and {@code mapper} is null, a {@code Try} with an exception
	 * will be returned.
	 *
	 * @param <U> The type of the result of the mapping function
	 * @param mapper a mapping function to apply to the value, if present
	 * @return an {@code Try} describing the result of applying a mapping function to the value of
	 *         this {@code Try}, if a value is present, otherwise a {@code Try} with an exception
	 */
	public abstract <U> Try<U> map(Function<? super T, ? extends U> mapper);

	/**
	 * If a value is present, apply the provided {@code Try}-bearing mapping function to it, return
	 * that result, otherwise return a {@code Try} with an exception. This method is similar to
	 * {@link #map(Function)}, but the provided mapper is one whose result is already an {@code Try}
	 * , and if invoked, {@code flatMap} does not wrap it with an additional {@code Try}. If a value
	 * is present and {@code mapper} is null, a {@code Try} with an exception will be returned.
	 *
	 * @param <U> The type parameter to the {@code Try} returned by
	 * @param mapper a mapping function to apply to the value, if present the mapping function
	 * @return the result of applying an {@code Try}-bearing mapping function to the value of this
	 *         {@code Try}, if a value is present, otherwise a {@code Try} with an exception
	 */
	public abstract <U> Try<U> flatMap(Function<? super T, Try<U>> mapper);

	/**
	 * Return the value if present, otherwise return {@code other}.
	 *
	 * @param other the value to be returned if there is no value present, may be null
	 * @return the value, if present, otherwise {@code other}
	 */
	public abstract T orElse(T other);

	/**
	 * Return the value if present, otherwise invoke {@code other} and return the result of that
	 * invocation.
	 *
	 * @param other a {@code Supplier} whose result is returned if no value is present
	 * @return the value if present otherwise the result of {@code other.get()}
	 */
	public abstract T orElseGet(Supplier<? extends T> other);

	/**
	 * Return the contained value, if present, otherwise throw an exception to be created by the
	 * provided supplier.
	 *
	 * @param <X> Type of the exception to be thrown
	 * @param exceptionSupplier The supplier which will return the exception to be thrown
	 * @return the present value
	 * @throws X if there is no value present
	 */
	public abstract <X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier)
			throws X;

	private static class Success<T> extends Try<T> {

		private T elem;

		public Success(T elem) {
			this.elem = elem;
		}

		@Override
		public boolean isSuccess() {
			return true;
		}

		@Override
		public boolean isFailure() {
			return false;
		}

		@Override
		public T get() {
			return this.elem;
		}

		@Override
		public void throwException() {
			// do nothing.
		}

		@Override
		public void ifPresent(Consumer<? super T> consumer) {
			consumer.accept(this.elem);
		}

		@Override
		public Try<T> filter(Predicate<? super T> predicate) {
			try {
				Objects.requireNonNull(predicate);
				return predicate.test(this.elem)
						? this
						: failure(new NoSuchElementException("Predicate does not hold for "
								+ this.elem));
			}
			catch (NullPointerException e) {
				return failure(e);
			}
		}

		@Override
		public <U> Try<U> map(Function<? super T, ? extends U> mapper) {
			try {
				Objects.requireNonNull(mapper);
				return of(mapper.apply(this.elem));
			}
			catch (NullPointerException e) {
				return failure(e);
			}
		}

		@Override
		public <U> Try<U> flatMap(Function<? super T, Try<U>> mapper) {
			try {
				Objects.requireNonNull(mapper);
				return Objects.requireNonNull(mapper.apply(this.elem));
			}
			catch (NullPointerException e) {
				return failure(e);
			}
		}

		@Override
		public T orElse(T other) {
			return this.elem;
		}

		@Override
		public T orElseGet(Supplier<? extends T> other) {
			return this.elem;
		}

		@Override
		public <X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier) {
			return this.elem;
		}

		@Override
		public boolean equals(Object other) {
			if (other instanceof Success) {
				Success<?> that = (Success<?>) other;
				return Objects.equals(this.elem, that.elem);
			}
			return false;
		}

		@Override
		public int hashCode() {
			return Objects.hash(this.elem);
		}

		@Override
		public String toString() {
			return "Try.Success(" + this.elem + ")";
		}
	}

	private static class Failure<T> extends Try<T> {

		private IllegalStateException exception;

		public Failure(Throwable e) {
			this.exception = new IllegalStateException(e);
		}

		@Override
		public boolean isSuccess() {
			return false;
		}

		@Override
		public boolean isFailure() {
			return true;
		}

		@Override
		public T get() {
			throw this.exception;
		}

		@Override
		public void throwException() {
			throw this.exception;
		}

		@Override
		public void ifPresent(Consumer<? super T> consumer) {
			// do nothing
		}

		@Override
		public Try<T> filter(Predicate<? super T> predicate) {
			return this;
		}

		@Override
		@SuppressWarnings("unchecked")
		public <U> Try<U> map(Function<? super T, ? extends U> mapper) {
			return (Try<U>) this;
		}

		@Override
		@SuppressWarnings("unchecked")
		public <U> Try<U> flatMap(Function<? super T, Try<U>> mapper) {
			return (Try<U>) this;
		}

		@Override
		public T orElse(T other) {
			return other;
		}

		@Override
		public T orElseGet(Supplier<? extends T> other) {
			return other.get();
		}

		@Override
		public <X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier)
				throws X {
			throw exceptionSupplier.get();
		}

		@Override
		public boolean equals(Object other) {
			if (other instanceof Failure) {
				Failure<?> that = (Failure<?>) other;
				return Objects.equals(this.exception.getMessage(), that.exception.getMessage());
			}
			return false;
		}

		@Override
		public int hashCode() {
			return Objects.hash(this.exception.getMessage());
		}

		@Override
		public String toString() {
			return "Try.Failure(" + this.exception.getMessage() + ")";
		}
	}
}
