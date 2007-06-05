package com.idega.block.websearch.presentation;

import com.idega.presentation.Block;
import com.idega.presentation.IWContext;

public class WebSearchWidget extends Block {
	
	private final static String IW_BUNDLE_IDENTIFIER = "com.idega.block.websearch";
	
	protected static final int ACTION_IDLE = 1;
	protected static final int ACTION_CRAWL = 2;
	protected static final int ACTION_SEARCH = 3;
	
	public static final String PARAMETER_ACTION = "iw_search_prm_action";
	
	private int parseAction(IWContext iwc) {
		if (iwc.isParameterSet(PARAMETER_ACTION)) {
			return Integer.parseInt(iwc.getParameter(PARAMETER_ACTION));
		}
		return ACTION_SEARCH;
	}
	
	protected void present(IWContext iwc) throws Exception {
		switch (parseAction(iwc)) {
			case ACTION_IDLE:
				//TODO
				break;
				
			case ACTION_SEARCH:
				//TODO
				break;
	
			case ACTION_CRAWL:
				//TODO
				break;
		}
	}
	
	public void main(IWContext iwc) throws Exception {
		//TODO perhaps some initialization
		present(iwc);
	}
	
	public String getBundleIdentifier() {
		//return IWBundleStarter.IW_BUNDLE_IDENTIFIER;
		return IW_BUNDLE_IDENTIFIER;
	}

}
