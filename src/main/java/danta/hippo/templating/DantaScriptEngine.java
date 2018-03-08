/**
 * Danta Hippo Bundle
 * (danta.hippo)
 *
 * Copyright (C) 2017 Tikal Technologies, Inc. All rights reserved.
 *
 * Licensed under GNU Affero General Public License, Version v3.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.gnu.org/licenses/agpl-3.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied;
 * without even the implied warranty of MERCHANTABILITY.
 * See the License for more details.
 */

package danta.hippo.templating;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.InputStream;

import java.util.HashMap;
import java.util.Map;

import java.lang.reflect.*;

import javax.jcr.Session;
import javax.jcr.Node;
import javax.jcr.Binary;
import javax.jcr.Credentials;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.minidev.json.JSONObject;

import org.apache.commons.io.IOUtils;

import org.hippoecm.hst.content.beans.standard.HippoDocument;
import org.hippoecm.hst.site.HstServices;
import org.hippoecm.hst.util.WebFileUtils;
import org.hippoecm.hst.core.container.ContainerConstants;
import org.hippoecm.hst.freemarker.jcr.RepositorySource;
import org.hippoecm.repository.util.JcrUtils;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DantaScriptEngine extends HttpServlet {

    protected final Logger LOG = LoggerFactory.getLogger(this.getClass());

    public static final String START_DELIM = "{%";
    public static final String END_DELIM = "%}";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        /* LOADING CONTENT MODEL */
        // TODO: This code should be moved to a controller */

        // TemplateContentModelImpl contentModel = (TemplateContentModelImpl)
        //      request.getAttribute(TEMPLATE_CONTENT_MODEL_ATTR_NAME);

        // Retrieve document bean
        HippoDocument document = (HippoDocument) request.getAttribute("document");

        // Another way to retrieve document bean (from a controller class)
        //HippoDocumentBean subject = request.getRequestContext().getContentBean(HippoDocumentBean.class);

        // Retrieve content from bean
        Map<String,String> contentModel = new HashMap<>();
        Class documentClass = document.getClass();
        for (Method method : documentClass.getDeclaredMethods()) {
            LOG.debug(documentClass.getName() + "." + method.getName());
            try {
                String methodName = method.getName();
                Object contentHippoObject = method.invoke(document);
                String content = contentHippoObject.toString();
                LOG.debug(content);

                contentModel.put(methodName, content);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        /* END OF LOADING CONTENT MODEL */

        /* LOADING TEMPLATE */
        // TODO: Need to add JCR listener for danta templates
        // See https://www.onehippo.org/library/concepts/hst-spring/how-to-add-custom-jcr-event-listener-through-spring-configuration.html

        String templatePath = (String) request.getAttribute("javax.servlet.include.servlet_path");
        String dispatchUriProtocol = (String) request.getAttribute(ContainerConstants.DISPATCH_URI_PROTOCOL);
        templatePath = dispatchUriProtocol + templatePath;

        RepositorySource templateSource = getTemplateSource(templatePath);
        String unprocessedResponse = "";
        if(templateSource != null) {
            unprocessedResponse = templateSource.getTemplate();
        }

        /* END OF LOADING TEMPLATE */

        /* RENDERING RESPONSE */

        // Create handlebars
        Handlebars handlebars = new Handlebars();

        handlebars.setStartDelimiter(START_DELIM);
        handlebars.setEndDelimiter(END_DELIM);
        handlebars.infiniteLoops(true);

        Template template = handlebars.compileInline(unprocessedResponse);
        String outputHTML = template.apply(new JSONObject(contentModel));

        request.setCharacterEncoding("UTF-8");
        PrintWriter out = null;
        try {
            out = response.getWriter();
            out.write(outputHTML);
            out.flush();

        } finally {
            if (out != null) {
                out.close();
            }
        }

        /* END OF RENDERING RESPONSE */
    }

    private RepositorySource getTemplateSource(final String templatePath) {
        String jcrWebFilePath = WebFileUtils.webFilePathToJcrPath(templatePath);

        Session session = null;
        RepositorySource templateNode;
        try {
            session = createSession();
            if (session.nodeExists(jcrWebFilePath)) {
                Node templateNode1 = session.getNode(jcrWebFilePath);
                RepositorySource var5;
                if (templateNode1.isNodeType("nt:file")) {
                    var5 = this.createRepositorySourceFromBinary(templateNode1, jcrWebFilePath);

                    return var5;
                }
            }
        } catch (RepositoryException var9) {
            LOG.warn("RepositoryException while fetching freemarker template from repository", var9);
            templateNode = RepositorySource.notFound(templatePath);

            return templateNode;
        } finally {
            if (session != null) session.logout();
        }

        return null;
    }

    private Session createSession() throws RepositoryException {
        Repository repository = HstServices.getComponentManager().getComponent("javax.jcr.Repository.delegating");
        Credentials creds = HstServices.getComponentManager().getComponent("javax.jcr.Credentials.hstconfigreader.delegating");

        return repository.login(creds);
    }

    private RepositorySource createRepositorySourceFromBinary(Node templateNode, String absPath) throws RepositoryException {
        Node content = templateNode.getNode("jcr:content");
        String mimeType = JcrUtils.getStringProperty(content, "jcr:mimeType", (String)null);
        if(!"application/octet-stream".equals(mimeType) && !"text/html".equals(mimeType)) {
            LOG.warn("Expected freemarker binary or HTML at \'{}\' with mimetype \'{}\' or \'{}\' but was \'{}\'. Cannot return ftl for wrong mimetype", new Object[]{absPath, "application/octet-stream", "text/html", mimeType});
            return RepositorySource.notFound(absPath);
        } else {
            Binary ftl = JcrUtils.getBinaryProperty(content, "jcr:data", (Binary)null);
            if(ftl == null) {
                LOG.warn("Expected freemarker binary at \'{}\' but binary was null. Cannot return ftl for wrong mimetype", absPath);
                return RepositorySource.notFound(absPath);
            } else {
                InputStream stream = ftl.getStream();

                RepositorySource var8;
                try {
                    String e = IOUtils.toString(stream, "UTF-8");
                    var8 = this.createRepositorySource(e, absPath);

                    return var8;
                } catch (IOException var12) {
                    LOG.warn("Exception while reading freemarker binary from \'{}\'", absPath, var12);
                    var8 = RepositorySource.notFound(absPath);
                } finally {
                    IOUtils.closeQuietly(stream);
                }

                return var8;
            }
        }
    }

    private RepositorySource createRepositorySource(String template, String absJcrPath) {
        if(template == null) {
            LOG.debug("Template source \'{}\' not found in the repository. ", absJcrPath);

            return RepositorySource.notFound(absJcrPath);
        } else {

            return RepositorySource.found(absJcrPath, template);
        }
    }

}
