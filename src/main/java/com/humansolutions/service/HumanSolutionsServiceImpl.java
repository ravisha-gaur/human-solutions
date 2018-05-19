package com.humansolutions.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.humansolutions.dao.HumanSolutionsDao;
import com.humansolutions.dom.BlobDataDom;
import com.humansolutions.dom.TaskDetailsDom;
import com.humansolutions.dom.UserDom;

@Service("humanSolutionsService")
public class HumanSolutionsServiceImpl implements HumanSolutionsService{
	
	@Autowired
	HumanSolutionsDao humanSolutionsDao;
	
	public HumanSolutionsDao getHumanSolutionsDao() {
		return humanSolutionsDao;
	}

	public void setHumanSolutionsDao(HumanSolutionsDao humanSolutionsDao) {
		this.humanSolutionsDao = humanSolutionsDao;
	}

	@Override
	public BlobDataDom getImage(int imageId) {
		return humanSolutionsDao.getImage(imageId);
	}

	@Override
	public UserDom getUserDetails(String userName) {
		return humanSolutionsDao.getUserDetails(userName);
	}

	@Override
	public void updateSessionNumber(String userName, int todaysSessionId) {
		humanSolutionsDao.updateSessionNumber(userName, todaysSessionId);
	}

	@Override
	public List<UserDom> getEarningsPerSession(String userName) {
		return humanSolutionsDao.getEarningsPerSession(userName);
	}

	@Override
	public List<Integer> getSessionImageIds(String userName, int todaysSessionId) {
		return humanSolutionsDao.getSessionImageIds(userName, todaysSessionId);
	}

	@Override
	public String getSolutionTranscribedText(Integer imageId) {
		return humanSolutionsDao.getSolutionTranscribedText(imageId);
	}

	@Override
	public void saveTranscribedTextAndScoreObtained(UserDom user, TaskDetailsDom taskDetailsDom) {
		humanSolutionsDao.saveTranscribedTextAndScoreObtained(user, taskDetailsDom);
	}

	@Override
	public void updateEarningsDetails(UserDom user) {
		humanSolutionsDao.updateEarningsDetails(user);
	}

	@Override
	public void updateFirstLoginDetails(String userName) {
		humanSolutionsDao.updateFirstLoginDetails(userName);
	}

	@Override
	public List<Integer> getReadableImageIds() {
		return humanSolutionsDao.getReadableImageIds();
	}

	@Override
	public List<Integer> getHardlyReadableImageIds() {
		return humanSolutionsDao.getHardlyReadableImageIds();
	}

	@Override
	public List<Integer> getUnreadableImageIds() {
		return humanSolutionsDao.getUnreadableImageIds();
	}

	@Override
	public List<Integer> getReadableImageIdsTranscribedByUser(String userName) {
		return humanSolutionsDao.getReadableImageIdsTranscribedByUser(userName);
	}

	@Override
	public List<Integer> getHardlyReadableImageIdsTranscribedByUser(String userName) {
		return humanSolutionsDao.getHardlyReadableImageIdsTranscribedByUser(userName);
	}

	@Override
	public List<Integer> getUnreadableImageIdsTranscribedByUser(String userName) {
		return humanSolutionsDao.getUnreadableImageIdsTranscribedByUser(userName);
	}

	@Override
	public void updateUserTranscribedImageTables(String userName, Integer imageId, Integer sessionId) {
		humanSolutionsDao.updateUserTranscribedImageTables(userName, imageId, sessionId);
	}

	@Override
	public void saveImageIdsForSession(String userName, List<Integer> finalImageIdsForSession, int sessionId) {
		humanSolutionsDao.saveImageIdsForSession(userName, finalImageIdsForSession, sessionId);
	}

	@Override
	public String getSessionStatus(String userName, int sessionId) {
		return humanSolutionsDao.getSessionStatus(userName, sessionId);
	}

	@Override
	public void updateSessionStatus(String userName, int sessionId, String sessionStatus) {
		humanSolutionsDao.updateSessionStatus(userName, sessionId, sessionStatus);
	}

}
