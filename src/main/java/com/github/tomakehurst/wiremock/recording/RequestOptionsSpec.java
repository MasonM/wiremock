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

import java.util.Map;

/**
 * Encapsulates options for generating the RequestPattern in StubMappings
 */
public class RequestOptionsSpec {
    // Headers from the request to include in the stub mapping, if they match the corresponding matcher
    private final Map<String, CaptureHeadersSpec> captureHeaders;
    // Whether duplicate requests should be recorded as scenarios or just discarded
    private final Boolean repeatsAsScenarios;
    private final JsonMatchingFlags jsonMatchingFlags;

    @JsonCreator
    public RequestOptionsSpec(
        @JsonProperty("captureHeaders") Map<String, CaptureHeadersSpec> captureHeaders,
        @JsonProperty("repeatsAsScenarios") Boolean repeatsAsScenarios,
        @JsonProperty("jsonMatchingFlags") JsonMatchingFlags jsonMatchingFlags) {
        this.captureHeaders = captureHeaders;
        this.repeatsAsScenarios = repeatsAsScenarios;
        this.jsonMatchingFlags = jsonMatchingFlags;
    }

    public Map<String, CaptureHeadersSpec> getCaptureHeaders() {
        return captureHeaders;
    }

    @JsonIgnore
    public boolean shouldRecordRepeatsAsScenarios() {
        return repeatsAsScenarios == null ? true : repeatsAsScenarios;
    }

    public Boolean getRepeatsAsScenarios() {
        return repeatsAsScenarios;
    }

    public JsonMatchingFlags getJsonMatchingFlags() {
        return jsonMatchingFlags;
    }
}
