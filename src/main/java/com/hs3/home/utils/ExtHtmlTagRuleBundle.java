package com.hs3.home.utils;

import org.sitemesh.SiteMeshContext;
import org.sitemesh.content.ContentProperty;
import org.sitemesh.content.tagrules.TagRuleBundle;
import org.sitemesh.content.tagrules.html.ExportTagToContentRule;
import org.sitemesh.tagprocessor.State;

public class ExtHtmlTagRuleBundle
        implements TagRuleBundle {
    public void install(State defaultState, ContentProperty contentProperty, SiteMeshContext siteMeshContext) {
        defaultState.addRule("maintable", new ExportTagToContentRule(siteMeshContext, (ContentProperty) contentProperty.getChild("maintable"), false));
        defaultState.addRule("search", new ExportTagToContentRule(siteMeshContext, (ContentProperty) contentProperty.getChild("search"), false));
        defaultState.addRule("buttons", new ExportTagToContentRule(siteMeshContext, (ContentProperty) contentProperty.getChild("buttons"), false));
    }

    public void cleanUp(State defaultState, ContentProperty contentProperty, SiteMeshContext siteMeshContext) {
    }
}
