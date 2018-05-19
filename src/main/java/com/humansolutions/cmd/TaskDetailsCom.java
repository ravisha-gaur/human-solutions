package com.humansolutions.cmd;

public class TaskDetailsCom {
	
	private String transcribedText;
	
	private Double accuracyScore;
	
	private String readable;
	
	public String getReadable() {
		return readable;
	}

	public void setReadable(String readable) {
		this.readable = readable;
	}
	
	public Double getAccuracyScore() {
		return accuracyScore;
	}

	public void setAccuracyScore(Double accuracyScore) {
		this.accuracyScore = accuracyScore;
	}

	public String getTranscribedText() {
		return transcribedText;
	}

	public void setTranscribedText(String transcribedText) {
		this.transcribedText = transcribedText;
	}
	
}
