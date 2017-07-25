package com.github.tomakehurst.wiremock.common;

import com.github.tomakehurst.wiremock.http.ContentTypeHeader;
import com.github.tomakehurst.wiremock.http.HttpHeaders;
import com.github.tomakehurst.wiremock.matching.AnythingPattern;
import com.github.tomakehurst.wiremock.matching.UrlPattern;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;
import com.google.common.hash.Hashing;
import org.apache.commons.lang3.StringUtils;

import java.net.URI;
import java.text.Normalizer;
import java.util.Locale;
import java.util.regex.Pattern;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

public class SafeNames {

    private static final Pattern NON_ALPHANUMERIC = Pattern.compile("[^\\w-]");
    private static final Pattern WHITESPACE = Pattern.compile("[\\s]");

    public static String makeSafeFileName(StubMapping mapping) {
        String suffix = "-" + mapping.getId() + ".json";
        return makeSafeFileName(mapping, suffix);
    }

    public static String makeSafeBodyFileName(StubMapping mapping) {
        byte[] body = mapping.getResponse().getByteBody();
        HttpHeaders responseHeaders = mapping.getResponse().getHeaders();
        String extension = ContentTypes.determineFileExtension(
            mapping.getRequest().getUrl(),
            responseHeaders != null ? responseHeaders.getContentTypeHeader() : ContentTypeHeader.absent(),
            body);
        // Generate unique fingerprint for the body contents to avoid duplicate files.
        // According to smhasher (https://github.com/rurban/smhasher), FarmHash64 is extremely fast and has few collisions
        String fingerprint = Hashing.farmHashFingerprint64().hashBytes(body).toString();
        String suffix = "-" + fingerprint + "." + extension;
        return makeSafeFileName(mapping, suffix);
    }

    private static String makeSafeFileName(StubMapping mapping, String suffix) {
        if (isNotEmpty(mapping.getName())) {
            return makeSafeName(mapping.getName()) + suffix;
        }

        UrlPattern urlMatcher = mapping.getRequest().getUrlMatcher();

        if (urlMatcher.getPattern() instanceof AnythingPattern) {
            return suffix.substring(1);
        }

        String expectedUrl = urlMatcher.getExpected();
        URI uri = URI.create(sanitise(expectedUrl));
        return makeSafeNameFromUrl(uri.getPath()) + suffix;
    }

    public static String makeSafeNameFromUrl(String urlPath) {
        String startingPath = urlPath.replace("/", "_");
        return makeSafeName(startingPath);
    }

    public static String makeSafeName(String name) {
        String nowhitespace = WHITESPACE.matcher(name).replaceAll("-");
        String normalized = Normalizer.normalize(nowhitespace, Normalizer.Form.NFD);
        String slug = sanitise(normalized);

        slug = slug.replaceAll("^[_]*", "");
        slug = slug.replaceAll("[_]*$", "");

        slug = StringUtils.truncate(slug, 200);

        return slug.toLowerCase(Locale.ENGLISH);
    }

    private static String sanitise(String s) {
        return NON_ALPHANUMERIC.matcher(s).replaceAll("");
    }
}
