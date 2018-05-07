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
import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Service;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;

@Component(label = "Support for processing templating requests", metatype = false)
@Service(TemplatingSupport.class)
// POC:
// The purpose here is to provide Danta templating support as an OSGi service to an external application
public class TemplatingSupport {

    protected final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Activate
    protected void activate(ComponentContext componentContext)
            throws Exception {

    }

    @Deactivate
    protected void deactivate(ComponentContext componentContext)
            throws Exception {
    }

    public void execute(final ServletRequest servletRequest, final ServletResponse servletResponse)
            throws IOException, ServletException {

        try {

                LOG.info("DANTA TEMPLATING SUPPORT SERVICE");

        } catch (RuntimeException re) {
            throw re;
        } catch (Exception ew) {
            throw new ServletException(ew);
        } finally {

        }
    }

    public String getName(){

        return "TemplatingSupport";
    };

}
