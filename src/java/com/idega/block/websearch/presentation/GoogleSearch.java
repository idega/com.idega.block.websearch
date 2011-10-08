package com.idega.block.websearch.presentation;

import javax.faces.context.FacesContext;

import com.idega.block.websearch.bean.GoogleBean;
import com.idega.facelets.ui.FaceletComponent;
import com.idega.idegaweb.IWBundle;
import com.idega.presentation.IWBaseComponent;
import com.idega.presentation.IWContext;
import com.idega.util.PresentationUtil;

public class GoogleSearch extends IWBaseComponent {

	private final static String IW_BUNDLE_IDENTIFIER = "com.idega.block.websearch";
	private static final String GOOGLE_API_KEY = "google.api.key";

	@Override
	protected void initializeComponent(FacesContext context) {
		IWContext iwc = IWContext.getIWContext(context);
		IWBundle iwb = iwc.getApplicationContext().getIWMainApplication().getBundle(getBundleIdentifier());
		String apiKey = iwc.getApplicationSettings().getProperty(GOOGLE_API_KEY, "");

		PresentationUtil.addStyleSheetToHeader(iwc, iwb.getVirtualPathWithFileNameString("style/gsc.css"));

		GoogleBean appBean = getBeanInstance("googleBean");
		appBean.setApiKey(apiKey);

		FaceletComponent facelet = (FaceletComponent) iwc.getApplication().createComponent(FaceletComponent.COMPONENT_TYPE);
		facelet.setFaceletURI(iwb.getFaceletURI("gsc/gsc-search.xhtml"));
		add(facelet);
	}

	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}

}