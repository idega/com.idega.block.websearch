package com.idega.block.websearch.elight.presentation;

import java.io.IOException;
import java.rmi.RemoteException;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.apache.myfaces.component.html.ext.HtmlGraphicImage;
import org.apache.myfaces.component.html.ext.HtmlInputText;
import org.apache.myfaces.component.html.ext.HtmlOutputText;
import org.apache.myfaces.renderkit.html.util.AddResource;
import org.apache.myfaces.renderkit.html.util.AddResourceFactory;

import com.idega.block.web2.business.Web2Business;
import com.idega.block.websearch.business.WebSearchBundleStarter;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWMainApplication;
import com.idega.presentation.IWBaseComponent;
import com.idega.webface.WFDivision;

/**
 * 
 * 
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version 1.0
 * 
 */
public class ELight extends IWBaseComponent {
	
	private static final String elight_id = "elight";
	private static final String elight_input_id = "elightInput";
	private static final String elight_search_button_id = "elightSearchButton";
	private static final String elight_search_input_id = "elightSearchInput";
	
	private static final String ELIGHT_SEARCH_BUTTON_SRC = "images/elightSearchButton.png";
	
	
	public ELight() {
		super();
		setRendererType(null);
	}
	
	@Override
	protected void initializeComponent(FacesContext context) {
		
		Application application = context.getApplication();
		
		WFDivision elight_div = (WFDivision)application.createComponent(WFDivision.COMPONENT_TYPE);
		elight_div.setId(elight_id);
		
		WFDivision input_division = (WFDivision)application.createComponent(WFDivision.COMPONENT_TYPE);
		input_division.setId(elight_input_id);
		
		WFDivision output_division = (WFDivision)application.createComponent(WFDivision.COMPONENT_TYPE);
		output_division.setId("elightOutput");
		
		WFDivision input_text_division = (WFDivision)application.createComponent(WFDivision.COMPONENT_TYPE);
		input_text_division.setId("elightInputText");
		
		WFDivision results_division = (WFDivision)application.createComponent(WFDivision.COMPONENT_TYPE);
		results_division.setId("elightResults");
		
		HtmlGraphicImage search_button = (HtmlGraphicImage) application.createComponent(HtmlGraphicImage.COMPONENT_TYPE);
		search_button.setId(elight_search_button_id);
		search_button.setValue(IWMainApplication.getIWMainApplication(context).getBundle(WebSearchBundleStarter.IW_BUNDLE_IDENTIFIER).getImageURI(ELIGHT_SEARCH_BUTTON_SRC));
		
		HtmlInputText search_input = (HtmlInputText) application.createComponent(HtmlInputText.COMPONENT_TYPE);
		search_input.setId(elight_search_input_id);

		input_division.add(search_button);
		input_text_division.add(search_input);
		input_division.add(input_text_division);
		
		output_division.add(results_division);
		
		elight_div.add(input_division);
		 elight_div.add(output_division);
		
		getFacets().put(elight_id, elight_div);
		
		addClientResources(context);
		
		HtmlOutputText text = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
		text.setValue("some great text");
		results_division.add(text);
	}
	
	@Override
	public boolean getRendersChildren() {
		return true;
	}
	
	@Override
	public void encodeChildren(FacesContext context) throws IOException {
		
		super.encodeChildren(context);
		
		UIComponent elight_div = getFacet(elight_id);
		
		if(elight_div != null) {
			
			elight_div.setRendered(true);
			renderChild(context, elight_div);
		}
	}
	
	protected Web2Business getWeb2Service(IWApplicationContext iwc) {
		try {
			return (Web2Business) IBOLookup.getServiceInstance(iwc, Web2Business.class);
		} catch (IBOLookupException e) {
			e.printStackTrace();
//			TODO log
			return null;
		}
	}
	
	protected void addClientResources(FacesContext context) {
		
		IWMainApplication iwma = IWMainApplication.getIWMainApplication(context);
		Web2Business web2_business = getWeb2Service(iwma.getIWApplicationContext());
		
		if (web2_business != null) {
			
			try {
				AddResource resource = AddResourceFactory.getInstance(context);
				resource.addJavaScriptAtPosition(context, AddResource.HEADER_BEGIN, web2_business.getBundleURIToMootoolsLib());
				
				IWBundle bundle = iwma.getBundle(WebSearchBundleStarter.IW_BUNDLE_IDENTIFIER);
				resource.addJavaScriptAtPosition(context, AddResource.HEADER_BEGIN, bundle.getVirtualPathWithFileNameString("javascript/elight.js"));
				resource.addJavaScriptAtPosition(context, AddResource.HEADER_BEGIN, "/dwr/interface/ElightSearchResults.js");
				resource.addJavaScriptAtPosition(context, AddResource.HEADER_BEGIN, "/dwr/engine.js");
				
				resource.addInlineScriptAtPosition(context, AddResource.HEADER_BEGIN, 
						new StringBuilder("var elight_working_uri = '")
						.append(bundle.getImageURI("images/elightWorking.gif"))
						.append("';\n")
						.append("var elight_search_uri = '")
						.append(bundle.getImageURI(ELIGHT_SEARCH_BUTTON_SRC))
						.append("';")
						.toString()
				);
				resource.addStyleSheet(context, AddResource.HEADER_BEGIN, bundle.getVirtualPathWithFileNameString("style/elight.css"));
				
			} catch (RemoteException e) {
				e.printStackTrace();
//				TODO: log
			}
		}
	}
}
