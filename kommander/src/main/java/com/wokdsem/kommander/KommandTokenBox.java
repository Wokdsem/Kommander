package com.wokdsem.kommander;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Keeps a bunch of {@link KommandToken} to be cancelled in the future. The tokens can be optionally bound
 * to a Tag to make a partial cancel.
 */
public class KommandTokenBox {

	private final HashMap<Object, List<KommandToken>> tokenTags;

	public KommandTokenBox() {
		this.tokenTags = new HashMap<>();
	}

	/**
	 * Appends a {@link KommandToken} to the {@link KommandTokenBox}. Tokens appended without tag, just are cancelled
	 * by the {@link KommandTokenBox} when {@link #cancelAll()} is called.
	 *
	 * @param kommandToken value to be added
	 * @throws IllegalArgumentException if kommandToken is {@code null}
	 */
	public void append(KommandToken kommandToken) {
		internalAppend(kommandToken, null);
	}

	/**
	 * Appends a {@link KommandToken} to the {@link KommandTokenBox} with a tag. The {@link KommandToken} is
	 * cancelled when {@link #cancel(Object)} or {@link #cancelAll()} are called.
	 *
	 * @param kommandToken value to be added
	 * @param tag value to be used as tag to the {@link KommandToken}
	 * @throws IllegalArgumentException if kommandToken or tag is {@code null}
	 */
	public <T> void append(KommandToken kommandToken, T tag) {
		assertNullTag(tag);
		internalAppend(kommandToken, tag);
	}

	private <T> void internalAppend(KommandToken kommandToken, T tag) {
		if (kommandToken == null) {
			throw new IllegalArgumentException("Null KommandToken not allowed");
		}
		synchronized (this) {
			List<KommandToken> tokens = tokenTags.get(tag);
			if (tokens == null) {
				tokens = new LinkedList<>();
				tokenTags.put(tag, tokens);
			}
			tokens.add(kommandToken);
		}
	}

	/**
	 * Cancels (through to its {@link KommandToken}) all pending active {@link Kommand} previously appended to the
	 * {@link KommandTokenBox} with the given tag.
	 *
	 * @param tag value that marks the kommands to be cancelled
	 * @throws IllegalArgumentException if tag is {@code null}
	 */
	public <T> void cancel(T tag) {
		assertNullTag(tag);
		synchronized (this) {
			List<KommandToken> tokens = tokenTags.remove(tag);
			if (tokens != null) {
				cancel(tokens);
			}
		}
	}

	/**
	 * Cancels (through to its {@link KommandToken}) all pending active {@link Kommand} previously appended to the
	 * {@link KommandTokenBox}.
	 */
	public synchronized void cancelAll() {
		for (List<KommandToken> tokens : tokenTags.values()) {
			cancel(tokens);
		}
		tokenTags.clear();
	}

	private void cancel(List<KommandToken> tokens) {
		for (KommandToken token : tokens) {
			token.cancel();
		}
	}

	private <T> void assertNullTag(T tag) {
		if (tag == null) {
			throw new IllegalArgumentException("Null tag not allowed");
		}
	}

}
