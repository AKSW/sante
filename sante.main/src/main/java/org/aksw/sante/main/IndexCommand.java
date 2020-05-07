package org.aksw.sante.main;

import java.io.File;
import java.util.Map;

import org.aksw.sante.core.URIPatternFilter;
import org.sante.lucene.RDFIndexer;

public class IndexCommand extends AbstractSimpleMapCommand {

	public IndexCommand() {
		super("index");
	}
	
	@Override
	public boolean canProcess(Map<String, String[]> commands) {
		return commands.containsKey("-path") &&
				commands.containsKey("-endpoint"); 
	}

	@Override
	public void process(Map<String, String[]> commands) throws Exception {
		String filePath = commands.get("-path")[0];
		String endpoint = commands.get("-endpoint")[0];
		RDFIndexer indexer = new RDFIndexer();
		File index = new File(filePath);
		URIPatternFilter subjectFilter = new URIPatternFilter();
		URIPatternFilter predicateFilter = new URIPatternFilter();
		URIPatternFilter typeFilter = new URIPatternFilter();
		indexer.createIndex(index,
				endpoint,
				subjectFilter,
				predicateFilter,
				typeFilter);
	}
}
