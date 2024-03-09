package com.ikkerens.filecaptest.model;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class MultipartBuilderTest {
    // This test ensures the boundary starts with 4 dashes, a requirement of multipart
    @Test
    void boundary() {
        // Arrange & Act
        final MultipartBuilder builder = new MultipartBuilder();

        // Assert
        assertEquals("----", builder.getBoundary().substring(0, 4));
    }

    @Test
    void addFilePart() {
        // Arrange
        final MultipartBuilder builder = new MultipartBuilder();
        final String boundary = builder.getBoundary();

        // Act
        builder.addFilePart("fileField", "dummy.txt", "text/plain", "(data)".getBytes(StandardCharsets.UTF_8));

        // Assert
        assertEquals(
                "--"+boundary+"\r\n" +
                        "Content-Disposition: form-data; name=\"fileField\"; filename=\"dummy.txt\"\r\n" +
                        "Content-Type: text/plain\r\n" +
                        "\r\n" +
                        "(data)\r\n" +
                        "--"+boundary+"--\r\n",
                // In this test we convert back to UTF-8 for ease-of-use when testing
                new String(builder.build(), StandardCharsets.UTF_8)
        );
    }

    @Test
    void addFormField() {
        // Arrange
        final MultipartBuilder builder = new MultipartBuilder();
        final String boundary = builder.getBoundary();

        // Act
        builder.addFormField("test", "value");

        // Assert
        assertEquals(
                "--" + boundary + "\r\n" +
                        "Content-Disposition: form-data; name=\"test\"\r\n" +
                        "Content-Type: text/plain; charset=UTF-8\r\n" +
                        "\r\n" +
                        "value\r\n" +
                        "--" + boundary + "--\r\n",
                // In this test we convert back to UTF-8 for ease-of-use when testing
                new String(builder.build(), StandardCharsets.UTF_8)
        );
    }
}