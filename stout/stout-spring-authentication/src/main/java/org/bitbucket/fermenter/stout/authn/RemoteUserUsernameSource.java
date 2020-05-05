package org.bitbucket.fermenter.stout.authn;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class RemoteUserUsernameSource implements UsernameSource {

    public String extractUsername(HttpServletRequest request) {
        String username = request.getRemoteUser();
        return this.stripPrefix(username);
    }

    public boolean isResponsible(HttpServletRequest request) {
        return request.getRemoteUser() != null;
    }

    private String stripPrefix(String username) {
        if (!StringUtils.isEmpty(username)) {
            int index = username.lastIndexOf("\\");
            if (index > -1) {
                return username.substring(index + 1);
            }
        }

        return username;
    }
}
