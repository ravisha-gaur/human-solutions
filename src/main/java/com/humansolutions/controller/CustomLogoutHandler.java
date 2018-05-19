package com.humansolutions.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

import com.humansolutions.service.HumanSolutionsService;

public class CustomLogoutHandler extends SimpleUrlLogoutSuccessHandler {

	@Autowired
	private HumanSolutionsService humanSolutionsService;
	
	/**
	 * This method is used to logout the user
	*/
    @Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, 
			Authentication authentication) throws IOException, ServletException {

		// Spring security authentication containing the logged in user details
		String userName = authentication.getName();
		int sessionId = 0;
		if(null != request.getParameter("sessionId")) 
			sessionId = Integer.parseInt(request.getParameter("sessionId"));
		
		List<Integer> sessionImageIds = humanSolutionsService.getSessionImageIds(userName, sessionId);
		
		if(sessionImageIds.size() != 0 && sessionId != 0) {
			humanSolutionsService.updateSessionStatus(userName, sessionId, "Incomplete");
		}
			
		// Redirect user to the home page
		response.sendRedirect("login.htm?logout=logout");
		
	}
    
	public HumanSolutionsService getHumanSolutionsService() {
		return humanSolutionsService;
	}
	
	public void setHumanSolutionsService(HumanSolutionsService humanSolutionsService) {
		this.humanSolutionsService = humanSolutionsService;
	}
	
}
