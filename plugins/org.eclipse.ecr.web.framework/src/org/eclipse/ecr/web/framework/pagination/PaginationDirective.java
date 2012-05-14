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

package org.eclipse.ecr.web.framework.pagination;

import java.io.IOException;
import java.util.Map;

import org.eclipse.ecr.common.utils.StringUtils;
import org.eclipse.ecr.web.framework.HTMLWriter;

import freemarker.core.Environment;
import freemarker.template.SimpleScalar;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

public class PaginationDirective implements TemplateDirectiveModel {

	@SuppressWarnings({ "rawtypes" })
	@Override
	public void execute(Environment env, final Map params, TemplateModel[] loopVars,
			TemplateDirectiveBody body) throws TemplateException, IOException {

		int limit = 0;
		SimpleScalar scalar = (SimpleScalar) params.get("limit");
        if (scalar != null) {
            limit = Integer.parseInt(scalar.getAsString());
        } else {
            throw new TemplateException("@paginate 'limit' attribute is not defined", env);
        }

        long offset = -1;
        scalar = (SimpleScalar) params.get("offset");
        if (scalar != null) {
            offset = Long.parseLong(scalar.getAsString());
        } else {
            throw new TemplateException("@paginate 'offset' attribute is not defined", env);
        }

        if (limit <= 0 || offset < 0) {
        	return;
        }
        
		Pagination pag = new Pagination();
		pag.limit = limit;
		pag.offset = offset;


        scalar = (SimpleScalar) params.get("count");
        if (scalar != null) {
            pag.count = Long.parseLong(scalar.getAsString());
        } else {
            throw new TemplateException("@paginate 'count' attribute is not defined", env);
        }
        
        scalar = (SimpleScalar) params.get("range");
        if (scalar != null) {
            pag.range = Integer.parseInt(scalar.getAsString());
        } else {
        	pag.range = 5;
        }
        
        scalar = (SimpleScalar) params.get("href");
        if (scalar != null) {
            pag.href = scalar.getAsString();
        } else {
        	throw new TemplateException("@paginate 'href' attribute is not defined", env);
        }

        scalar = (SimpleScalar) params.get("next");
        if (scalar != null) {
            pag.next = scalar.getAsString();
        } else {
        	pag.next = "&raquo;";
        }

        scalar = (SimpleScalar) params.get("prev");
        if (scalar != null) {
            pag.prev = scalar.getAsString();
        } else {
        	pag.prev = "&laquo;";
        }

        scalar = (SimpleScalar) params.get("limits");        
        if (scalar != null) {
        	String v = scalar.getAsString().trim();
        	if (v.length() != 0) {
        		String[] ar = StringUtils.split(v, ',', true);
        		pag.limits = new int[ar.length];
        		for (int i=0; i<ar.length; i++) {
        			pag.limits[i] = Integer.parseInt(ar[i]);
        		}
        	}
        }
        if (pag.limits == null) {
        	pag.limits = new int[] {15, 30, 50};
        }

        pag.compute();
        
        if (pag.pageCount <= 1) {
        	return;
        }
        
        HTMLWriter writer = new HTMLWriter(env.getOut());
		try {
			writer.println("<table width='100%'><tr><td>");
			writer.println("<div class=\"pagination\">").println("<ul>");
			//write prev
			writePrevious(writer, pag);
			if (pag.firstPage > 2) {
				// write the page 1
				writePage(writer, pag, 1);
				writeDots(writer);
				if (pag.lastPage < pag.pageCount-1) {
					for (long i=pag.firstPage; i<=pag.lastPage; i++) {
						writePage(writer, pag, i);
					}
					writeDots(writer);
					writePage(writer, pag, pag.pageCount);	
				} else {
					// write all pages until the end
					for (long i=pag.firstPage; i<=pag.pageCount; i++) {
						writePage(writer, pag, i);
					}
				}
			} else {
				// write all entries until the lastPage
				for (long i=1; i<=pag.lastPage; i++) {
					writePage(writer, pag, i);
				}
				if (pag.lastPage < pag.pageCount-1) {
					writeDots(writer);
					writePage(writer, pag, pag.pageCount);
				} else if (pag.lastPage < pag.pageCount) {
					writePage(writer, pag, pag.pageCount);
				} // else no page to write
			}
			// write next
			writeNext(writer, pag);
			writer.println("</ul>").println("</div>");
			
			writer.println("</td><td align='right'>");
			
			writer.println("<div class=\"pagination\">").println("<ul>");
			for (int i=0; i<pag.limits.length; i++) {
				writeLimitChoice(writer, pag, pag.limits[i]);
			}
			writer.println("<ul>").println("</div>");

			writer.println("</td></tr></table>");
		} finally {
			writer.flush();
		}
	}

	private void writePrevious(HTMLWriter writer, Pagination pag) throws IOException {
		if (pag.isFirstPage()) {
			writer.print("<li class=\"prev disabled\"><a href=\"#\" onclick=\"return false;\">").print(pag.prev).println("</a></li>");
		} else {
			writer.print("<li class=\"prev\"><a href=\"");
			writer.print(pag.getHref(pag.pageIndex-1)).print("\">").print(pag.prev).println("</a></li>");			
		}
	}

	private void writeNext(HTMLWriter writer, Pagination pag) throws IOException {
		if (pag.isLastPage()) {
			writer.print("<li class=\"prev disabled\"><a href=\"#\" onclick=\"return false;\">").print(pag.next).println("</a></li>");
		} else {
			writer.print("<li class=\"prev\"><a href=\"");
			writer.print(pag.getHref(pag.pageIndex+1)).print("\">").print(pag.next).println("</a></li>");			
		}
	}
	
	private void writePage(HTMLWriter writer, Pagination pag, long index) throws IOException {
		writer.print("<li");
		if (index == pag.pageIndex) {
			writer.print(" class=\"active\"");	
		}
		writer.print("><a href=\"").print(pag.getHref(index)).print("\">")
		.print(Long.toString(index)).println("</a></li>");		
	}

	private void writeDots(HTMLWriter writer) throws IOException {
		writer.println("<li class=\"disabled\"><a href=\"#\">...</a></li>");		
	}

	private void writeLimitChoice(HTMLWriter writer, Pagination pag, int limit) throws IOException {
		writer.print("<li");
		if (pag.limit == limit) {
			writer.print(" class=\"active\"");	
		}
		writer.print("><a href=\"").print(pag.getResetHref(limit)).print("\">");
		writer.print(Integer.toString(limit)).println("</a></li>");
	}
	
	static class Pagination {
		long offset;
		int limit;
		long count;
		
		int range;
		
		long pageIndex;
		long pageCount;
		long firstPage; // the first page to show in range
		long lastPage; // the last page to show in range

		
		String next;
		String prev;
		String href;
		
		int[] limits;
		
		
		final void compute() {
			pageCount = (count + limit - 1) / limit;
			pageIndex = offset / limit + 1;
			firstPage = pageIndex - (range / 2); 
			if (firstPage < 1) {
				firstPage = 1;
			}
			lastPage = firstPage + range;
			if (lastPage > pageCount) {
				lastPage = pageCount;
			}
		}
	
		final boolean isLastPage() {
			return pageIndex == pageCount; 
		}
		
		final boolean isFirstPage() {
			return pageIndex == 1;
		}

		final String getHref(long index) {
			return String.format(href, limit*(index-1), limit);
		}
		
		final String getResetHref(int limit) {
			return String.format(href, 0, limit);
		}
	}
	
}
