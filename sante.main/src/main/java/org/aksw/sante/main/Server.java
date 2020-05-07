package org.aksw.sante.main;

import org.eclipse.jetty.webapp.WebAppContext;

public class Server extends org.eclipse.jetty.server.Server {
	
	public Server(int port) {
		super(port);
	}
	
	public void start(String warPath) throws Exception {
	    WebAppContext context = new WebAppContext();
	    context.setContextPath("/");
	    context.setWar(warPath);
	    setHandler(context);
	    start();
	    join();
	}
	
	public static void main(String[] args) throws Exception {
		Server server = new Server(8080);
		server.start("d://smile.war");
	}
}