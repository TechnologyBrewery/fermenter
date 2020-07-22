package org.bitbucket.fermenter.stout.authn;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.UnavailableSecurityManagerException;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.support.SubjectThreadState;
import org.apache.shiro.util.LifecycleUtils;
import org.apache.shiro.util.ThreadState;
import org.junit.AfterClass;

/**
 * Provides test authentication capabilities.
 */
public final class AuthenticationTestUtils {

    private static ThreadState subjectThreadState;

    private AuthenticationTestUtils() {
        // prevent instantiation of all static class
    }

    /**
     * Allows subclasses to set the currently executing {@link Subject} instance.
     *
     * @param subject
     *            the Subject instance
     */
    public static void login(String username) {
        logout();
        StoutTestSubject subject = new StoutTestSubject(username);
        subjectThreadState = createThreadState(subject);
        subjectThreadState.bind();
    }

    /**
     * Clears Shiro's thread state, ensuring the thread remains clean for future test execution.
     */
    public static void logout() {
        doClearSubject();
    }

    private static void doClearSubject() {
        if (subjectThreadState != null) {
            subjectThreadState.clear();
            subjectThreadState = null;
        }
    }
    
    private static ThreadState createThreadState(Subject subject) {
        return new SubjectThreadState(subject);
    }

    public static void setSecurityManager(SecurityManager securityManager) {
        SecurityUtils.setSecurityManager(securityManager);
    }

    public static SecurityManager getSecurityManager() {
        return SecurityUtils.getSecurityManager();
    }

    @AfterClass
    public static void tearDownShiro() {
        doClearSubject();
        try {
            SecurityManager securityManager = getSecurityManager();
            LifecycleUtils.destroy(securityManager);
        } catch (UnavailableSecurityManagerException e) {
            // we don't care about this when cleaning up the test environment
            // (for example, maybe the subclass is a unit test and it didn't
            // need a SecurityManager instance because it was using only
            // mock Subject instances)
        }
        setSecurityManager(null);
    }

}
