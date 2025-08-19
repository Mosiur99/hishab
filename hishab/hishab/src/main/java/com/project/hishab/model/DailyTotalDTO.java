package com.project.hishab.model;

public class DailyTotalDTO {

	private String date;
	private Double total;

	public DailyTotalDTO() {}

	public DailyTotalDTO(String date, Double total) {
		this.date = date;
		this.total = total;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}
}


