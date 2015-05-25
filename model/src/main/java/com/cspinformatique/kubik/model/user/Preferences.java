package com.cspinformatique.kubik.model.user;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Preferences {
	private int id;
	private PurchaseSessionPreferences purchaseSession;
	
	public Preferences(){
		
	}

	public Preferences(int id, PurchaseSessionPreferences purchaseSession) {
		this.id = id;
		this.purchaseSession = purchaseSession;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	public PurchaseSessionPreferences getPurchaseSession() {
		return purchaseSession;
	}

	public void setPurchaseSession(PurchaseSessionPreferences purchaseSession) {
		this.purchaseSession = purchaseSession;
	}
}
