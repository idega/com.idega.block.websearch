package com.idega.block.websearch.elight.presentation.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;

import com.idega.block.websearch.business.WebSearchHitIterator;
import com.idega.block.websearch.business.WebSearchManager;
import com.idega.block.websearch.data.WebSearchHit;
import com.idega.core.cache.IWCacheManager2;
import com.idega.idegaweb.IWMainApplication;

/**
 * 
 * 
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version 1.0
 * 
 */
public class SearchResults implements Serializable {
	
	private static final long serialVersionUID = -6155590432961913762L;
	
	private IWCacheManager2 iwcm2;
	private static final String hit_iterator_cache = "elight.hit_iterator_cache";
	
	public List<SearchResult> search(String query) {
		
		if(query == null)
			return null;
		
		WebSearchHitIterator hits = getHitIteratorCacheMap().get(query);
		
		if (hits == null) {
			
			try {
				com.idega.block.websearch.business.WebSearcher searcher = new com.idega.block.websearch.business.WebSearcher(WebSearchManager.getInstance().getIndex("main"));
				
				//hits per page
				searcher.setHitsPerSet(0);
				
				// exact phrase
				searcher.setPhraseSearch(true);
				
				// from days
//				searcher.setFromDays(this.publishedFromDays);
				
				hits = searcher.search(query);
				
				if(hits != null && hits.getTotalHits() != 0)
					getHitIteratorCacheMap().put(query, hits);
			}
			catch (Exception e) {
				e.printStackTrace();
				return getMessageToTheUser("You need to run the indexer first!");
				
//				TODO: log and return message, you need to index that first
//				this.iwrb.getLocalizedString("you.have.to.index.first", "You need to run the indexer first!")
			}
		}
		
		if(hits == null || hits.getTotalHits() == 0) {
//			TODO: localize
			return getMessageToTheUser("no results are found");
		}
		
		hits.resetPosition();
		
//		TODO: add status directions and etc @see WebSearcher.getResultSetInfo
		
		List<SearchResult> results = new ArrayList<SearchResult>();
		hits.resetPosition();
		
		while (hits.hasNextInSet()) {
			
			WebSearchHit hit = hits.next();
			
			SearchResult result = new SearchResult();
			result.setUrl(hit.getURL());
			result.setTitle(hit.getTitle());
			
			result.setContents(hit.getContents(query));
//			TODO: localize
			String extra_info = hit.getHREF() + " - " + hit.getContentType() + " - " + /*this.iwrb.getLocalizedString("rank", "rank")*/"rank" + ": " + hit.getRank();
			result.setExtraInfo(extra_info);
			results.add(result);
		}
		
		return results;
	}
	
	private List<SearchResult> getMessageToTheUser(String message) {
		
		List<SearchResult> results_error = new ArrayList<SearchResult>();
		SearchResult res_error = new SearchResult();
		res_error.setMessage(message);
		results_error.add(res_error);
		return results_error;
	}
	
	protected Map<String, WebSearchHitIterator> getHitIteratorCacheMap() {
		
		if(iwcm2 == null)
			iwcm2 = IWCacheManager2.getInstance(IWMainApplication.getDefaultIWMainApplication());
		
//		TODO: is this map thread safe?
		return (Map<String, WebSearchHitIterator>)iwcm2.getCache(hit_iterator_cache);
	}
}