package org.bitbucket.fermenter.stout.messages;

import org.junit.After;

/**
 * Base test support class that may be extended by tests that leverage Spring's
 * {@link org.springframework.test.context.web.WebAppConfiguration} to manage the underlying {@link Messages} object
 * that is injected into the {@link MessageManager} utilized by tested Fermenter-generated code. <br>
 * <br>
 * As Spring will maintain the same request-scoped {@link Messages} object across multiple tests, the {@link Messages}
 * need to be manually cleared out after each test in order to prevent messages collected during the execution of one
 * test method to seep into others.
 */
public class AbstractMsgMgrAwareTestSupport {

	@After
	public void cleanupMsgMgr() throws Exception {
		MessageManagerInitializationDelegate.cleanupMessageManager();
	}
}
