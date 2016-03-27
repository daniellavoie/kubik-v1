package com.cspinformatique.kubik.server.model.warehouse;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
public class Stocktaking {
	public enum Status {
		IN_PROGRESS, CANCELED, COMPLETED
	}

	private long id;
	private Status status;
	private Date creationDate;
	private Date canceledDate;
	private Date completionDate;
	private List<StocktakingCategory> categories;
	private List<StocktakingDiff> diffs;

	public Stocktaking() {

	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getCanceledDate() {
		return canceledDate;
	}

	public void setCanceledDate(Date canceledDate) {
		this.canceledDate = canceledDate;
	}

	public Date getCompletionDate() {
		return completionDate;
	}

	public void setCompletionDate(Date completionDate) {
		this.completionDate = completionDate;
	}

	@JsonManagedReference
	@OneToMany(mappedBy = "stocktaking", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<StocktakingCategory> getCategories() {
		return categories;
	}

	public void setCategories(List<StocktakingCategory> categories) {
		this.categories = categories;
	}

	@JsonManagedReference
	@OneToMany(cascade = CascadeType.ALL, mappedBy="stocktaking", orphanRemoval = true)
	public List<StocktakingDiff> getDiffs() {
		return diffs;
	}

	public void setDiffs(List<StocktakingDiff> diffs) {
		this.diffs = diffs;
	}
}
