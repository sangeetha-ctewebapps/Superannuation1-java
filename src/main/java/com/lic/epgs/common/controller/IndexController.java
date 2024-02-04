/**
 * 
 */
package com.lic.epgs.common.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Muruganandam
 *
 */
//@RestController
public class IndexController {
	
	 @GetMapping("/") void handleFoo(HttpServletResponse response) throws
	 IOException { response.sendRedirect("swagger-ui/index.html"); }
	 

}
