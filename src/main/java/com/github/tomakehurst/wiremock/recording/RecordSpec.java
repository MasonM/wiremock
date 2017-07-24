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
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Encapsulates options for generating and outputting StubMappings
 */
public class RecordSpec {

    private final String targetBaseUrl;

    // Whitelist requests to generate StubMappings for
    private final ProxiedServeEventFilters filters;
    // Options for generating StubMappings
    private final GeneratorOptionsSpec generatorOptions;
    // How to format StubMappings in the response body
    private final SnapshotOutputFormatter outputFormat;

    @JsonCreator
    public RecordSpec(
        @JsonProperty("targetBaseUrl") String targetBaseUrl,
        @JsonProperty("filters") ProxiedServeEventFilters filters,
        @JsonProperty("generatorOptions") GeneratorOptionsSpec generatorOptions,
        @JsonProperty("outputFormat") SnapshotOutputFormatter outputFormat) {
        this.targetBaseUrl = targetBaseUrl;
        this.filters = filters == null ? new ProxiedServeEventFilters() : filters;
        this.generatorOptions = generatorOptions == null ? GeneratorOptionsSpec.DEFAULTS : generatorOptions;
        this.outputFormat = outputFormat == null ? SnapshotOutputFormatter.FULL : outputFormat;
    }

    private RecordSpec() {
        this(null, null, null, null);
    }

    public static final RecordSpec DEFAULTS = new RecordSpec();

    public static RecordSpec forBaseUrl(String targetBaseUrl) {
        return new RecordSpec(targetBaseUrl, null, null, null);
    }

    public String getTargetBaseUrl() {
        return targetBaseUrl;
    }

    public ProxiedServeEventFilters getFilters() { return filters; }

    public SnapshotOutputFormatter getOutputFormat() { return outputFormat; }

    public GeneratorOptionsSpec getGeneratorOptions() {
        return generatorOptions;
    }
}
