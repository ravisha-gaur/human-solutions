package com.humansolutions.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.humansolutions.dom.BlobDataDom;
import com.humansolutions.dom.TaskDetailsDom;
import com.humansolutions.dom.UserDom;

public class HumanSolutionsDaoImpl implements HumanSolutionsDao{
	
	private DataSource dataSource;

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	@SuppressWarnings("serial")
	private final List<String> listOfTreatments = new ArrayList<String>(){{
	    add("Baseline");
	    add("Msg_once");
	    add("Msg_low");
	    add("Msg_high");
	    add("Monitoring");
	}};
	

	@Override
	public BlobDataDom getImage(int imageId) {
		
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		
		String tableName = "";
		
		if(imageId > 0 && imageId <= 150)
			tableName = "readable_img_tbl";
		if(imageId > 150 && imageId <= 200)
			tableName = "hardly_readable_img_tbl";
		if(imageId > 200 && imageId <= 255)
			tableName = "unreadable_img_tbl";
		
		String query = "select * from " + tableName + " where image_id = ?";
		
		BlobDataDom imageObject = jdbcTemplate.queryForObject(query, new ImageRowMapper(), new Object[]{ imageId });
		
		return imageObject;
		
	}

	private class ImageRowMapper implements RowMapper<BlobDataDom> {
		@Override
		public BlobDataDom mapRow(ResultSet rs, int rowNum) throws SQLException {
			BlobDataDom imageObject = new BlobDataDom();
			imageObject.setImageId(rs.getInt("image_id"));
			imageObject.setImage(rs.getBlob("img"));
			return imageObject;
		}

	}

	@Override
	public void updateSessionNumber(String userName, int todaysSessionId) {
		
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		
		String query = "update user set session_count = ? where username = ?";
		
		jdbcTemplate.update(query, new Object[]{ todaysSessionId, userName });
	}

	@Override
	public UserDom getUserDetails(String userName) {
		
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

		String query = "select * from user where username = ?";
		
		UserDom user = jdbcTemplate.queryForObject(query,new UserRowMapper(), new Object[]{userName});
		
		return user;
	}

	
	private class UserRowMapper implements RowMapper<UserDom>{

		@Override
		public UserDom mapRow(ResultSet rs, int rowNum) throws SQLException {
			UserDom user = new UserDom();
			user.setId(rs.getInt("id"));
			user.setSessionNumber(rs.getInt("session_count"));
			user.setEarnings(rs.getDouble("total_earnings"));
			user.setAuthority(rs.getString("authority"));
			user.setEnabled(rs.getInt("enabled"));
			user.setUsername(rs.getString("username"));
			user.setPassword(rs.getString("password"));
			user.setTreatmentMsgType(rs.getString("treatment_msg_type"));
			user.setIsFirstLogin(rs.getInt("is_first_login"));
			user.setStartDate(rs.getDate("start_date"));
			return user;
		}
		
	}


	@Override
	public List<UserDom> getEarningsPerSession(String userName) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		
		String query = "select * from earnings where username = ?";
		
		List<UserDom> earningsList = jdbcTemplate.query(query, new EarningsMapper(), new Object[]{userName});
		
