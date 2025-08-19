package com.project.hishab.model;

import java.util.List;

public class MonthlyReportResponse {

	private boolean result;
	private String message;
	private Integer year;
	private Integer month;
	private Double monthTotal;
	private Double dailyAverage;
	private String topCategory;
	private List<DailyTotalDTO> dailyTotals;

	public boolean isResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public Integer getMonth() {
		return month;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

	public Double getMonthTotal() {
		return monthTotal;
	}

	public void setMonthTotal(Double monthTotal) {
		this.monthTotal = monthTotal;
	}

	public Double getDailyAverage() {
		return dailyAverage;
	}

	public void setDailyAverage(Double dailyAverage) {
		this.dailyAverage = dailyAverage;
	}

	public String getTopCategory() {
		return topCategory;
	}

	public void setTopCategory(String topCategory) {
		this.topCategory = topCategory;
	}

	public List<DailyTotalDTO> getDailyTotals() {
		return dailyTotals;
	}

	public void setDailyTotals(List<DailyTotalDTO> dailyTotals) {
		this.dailyTotals = dailyTotals;
	}
}


