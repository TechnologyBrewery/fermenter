package org.bitbucket.fermenter.stout.authn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.pac4j.core.context.HttpConstants;
import org.pac4j.core.context.WebContext;
import org.pac4j.core.matching.checker.MatchingChecker;
import org.pac4j.core.matching.matcher.CacheControlMatcher;
import org.pac4j.core.matching.matcher.CorsMatcher;
import org.pac4j.core.matching.matcher.DefaultMatchers;
import org.pac4j.core.matching.matcher.HttpMethodMatcher;
import org.pac4j.core.matching.matcher.Matcher;
import org.pac4j.core.matching.matcher.StrictTransportSecurityMatcher;
import org.pac4j.core.matching.matcher.XContentTypeOptionsMatcher;
import org.pac4j.core.matching.matcher.XFrameOptionsMatcher;
import org.pac4j.core.matching.matcher.XSSProtectionMatcher;
import org.pac4j.core.matching.matcher.csrf.CsrfTokenGeneratorMatcher;
import org.pac4j.core.matching.matcher.csrf.DefaultCsrfTokenGenerator;
import org.pac4j.core.util.CommonHelper;
import org.pac4j.core.util.Pac4jConstants;

/**
 * Default way to check the matchers (with default matchers).
 *
 */
public class ModifiedDefaultMatchingChecker implements MatchingChecker {

    private static final Matcher GET_MATCHER = new HttpMethodMatcher(HttpConstants.HTTP_METHOD.GET);
    private static final Matcher POST_MATCHER = new HttpMethodMatcher(HttpConstants.HTTP_METHOD.POST);
    private static final Matcher PUT_MATCHER = new HttpMethodMatcher(HttpConstants.HTTP_METHOD.PUT);
    private static final Matcher DELETE_MATCHER = new HttpMethodMatcher(HttpConstants.HTTP_METHOD.DELETE);

    static final StrictTransportSecurityMatcher STRICT_TRANSPORT_MATCHER = new StrictTransportSecurityMatcher();
    static final XContentTypeOptionsMatcher X_CONTENT_TYPE_OPTIONS_MATCHER = new XContentTypeOptionsMatcher();
    static final XFrameOptionsMatcher X_FRAME_OPTIONS_MATCHER = new XFrameOptionsMatcher();
    static final XSSProtectionMatcher XSS_PROTECTION_MATCHER = new XSSProtectionMatcher();
    static final CacheControlMatcher CACHE_CONTROL_MATCHER = new CacheControlMatcher();
    static final CsrfTokenGeneratorMatcher CSRF_TOKEN_MATCHER = new CsrfTokenGeneratorMatcher(new DefaultCsrfTokenGenerator());
    static final CorsMatcher CORS_MATCHER = new CorsMatcher();

    static {
        CORS_MATCHER.setAllowOrigin("*");
        CORS_MATCHER.setAllowCredentials(true);
        final Set<HttpConstants.HTTP_METHOD> methods = new HashSet<>();
        methods.add(HttpConstants.HTTP_METHOD.GET);
        methods.add(HttpConstants.HTTP_METHOD.PUT);
        methods.add(HttpConstants.HTTP_METHOD.POST);
        methods.add(HttpConstants.HTTP_METHOD.DELETE);
        methods.add(HttpConstants.HTTP_METHOD.OPTIONS);
        CORS_MATCHER.setAllowMethods(methods);
    }

