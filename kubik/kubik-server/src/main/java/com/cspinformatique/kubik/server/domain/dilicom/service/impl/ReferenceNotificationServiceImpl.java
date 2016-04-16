package com.cspinformatique.kubik.server.domain.dilicom.service.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.cspinformatique.kubik.server.domain.dilicom.model.ReferenceNotification;
import com.cspinformatique.kubik.server.domain.dilicom.model.ReferenceNotification.Status;
import com.cspinformatique.kubik.server.domain.dilicom.repository.jpa.ReferenceNotificationRepository;
import com.cspinformatique.kubik.server.domain.dilicom.service.ReferenceNotificationService;
import com.cspinformatique.kubik.server.domain.product.service.ProductService;
import com.cspinformatique.kubik.server.model.product.Product;

@Service
public class ReferenceNotificationServiceImpl implements ReferenceNotificationService {
	@Autowired
	private ReferenceNotificationRepository referenceNotificationRepository;

	@Autowired
	private ProductService productService;

	@Override
	public Long countByStatus(Status status) {
		return referenceNotificationRepository.countByStatus(status);
	}

	@Override
	public void delete(ReferenceNotification referenceNotification) {
		referenceNotificationRepository.delete(referenceNotification);
	}

	public Optional<ReferenceNotification> findByProduct(Product product) {
		return referenceNotificationRepository.findByProduct(product);
	}

	@Override
	public Page<ReferenceNotification> findByStatus(Status status, Pageable pageable) {
		return referenceNotificationRepository.findByStatus(status, pageable);
	}

	@Override
	public ReferenceNotification save(ReferenceNotification referenceNotification) {
		return save(Arrays.asList(new ReferenceNotification[] { referenceNotification })).iterator().next();
	}

	@Override
	public Iterable<ReferenceNotification> save(Iterable<ReferenceNotification> referenceNotifications) {
		for (ReferenceNotification referenceNotification : referenceNotifications) {
			findByProduct(referenceNotification.getProduct())
					.ifPresent(existingReference -> referenceNotification.setId(existingReference.getId()));

			if (referenceNotification.getStatus().equals(Status.NEW)
					&& referenceNotification.getCreationDate() == null) {
				referenceNotification.setCreationDate(new Date());

			}

			if (referenceNotification.getStatus().equals(Status.PROCESSED)
					&& referenceNotification.getProcessedDate() == null) {
				referenceNotification.setProcessedDate(new Date());
			}
		}

		return referenceNotificationRepository.save(referenceNotifications);
	}

	@Transactional
	public void validate(Integer referenceNotificationId, Product product) {
		ReferenceNotification notification = referenceNotificationRepository.findOne(referenceNotificationId);

		if (notification == null) {
			throw new RuntimeException("Reference notification " + referenceNotificationId + " does not exists.");
		}

		notification.setStatus(Status.PROCESSED);

		save(notification);

		productService.save(product);
	}
}
