package co.uk.bocaditos.jdocs.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import co.uk.bocaditos.jdocs.services.JavadocService;


/**
 * .
 * 
 * @author aasco
 */
@Controller
public class JavadocController {

	private static final Logger logger = LogManager.getLogger(JavadocController.class);

    private final JavadocService service;


	public JavadocController(final JavadocService service) {
		this.service = service;
	}

    @GetMapping({"/", "/home"})
    public String home(final Model model) {
    	logger.debug("Setting up home page...");
        model.addAttribute("route", this.service.getRoute());
        model.addAttribute("docs", this.service.getDocs());
    	logger.debug("Home page was successfully set up");

        return "home";
    }

    @GetMapping({"/lib"})
    public String libHome(final Model model, @RequestParam(name="name", required=true) final String name, 
    		@RequestParam(name="version", required=false) final String version) {
    	final String url;

    	logger.debug("Setting up {}{} lib page...", name, notNull(version) ? " v" + version : "");
    	if (notNull(version)) {
    		url = buildJavadocUrl(name, version);
    	} else {
            model.addAttribute("name", this.service.getDocs().new StringName(name));
            model.addAttribute("versions", this.service.getDocs().get(name));
            model.addAttribute("route", this.service.getRoute());
    		url = "lib";
    	}
    	logger.debug("{}{} lib page was successfully set up", name, notNull(version) ? " v" + version : "");

    	return url;
    }

    @GetMapping({"/licence"})
    public String lice(final Model model) {
        return "licence";
    }
    
    static boolean notNull(final String str) {
    	return (str != null && !str.isEmpty());
    }
    
    static String buildJavadocUrl(final String name, final String version) {
    	return "redirect:/javadoc/" + name + "/" + version + "/index.html";
    }

} // end class JavadocController
