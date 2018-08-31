package org.bitbucket.fermenter.stout.authz.json;

import java.security.Key;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;

import org.aeonbits.owner.KrauseningConfigFactory;
import org.apache.commons.lang3.StringUtils;
import org.bitbucket.fermenter.stout.authz.PolicyDecision;
import org.bitbucket.fermenter.stout.authz.PolicyDecisionPoint;
import org.bitbucket.fermenter.stout.authz.config.AuthorizationConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

/**
 * Builds a JSON Web Token (JWT) for a given subject and set of policies (represented as resource/action pairs). This
 * allows you to ask the PDP for a number of claims and represent them in a JWT format. Within a service, you can simply
 * return this token as a string.
 *
 */
public final class JsonWebTokenBuilder {

    private static final Logger logger = LoggerFactory.getLogger(JsonWebTokenBuilder.class);

    private static AuthorizationConfig config = KrauseningConfigFactory.create(AuthorizationConfig.class);

    private static Key signingKey = getSigningKey();

    private static PolicyDecisionPoint pdp = PolicyDecisionPoint.getInstance();

    /**
     * Creates a JSON Web Token (JWT) for the given subject, audience, and resource/action pairs.
     * @param subject The subject for which the token will contain information
     * @param audience The audience to whom this token is intended
     * @param ruleClaims resource/action pairs for which the PDP will be asked for decisions
     * @return A signed, compressed JWT token
     */
    public static String createToken(String subject, String audience, Collection<PolicyRequest> ruleClaims) {
        JwtBuilder builder = Jwts.builder();
        builder.setId(UUID.randomUUID().toString());
        builder.setSubject(subject);
        builder.setAudience(audience);
        builder.setIssuer(getIssuer());

        // handle dates off a common baseline to ensure millisecond consistency:
        Date currentTime = new Date();
        builder.setNotBefore(new Date(currentTime.getTime() - getSkewInMillis()));
        builder.setIssuedAt(currentTime);
        builder.setExpiration(new Date(currentTime.getTime() + getExpirationInMillis() + getSkewInMillis()));

        if (ruleClaims != null) {
            for (PolicyRequest ruleClaim : ruleClaims) {
                PolicyDecision decision = pdp.isAuthorized(subject, ruleClaim.getResource(), ruleClaim.getAction());
                builder.claim(ruleClaim.toString(), decision.toString());
            }
        }

        builder.signWith(signingKey);

        return builder.compact();
    }

    /**
     * Decodes a JSON Web Token.
     * @param token the token to decode
     * @return the decoded token.  See JJWT for usage details.
     */
    public static Jws<Claims> parseLocalToken(String token) {
        JwtParser parser = Jwts.parser();
        parser.setSigningKey(signingKey);
        Jws<Claims> jwt = parser.parseClaimsJws(token);
        if (logger.isDebugEnabled()) {
            logger.debug("Parsed the following JWT: {}", jwt);
        }
        return jwt;
    }

    private static long getSkewInMillis() {
        return config.getTokenSkewInSeconds() * 1000;
    }

    private static long getExpirationInMillis() {
        return config.getTokenExpirationInSeconds() * 1000;
    }

    private static String getIssuer() {
        String issuer = null;

        // TODO: FER-128 - if we have a private key set, we should just use that. But not there just yet.
        // Probably the krausening value, then the key, then a placeholder.
        issuer = config.getTokenIssuer();
        if (StringUtils.isBlank(issuer)) {
            issuer = "unspecified";
        }

        return issuer;
    }

    private static Key getSigningKey() {
        // TODO: FER-128 - look up server key first, but for now, just create one:
        return Keys.secretKeyFor(SignatureAlgorithm.HS256);
    }

}
