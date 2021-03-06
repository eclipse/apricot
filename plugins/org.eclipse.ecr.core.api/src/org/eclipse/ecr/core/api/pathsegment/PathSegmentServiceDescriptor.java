/*
 * Copyright (c) 2006-2012 Nuxeo SA (http://nuxeo.com/) and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Florent Guillaume
 */
package org.eclipse.ecr.core.api.pathsegment;

import java.io.Serializable;

import org.eclipse.ecr.common.xmap.annotation.XNode;
import org.eclipse.ecr.common.xmap.annotation.XObject;

/**
 * Descriptor to contribute a {@link PathSegmentService}.
 */
@XObject("service")
public class PathSegmentServiceDescriptor implements Serializable {

    private static final long serialVersionUID = 1L;

    @XNode("@class")
    protected String className;

}
