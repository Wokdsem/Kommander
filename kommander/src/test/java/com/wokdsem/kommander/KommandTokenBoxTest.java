package com.wokdsem.kommander;

import org.junit.Test;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class KommandTokenBoxTest {

	private KommandTokenBox tokenBox;

	public KommandTokenBoxTest() {
		this.tokenBox = new KommandTokenBox();
	}

	@Test
	public void append_nullToken_exceptionThrown() {
		try {
			tokenBox.append(null);
			fail();
		} catch (Exception ignored) {
		}
	}

	@Test
	public void append_tokenWithNullTag_exceptionThrown() {
		KommandToken kommandToken = mock(KommandToken.class);
		try {
			tokenBox.append(kommandToken, null);
			fail();
		} catch (Exception ignored) {
		}
	}

	@Test
	public void cancel_withKnownTag_cancelled() {
		String tag = "TAG";
		KommandToken kommandToken = mock(KommandToken.class);
		tokenBox.append(kommandToken, tag);
		tokenBox.cancel(tag);
		verify(kommandToken).cancel();
	}

	@Test
	public void cancel_toUnknownTag_tokenIsNotCancelled() {
		KommandToken kommandToken = mock(KommandToken.class);
		tokenBox.append(kommandToken);
		tokenBox.cancel("TAG");
		verify(kommandToken, never()).cancel();
	}

	@Test
	public void cancelAll_withAppendedTokens_areCancelled() {
		KommandToken token1 = mock(KommandToken.class);
		KommandToken token2 = mock(KommandToken.class);
		tokenBox.append(token1);
		tokenBox.append(token2, "TAG");
		tokenBox.cancelAll();
		verify(token1).cancel();
		verify(token2).cancel();
	}
	
}