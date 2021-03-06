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

package org.eclipse.ecr.runtime.services.streaming;

import java.io.IOException;




/**
 * @author <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 *
 */
public interface StreamManager {

    String addStream(StreamSource src) throws IOException;

    void removeStream(String uri);

    StreamSource getStream(String uri) throws IOException;

    boolean hasStream(String uri);

    void start() throws Exception;

    void stop() throws Exception;

}
