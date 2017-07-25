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
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.extension.Parameters;
import com.github.tomakehurst.wiremock.http.ResponseDefinition;

import java.util.List;
import java.util.Map;

/**
 * Encapsulates options for generating and outputting StubMappings
 */
public class RecordSpec {

    private final ResponseDefinition proxyResponse;

    // Whitelist requests to generate StubMappings for
    private final ProxiedServeEventFilters filters;
    // Headers from the request to include in the stub mapping, if they match the corresponding matcher
    private final Map<String, CaptureHeadersSpec> captureHeaders;
    // Criteria for extracting body from responses
    private final ResponseDefinitionBodyMatcher extractBodyCriteria;
    // How to format StubMappings in the response body
    private final SnapshotOutputFormatter outputFormat;
    // Whether to persist stub mappings
    private final Boolean persist;
    // Whether duplicate requests should be recorded as scenarios or just discarded
    private final Boolean repeatsAsScenarios;
    // Stub mapping transformers
    private final List<String> transformers;
    // Parameters for stub mapping transformers
    private final Parameters transformerParameters;
    private final JsonMatchingFlags jsonMatchingFlags;

    @JsonCreator
    public RecordSpec(
        @JsonProperty("targetBaseUrl") String targetBaseUrl,
        @JsonProperty("proxyResponse") ResponseDefinition proxyResponse,
        @JsonProperty("filters") ProxiedServeEventFilters filters,
        @JsonProperty("captureHeaders") Map<String, CaptureHeadersSpec> captureHeaders,
        @JsonProperty("extractBodyCriteria") ResponseDefinitionBodyMatcher extractBodyCriteria,
        @JsonProperty("outputFormat") SnapshotOutputFormatter outputFormat,
        @JsonProperty("persist") Boolean persist,
        @JsonProperty("repeatsAsScenarios") Boolean repeatsAsScenarios,
        @JsonProperty("transformers") List<String> transformers,
        @JsonProperty("transformerParameters") Parameters transformerParameters,
        @JsonProperty("jsonMatchingFlags") JsonMatchingFlags jsonMatchingFlags) {
        if (proxyResponse == null && targetBaseUrl != null) {
            proxyResponse = new ResponseDefinitionBuilder().proxiedFrom(targetBaseUrl).build();
        }
        this.proxyResponse = proxyResponse;
        this.filters = filters == null ? new ProxiedServeEventFilters() : filters;
        this.captureHeaders = captureHeaders;
        this.extractBodyCriteria = extractBodyCriteria;
        this.outputFormat = outputFormat == null ? SnapshotOutputFormatter.FULL : outputFormat;
        this.persist = persist == null ? true : persist;
        this.repeatsAsScenarios = repeatsAsScenarios;
        this.transformers = transformers;
        this.transformerParameters = transformerParameters;
        this.jsonMatchingFlags = jsonMatchingFlags;
    }

    private RecordSpec() {
        this(null, null, null, null, null, null, null, null, null, null, null);
    }

    public static final RecordSpec DEFAULTS = new RecordSpec();

    public static RecordSpec forBaseUrl(String targetBaseUrl) {
        return new RecordSpec(targetBaseUrl, null, null, null, null, null, null, true, null, null, null);
    }

    public ResponseDefinition getProxyResponse() {
        return proxyResponse;
    }

    public ProxiedServeEventFilters getFilters() { return filters; }

    public Map<String, CaptureHeadersSpec> getCaptureHeaders() { return captureHeaders; }

    public SnapshotOutputFormatter getOutputFormat() { return outputFormat; }

    @JsonProperty("persist")
    public boolean shouldPersist() { return persist; }

    @JsonIgnore
    public boolean shouldRecordRepeatsAsScenarios() {
        return repeatsAsScenarios == null ? true : repeatsAsScenarios;
    }

    public Boolean getRepeatsAsScenarios() {
        return repeatsAsScenarios;
    }

    public List<String> getTransformers() { return transformers; }

    public Parameters getTransformerParameters() { return transformerParameters; }

    public ResponseDefinitionBodyMatcher getExtractBodyCriteria() { return extractBodyCriteria; }

    public JsonMatchingFlags getJsonMatchingFlags() {
        return jsonMatchingFlags;
    }
}
