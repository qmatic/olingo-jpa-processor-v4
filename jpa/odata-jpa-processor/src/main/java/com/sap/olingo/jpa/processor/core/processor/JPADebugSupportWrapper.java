package com.sap.olingo.jpa.processor.core.processor;

import java.util.List;

import org.apache.olingo.server.api.OData;
import org.apache.olingo.server.api.ODataResponse;
import org.apache.olingo.server.api.debug.DebugInformation;
import org.apache.olingo.server.api.debug.DebugSupport;
import org.apache.olingo.server.api.debug.RuntimeMeasurement;

import com.sap.olingo.jpa.processor.core.api.JPAServiceDebugger;

public class JPADebugSupportWrapper implements DebugSupport {
  private final DebugSupport debugSupport;
  private JPAServiceDebugger debugger;

  public JPADebugSupportWrapper(final DebugSupport debugSupport) {
    super();
    this.debugSupport = debugSupport;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.apache.olingo.server.api.debug.DebugSupport#createDebugResponse(java.lang.String,
   * org.apache.olingo.server.api.debug.DebugInformation)
   */
  @Override
  public ODataResponse createDebugResponse(final String debugFormat, final DebugInformation debugInfo) {
    joinRuntimeInfo(debugInfo);
    return debugSupport.createDebugResponse(debugFormat, debugInfo);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.apache.olingo.server.api.debug.DebugSupport#init(org.apache.olingo.server.api.OData)
   */
  @Override
  public void init(final OData odata) {
    debugSupport.init(odata);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.apache.olingo.server.api.debug.DebugSupport#isUserAuthorized()
   */
  @Override
  public boolean isUserAuthorized() {
    return debugSupport.isUserAuthorized();
  }

  void setDebugger(final JPAServiceDebugger debugger) {
    this.debugger = debugger;
  }

  private void joinRuntimeInfo(final DebugInformation debugInfo) {
    // Olingo create a tree for runtime measurement in DebugTabRuntime.add(final RuntimeMeasurement
    // runtimeMeasurement). The current algorithm (V4.3.0) not working well for batch requests if the own runtime info
    // is just appended (addAll), so insert sorted:
    final List<RuntimeMeasurement> olingoInfo = debugInfo.getRuntimeInformation();
    int startIndex = 0;
    for (final RuntimeMeasurement m : debugger.getRuntimeInformation()) {
      for (; startIndex < olingoInfo.size(); startIndex++) {
        if (olingoInfo.get(startIndex).getTimeStarted() > m.getTimeStarted()) {
          break;
        }
      }
      olingoInfo.add(startIndex, m);
      startIndex += 1;
    }
  }
}
