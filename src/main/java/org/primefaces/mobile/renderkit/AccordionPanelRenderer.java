/*
 * Copyright 2009-2011 Prime Technology.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.primefaces.mobile.renderkit;

import java.io.IOException;
import java.util.Map;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.primefaces.component.accordionpanel.AccordionPanel;
import org.primefaces.component.tabview.Tab;
import org.primefaces.renderkit.CoreRenderer;

public class AccordionPanelRenderer extends CoreRenderer {

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        AccordionPanel acco = (AccordionPanel) component;
        Map<String,Object> attrs = acco.getAttributes();
        Object swatch = (String) attrs.get("swatch");
        String activeIndex = acco.getActiveIndex();                
        
        writer.startElement("div", acco);
        writer.writeAttribute("id", acco.getClientId(context), null);
        writer.writeAttribute("data-role", "collapsible-set", null);
        if(acco.getStyle() != null) writer.writeAttribute("style", acco.getStyle(), null);
        if(acco.getStyleClass() != null) writer.writeAttribute("class", acco.getStyleClass(), null);        
        if(swatch != null) writer.writeAttribute("data-theme", swatch, null);         
        int i = 0;
        for(UIComponent child : acco.getChildren()) {
            if(child.isRendered() && child instanceof Tab) {  
                Tab tab = (Tab) child;
                String title = tab.getTitle();
                Boolean inset = (tab.getAttributes().get("inset") == null ? true : Boolean.valueOf(tab.getAttributes().get("inset").toString()));             
                
                writer.startElement("div", null);
                writer.writeAttribute("data-role", "collapsible", null);
                writer.writeAttribute("data-inset", inset.toString(), null);
                
                if(activeIndex != null && activeIndex.equals(String.valueOf(i))) { 
                    writer.writeAttribute("data-collapsed", "false", null);
                }

                //header
                writer.startElement("h3", null);
                if(title != null) {
                    writer.writeText(title, null);
                }
                writer.endElement("h3");

                //content
                if (inset) writer.startElement("p", null);
                tab.encodeAll(context);
                if (inset) writer.endElement("p");

                writer.endElement("div");
                
                i++;
            }
        }
        
        writer.endElement("div");
    }

    @Override
	public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
		//Rendering happens on encodeEnd
	}

    @Override
	public boolean getRendersChildren() {
		return true;
	}
}
