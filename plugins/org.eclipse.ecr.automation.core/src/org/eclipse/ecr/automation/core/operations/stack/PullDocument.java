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
package org.eclipse.ecr.automation.core.operations.stack;

import org.eclipse.ecr.automation.OperationContext;
import org.eclipse.ecr.automation.OperationException;
import org.eclipse.ecr.automation.core.Constants;
import org.eclipse.ecr.automation.core.annotations.Context;
import org.eclipse.ecr.automation.core.annotations.Operation;
import org.eclipse.ecr.automation.core.annotations.OperationMethod;
import org.eclipse.ecr.core.api.DocumentModel;
import org.eclipse.ecr.core.api.DocumentRef;

/**
 * @author <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 */
@Operation(id = PullDocument.ID, category = Constants.CAT_EXECUTION_STACK, label = "Pull Document", description = "Restore the first saved input document in the context input stack. This operation must be used only if a PUSH operation was previously made. Return the first <i>pushed</i> document.")
public class PullDocument {

    public static final String ID = "Document.Pull";

    @Context
    protected OperationContext ctx;

    @OperationMethod
    public DocumentModel run() throws Exception {
        Object obj = ctx.pull(Constants.O_DOCUMENT);
        if (obj instanceof DocumentModel) {
            return (DocumentModel) obj;
        } else if (obj instanceof DocumentRef) {
            return ctx.getCoreSession().getDocument((DocumentRef) obj);
        }
        throw new OperationException(
                "Illegal state error for pull document operation. The context stack doesn't contains a document on its bottom");
    }

}
