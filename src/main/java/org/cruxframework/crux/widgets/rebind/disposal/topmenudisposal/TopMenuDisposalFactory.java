/*
 * Copyright 2014 cruxframework.org.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.cruxframework.crux.widgets.rebind.disposal.topmenudisposal;

import org.cruxframework.crux.core.client.utils.EscapeUtils;
import org.cruxframework.crux.core.client.utils.StringUtils;
import org.cruxframework.crux.core.rebind.AbstractProxyCreator.SourcePrinter;
import org.cruxframework.crux.core.rebind.CruxGeneratorException;
import org.cruxframework.crux.core.rebind.screen.widget.WidgetCreator;
import org.cruxframework.crux.core.rebind.screen.widget.WidgetCreatorContext;
import org.cruxframework.crux.core.rebind.screen.widget.creator.children.WidgetChildProcessor;
import org.cruxframework.crux.core.rebind.screen.widget.declarative.DeclarativeFactory;
import org.cruxframework.crux.core.rebind.screen.widget.declarative.TagAttributeDeclaration;
import org.cruxframework.crux.core.rebind.screen.widget.declarative.TagAttributesDeclaration;
import org.cruxframework.crux.core.rebind.screen.widget.declarative.TagChild;
import org.cruxframework.crux.core.rebind.screen.widget.declarative.TagChildren;
import org.cruxframework.crux.core.rebind.screen.widget.declarative.TagConstraints;
import org.cruxframework.crux.widgets.client.disposal.topmenudisposal.TopMenuDisposal;
import org.cruxframework.crux.widgets.rebind.disposal.topmenudisposal.TopMenuDisposalFactory.MenuItemProcessor;
import org.cruxframework.crux.widgets.rebind.disposal.topmenudisposal.TopMenuDisposalFactory.TopMenuDisposalContext;

/**
 * 
 * @author Gesse Dafe
 *
 */
@DeclarativeFactory(library="widgets", id="topMenuDisposal", targetWidget=TopMenuDisposal.class, 
			description="A component to dispose elements on the screen using a menu on the top and a panel above it to render views.")

@TagChildren({
	@TagChild(MenuItemProcessor.class)
})
@TagAttributesDeclaration({
	@TagAttributeDeclaration("defaultView")
})
public class TopMenuDisposalFactory extends WidgetCreator<TopMenuDisposalContext>
{
	@TagConstraints(minOccurs="0", maxOccurs="unbounded", tagName="menuEntry")
	@TagAttributesDeclaration({
		@TagAttributeDeclaration(value="label", required=true, supportsI18N=true),
		@TagAttributeDeclaration(value="targetView", required=true)
	})
	public static class MenuItemProcessor extends WidgetChildProcessor<WidgetCreatorContext>
	{
		@Override
		public void processChildren(SourcePrinter out, WidgetCreatorContext context) throws CruxGeneratorException
		{
			String label = context.readChildProperty("label");
			String targetView = context.readChildProperty("targetView");
			out.print(context.getWidget() + ".addMenuEntry(" + getWidgetCreator().resolveI18NString(label) + ", " + EscapeUtils.quote(targetView));
			out.println(");");
		}
	}
	
    @Override
    public TopMenuDisposalContext instantiateContext()
    {
	    return new TopMenuDisposalContext();
    }

    @Override
	public void instantiateWidget(SourcePrinter out, TopMenuDisposalContext context) throws CruxGeneratorException
	{
		String className = getWidgetClassName();
		out.println("final "+className + " " + context.getWidget()+" = GWT.create("+className+".class);");
		String defView = context.readChildProperty("defaultView");
		context.setDefaultView(defView);
	}
    
    public static class TopMenuDisposalContext extends WidgetCreatorContext
    {
    	private String defaultView;

		public String getDefaultView() 
		{
			return defaultView;
		}

		public void setDefaultView(String defaultView) 
		{
			this.defaultView = defaultView;
		} 
    }
    
    @Override
    public void postProcess(SourcePrinter out, TopMenuDisposalContext context)
    		throws CruxGeneratorException 
	{
    	String defaultView = context.getDefaultView();
		if(!StringUtils.isEmpty(defaultView))
		{
			out.println(context.getWidget() + ".setDefaultView(" + EscapeUtils.quote(defaultView) + ");");
		}
    }
}
