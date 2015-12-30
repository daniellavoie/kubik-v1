package com.cspinformatique.kubik.server.domain.kos.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cspinformatique.kubik.server.domain.kos.repository.KosNotificationRepository;
import com.cspinformatique.kubik.server.domain.kos.rest.KosTemplate;
import com.cspinformatique.kubik.server.domain.kos.service.KosNotificationService;
import com.cspinformatique.kubik.server.domain.product.service.CategoryService;
import com.cspinformatique.kubik.server.domain.product.service.ProductService;
import com.cspinformatique.kubik.server.model.kos.KosNotification;
import com.cspinformatique.kubik.server.model.kos.KosNotification.Action;
import com.cspinformatique.kubik.server.model.kos.KosNotification.Status;
import com.cspinformatique.kubik.server.model.kos.KosNotification.Type;
import com.cspinformatique.kubik.server.model.product.Product;

@Service
public class KosNotificationServiceImpl implements KosNotificationService {
	private static final Logger LOGGER = LoggerFactory.getLogger(KosNotificationServiceImpl.class);

	@Resource
	private CategoryService categoryService;

	@Resource
	private KosNotificationRepository kosNotificationRepository;

	@Resource
	private KosTemplate kosTemplate;

	@Resource
	private ProductService productService;

	@Value("${kos.notification.enabled}")
	private boolean notificationEnabled;

	@Override
	public KosNotification createNewNotification(int kubikId, Type type, Action action) {
		return save(new KosNotification(0, kubikId, Status.TO_PROCESS, type, action, new Date(), null, null, null));
	}

	@Override
	public Page<KosNotification> findAll(Pageable pageable) {
		return kosNotificationRepository.findAll(pageable);
	}

	@Override
	public KosNotification findOne(int id) {
		return kosNotificationRepository.findOne(id);
	}

	@Override
	public List<KosNotification> findByStatus(Status status, Sort sort) {
		return kosNotificationRepository.findByStatus(status, sort);
	}

	@Override
	public void initialLoad() {
		LOGGER.info("Starting initial load.");

		// Loads all the categories.
		categoryService.findByRootCategory(true).forEach(rootCategory -> save(new KosNotification(0,
				rootCategory.getId(), Status.TO_PROCESS, Type.CATEGORY, Action.UPDATE, new Date(), null, null, null)));

		Pageable pageRequest = new PageRequest(0, 100, Direction.ASC, "id");
		Page<Product> productPage = null;
		do {
			productPage = productService.findAll(pageRequest);

			productPage.getContent().stream()
					.forEach(product -> createNewNotification(product.getId(), Type.PRODUCT, Action.UPDATE));

			pageRequest = pageRequest.next();
		} while (productPage.hasNext());

		LOGGER.info("Initial load request completed.");
	}

	@Override
	@Transactional
	public void process(KosNotification kosNotification) {
		try {
			ResponseEntity<Void> response = kosTemplate.exchange("/kosNotification", HttpMethod.POST, kosNotification,
					Void.class);

			if (!response.getStatusCode().equals(HttpStatus.NO_CONTENT)) {
				throw new RuntimeException("Kos server did not process notification successfully. Status code : "
						+ response.getStatusCode() + ".");
			}

			kosNotification.setStatus(Status.PROCESSED);
		} catch (Exception ex) {
			LOGGER.error("Error while processing broadleaf notification " + kosNotification.getId() + ".", ex);

			kosNotification.setError(ExceptionUtils.getStackTrace(ex));

			kosNotification.setStatus(Status.ERROR);
		} finally {
			save(kosNotification);
		}
	}

	@Override
	public KosNotification save(KosNotification kosNotification) {
		if (notificationEnabled) {
			if (kosNotification.getCreationDate() == null) {
				kosNotification.setCreationDate(new Date());
			}

			if (kosNotification.getStatus().equals(Status.PROCESSED) && kosNotification.getProcessedDate() == null) {
				kosNotification.setProcessedDate(new Date());
			}

			if (kosNotification.getStatus().equals(Status.ERROR) && kosNotification.getErrorDate() == null) {
				kosNotification.setErrorDate(new Date());
			}

			return kosNotificationRepository.save(kosNotification);
		} else
			return kosNotification;

	}
}
