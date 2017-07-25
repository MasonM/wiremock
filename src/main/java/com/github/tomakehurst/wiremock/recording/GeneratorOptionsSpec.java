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
package com.github.tomakehurst.wiremock.recording;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.tomakehurst.wiremock.extension.Parameters;

import java.util.List;
import java.util.Map;

/**
 * Encapsulates options for generating StubMappings
 */
public class GeneratorOptionsSpec {
    private final RequestOptionsSpec request;
    private final ResponseOptionsSpec response;
    // Stub mapping transformers
    private final List<String> transformers;
    // Parameters for stub mapping transformers
    private final Parameters transformerParameters;
    // Whether to persist stub mappings
    private final Boolean persist;

    @JsonCreator
    public GeneratorOptionsSpec(
        @JsonProperty("request") RequestOptionsSpec request,
        @JsonProperty("response") ResponseOptionsSpec response,
        @JsonProperty("transformers") List<String> transformers,
        @JsonProperty("transformerParameters") Parameters transformerParameters,
        @JsonProperty("persist") Boolean persist) {
        this.request = request == null ? new RequestOptionsSpec(null, null, null) : request;
        this.response = response == null ? new ResponseOptionsSpec(null) : response;
        this.transformers = transformers;
        this.transformerParameters = transformerParameters;
        this.persist = persist == null ? true : persist;
    }

    private GeneratorOptionsSpec() {
        this(null, null, null, null, null);
    }

    public static final GeneratorOptionsSpec DEFAULTS = new GeneratorOptionsSpec();

    public RequestOptionsSpec getRequest() {
        return request;
    }

    public ResponseOptionsSpec getResponse() {
        return response;
    }

    @JsonProperty("persist")
    public boolean shouldPersist() { return persist; }

    public List<String> getTransformers() {
        return transformers;
    }

    public Parameters getTransformerParameters() {
        return transformerParameters;
    }
}
