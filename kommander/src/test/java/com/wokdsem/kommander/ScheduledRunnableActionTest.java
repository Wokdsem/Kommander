package com.wokdsem.kommander;

import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ScheduledRunnableActionTest {
	
	private final Dispatcher dispatcher;
	private final RunnableActionBundle bundle;
	
	public ScheduledRunnableActionTest() {
		this.dispatcher = mock(Dispatcher.class);
		this.bundle = mock(RunnableActionBundle.class);
	}
	
	@Test
	public void run_scheduledRunnableIsRun_kommandIsDispatched() {
		new ScheduledRunnableAction(dispatcher, bundle).run();
		verify(dispatcher).kommand(bundle, 0);
	}
	
	@Test
	public void run_scheduledRunnableDoubleRun_secondTimeIsIgnored() {
		ScheduledRunnableAction runnableAction = new ScheduledRunnableAction(dispatcher, bundle);
		runnableAction.run();
		runnableAction.run();
		verify(dispatcher).kommand(bundle, 0);
	}
	
	@Test
	public void cancel_onCanceledAction_scheduledIsNotExecuted() {
		ScheduledRunnableAction runnableAction = new ScheduledRunnableAction(dispatcher, bundle);
		runnableAction.cancel();
		runnableAction.run();
		verify(dispatcher, never()).kommand(bundle, 0);
	}
	
	@Test
	public void cancel_onDelayDispatched_dispatchedKommandIsCanceled() {
		KommandToken token = mock(KommandToken.class);
		when(dispatcher.kommand(bundle, 0)).thenReturn(token);
		ScheduledRunnableAction runnableAction = new ScheduledRunnableAction(dispatcher, bundle);
		runnableAction.run();
		runnableAction.cancel();
		verify(token).cancel();
	}
	
}