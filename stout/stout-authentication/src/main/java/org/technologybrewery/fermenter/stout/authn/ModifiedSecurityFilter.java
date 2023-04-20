package org.technologybrewery.fermenter.stout.authn;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pac4j.core.context.JEEContext;
import org.pac4j.core.context.session.SessionStore;
import org.pac4j.core.engine.SecurityLogic;
import org.pac4j.core.http.adapter.HttpActionAdapter;
import org.pac4j.core.http.adapter.JEEHttpActionAdapter;
import org.pac4j.core.util.FindBest;

import io.buji.pac4j.context.ShiroSessionStore;
import io.buji.pac4j.filter.SecurityFilter;

/**
 * Modified security filter that creates a SecurityLogic object through the use of a
 * ModifiedDefaultSecurityLogic object.
 *
 */
public class ModifiedSecurityFilter extends SecurityFilter {

    @Override
    public void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse, final FilterChain filterChain) throws IOException, ServletException {

        final SessionStore<JEEContext> bestSessionStore = FindBest.sessionStore(null, getConfig(), ShiroSessionStore.INSTANCE);
        final HttpActionAdapter<Object, JEEContext> bestAdapter = FindBest.httpActionAdapter(null, getConfig(), JEEHttpActionAdapter.INSTANCE);
        final SecurityLogic<Object, JEEContext> bestLogic = FindBest.securityLogic(getSecurityLogic(), getConfig(), ModifiedDefaultSecurityLogic.INSTANCE);

        final HttpServletRequest request = (HttpServletRequest) servletRequest;
        final HttpServletResponse response = (HttpServletResponse) servletResponse;
        final JEEContext context = new JEEContext(request, response, bestSessionStore);
        bestLogic.perform(context, getConfig(), (ctx, profiles, parameters) -> {

            filterChain.doFilter(request, response);
            return null;

        }, bestAdapter, getClients(), getAuthorizers(), getMatchers(), getMultiProfile());
    }

}
