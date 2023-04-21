package org.technologybrewery.fermenter.stout.authn;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;

import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;

/**
 * A simple mocked out Subject that allows a username to be asserted at the logged in subject.
 * 
 * We could have used a mock utility to mock this class out instead. While this is more lines of code, it eliminates the
 * extra dependency and is likely more clear to users who aren't familiar with those libraries (which Stout reserves for
 * complex mocking cases ).
 */
public class StoutTestSubject implements Subject {

    private String principal;
    private boolean isAuthenticated;

    public StoutTestSubject(String principal) {
        this.principal = principal;
        isAuthenticated = true;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    @Override
    public PrincipalCollection getPrincipals() {
        // Auto-generated method stub
        return null;
    }

    @Override
    public boolean isPermitted(String permission) {
        // Auto-generated method stub
        return false;
    }

    @Override
    public boolean isPermitted(Permission permission) {
        // Auto-generated method stub
        return false;
    }

    @Override
    public boolean[] isPermitted(String... permissions) {
        // Auto-generated method stub
        return null;
    }

    @Override
    public boolean[] isPermitted(List<Permission> permissions) {
        // Auto-generated method stub
        return null;
    }

    @Override
    public boolean isPermittedAll(String... permissions) {
        // Auto-generated method stub
        return false;
    }

    @Override
    public boolean isPermittedAll(Collection<Permission> permissions) {
        // Auto-generated method stub
        return false;
    }

    @Override
    public void checkPermission(String permission) {
        // Auto-generated method stub

    }

    @Override
    public void checkPermission(Permission permission) {
        // Auto-generated method stub

    }

    @Override
    public void checkPermissions(String... permissions) {
        // Auto-generated method stub

    }

    @Override
    public void checkPermissions(Collection<Permission> permissions) {
        // Auto-generated method stub

    }

    @Override
    public boolean hasRole(String roleIdentifier) {
        // Auto-generated method stub
        return false;
    }

    @Override
    public boolean[] hasRoles(List<String> roleIdentifiers) {
        // Auto-generated method stub
        return null;
    }

    @Override
    public boolean hasAllRoles(Collection<String> roleIdentifiers) {
        // Auto-generated method stub
        return false;
    }

    @Override
    public void checkRole(String roleIdentifier) {
        // Auto-generated method stub

    }

    @Override
    public void checkRoles(Collection<String> roleIdentifiers) {
        // Auto-generated method stub

    }

    @Override
    public void checkRoles(String... roleIdentifiers) {
        // Auto-generated method stub

    }

    @Override
    public void login(AuthenticationToken token) {
        // Auto-generated method stub

    }

    @Override
    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    @Override
    public boolean isRemembered() {
        // Auto-generated method stub
        return false;
    }

    @Override
    public Session getSession() {
        // Auto-generated method stub
        return null;
    }

    @Override
    public Session getSession(boolean create) {
        // Auto-generated method stub
        return null;
    }

    @Override
    public void logout() {
        isAuthenticated = false;

    }

    @Override
    public <V> V execute(Callable<V> callable) {
        // Auto-generated method stub
        return null;
    }

    @Override
    public void execute(Runnable runnable) {
        // Auto-generated method stub

    }

    @Override
    public <V> Callable<V> associateWith(Callable<V> callable) {
        // Auto-generated method stub
        return null;
    }

    @Override
    public Runnable associateWith(Runnable runnable) {
        // Auto-generated method stub
        return null;
    }

    @Override
    public void runAs(PrincipalCollection principals) {
        // Auto-generated method stub

    }

    @Override
    public boolean isRunAs() {
        // Auto-generated method stub
        return false;
    }

    @Override
    public PrincipalCollection getPreviousPrincipals() {
        // Auto-generated method stub
        return null;
    }

    @Override
    public PrincipalCollection releaseRunAs() {
        // Auto-generated method stub
        return null;
    }

}
