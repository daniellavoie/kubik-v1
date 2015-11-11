package com.cspinformatique.kubik.onlinesales.service;

import com.cspinformatique.kubik.model.kos.KosNotification;
import com.cspinformatique.kubik.onlinesales.model.Category;

public interface CategoryService {
	Category findByKubikId(int kubikId);

	void processKosNotification(KosNotification kosNotification);
}
