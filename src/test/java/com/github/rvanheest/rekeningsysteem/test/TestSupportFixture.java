package com.github.rvanheest.rekeningsysteem.test;

import org.apache.commons.io.FileUtils;
import org.slf4j.bridge.SLF4JBridgeHandler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public interface TestSupportFixture {

  static void slfBridger() {
    SLF4JBridgeHandler.removeHandlersForRootLogger();
    SLF4JBridgeHandler.install();
  }

  Path getTestRootDir = Paths.get("target/test/");

  default Path getTestDir() {
    return TestSupportFixture.getTestRootDir.resolve(this.getClass().getSimpleName()).toAbsolutePath();
  }

  default Path resetTestDir() throws IOException {
    Path path = getTestDir();
    FileUtils.deleteQuietly(path.toFile());
    Files.createDirectories(path);
    return path;
  }
}