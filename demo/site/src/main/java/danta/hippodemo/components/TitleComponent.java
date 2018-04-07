package danta.hippodemo.components;

import danta.hippodemo.beans.Title;
import org.hippoecm.hst.component.support.bean.BaseHstComponent;
import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TitleComponent extends BaseHstComponent {

    public static final Logger log = LoggerFactory.getLogger(TitleComponent.class);

    @Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) throws HstComponentException {
        super.doBeforeRender(request, response);
        final HstRequestContext ctx = request.getRequestContext();

        // Retrieve the document based on the URL
        Title document = (Title) ctx.getContentBean();

        if (document != null) {
            // Put the document on the request
            request.setAttribute("document", document);
        }
    }
}