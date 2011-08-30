/*
 * Copyright 2011 JBoss, a divison Red Hat, Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jboss.errai.codegen.framework.builder.callstack;

import org.jboss.errai.codegen.framework.Context;
import org.jboss.errai.codegen.framework.Statement;

/**
 * An element for deferring and offloading validation and generation work for building the
 * call stack.
 * 
 * @author Mike Brock <cbrock@redhat.com>
 */
public class DeferredCallElement extends AbstractCallElement {
  private DeferredCallback callback;

  public DeferredCallElement(DeferredCallback callback) {
    this.callback = callback;
  }

  @Override
  public void handleCall(CallWriter writer, Context context, Statement statement) {
    callback.doDeferred(writer, context, statement);

    if (next != null) {
      writer.append(".");
      getNext().handleCall(writer, context, statement);
    }
  }
}