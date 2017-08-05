package com.github.rvanheest.rekeningsysteem.test;

import org.slf4j.bridge.SLF4JBridgeHandler;

public interface TestSupportFixture {

  static void slfBridger() {
    SLF4JBridgeHandler.removeHandlersForRootLogger();
    SLF4JBridgeHandler.install();
  }
}
