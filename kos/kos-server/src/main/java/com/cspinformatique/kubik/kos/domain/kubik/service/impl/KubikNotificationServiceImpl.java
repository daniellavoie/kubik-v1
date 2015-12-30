package com.cspinformatique.kubik.kos.domain.kubik.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cspinformatique.kubik.kos.domain.kubik.repository.KubikNotificationRepository;
import com.cspinformatique.kubik.kos.domain.kubik.service.KubikNotificationService;
import com.cspinformatique.kubik.kos.model.KubikNotification;
import com.cspinformatique.kubik.kos.model.KubikNotification.Action;
import com.cspinformatique.kubik.kos.model.KubikNotification.Status;
import com.cspinformatique.kubik.kos.model.KubikNotification.Type;
import com.cspinformatique.kubik.kos.rest.KubikTemplate;

@Service
public class KubikNotificationServiceImpl implements KubikNotificationService {
	private static final Logger LOGGER = LoggerFactory.getLogger(KubikNotificationServiceImpl.class);

	@Resource
	private KubikNotificationRepository kubikNotificationRepository;

	@Resource
	private KubikTemplate kubikTemplate;

	@Value("${kubik.notification.enabled}")
	private boolean notificationEnabled;

	@Override
	public KubikNotification createNewNotification(long kosId, Type type, Action action) {
		KubikNotification kubikNotification = new KubikNotification();
		
		kubikNotification.setKosId(kosId);
		kubikNotification.setStatus(Status.TO_PROCESS);
		kubikNotification.setAction(action);
		kubikNotification.setType(type);
		kubikNotification.setCreationDate(new Date());
		
		return save(kubikNotification);
	}

	@Override
	public Page<KubikNotification> findAll(Pageable pageable) {
		return kubikNotificationRepository.findAll(pageable);
	}

	@Override
	public KubikNotification findOne(long id) {
		return kubikNotificationRepository.findOne(id);
	}

	@Override
	public List<KubikNotification> findByStatus(Status status, Sort sort) {
		return kubikNotificationRepository.findByStatus(status, sort);
	}

	@Override
	public void initialLoad() {
//		LOGGER.info("Starting initial load.");
//
//		// Loads all the categories.
//		categoryService.findByRootCategory(true).forEach(rootCategory -> save(new KubikNotification(0,
//				rootCategory.getId(), Status.TO_PROCESS, Type.CATEGORY, Action.UPDATE, new Date(), null, null, null)));
//
//		Pageable pageRequest = new PageRequest(0, 100, Direction.ASC, "id");
//		Page<Product> productPage = null;
//		do {
//			productPage = productService.findAll(pageRequest);
//
//			productPage.getContent().stream()
//					.forEach(product -> createNewNotification(product.getId(), Type.PRODUCT, Action.UPDATE));
//
//			pageRequest = pageRequest.next();
//		} while (productPage.hasNext());
//
//		LOGGER.info("Initial load request completed.");
	}

	@Override
	@Transactional
	public void process(KubikNotification kubikNotification) {
		try {
			if (notificationEnabled) {
				ResponseEntity<Void> response = kubikTemplate.exchange("/kubikNotification", HttpMethod.POST,
						kubikNotification, Void.class);

				if (!response.getStatusCode().equals(HttpStatus.NO_CONTENT)) {
					throw new RuntimeException("Kubik server did not process notification successfully. Status code : "
							+ response.getStatusCode() + ".");
				}

				kubikNotification.setStatus(Status.PROCESSED);
			} else {
				kubikNotification.setStatus(Status.SKIPPED);
				kubikNotification.setError("Kubik notification are disabled.");
			}
		} catch (Exception ex) {
			LOGGER.error("Error while processing broadleaf notification " + kubikNotification.getId() + ".", ex);

			kubikNotification.setError(ExceptionUtils.getStackTrace(ex));

			kubikNotification.setStatus(Status.ERROR);
		} finally {
			save(kubikNotification);
		}
	}

	@Override
	public KubikNotification save(KubikNotification kubikNotification) {
		if (notificationEnabled) {
			if (kubikNotification.getCreationDate() == null) {
				kubikNotification.setCreationDate(new Date());
			}

			if (kubikNotification.getStatus().equals(Status.PROCESSED)
					&& kubikNotification.getProcessedDate() == null) {
				kubikNotification.setProcessedDate(new Date());
			}

			if (kubikNotification.getStatus().equals(Status.ERROR) && kubikNotification.getErrorDate() == null) {
				kubikNotification.setErrorDate(new Date());
			}

			return kubikNotificationRepository.save(kubikNotification);
		} else {
			return kubikNotification;
		}
	}
}
