package com.cspinformatique.kubik.model.user;

import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.cspinformatique.kubik.model.purchase.PurchaseSession.Status;

@Entity
public class PurchaseSessionPreferences {
	private int id;
	private List<Status> status;

	public PurchaseSessionPreferences() {

	}

	public PurchaseSessionPreferences(int id, List<Status> status) {
		this.id = id;
		this.status = status;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@ElementCollection
	public List<Status> getStatus() {
		return status;
	}

	public void setStatus(List<Status> status) {
		this.status = status;
	}
}
