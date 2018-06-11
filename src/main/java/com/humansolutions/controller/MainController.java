package com.humansolutions.controller;

/**
 * @author RavishaG
 */


import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.humansolutions.common.HumanSolutionsJSPConstants;
import com.humansolutions.common.HumanSolutionsStringConstants;
import com.humansolutions.common.HumanSolutionsURLConstants;
import com.humansolutions.dom.BlobDataDom;
import com.humansolutions.dom.TaskDetailsDom;
import com.humansolutions.dom.UserDom;
import com.humansolutions.service.HumanSolutionsService;

@Controller
public class MainController {
	
	@Resource(name = "humanSolutionsService")
	private HumanSolutionsService humanSolutionsService;
	
	@RequestMapping(value = HumanSolutionsURLConstants.SYSTEM_LOGIN, method = RequestMethod.POST)
	public String systemLogin(HttpServletRequest request, @RequestParam(value = "username") String username, @RequestParam(value = "password") String password) {
		int count = humanSolutionsService.checkLoginCredentials(username.trim(), password);
		if(count == 1){
			HttpSession session = request.getSession();  
			session.setAttribute("username", username.trim());
			session.setAttribute("password", password);
			return HumanSolutionsStringConstants.REDIRECT + HumanSolutionsURLConstants.WELCOME;
		}
		else
			return HumanSolutionsStringConstants.REDIRECT + HumanSolutionsURLConstants.LOGIN + "?error=error";
	}

	/**
	 * This method returns the welcome page
	 * @return ModelAndView
	 */
	@RequestMapping(value = HumanSolutionsURLConstants.WELCOME, method = RequestMethod.GET)
	public ModelAndView welcomePage(HttpServletRequest request) {
		
		HttpSession session = request.getSession();
		String userName = (String) session.getAttribute("username");
		UserDom user = humanSolutionsService.getUserDetails(userName);
		ModelAndView model = new ModelAndView();
		
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.HOUR, 1);
	    DateFormat gmtFormat = new SimpleDateFormat("HH:mm:ss");
	    TimeZone gmtTime = TimeZone.getTimeZone("GMT");
	    gmtFormat.setTimeZone(gmtTime);
	    
