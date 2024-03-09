package com.ikkerens.filecaptest.filecap;

import jakarta.validation.constraints.NotNull;

import java.util.Objects;

public class UploadFilesResponse {
    private final boolean success;
    private final String error;

    public static UploadFilesResponse parseResponse(@NotNull final String response) {
        Objects.requireNonNull(response);

        if ("true".equals(response))
            return new UploadFilesResponse(true, null);

        if (!response.contains("|"))
            throw new IllegalArgumentException("Not a valid failure response");
        final String[] parts = response.split("\\|");
        if (parts.length != 2)
            throw new IllegalArgumentException("Not a valid failure response");

        return new UploadFilesResponse(Boolean.parseBoolean(parts[0]), parts[1]);
    }

    private UploadFilesResponse(final boolean success, final String error) {
        this.success = success;
        this.error = error;
    }

    public boolean isSuccess() {
        return this.success;
    }

    public boolean hasError() {
        return this.error != null;
    }

    public String getError() {
        return this.error;
    }
}
