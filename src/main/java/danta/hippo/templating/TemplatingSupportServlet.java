/**
 * Danta Hippo Bundle
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

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.minidev.json.JSONObject;

import javax.servlet.ServletException;
import java.io.IOException;
import java.io.PrintWriter;

//
/**
 * POC:
 * The purpose here is to provide Danta templating support as a servlet running inside OSGi *
 * @author      jarriola
 * @version     1.0.0
 * @since       2018-01-18
 */
@Component
@Service
@Properties({
        @Property(name = "service.description", value = "Template Support"),
        @Property(name = "sling.servlet.selectors", value = "support"),
        @Property(name = "sling.servlet.extensions", value = "json"),
        @Property(name = "sling.servlet.resourceTypes", value = "sling/servlet/default")
})
public class TemplatingSupportServlet extends SlingSafeMethodsServlet {

    protected final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException {

        PrintWriter out = response.getWriter();
        JSONObject contentModel = new JSONObject();

        try {
            contentModel.put("DANTA" ,"content model");

            out.write(contentModel.toString());
            out.close();

        } catch (Exception ew) {
            throw new ServletException(ew);
        }
    }
}


