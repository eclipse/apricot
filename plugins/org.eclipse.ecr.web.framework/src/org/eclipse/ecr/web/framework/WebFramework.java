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
package org.eclipse.ecr.web.framework;


/**
 * @author <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 * 
 */
public interface WebFramework {

    WebApplication getDefaultApplication();
    
    /**
     * Get or create a new application given the name.
     * 
     * @param name
     * @return
     */
    WebApplication getOrCreateApplication(String name);
    
    WebApplication getApplication(String name);
    
    WebApplication[] getApplications();
        
    String getMimeType(String fileName, String defaultType);
    
}
