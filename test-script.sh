#!/bin/bash

curl -s -d '(function() {
    var ResponseDefinitionTransformer = Java.type("com.github.tomakehurst.wiremock.extension.ResponseDefinitionTransformer");
    var ResponseDefinition = Java.type("com.github.tomakehurst.wiremock.http.ResponseDefinition");
    var CustomResponseDefinitionTransformer = Java.extend(ResponseDefinitionTransformer);

    return new CustomResponseDefinitionTransformer {
        transform: function(request, responseDefinition, files, pathParams) {
            return new ResponseDefinition(201, "TRANSFORMED!");
        }
    };
} ());' http://localhost:8080/__admin/load-js-extension