    @Override
    public boolean matches(final WebContext context, final String matchersValue, final Map<String, Matcher> matchersMap) {
        String matcherNames = matchersValue;
        // by default, if we have no matchers defined, we apply the security headers and the CSRF token generation
        if (CommonHelper.isBlank(matcherNames)) {
            matcherNames = DefaultMatchers.SECURITYHEADERS + Pac4jConstants.ELEMENT_SEPARATOR + DefaultMatchers.CSRF_TOKEN;
        }
        final List<Matcher> matchers = new ArrayList<>();
        // we must have matchers
        CommonHelper.assertNotNull("matchersMap", matchersMap);
        final Map<String, Matcher> allMatchers = buildAllMatchers(matchersMap);
        final String[] names = matcherNames.split(Pac4jConstants.ELEMENT_SEPARATOR);
        final int nb = names.length;
        for (int i = 0; i < nb; i++) {
            String name = names[i].trim();

            // If this name is a key-value map entry incoming from a Shiro
            // configuration file, parse it to obtain just the key as a name
            if (name.indexOf(":") != -1) {
                name = convertNameFromMapEntryToString(name);
            }

            if (DefaultMatchers.HSTS.equalsIgnoreCase(name)) {
                matchers.add(STRICT_TRANSPORT_MATCHER);
            } else if (DefaultMatchers.NOSNIFF.equalsIgnoreCase(name)) {
                matchers.add(X_CONTENT_TYPE_OPTIONS_MATCHER);
            } else if (DefaultMatchers.NOFRAME.equalsIgnoreCase(name)) {
                matchers.add(X_FRAME_OPTIONS_MATCHER);
            } else if (DefaultMatchers.XSSPROTECTION.equalsIgnoreCase(name)) {
                matchers.add(XSS_PROTECTION_MATCHER);
            } else if (DefaultMatchers.NOCACHE.equalsIgnoreCase(name)) {
                matchers.add(CACHE_CONTROL_MATCHER);
            } else if (DefaultMatchers.SECURITYHEADERS.equalsIgnoreCase(name)) {
                matchers.add(CACHE_CONTROL_MATCHER);
                matchers.add(X_CONTENT_TYPE_OPTIONS_MATCHER);
                matchers.add(STRICT_TRANSPORT_MATCHER);
                matchers.add(X_FRAME_OPTIONS_MATCHER);
                matchers.add(XSS_PROTECTION_MATCHER);
            } else if (DefaultMatchers.CSRF_TOKEN.equalsIgnoreCase(name)) {
                matchers.add(CSRF_TOKEN_MATCHER);
            } else if (DefaultMatchers.ALLOW_AJAX_REQUESTS.equalsIgnoreCase(name)) {
                matchers.add(CORS_MATCHER);
                // we don't add any matcher for none
            } else if (!DefaultMatchers.NONE.equalsIgnoreCase(name)) {
                Matcher result = null;
                for (final Map.Entry<String, Matcher> entry : allMatchers.entrySet()) {
                    if (CommonHelper.areEqualsIgnoreCaseAndTrim(entry.getKey(), name)) {
                        result = entry.getValue();
                        break;
                    }
                }
                // we must have a matcher defined for this name
                CommonHelper.assertNotNull("allMatchers['" + name + "']", result);
                matchers.add(result);
            }
        }
        if (!matchers.isEmpty()) {
            // check matching using matchers: all must be satisfied
            for (Matcher matcher : matchers) {
                if (!matcher.matches(context)) {
                    return false;
                }
            }
        }
        return true;
    }

    private Map<String, Matcher> buildAllMatchers(final Map<String, Matcher> matchersMap) {
        final Map<String, Matcher> allMatchers = new HashMap<>();
        allMatchers.putAll(matchersMap);
        addDefaultMatcherIfNotDefined(allMatchers, DefaultMatchers.GET, GET_MATCHER);
        addDefaultMatcherIfNotDefined(allMatchers, DefaultMatchers.POST, POST_MATCHER);
        addDefaultMatcherIfNotDefined(allMatchers, DefaultMatchers.PUT, PUT_MATCHER);
        addDefaultMatcherIfNotDefined(allMatchers, DefaultMatchers.DELETE, DELETE_MATCHER);
        return allMatchers;
    }

    private void addDefaultMatcherIfNotDefined(final Map<String, Matcher> allMatchers, final String name, final Matcher matcher) {
        if (!allMatchers.containsKey(name)) {
            allMatchers.put(name, matcher);
        }
    }

    /**
     * Convert the incoming name of a Matcher from a key-value map entry 
     * (which Shiro uses/expects), to a String entry (which PAC4J uses/expects).
     *
     * Example:
     * 'nocache:$noCacheMatcher' -> 'nocache'
     * @param nameToModify the incoming Matcher name
     * @return the Matcher name as a String
     */
    private String convertNameFromMapEntryToString(String nameToModify) {
        String[] keyValuePair = nameToModify.split(":");
        CommonHelper.assertTrue(keyValuePair.length == 2, "Key/value pair not correctly parsed for matcher name " + nameToModify + "!");
        return keyValuePair[0];
    }
}
