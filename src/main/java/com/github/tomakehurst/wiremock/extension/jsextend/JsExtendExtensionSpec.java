package com.github.tomakehurst.wiremock.extension.jsextend;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class JsExtendExtensionSpec {
    private final String type;
    private final String javascript;
    private UUID id;

    @JsonCreator
    public JsExtendExtensionSpec(
        @JsonProperty("type") String type,
        @JsonProperty("javascript") String javascript
    ) {
        this.type = type;
        this.javascript = javascript;
        this.id = null;
    }

    public String getType() { return type; }

    public String getJavascript() { return javascript; }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return this.id;
    }
}
