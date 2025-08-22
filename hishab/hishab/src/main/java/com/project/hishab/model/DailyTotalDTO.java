package com.project.hishab.model;

public class DailyTotalDTO {

	private String date;
	private Double total;
	private String itemsWithCost;

	public DailyTotalDTO() {}

	public DailyTotalDTO(String date, Double total, String itemsWithCost) {
		this.date = date;
		this.total = total;
		this.itemsWithCost = itemsWithCost;
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

	public String getItemsWithCost() {
		return itemsWithCost;
	}

	public void setItemsWithCost(String itemsWithCost) {
		this.itemsWithCost = itemsWithCost;
	}
}


