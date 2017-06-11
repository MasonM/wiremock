/*
 * Copyright (C) 2011 Thomas Akehurst
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.tomakehurst.wiremock.admin.tasks;

import com.github.tomakehurst.wiremock.admin.AdminTask;
import com.github.tomakehurst.wiremock.admin.model.PathParams;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.core.Admin;
import com.github.tomakehurst.wiremock.extension.ResponseDefinitionTransformer;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.http.ResponseDefinition;
import javax.script.*;

import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static java.net.HttpURLConnection.HTTP_OK;

public class LoadJavascriptExtension implements AdminTask {

    @Override
    public ResponseDefinition execute(Admin admin, Request request, PathParams pathParams) {
        ResponseDefinitionTransformer transformer;
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("nashorn");

        try {
            String script = request.getBodyAsString();
            transformer = (ResponseDefinitionTransformer) engine.eval(script);
        } catch (ScriptException ex) {
            return ResponseDefinitionBuilder.jsonResponse("Error: " + ex.getMessage(), HTTP_BAD_REQUEST);
        }

        // Just a example response definition for the POC
        ResponseDefinition testResponseDefinition = new ResponseDefinitionBuilder()
            .withStatus(HTTP_OK)
            .withBody("TEST BODY")
            .build();

        // Invoke the JS transformer. Pass nulls for everything else since this is a POC
        ResponseDefinition transformed = transformer.transform(null, testResponseDefinition, null, null);

        return ResponseDefinitionBuilder.jsonResponse(transformed, HTTP_OK);
    }
}
