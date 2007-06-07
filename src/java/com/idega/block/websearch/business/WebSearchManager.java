package com.idega.block.websearch.business;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import com.idega.block.websearch.data.WebSearchIndex;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.idegaweb.IWMainApplication;
import com.idega.slide.business.IWSlideService;
import com.idega.slide.util.WebdavExtendedResource;
import com.idega.util.bundles.BundleResourceResolver;
import com.idega.util.bundles.Resource;

/**
 * <p><code>WebSearchManager</code> Manages WebSearchIndexes.
* This class is a part of the websearch webcrawler and search engine block. <br>
* It is based on the <a href="http://lucene.apache.org">Lucene</a> java search engine from the Apache group and loosly <br>
* from the work of David Duddleston of i2a.com.<br>
*
* @copyright Idega Software 2002
* @author <a href="mailto:eiki@idega.is">Eirikur Hrafnsson</a>
 */
public final class WebSearchManager {
    
	private Logger logger;
	
    private Map<String, WebSearchIndex> indexes;
    private static WebSearchManager me;
    
    private WebSearchManager() {
    	logger = Logger.getLogger(this.getClass().getName());
    }
    
    public static synchronized WebSearchManager getInstance() {
		
    	if(me == null)
    		me = new WebSearchManager();
    	
    	return me;
	}
    
    private Map<String, WebSearchIndex> getIndexes() throws IOException {
    	
    	if(indexes == null)
    		parseConfigXML(resolveCfg());
    	
    	return indexes;
    }
    
    private static final String slide_path_to_cfg = "/files/cfg/websearch/";
    private static final String slide_cfg_file_name = "websearch.xml";
    
    private InputStream resolveCfg() throws IOException {

    	IWSlideService service = getIWSlideService();
    	WebdavExtendedResource resource = service.getWebdavExtendedResource(slide_path_to_cfg+slide_cfg_file_name, service.getRootUserCredentials());
    	
    	if(resource.exists()) {
    		
    		return resource.getMethodData();
    		
    	} else {
    		
    		URI cfg_uri = URI.create("bundle://" + WebSearchBundleStarter.IW_BUNDLE_IDENTIFIER + "/resources/websearch.xml");
    		BundleResourceResolver resolver = new BundleResourceResolver(IWMainApplication.getDefaultIWMainApplication());
    		
    		Resource res = resolver.resolve(cfg_uri);
    		service.uploadFileAndCreateFoldersFromStringAsRoot(slide_path_to_cfg, slide_cfg_file_name, res.getInputStream(), "text/xml", false);
    		
    		return res.getInputStream();
    	}
    }
    
    
    public WebSearchIndex getIndex(String key) {
    	
    	try {
    		
    		WebSearchIndex index = getIndexes().get(key);
    		
        	if (index == null)
        		logger.log(Level.WARNING, "no index: " + key);

        	return index;
			
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error while retrieving indexes", e);
			return null;
		}
    }
    
    public void addIndex(String name, String index,
            String[] seeds, String[] scopes) {
        
    	try {
    		getIndexes().put(name, new WebSearchIndex(index, seeds, scopes));
    		
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error while retrieving indexes", e);
		}
    }
    
    /**
	 * Use JDOM to parse database configuration XML
	 * Creation date: (11/7/99 7:30:26 PM)
	 */
	public void parseConfigXML(InputStream xml_is) {

		// if another xml is provided, we will append/override indexes, as no new Map is created 
		if(indexes == null)
			indexes = new HashMap<String, WebSearchIndex>();
		
		IWMainApplication iw_app = IWMainApplication.getDefaultIWMainApplication();
		String app_real_path = iw_app.getApplicationRealPath();
		
		try {
			SAXBuilder builder = new SAXBuilder(false);
			Document doc = builder.build(xml_is);
			Element root = doc.getRootElement();
			
			// Get indexes 
			List<Element> indexElements = root.getChildren("index");
			
			// Iterate elements Elements and add to Types
			for (Element indexElement : indexElements) {
				
				// add new index to indexes HashMap
               String name = indexElement.getChild("name").getTextTrim();
               String indexURI = indexElement.getChild("indexURI").getTextTrim();
               indexURI = LinkParser.getRealPath(app_real_path, indexURI, File.separator);
               
               List<Element> seedElements = indexElement.getChildren("seed");
               String[] seeds = new String[seedElements.size()];
               int i2 = 0;
               
               for (Element seed_element : seedElements)
                   seeds[i2++] = seed_element.getTextTrim();
               
               List<Element> scopeElements = indexElement.getChildren("scope");
               String[] scopes = new String[scopeElements.size()];
               i2 = 0;
               
               for (Element scope_element : scopeElements)
                   scopes[i2] = scope_element.getTextTrim();
                      
               WebSearchIndex webSearchIndex =  new WebSearchIndex(
                        indexURI, seeds, scopes);
               
               indexes.put(name, webSearchIndex);		
			}
		
		} catch (Exception e) {
			logger.log(Level.SEVERE, "WebSearch: Error reading config file, pointing index to http://localhost by default", e);
			String localhost = new String("http://localhost/");
			
			String indexURI = LinkParser.getRealPath(app_real_path, "../search/main", File.separator); 
			String[] seeds = {localhost};
			String[] scopes = {localhost};

			
			WebSearchIndex webSearchIndex =  new WebSearchIndex(indexURI, seeds, scopes);
               
            indexes.put("main", webSearchIndex);
               
               
		}
	}
	
	protected IWSlideService getIWSlideService() throws IBOLookupException {
		
		try {
			return (IWSlideService) IBOLookup.getServiceInstance(IWMainApplication.getDefaultIWApplicationContext(), IWSlideService.class);
		} catch (IBOLookupException e) {
			logger.log(Level.SEVERE, "Error getting IWSlideService");
			throw e;
		}
	}
}
