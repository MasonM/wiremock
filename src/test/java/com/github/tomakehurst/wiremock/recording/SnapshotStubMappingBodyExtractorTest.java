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

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.common.FileSource;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;
import com.google.common.collect.ImmutableList;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.github.tomakehurst.wiremock.client.WireMock.ok;
import static com.github.tomakehurst.wiremock.client.WireMock.okForContentType;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.stringContainsInOrder;
import static org.junit.Assert.assertThat;

@RunWith(JMock.class)
public class SnapshotStubMappingBodyExtractorTest {
    private FileSource filesSource;
    private SnapshotStubMappingBodyExtractor bodyExtractor;
    private Mockery context;

    @Before
    public void init() {
        context = new Mockery();
        filesSource = context.mock(FileSource.class, "filesFileSource");
        bodyExtractor = new SnapshotStubMappingBodyExtractor(filesSource);
    }

    @Test
    public void updatesStubMapping() {
        StubMapping stubMapping = WireMock.get("/foo")
            .willReturn(ok(""))
            .build();
        context.checking(new Expectations() {{
            // ignore arguments because this test is only for checking stub mapping changes
            one(filesSource).writeBinaryFile(with(any(String.class)), with(any(byte[].class)));
        }});
        bodyExtractor.extractInPlace(stubMapping);
        assertThat(stubMapping.getResponse().getBodyFileName(), stringContainsInOrder(ImmutableList.of("foo-", ".txt")));
        assertThat(stubMapping.getResponse().specifiesBodyFile(), is(true));
        assertThat(stubMapping.getResponse().specifiesBodyContent(), is(false));
    }

    @Test
    public void determinesFileNameProperlyFromUrlWithJson() {
        StubMapping stubMapping = WireMock.get("/foo/bar.json")
            .willReturn(ok(""))
            .build();
        setFileExpectations("foobarjson-", ".json");
        bodyExtractor.extractInPlace(stubMapping);
    }

    @Test
    public void determinesFileNameProperlyFromUrlWithText() {
        StubMapping stubMapping = WireMock.get("/foo/bar.txt")
            .willReturn(ok(""))
            .build();
        setFileExpectations("foobartxt-", ".txt");
        bodyExtractor.extractInPlace(stubMapping);
    }

    @Test
    public void determinesFileNameProperlyFromMimeTypeWithJson() {
        StubMapping stubMapping = WireMock.get("/foo")
            .willReturn(okJson(""))
            .build();
        setFileExpectations( "foo-", ".json");
        bodyExtractor.extractInPlace(stubMapping);
    }

    @Test
    public void determinesFileNameProperlyWithNamedStubMapping() {
        StubMapping stubMapping = WireMock.get("/foo/bar.txt")
            .willReturn(ok(""))
            .build();
        stubMapping.setName("TEST NAME!");
        setFileExpectations( "test-name-", ".txt");
        bodyExtractor.extractInPlace(stubMapping);
    }

    private void setFileExpectations(final String prefix, final String extension) {
        context.checking(new Expectations() {{
            one(filesSource).writeBinaryFile(
                with(stringContainsInOrder(ImmutableList.of(prefix, extension))),
                with(equal(new byte[0]))
            );
        }});
    }
}
