package com.ikkerens.filecaptest;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FilecapUtilTest {
    @Test
    void prefixTest() {
        // Act
        final String id = FilecapUtil.generateFilecapID();

        // Assert
        assertEquals("iiii", id.substring(0, 4));
    }

    @Test
    void lengthTest() {
        // Act
        final String id = FilecapUtil.generateFilecapID();

        // Assert
        assertEquals(29, id.length());
    }
}