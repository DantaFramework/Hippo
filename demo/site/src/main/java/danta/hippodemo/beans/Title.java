package danta.hippodemo.beans;

import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;
import org.hippoecm.hst.content.beans.Node;

@HippoEssentialsGenerated(internalName = "hippodemo:title")
@Node(jcrType = "hippodemo:title")
public class Title extends BaseDocument {
    @HippoEssentialsGenerated(internalName = "hippodemo:title")
    public String getTitle() {
        return getProperty("hippodemo:title");
    }

    @HippoEssentialsGenerated(internalName = "hippodemo:text")
    public String getText() {
        return getProperty("hippodemo:text");
    }
}
