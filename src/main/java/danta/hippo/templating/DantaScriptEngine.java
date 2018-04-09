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

import javax.servlet.ServletConfig;

import org.onehippo.forge.templating.support.handlebars.servlet.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.jknack.handlebars.Handlebars;

public class DantaScriptEngine extends HandlebarsHstTemplateServlet {

    protected final Logger LOG = LoggerFactory.getLogger(this.getClass());

    public static final String START_DELIM = "{%";
    public static final String END_DELIM = "%}";

    /**
     * This method is invoked from HttpServlet#init(...) after initializing the handlebars to register
     * any extra helpers. Its parent method is designed to be extended for any customization as well in child class,
     * having a chance to access the internal handlebars object.
     * @param config ServletConfig instance
     * @param handlebars Handlebars instance
     */
    @Override
    protected void registerHelpers(ServletConfig config, Handlebars handlebars) {
        super.registerHelpers(config, handlebars);

        handlebars.setStartDelimiter(START_DELIM);
        handlebars.setEndDelimiter(END_DELIM);
    }

}
