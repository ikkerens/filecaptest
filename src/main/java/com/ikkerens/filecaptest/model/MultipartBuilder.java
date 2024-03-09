package com.ikkerens.filecaptest.model;

import jakarta.validation.constraints.NotNull;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class MultipartBuilder {
    private static final String CRLF = "\r\n";

    private final String boundary;
    private final StringBuilder body;

    public MultipartBuilder() {
        this.boundary = "----FormBoundary" + System.currentTimeMillis();
        this.body = new StringBuilder();
    }

    public void addFilePart(
            @NotNull final String fieldName,
            @NotNull final String fileName,
            @NotNull final String mimeType,
            @NotNull final byte[] fileBytes
    ) {
        Objects.requireNonNull(fieldName);
        Objects.requireNonNull(fileName);
        Objects.requireNonNull(mimeType);
        Objects.requireNonNull(fileBytes);

        this.body.append("--").append(this.boundary).append(CRLF);
        this.body.append("Content-Disposition: form-data; name=\"").append(fieldName).append("\"; filename=\"").append(fileName).append("\"").append(CRLF);
        this.body.append("Content-Type: ").append(mimeType).append(CRLF);
        this.body.append(CRLF);
        this.body.append(new String(fileBytes, StandardCharsets.UTF_8)).append(CRLF);
    }

    public void addFormField(
            @NotNull final String name,
            @NotNull final String value
    ) {
        Objects.requireNonNull(name);
        Objects.requireNonNull(value);

        this.body.append("--").append(this.boundary).append(CRLF);
        this.body.append("Content-Disposition: form-data; name=\"").append(name).append("\"").append(CRLF);
        this.body.append("Content-Type: text/plain; charset=").append(StandardCharsets.UTF_8).append(CRLF);
        this.body.append(CRLF);
        this.body.append(value).append(CRLF);
    }

    public String getBoundary() {
        return boundary;
    }

    public byte[] build() {
        this.body.append("--").append(this.boundary).append("--").append(CRLF);
        return this.body.toString().getBytes(StandardCharsets.UTF_8);
    }
}