		return earningsList;
	}
	
	private class EarningsMapper implements RowMapper<UserDom>{

		@Override
		public UserDom mapRow(ResultSet rs, int rowNum) throws SQLException {
			UserDom userDom = new UserDom();
			userDom.setUsername(rs.getString("userName"));
			userDom.setSessionNumber(rs.getInt("session_number"));
			userDom.setEarnings(rs.getDouble("earning_per_session"));
			userDom.setSessionStatus(rs.getString("session_status"));
			return userDom;
		}
		
	}

	@Override
	public List<Integer> getSessionImageIds(String userName, int todaysSessionId) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		
		String query = "select image_id from session_image_ids_tbl where username = ? and session_number = ?";
		
		List<Integer> sessionImageDetails = jdbcTemplate.queryForList(query, new Object[]{ userName, todaysSessionId }, Integer.class);
		
		return sessionImageDetails;
	}


	@Override
	public String getSolutionTranscribedText(Integer imageId) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		
		String query = "select solution_text from solution_tbl where image_id = ?";
		String solution = "";
		try{
			solution = jdbcTemplate.queryForObject(query, new Object[] { imageId }, String.class);
		}
		catch(Exception e) {
			System.out.println("Solution does not exist!");
		}
		return solution;
	}

	@Override
	public void saveTranscribedTextAndScoreObtained(UserDom user, TaskDetailsDom taskDetailsDom) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		
		String query = "insert into transcribed_text_and_score_details values(?, ?, ?, ?, ?, ?)";
		
		try{
			jdbcTemplate.update(query, new Object[]{user.getUsername(), user.getSessionNumber(), user.getImageId(), 
												taskDetailsDom.getTranscribedText(), taskDetailsDom.getAccuracyScore(), taskDetailsDom.getReadable()});
		}
		
		catch(Exception e) {
			System.out.println("Duplicate Entry!");
		}
	}

	@Override
	public void updateEarningsDetails(UserDom user) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		
		String query1 = "update earnings set earning_per_session = '5.0' where username = ? and session_number = ?";
		
		String getTotalEarningsQuery = "select total_earnings from user where username = ?";
		Double totalEarnings = jdbcTemplate.queryForObject(getTotalEarningsQuery, new Object[] { user.getUsername() }, Double.class);
		totalEarnings += 5.0;
		
		String query2 = "update user set total_earnings = ? where username = ?";
		
		jdbcTemplate.update(query1, new Object[]{ user.getUsername(), user.getSessionNumber() });
		jdbcTemplate.update(query2, new Object[]{ totalEarnings, user.getUsername() });
	}

	@Override
	public void updateFirstLoginDetails(String userName) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		
		String treatmentMsgType = "Baseline";
		
		String queryForUserId = "select id from user where username = ?";
		
		int userId = jdbcTemplate.queryForObject(queryForUserId, new Object[] { userName }, Integer.class);
		
		if(userId % listOfTreatments.size() == 0)
			treatmentMsgType = listOfTreatments.get(4);
		else
			treatmentMsgType = listOfTreatments.get((userId % listOfTreatments.size()) - 1);
			
		
		String query = "update user set is_first_login = 1, start_date = ?, treatment_msg_type = ? where username = ?";
		
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.HOUR, 1);
	    DateFormat gmtFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	    TimeZone gmtTime = TimeZone.getTimeZone("GMT");
	    gmtFormat.setTimeZone(gmtTime);
	    
	    String dateString = gmtFormat.format(calendar.getTime());
	    
		try {
			Date loginDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(dateString);
			jdbcTemplate.update(query, new Object[]{ loginDate, treatmentMsgType, userName });
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		String insertEarningDetails = "insert into earnings values(?, ?, 0, 'Incomplete')";
		
		for(int i = 1; i < 8; i++){
			jdbcTemplate.update(insertEarningDetails, new Object[]{ userName, i });
		}
		
	}

	@Override
	public List<Integer> getReadableImageIds() {
		
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		
		String query = "select image_id from readable_img_tbl";
		
		List<Integer> readableImageIds = jdbcTemplate.queryForList(query, Integer.class);
		
		return readableImageIds;
	}

	@Override
	public List<Integer> getHardlyReadableImageIds() {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		
		String query = "select image_id from hardly_readable_img_tbl";
		
		List<Integer> hardlyReadableImageIds = jdbcTemplate.queryForList(query, Integer.class);
		
		return hardlyReadableImageIds;
	}

	@Override
	public List<Integer> getUnreadableImageIds() {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		
		String query = "select image_id from unreadable_img_tbl";
		
		List<Integer> UnreadableImageIds = jdbcTemplate.queryForList(query, Integer.class);
		
		return UnreadableImageIds;
	}

	@Override
	public List<Integer> getReadableImageIdsTranscribedByUser(String userName) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		
		String query = "select image_id from user_transcribed_readable_images where username = ?";
		
		List<Integer> readableImageIdsTranscribedByUser = jdbcTemplate.queryForList(query, new Object[]{ userName }, Integer.class);
		
		return readableImageIdsTranscribedByUser;
	}

	@Override
	public List<Integer> getHardlyReadableImageIdsTranscribedByUser(String userName) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		
		String query = "select image_id from user_transcribed_hardly_readable_images where username = ?";
		
		List<Integer> hardlyReadableImageIdsTranscribedByUser = jdbcTemplate.queryForList(query, new Object[]{ userName }, Integer.class);
		
		return hardlyReadableImageIdsTranscribedByUser;
	}

	@Override
	public List<Integer> getUnreadableImageIdsTranscribedByUser(String userName) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		
		String query = "select image_id from user_transcribed_unreadable_images where username = ?";
		
		List<Integer> unreadableImageIdsTranscribedByUser = jdbcTemplate.queryForList(query, new Object[]{ userName }, Integer.class);
		
		return unreadableImageIdsTranscribedByUser;
	}

	@Override
	public void updateUserTranscribedImageTables(String userName, Integer imageId, Integer sessionId) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		
		String tableName = "";
		
		if(imageId > 0 && imageId <= 150)
			tableName = "user_transcribed_readable_images";
		if(imageId > 150 && imageId <= 200)
			tableName = "user_transcribed_hardly_readable_images";
		if(imageId > 200 && imageId <= 255)
			tableName = "user_transcribed_unreadable_images";
		
		String query = "insert into " + tableName + " (username, image_id, session_id) values (?, ?, ?)";
		
		String deleteFromImageTable = "delete from session_image_ids_tbl where username = ? and image_id = ? and session_number = ?";
		
		jdbcTemplate.update(deleteFromImageTable, userName, imageId, sessionId);
		
		try {
			jdbcTemplate.update(query, new Object[] { userName, imageId, sessionId });
		}
		catch (Exception e) {
			
			System.out.println("Already Transcribed!");
		}
		
	}

	@Override
	public void saveImageIdsForSession(String userName, List<Integer> finalImageIdsForSession, int sessionId) {
		
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		
		String query = "insert into session_image_ids_tbl (username, image_id, session_number) values (?, ?, ?)";
		
		for (Integer imageId : finalImageIdsForSession) {
			
			jdbcTemplate.update(query, new Object[] { userName, imageId, sessionId });
			
		}
	}

	@Override
	public String getSessionStatus(String userName, int sessionId) {
		
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		
		String query = "select session_status from earnings where username = ? and session_number = ?";
		
		String sessionStatus = jdbcTemplate.queryForObject(query, new Object[] { userName, sessionId }, String.class);
		
		return sessionStatus;
	}

	@Override
	public void updateSessionStatus(String userName, int sessionId, String sessionStatus) {
		
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		
		String query = "update earnings set session_status = ? where username = ? and session_number = ?";
		
		jdbcTemplate.update(query, new Object[] { sessionStatus, userName, sessionId });
		
	}
	
}

