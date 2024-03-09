package com.ikkerens.filecaptest.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public record FileMetaData(String fileName, long fileSize, String mimeType) {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    @JsonProperty("filename")
    public String fileName() {
        return this.fileName;
    }

    @Override
    @JsonProperty("size")
    public long fileSize() {
        return this.fileSize;
    }

    @Override
    @JsonProperty("mimetype")
    public String mimeType() {
        return this.mimeType;
    }

    public String toJSON() throws JsonProcessingException {
        return objectMapper.writeValueAsString(new FileMetaData[]{this});
    }
}
