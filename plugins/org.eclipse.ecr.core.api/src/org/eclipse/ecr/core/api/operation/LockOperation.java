/*
 * Copyright (c) 2006-2012 Nuxeo SA (http://nuxeo.com/) and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     slacoin
 *
 * $Id$
 */
package org.eclipse.ecr.core.api.operation;

import org.eclipse.ecr.core.api.DocumentRef;

public class LockOperation extends  Operation<String> {

    // TODO key ignored
    public LockOperation(DocumentRef ref, String key) {
        super("__LOCK__");
        this.ref = ref;
    }

    private static final long serialVersionUID = 1L;
    protected final DocumentRef ref;

    @Override
    public String doRun(ProgressMonitor montior) throws Exception {
       session.setLock(ref);
       addModification(new Modification(ref, Modification.STATE));
       return ref + " is locked";
    }

}
