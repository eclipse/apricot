/*
 * Copyright (c) 2006-2012 Nuxeo SA (http://nuxeo.com/) and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     bstefanescu
 */
package org.eclipse.ecr.automation.core.operations.execution;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.ecr.automation.AutomationService;
import org.eclipse.ecr.automation.OperationContext;
import org.eclipse.ecr.automation.core.Constants;
import org.eclipse.ecr.automation.core.annotations.Context;
import org.eclipse.ecr.automation.core.annotations.Operation;
import org.eclipse.ecr.automation.core.annotations.OperationMethod;
import org.eclipse.ecr.automation.core.annotations.Param;

/**
 * Run an embedded operation chain using the current input. The output is
 * undefined (Void)
 * 
 * @author <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 */
@Operation(id = RunOperation.ID, category = Constants.CAT_SUBCHAIN_EXECUTION, label = "Run Chain", description = "Run an operation chain in the current context")
public class RunOperation {

    public static final String ID = "Context.RunOperation";

    @Context
    protected OperationContext ctx;

    @Context
    protected AutomationService service;

    @Param(name = "id")
    protected String chainId;

    @Param(name = "isolate", required = false, values = "false")
    protected boolean isolate = false;

    @OperationMethod
    public void run() throws Exception {
        Map<String, Object> vars = isolate ? new HashMap<String, Object>(
                ctx.getVars()) : ctx.getVars();
        OperationContext subctx = new OperationContext(ctx.getCoreSession(),
                vars);
        subctx.setInput(ctx.getInput());
        service.run(subctx, chainId);
    }

}
