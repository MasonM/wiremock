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
    // Headers from the request to include in the stub mapping, if they match the corresponding matcher
    private final Map<String, CaptureHeadersSpec> captureHeaders;
    // Criteria for extracting body from responses
    private final ResponseDefinitionBodyMatcher extractBodyCriteria;
    // Whether duplicate requests should be recorded as scenarios or just discarded
    private final Boolean repeatsAsScenarios;
    private final JsonMatchingFlags jsonMatchingFlags;
    // Stub mapping transformers
    private final List<String> transformers;
    // Parameters for stub mapping transformers
    private final Parameters transformerParameters;
    // Whether to persist stub mappings
    private final Boolean persist;

    @JsonCreator
    public GeneratorOptionsSpec(
        @JsonProperty("captureHeaders") Map<String, CaptureHeadersSpec> captureHeaders,
        @JsonProperty("extractBodyCriteria") ResponseDefinitionBodyMatcher extractBodyCriteria,
        @JsonProperty("repeatsAsScenarios") Boolean repeatsAsScenarios,
        @JsonProperty("jsonMatchingFlags") JsonMatchingFlags jsonMatchingFlags,
        @JsonProperty("transformers") List<String> transformers,
        @JsonProperty("transformerParameters") Parameters transformerParameters,
        @JsonProperty("persist") Boolean persist) {
        this.captureHeaders = captureHeaders;
        this.extractBodyCriteria = extractBodyCriteria;
        this.repeatsAsScenarios = repeatsAsScenarios;
        this.jsonMatchingFlags = jsonMatchingFlags;
        this.transformers = transformers;
        this.transformerParameters = transformerParameters;
        this.persist = persist == null ? true : persist;
    }

    private GeneratorOptionsSpec() {
        this(null, null, null, null, null, null, null);
    }

    public static final GeneratorOptionsSpec DEFAULTS = new GeneratorOptionsSpec();

    public Map<String, CaptureHeadersSpec> getCaptureHeaders() {
        return captureHeaders;
    }

    @JsonProperty("persist")
    public boolean shouldPersist() { return persist; }

    @JsonIgnore
    public boolean shouldRecordRepeatsAsScenarios() {
        return repeatsAsScenarios == null ? true : repeatsAsScenarios;
    }

    public Boolean getRepeatsAsScenarios() {
        return repeatsAsScenarios;
    }

    public ResponseDefinitionBodyMatcher getExtractBodyCriteria() {
        return extractBodyCriteria;
    }

    public JsonMatchingFlags getJsonMatchingFlags() {
        return jsonMatchingFlags;
    }

    public List<String> getTransformers() {
        return transformers;
    }

    public Parameters getTransformerParameters() {
        return transformerParameters;
    }
}
