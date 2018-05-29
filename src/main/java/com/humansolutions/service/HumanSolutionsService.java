package com.humansolutions.service;

import java.util.List;

import com.humansolutions.dom.BlobDataDom;
import com.humansolutions.dom.TaskDetailsDom;
import com.humansolutions.dom.UserDom;

public interface HumanSolutionsService {

	public BlobDataDom getImage(int imageId);

	public UserDom getUserDetails(String userName);

	public void updateSessionNumber(String userName, int todaysSessionId);

	public List<UserDom> getEarningsPerSession(String userName);

	public List<Integer> getSessionImageIds(String userName, int todaysSessionId);

	public String getSolutionTranscribedText(Integer imageId);

	public void saveTranscribedTextAndScoreObtained(UserDom user, TaskDetailsDom taskDetailsDom);

	public void updateEarningsDetails(UserDom user);

	public void updateFirstLoginDetails(String userName);

	public List<Integer> getReadableImageIds();

	public List<Integer> getHardlyReadableImageIds();

	public List<Integer> getUnreadableImageIds();

	public List<Integer> getReadableImageIdsTranscribedByUser(String userName);

	public List<Integer> getHardlyReadableImageIdsTranscribedByUser(String userName);

	public List<Integer> getUnreadableImageIdsTranscribedByUser(String userName);

	public void updateUserTranscribedImageTables(String userName, Integer imageId, Integer sessionId);

	public void saveImageIdsForSession(String userName, List<Integer> finalImageIdsForSession, int sessionId);

	public String getSessionStatus(String userName, int sessionId);

	public void updateSessionStatus(String userName, int sessionId, String sessionStatus);

	public int checkLoginCredentials(String username, String password);
}