		try {
			gmtFormat.setTimeZone(gmtTime);
			Date zero = gmtFormat.parse("00:00:00");
			Date twenty = gmtFormat.parse("20:00:00");

		    Date userDate = new SimpleDateFormat("HH:mm:ss").parse(gmtFormat.format(calendar.getTime()));
		    
	        if (!(userDate.after(zero) && userDate.before(twenty))) {
	        	model.addObject("tasksNotAvailable", true);
	        }
	        else
	        {
	        	if(user.getIsFirstLogin() == 0) {
	    			humanSolutionsService.updateFirstLoginDetails(userName);
	    		}
	    		user = humanSolutionsService.getUserDetails(userName);
	    		List<UserDom> earningsList = getEarningsList(userName, user);
	    		int i = 0;
	    		
	    		boolean endContract = false;
	    		for (UserDom earningDetails : earningsList) {
	    			int sessionNumber = earningDetails.getSessionNumber();
	    			if(earningDetails.getDiffInDays() > 0 && earningDetails.getSessionStatus().equals("Incomplete")) {
	    				model.addObject("endContract", "true");
	    				endContract = true;
	    			}
	    			if(earningDetails.getDiffInDays() > 0 && earningDetails.getSessionStatus().equals("Pending Approval")) {
	    				humanSolutionsService.updateSessionStatus(userName, sessionNumber, "Approved");
	    				earningsList.get(i).setSessionStatus("Approved");
	    				humanSolutionsService.updateEarningsDetails(user);
	    			}
	    			if(earningDetails.getDiffInDays() == 0) {
	    				int sessionId = earningDetails.getSessionNumber();
	    				humanSolutionsService.updateSessionNumber(userName, sessionId);
	    			}
	    			
	    			i += 1;
	    		}
	    		
	    		user = humanSolutionsService.getUserDetails(userName);
	    		
	    		int sessionNumber = user.getSessionNumber();
	    		String treatmentMsgType = user.getTreatmentMsgType();
	    		
	    		if(treatmentMsgType.equalsIgnoreCase("baseline"))
	    			user.setTreatmentMsg(HumanSolutionsStringConstants.BASELINE);
	    		if(treatmentMsgType.equalsIgnoreCase("monitoring"))
	    			user.setTreatmentMsg(HumanSolutionsStringConstants.MONITORING_MSG);
	    		
	    		if(sessionNumber == 7 && !endContract) {
	    			model.addObject("lastSession", "true");
	    		}
	    		
	    		// Popup messages for session 1
	    		if(sessionNumber == 1){
	    			if(treatmentMsgType.equalsIgnoreCase("msg_once") || treatmentMsgType.equalsIgnoreCase("msg_low") || treatmentMsgType.equalsIgnoreCase("msg_high"))
	    				user.setTreatmentMsg(HumanSolutionsStringConstants.MSG_ALL_SESSIONS);
	    		}
	    		
	    		// Popup messages for session 4
	    		else if(sessionNumber == 4){
	    			if(treatmentMsgType.equalsIgnoreCase("msg_once"))
	    				user.setTreatmentMsg(HumanSolutionsStringConstants.BASELINE);
	    			else
	    			if(treatmentMsgType.equalsIgnoreCase("msg_low") || treatmentMsgType.equalsIgnoreCase("msg_high"))
	    				user.setTreatmentMsg(HumanSolutionsStringConstants.MSG_ALL_SESSIONS);
	    		}
	    		
	    		// Popup messages for all other sessions (2,3,5-7)
	    		else {
	    			if(treatmentMsgType.equalsIgnoreCase("msg_once") || treatmentMsgType.equalsIgnoreCase("msg_low"))
	    				user.setTreatmentMsg(HumanSolutionsStringConstants.BASELINE);
	    			if(treatmentMsgType.equalsIgnoreCase("msg_high"))
	    				user.setTreatmentMsg(HumanSolutionsStringConstants.MSG_ALL_SESSIONS);
	    			else
	    				user.setTreatmentMsg(HumanSolutionsStringConstants.BASELINE);
	    		}
	    		
	        }
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
	     
		model.addObject("sessionId", user.getSessionNumber());
		model.addObject("earnings",user.getEarnings());
		model.addObject("treatmentMsg",user.getTreatmentMsg());
		model.addObject("treatmentMsgType",user.getTreatmentMsgType());
		model.addObject("sessionNumber", user.getSessionNumber());
		model.setViewName(HumanSolutionsJSPConstants.WELCOME);
		
		request.getSession().setAttribute("count", 1);
		
		return model;

	}
	
	private List<UserDom> getEarningsList(String userName, UserDom user) {
		
		List<UserDom> earningsList = humanSolutionsService.getEarningsPerSession(userName);
		
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.HOUR, 1);
	    DateFormat gmtFormat = new SimpleDateFormat("dd/MM/yyyy");
	    TimeZone gmtTime = TimeZone.getTimeZone("GMT");
	    gmtFormat.setTimeZone(gmtTime);
	    
	    String dateString = gmtFormat.format(calendar.getTime());
	    
