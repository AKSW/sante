package org.aksw.sante.main;

import org.aksw.sante.smile.core.SmileParams;
import org.aksw.sante.smile.servlet.DBpediaLookupServlet;
import org.aksw.sante.smile.servlet.ResourceServlet;
import org.aksw.sante.smile.servlet.ReconcileSearchServlet;
import org.aksw.sante.smile.servlet.SearchServlet;
import org.aksw.sante.smile.servlet.SuggestServlet;
import org.apache.tomcat.util.scan.StandardJarScanner;
import org.eclipse.jetty.apache.jsp.JettyJasperInitializer;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.util.component.AbstractLifeCycle;
import org.eclipse.jetty.webapp.WebAppContext;

public class Server extends org.eclipse.jetty.server.Server {
	
	public Server(int port) {
		super(port);
	}
	
	public void start(String warPath, String indexPath) throws Exception {
	    WebAppContext context = new WebAppContext();
	    context.setContextPath("/");
	    
	    //for jsp support
	    context.setResourceAlias("/WEB-INF/classes/", "/classes/");
	    context.addBean(new JettyJspParser(context));
	    context.getServletContext().setExtendedListenerTypes(true);
	    context.setParentLoaderPriority(true);
	    context.setThrowUnavailableOnStartupException(true);
	    context.setConfigurationDiscovered(true);
	    
	    context.addServlet(DBpediaLookupServlet.class, "/API/lookup/search");
        context.addServlet(SearchServlet.class, "/API/search");
        context.addServlet(SuggestServlet.class, "/API/suggest");
        context.addServlet(ReconcileSearchServlet.class, "/API/reconcile");
        context.addServlet(ResourceServlet.class, "/API/resource");

//	    context.setWelcomeFiles(new String[] { "index_full.xhtml" });
	    SmileParams.getInstance().indexPath = indexPath;
	    context.setWar(warPath);
	    setHandler(context);
	    start();
	    join();	    
	}
	
	private static class JettyJspParser extends AbstractLifeCycle implements ServletContextHandler.ServletContainerInitializerCaller {

        private JettyJasperInitializer jasperInitializer;
        private ServletContextHandler context;

        public JettyJspParser(ServletContextHandler context) {
            this.jasperInitializer = new JettyJasperInitializer();
            this.context = context;
            this.context.setAttribute("org.apache.tomcat.JarScanner", new StandardJarScanner());
        }

        @Override
        protected void doStart() throws Exception {
//            ClassLoader old = Thread.currentThread().getContextClassLoader();
//            Thread.currentThread().setContextClassLoader(context.getClassLoader());
            try {
                jasperInitializer.onStartup(null, context.getServletContext());
                super.doStart();
            } finally {
//                Thread.currentThread().setContextClassLoader(old);
            }
        }

    }
}