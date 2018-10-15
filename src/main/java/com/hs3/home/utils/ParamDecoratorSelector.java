package com.hs3.home.utils;

import com.hs3.utils.StrUtils;
import org.sitemesh.DecoratorSelector;
import org.sitemesh.content.Content;
import org.sitemesh.webapp.WebAppContext;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


public class ParamDecoratorSelector
        implements DecoratorSelector<WebAppContext> {
    private DecoratorSelector<WebAppContext> defaultDecoratorSelector;

    public ParamDecoratorSelector(DecoratorSelector<WebAppContext> defaultDecoratorSelector) {
        this.defaultDecoratorSelector = defaultDecoratorSelector;
    }

    private boolean isMobile(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null)
            return false;
        Cookie[] arrayOfCookie1;
        int j = (arrayOfCookie1 = cookies).length;
        for (int i = 0; i < j; i++) {
            Cookie c = arrayOfCookie1[i];
            if (("mobile".equals(c.getName())) && ("1".equals(c.getValue()))) {
                return true;
            }
        }
        return false;
    }

    public String[] selectDecoratorPaths(Content content, WebAppContext context) throws IOException {
        HttpServletRequest request = context.getRequest();
        boolean isMobile = isMobile(request);
        if (isMobile) {
            return new String[0];
        }

        String decorator1 = request.getParameter("decorator");
        Object decorator2 = request.getAttribute("decorator");
        if (StrUtils.hasNotEmpty(new Object[]{decorator1, decorator2})) {
            return new String[0];
        }

        return this.defaultDecoratorSelector.selectDecoratorPaths(content, context);
    }
}
