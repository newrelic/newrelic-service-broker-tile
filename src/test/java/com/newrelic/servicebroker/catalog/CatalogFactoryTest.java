package com.newrelic.servicebroker.catalog;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

final class CatalogFactoryTest {

    private static final String PLANS = "[{\"planName\":\"p1\",\"licenseKey\":\"k1\"}]";

    @TempDir
    File folder;

    @Test
    void plainJsonIsReturnedUnchanged() throws Exception {
        assertEquals(PLANS, CatalogFactory.resolvePlans(PLANS));
    }

    @Test
    void nullIsReturnedUnchanged() throws Exception {
        assertNull(CatalogFactory.resolvePlans(null));
    }

    @Test
    void fileOnDiskIsRead() throws Exception {
        File f = new File(folder, "plans.json");
        Files.write(f.toPath(), PLANS.getBytes(StandardCharsets.UTF_8));
        assertEquals(PLANS, CatalogFactory.resolvePlans("file:" + f.getAbsolutePath()));
    }

    @Test
    void classpathFileIsRead() throws Exception {
        assertEquals(PLANS, CatalogFactory.resolvePlans("file:test-plans.json").trim());
    }

    @Test
    void missingFileFails() {
        assertThrows(IOException.class, () -> CatalogFactory.resolvePlans("file:/no/such/plans.json"));
    }
}
