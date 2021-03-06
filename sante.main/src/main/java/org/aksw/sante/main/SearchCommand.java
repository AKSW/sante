package org.aksw.sante.main;

import java.io.File;
import java.nio.file.Path;
import java.util.Map;

import org.aksw.sante.entity.Entity;
import org.apache.lucene.index.IndexReader;
import org.sante.lucene.ResultSet;
import org.sante.lucene.SearchEngine;

public class SearchCommand extends AbstractSimpleMapCommand {
	public SearchCommand() {
		super("search");
	}
	
	@Override
	public boolean canProcess(Map<String, String[]> commands) {
		return commands.containsKey("-path") &&
				commands.containsKey("-query"); 
	}

	@Override
	public void process(Map<String, String[]> commands) throws Exception {
		String queryParam = commands.get("-query")[0];
		String pathParam = commands.get("-path")[0];
		int limit = 100;
		if(commands.containsKey("-limit")) {
			String limitParam = commands.get("-limit")[0];
			limit = Integer.parseInt(limitParam);
		}		
		File indexDir = new File(pathParam);
		Path indexPath = indexDir.toPath();
		try(IndexReader reader = SearchEngine.newReader(indexPath);) {
			SearchEngine searchEngine = new SearchEngine(reader);
			ResultSet<Entity> rs = searchEngine.search(queryParam, 0, limit);
			rs.accept(new SystemOutURIResult());
		}
	}
}
