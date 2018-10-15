package com.hs3.home.utils;

import org.sitemesh.DecoratorSelector;
import org.sitemesh.builder.SiteMeshFilterBuilder;
import org.sitemesh.config.ConfigurableSiteMeshFilter;
import org.sitemesh.webapp.WebAppContext;

public class ParamConfigurableSiteMeshFilter extends ConfigurableSiteMeshFilter {
    protected void applyCustomConfiguration(SiteMeshFilterBuilder builder) {
        DecoratorSelector<WebAppContext> defaultDecoratorSelector = builder.getDecoratorSelector();

        builder.setCustomDecoratorSelector(new ParamDecoratorSelector(defaultDecoratorSelector));
    }
}
