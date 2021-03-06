/*
 * Copyright (c) 2006-2012 Nuxeo SA (http://nuxeo.com/) and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Nuxeo - initial API and implementation
 *
 * $Id$
 */

package org.eclipse.ecr.core.api.impl;

import java.util.HashMap;

import org.eclipse.ecr.core.api.DataModel;
import org.eclipse.ecr.core.api.DataModelMap;

/**
 * @author <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 *
 */
public class DataModelMapImpl extends HashMap<String, DataModel>  implements DataModelMap {

    private static final long serialVersionUID = 8797227773838852959L;

    @Override
    public String toString() {
        return values().toString();
    }

}
