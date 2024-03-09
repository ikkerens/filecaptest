package com.ikkerens.filecaptest.model;

import jakarta.validation.constraints.NotNull;
import org.apache.tika.Tika;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class File {
    private final String fileName;
    private final String fileMimeType;
    private final byte[] fileData;

    public static File fromRequest(
            @NotNull final String fileName,
            @NotNull final InputStream fileStream
    ) throws IOException {
        Objects.requireNonNull(fileName);
        Objects.requireNonNull(fileStream);

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int n;
        byte[] data = new byte[1024];
        while ((n = fileStream.read(data, 0, data.length)) != -1)
            buffer.write(data, 0, n);
        buffer.flush();

        return new File(fileName, buffer.toByteArray());
    }

    public File(
            @NotNull final String fileName,
            @NotNull final byte[] fileData
    ) {
        // If no mimetype is given through the constructor, attempt to detect it.
        // In this case we resort to Apache Tika to have a comprehensive collection of mimetypes.
        this(fileName, new Tika().detect(fileName), fileData);
    }

    public File(
            @NotNull final String fileName,
            @NotNull final String fileMimeType,
            @NotNull final byte[] fileData
    ) {
        Objects.requireNonNull(fileName);
        Objects.requireNonNull(fileMimeType);
        Objects.requireNonNull(fileData);

        this.fileName = fileName;
        this.fileMimeType = fileMimeType;
        this.fileData = fileData;
    }

    public byte[] getFileData() {
        return this.fileData;
    }

    public FileMetaData toMetaData() {
        return new FileMetaData(this.fileName, this.fileData.length, this.fileMimeType);
    }
}
