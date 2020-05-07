package org.aksw.sante.core;

import java.io.InputStream;
import java.io.StringReader;
import java.net.URL;
import java.util.Scanner;

import org.apache.jena.graph.Triple;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFLanguages;
import org.apache.jena.riot.RDFParserBuilder;
import org.apache.jena.riot.system.StreamRDF;
import org.apache.jena.sparql.core.Quad;

public class RDFParser {

	public static void load(String uri, Model model) {
		try {
			StreamRDF reader = new StreamRDF() {
				@Override
				public void base(String arg0) {
				}
				@Override
				public void finish() {
				}
				@Override
				public void prefix(String arg0, String arg1) {
				}
				@Override
				public void quad(Quad arg0) {
					Statement statement = model.asStatement(arg0.asTriple());
					model.add(statement);
				}
				@Override
				public void start() {
				}
				@Override
				public void triple(Triple triple) {
					Statement statement = model.asStatement(triple);
					model.add(statement);
				}
			};
			RDFParserBuilder a = RDFParserBuilder.create();
			a.forceLang(RDFLanguages.filenameToLang(uri, Lang.TTL));
			URL url = new URL(uri);
			InputStream is = url.openStream();
			try (Scanner in = new Scanner(is);){				
				while(in.hasNextLine()) {
					a.source(new StringReader(in.nextLine()));
					try {
						a.parse(reader);
					} catch (Exception e) {
					}
				}
			}
		} catch (Exception ex) {
		}
	}
}
