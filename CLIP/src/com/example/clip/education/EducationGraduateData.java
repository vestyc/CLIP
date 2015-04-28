package com.example.clip.education;

import com.example.clip.R.*;

public class EducationGraduateData {

	private String schoolName;
	private String graduateType;
	
	public EducationGraduateData() {
		schoolName = new String();
		graduateType = new String();
	}
	
	public EducationGraduateData(String schoolName, String graduateType) {
		this.schoolName = schoolName;
		this.graduateType = graduateType;
	}
	
	public String getName() {
		return schoolName;
	}
	
	public String getType() {
		return graduateType;
	}
	
	public void setName(String name) {
		this.schoolName = name;
	}
	
	public void setType(String type) {
		this.graduateType = type;
	}
	
	@Override
	public String toString() {
		return schoolName;
	}
}