		try {
			Date today = new SimpleDateFormat("dd/MM/yyyy").parse(dateString);
			long days = 0L;
			for (int i = 0; i < 7; i++) {
				try {
					Calendar c = Calendar.getInstance();   
					c.setTime(new SimpleDateFormat("dd/MM/yyyy").parse(gmtFormat.format(user.getStartDate())));
					c.add(Calendar.DATE, i);
					long diff = today.getTime() - c.getTime().getTime();
				    days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
				    if(earningsList.size() > 1)
				    	earningsList.get(i).setDiffInDays(days);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		}catch (ParseException e) {
			e.printStackTrace();
		}
		
		return earningsList;
	}
	
	/**
	 * 
	 * This method returns the instructions page
	 * @return ModelAndView
	 */
	@RequestMapping(value = HumanSolutionsURLConstants.INSTRUCTIONS, method = RequestMethod.GET)
	public ModelAndView displayInstructions(HttpServletRequest request, @RequestParam(value = "sessionId") String sessionId){
		Integer count = (Integer) request.getSession().getAttribute("count");
		if(null == count)
			request.getSession().setAttribute("count", 1);
		
		ModelAndView mav = new ModelAndView(HumanSolutionsJSPConstants.INSTRUCTIONS);
		mav.addObject("sessionId", sessionId);
		return mav;
	}


	/**
	 * This method handles the login and logout of the system
	 * @param error
	 * @param logout
	 * @return ModelAndView
	 */
	@RequestMapping(value = { HumanSolutionsURLConstants.ROOT_LOGIN, HumanSolutionsURLConstants.LOGIN }, method = RequestMethod.GET)
	public ModelAndView login(@RequestParam(value = "error", required = false) String error,
			@RequestParam(value = "logout", required = false) String logout) {

		ModelAndView model = new ModelAndView();
		if (error != null && error.equals("error")) {
			model.addObject("error", "Invalid username or password!");
		}

		if (logout != null && logout.equals("logout")) {
			model.addObject("msg", "You've been logged out successfully.");
		}
		model.setViewName(HumanSolutionsJSPConstants.LOGIN);

		return model;

	}
	
	@RequestMapping(value = HumanSolutionsURLConstants.SYSTEM_LOGOUT)
	public String logout(HttpServletRequest request){
		HttpSession session = request.getSession();
		String userName = (String) session.getAttribute("username");
		int sessionId = 0;
		if(null != request.getParameter("sessionId")) 
			sessionId = Integer.parseInt(request.getParameter("sessionId"));
		
		List<Integer> sessionImageIds = humanSolutionsService.getSessionImageIds(userName, sessionId);
		
		if(sessionImageIds.size() != 0 && sessionId != 0) {
			humanSolutionsService.updateSessionStatus(userName, sessionId, "Incomplete");
		}
			
		session.invalidate();
		// Redirect user to the home page
		return HumanSolutionsStringConstants.REDIRECT + HumanSolutionsURLConstants.LOGIN + "?logout=logout";
	}
	

	/**
	 * This method is used to display images
	 * @param id
	 * @param response
	 * @param request
	 * @throws ServletException
	 * @throws IOException
	 */
	@RequestMapping(value = HumanSolutionsURLConstants.DISPLAY_IMAGE, method = RequestMethod.GET)
	public void displayImage(@RequestParam(value = "sessionId", required = false) Integer sessionId,@RequestParam(value = "imageId", required = false) Integer imageId,
			HttpServletResponse response, HttpServletRequest request) throws ServletException, IOException{

		BlobDataDom image = humanSolutionsService.getImage(imageId);
		
	    int blobLength;
		try {
			blobLength = (int) image.getImage().length();
			byte[] blobAsBytes = image.getImage().getBytes(1, blobLength);

		    image.getImage().free();
		    
		    response.setContentType("image/jpeg, image/jpg, image/png, image/gif");
		    response.getOutputStream().write(blobAsBytes);
		    response.getOutputStream().close();
		    
		} catch (SQLException e) {
			e.printStackTrace();
		}  
	    
	}
	
	/**
	 * This method displays the end screen
	 * @return ModelAndView
	 */
	@RequestMapping(value = HumanSolutionsURLConstants.END_TASK, method = RequestMethod.GET)
	public ModelAndView endTask(@RequestParam(value = "sessionId") String sessionId, HttpServletRequest request){
		
		ModelAndView mav = new ModelAndView(HumanSolutionsJSPConstants.END_TASK);
		request.getSession().setAttribute("endTask", "endTask");
		mav.addObject("sessionId", sessionId);
		return mav;
	}

	
	/**
	 * This method returns the overview screen of the earnings
	 * @return ModelAndView
	 */
	@RequestMapping(value = HumanSolutionsURLConstants.OVERVIEW, method = RequestMethod.GET)
	public ModelAndView overview(HttpServletRequest request, @RequestParam(value = "imageId", required = false) Integer imageId, @RequestParam(value = "sessionId") Integer sessionId){

		HttpSession session = request.getSession();
		String userName = (String) session.getAttribute("username");
		UserDom user = humanSolutionsService.getUserDetails(userName);
		List<UserDom> earningsList = getEarningsList(userName, user);
		ModelAndView mav = new ModelAndView(HumanSolutionsJSPConstants.OVERVIEW);
		
		int todaysSessionId = 0;
		int i = 0;
		String sessionStatus = "";
		
		for (UserDom earningDetails : earningsList) {
			int sessionNumber = earningDetails.getSessionNumber();
			if(earningDetails.getDiffInDays() > 0 && earningDetails.getSessionStatus().equals("Pending Approval")) {
				humanSolutionsService.updateSessionStatus(userName, sessionNumber, "Approved");
				earningsList.get(i).setSessionStatus("Approved");
				humanSolutionsService.updateEarningsDetails(user);
			}
			if(earningDetails.getDiffInDays() == 0 ) {
				todaysSessionId = earningDetails.getSessionNumber();
				humanSolutionsService.updateSessionNumber(userName, todaysSessionId);
				if(earningDetails.getSessionStatus().equals("Incomplete")) {
					mav.addObject("dueToday","dueToday");
				}
			}
			
			i += 1;
		}
		
		List<Integer> sessionImageIds = new ArrayList<Integer>();
		
		if(todaysSessionId != 0) {
			sessionStatus = humanSolutionsService.getSessionStatus(userName, todaysSessionId);
		}
		
		if(!sessionStatus.isEmpty() && sessionStatus.equalsIgnoreCase("Incomplete")) {
			sessionImageIds = humanSolutionsService.getSessionImageIds(userName, todaysSessionId);
			
			if(sessionImageIds.size() == 0) {
				List<Integer> readableImageIds = humanSolutionsService.getReadableImageIds();
				List<Integer> hardlyReadableImageIds = humanSolutionsService.getHardlyReadableImageIds();
				List<Integer> unreadableImageIds = humanSolutionsService.getUnreadableImageIds();
				
				List<Integer> readableImageIdsTranscribedByUser = humanSolutionsService.getReadableImageIdsTranscribedByUser(userName);
				List<Integer> hardlyReadableImageIdsTranscribedByUser = humanSolutionsService.getHardlyReadableImageIdsTranscribedByUser(userName);
				List<Integer> unreadableImageIdsTranscribedByUser = humanSolutionsService.getUnreadableImageIdsTranscribedByUser(userName);
				
				readableImageIds.removeAll(readableImageIdsTranscribedByUser);
				hardlyReadableImageIds.removeAll(hardlyReadableImageIdsTranscribedByUser);
				unreadableImageIds.removeAll(unreadableImageIdsTranscribedByUser);
				
				sessionImageIds = getImageIdsForSession(readableImageIds, hardlyReadableImageIds, unreadableImageIds);
				humanSolutionsService.saveImageIdsForSession(userName, sessionImageIds, sessionId);
			}
			
		}
		
		if(null != sessionImageIds && sessionImageIds.size() > 0) {
			mav.addObject("imageId", sessionImageIds.get(0));
		}
		
		mav.addObject("earningsList", earningsList);
		mav.addObject("sessionId", sessionId);
		mav.addObject("finalImageIdsForSession", sessionImageIds);
		
		return mav;
	}
	
	/**
	 * This method returns the images for a session for a user
	 * @param readableImageIds
	 * @param hardlyReadableImageIds
	 * @param unreadableImageIds
	 * @return finalImageIdsForSession
	 */
	private List<Integer> getImageIdsForSession(List<Integer> readableImageIds, List<Integer> hardlyReadableImageIds, 
			List<Integer> unreadableImageIds) {
		
		List<Integer> finalImageIdsForSession = new ArrayList<Integer>();

		Collections.shuffle(readableImageIds);
		Collections.shuffle(hardlyReadableImageIds);
		Collections.shuffle(unreadableImageIds);
		if(readableImageIds.size() > 1)
			readableImageIds = readableImageIds.subList(0, 6);
		if(hardlyReadableImageIds.size() > 1)
			hardlyReadableImageIds = hardlyReadableImageIds.subList(0, 2);
		if(unreadableImageIds.size() > 1)
			unreadableImageIds = unreadableImageIds.subList(0, 2);
		
		
		finalImageIdsForSession.addAll(readableImageIds);
		finalImageIdsForSession.addAll(hardlyReadableImageIds);
		finalImageIdsForSession.addAll(unreadableImageIds);
		
		Collections.shuffle(finalImageIdsForSession);
		
		return finalImageIdsForSession;
	}
	
	/**
	 * This method displays the task page
	 * @return ModelAndView
	 */
	@RequestMapping(value = HumanSolutionsURLConstants.TRANSCRIBE_TEXTS, method = RequestMethod.GET)
	public ModelAndView transcribeText(@RequestParam(value = "sessionId", required = false) Integer sessionId, @RequestParam(value = "imageId", required = false) 
			Integer imageId, HttpServletRequest request) {
		
		HttpSession session = request.getSession();
		String userName = (String) session.getAttribute("username");
		
		request.getSession().setAttribute("count", 2);
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("sessionId", sessionId);
		modelAndView.addObject("imageId", imageId);
		modelAndView.addObject("taskDetails", new TaskDetailsDom());
		
		List<Integer> sessionImageIds = humanSolutionsService.getSessionImageIds(userName, sessionId);
		
		Double progressPercent = (double) Math.round(((10-sessionImageIds.size())/10.0)*100.0);
		modelAndView.addObject("progressPercent", progressPercent.intValue());
		modelAndView.addObject("sessionImageIdsSize", (10-sessionImageIds.size()));
		
		modelAndView.setViewName(HumanSolutionsJSPConstants.TRANSCRIBE_TEXTS_JSP);
		return modelAndView;
	}
	
	/**
	 * This method saves the text entered by the user
	 * @return ModelAndView
	 */
	@RequestMapping(value = HumanSolutionsURLConstants.TRANSCRIBE_TEXTS, method = RequestMethod.POST)
	public String saveTranscribedText(HttpServletRequest request, @ModelAttribute("taskDetails") TaskDetailsDom taskDetailsDom ,@RequestParam(value = "sessionId", required = false) Integer sessionId, 
			@RequestParam(value = "imageId", required = false) Integer imageId){
		
		HttpSession session = request.getSession();
		String userName = (String) session.getAttribute("username");
		
		String solutionText = humanSolutionsService.getSolutionTranscribedText(imageId);
		
		double accuracyScore = similarity(solutionText, taskDetailsDom.getTranscribedText());
		
		UserDom user = new UserDom();
		user.setUsername(userName);
		user.setSessionNumber(sessionId);
		user.setImageId(imageId);
		
		taskDetailsDom.setAccuracyScore(accuracyScore);
		
		humanSolutionsService.saveTranscribedTextAndScoreObtained(user, taskDetailsDom);
		humanSolutionsService.updateUserTranscribedImageTables(userName, imageId, sessionId);
		
		List<Integer> sessionImageIds = humanSolutionsService.getSessionImageIds(userName, sessionId);
		
		if(sessionImageIds.size() != 0) {
			String redirectString = HumanSolutionsStringConstants.REDIRECT + HumanSolutionsURLConstants.TRANSCRIBE_TEXTS + "?sessionId=" + sessionId
					+ "&imageId=" + (sessionImageIds.get(0));
			return redirectString;
		}
		else {
			humanSolutionsService.updateSessionStatus(userName, sessionId, "Pending Approval");
			return HumanSolutionsStringConstants.REDIRECT + HumanSolutionsURLConstants.END_TASK + "?sessionId=" + sessionId;
		}
			
	}
	
	
	
	/* Reference: https://stackoverflow.com/questions/955110/similarity-string-comparison-in-java */
	
	   /**
	   * Calculates the similarity (a number within 0 and 1) between two strings.
	   */
	  public static double similarity(String s1, String s2) {
	    String longer = s1, shorter = s2;
	    if (s1.length() < s2.length()) { // longer should always have greater length
	      longer = s2; shorter = s1;
	    }
	    int longerLength = longer.length();
	    if (longerLength == 0) { return 1.0; /* both strings are zero length */ }
	    return (longerLength - editDistance(longer, shorter)) / (double) longerLength;

	  }

	  // Example implementation of the Levenshtein Edit Distance
	  public static int editDistance(String s1, String s2) {
	    s1 = s1.toLowerCase();
	    s2 = s2.toLowerCase();

	    int[] costs = new int[s2.length() + 1];
	    for (int i = 0; i <= s1.length(); i++) {
	      int lastValue = i;
	      for (int j = 0; j <= s2.length(); j++) {
	        if (i == 0)
	          costs[j] = j;
	        else {
	          if (j > 0) {
	            int newValue = costs[j - 1];
	            if (s1.charAt(i - 1) != s2.charAt(j - 1))
	              newValue = Math.min(Math.min(newValue, lastValue),
	                  costs[j]) + 1;
	            costs[j - 1] = lastValue;
	            lastValue = newValue;
	          }
	        }
	      }
	      if (i > 0)
	        costs[s2.length()] = lastValue;
	    }
	    return costs[s2.length()];
	  }

	
}