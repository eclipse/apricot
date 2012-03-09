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
 *
 */

package org.eclipse.ecr.web.framework;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriInfo;

import org.eclipse.ecr.runtime.api.Framework;

/**
 * @author bstefanescu
 *
 */
public abstract class WebRoot extends AdaptableResource {

	protected WebRoot() {
		this (WebApplication.DEFAULT_APP_NAME);
	}
	
	protected WebRoot(String appName) {
		super (new WebContext());
		this.ctx.app = Framework.getLocalService(WebFramework.class).getApplication(appName);
	}

	@Context public void setRequest(HttpServletRequest request) {
		ctx.setRequest(request);
	}

	@Context public void setResponse(HttpServletResponse response) {
		ctx.setResponse(response);
	}

	@Context public void setUriInfo(UriInfo uriInfo) {
		ctx.setUriInfo(uriInfo);
		ctx.setContextPath(computeContextPath(uriInfo));
		initPath(uriInfo);
	}
	
	@Override
	protected void initContext(WebContext ctx) { //TODO		
		super.initContext(ctx);
	}
	
	/**
	 * TODO: Override to add proxy path rewrite support
	 * @param uriInfo
	 * @return
	 */
	protected String computeContextPath(UriInfo uriInfo) {		
		return uriInfo.getBaseUri().getPath();
	}
	
	public Object handleError() {
		return null; //TODO
	}

    @GET
    @Path("skin/{path:.*}")
    public Response getSkinResource(@PathParam("path") String path) throws IOException {
    	URL url = getApplication().resolve("resources/"+path);
    	String mimeType = null;
    	if (url != null) {
    		URLConnection conn = url.openConnection();
    		int x = url.getPath().lastIndexOf('.');
    		if (x > -1) {
    			mimeType = Framework.getLocalService(WebFramework.class).getMimeType(url.getPath(), "text/plain");
    		}
    		ResponseBuilder resp = Response.ok(conn);
    		addCacheHeaders(resp, conn, path);
    		resp.type(mimeType);
    		return resp.build();
    	}
        return Response.status(404).build();
    }
    
    /**
     * Override to control cache headers. By default for all skin resources 
     * a Cache-Control: public, Expires of one month and lastModified header is used.
     */
    protected void addCacheHeaders(ResponseBuilder resp, URLConnection conn, String path) throws IOException {
    	final Date afterOneMonth = new Date(System.currentTimeMillis()+2678400000L);
    	long lastModified = conn.getLastModified();
    	resp.lastModified(new Date(lastModified)).header("Cache-Control",
    					"public").expires(afterOneMonth);
    }

}
