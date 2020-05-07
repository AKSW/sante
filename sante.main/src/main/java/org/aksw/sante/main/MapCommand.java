package org.aksw.sante.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class MapCommand implements Command {
	public final static String COMMAND_PRAGMA = "-";
	public final static String SUB_COMMAND_PRAGMA = "/";

	public abstract boolean canProcess(Map<String, String[]> commands);

	public abstract void process(Map<String, String[]> commands) throws Exception;
	
	/**
	 * Command line parser.
	 * 
	 * @param args
	 *            a set o arguments received by command line.
	 * 
	 * @return a Map containing the parsed arguments.
	 */
	public static Map<String, String[]> parse(String[] args) {
		Map<String, String[]> map = new HashMap<String, String[]>();
		for (int i = 0; i < args.length; i++) {
			if (args[i].startsWith(COMMAND_PRAGMA) || 
					args[i].startsWith(SUB_COMMAND_PRAGMA)) { // is it a command
				List<String> params = new ArrayList<>();
				int j = i + 1;
				while (j < args.length && !args[j].startsWith(COMMAND_PRAGMA)) {
					params.add(args[j]);
					j++;
				}
				map.put(args[i], params.toArray(new String[params.size()]));
			}
		}
		return map;
	}

	@Override
	public boolean canProcess(String[] args) {
		return canProcess(parse(args));
	}

	@Override
	public void process(String[] args) throws Exception {
		process(parse(args));
	}

}
