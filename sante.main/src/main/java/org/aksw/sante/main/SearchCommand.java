package org.aksw.sante.main;

import java.io.File;
import java.nio.file.Path;
import java.util.Map;

import org.sante.lucene.ResultSet;
import org.sante.lucene.SanteEngine;

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
		int limit = Integer.MAX_VALUE;
		if(commands.containsKey("-limit")) {
			String limitParam = commands.get("-limit")[0];
			limit = Integer.getInteger(limitParam);
		}		
		File indexDir = new File(pathParam);
		Path indexPath = indexDir.toPath();
		SanteEngine searchEngine = new SanteEngine(indexPath);
		try (ResultSet rs = searchEngine.search(queryParam, 0, limit);) {
			rs.accept(new SystemOutTriplesResult());
		}
	}
}
