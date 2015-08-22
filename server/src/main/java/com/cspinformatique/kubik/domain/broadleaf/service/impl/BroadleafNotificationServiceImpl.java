package com.cspinformatique.kubik.domain.broadleaf.service.impl;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import com.cspinformatique.broadleaf.client.BroadleafTemplate;
import com.cspinformatique.broadleaf.client.model.BlcProduct;
import com.cspinformatique.kubik.domain.broadleaf.repository.BroadleafNotificationRepository;
import com.cspinformatique.kubik.domain.broadleaf.service.BroadleafNotificationService;
import com.cspinformatique.kubik.domain.product.service.ProductService;
import com.cspinformatique.kubik.model.broadleaf.BroadleafNotification;
import com.cspinformatique.kubik.model.broadleaf.BroadleafNotification.Status;
import com.cspinformatique.kubik.model.product.Product;

@Service
public class BroadleafNotificationServiceImpl implements BroadleafNotificationService {
	private static final Logger LOGGER = LoggerFactory.getLogger(BroadleafNotificationServiceImpl.class);

	@Autowired
	private BroadleafNotificationRepository broadleafNotificationRepository;

	@Autowired
	private BroadleafTemplate broadleafTemplate;

	@Autowired
	private ProductService productService;

	@Value("{broadleaf.url}")
	private String broadleafUrl;

	@Value("${broadleaf.activated}")
	private boolean activated;

	@Override
	public Page<BroadleafNotification> findAll(Pageable pageable) {
		return broadleafNotificationRepository.findAll(pageable);
	}

	@Override
	public BroadleafNotification findOne(int id) {
		return broadleafNotificationRepository.findOne(id);
	}

	@Override
	public List<BroadleafNotification> findByStatus(Status status) {
		return broadleafNotificationRepository.findByStatus(status);
	}

	@Override
	@Transactional
	public void process(BroadleafNotification broadleafNotification) {
		try {
			Product product = productService.findOne(broadleafNotification.getProduct().getId());

			BlcProduct blcProduct = broadleafTemplate.exchange("/catalog/product", HttpMethod.POST, product,
					BlcProduct.class);

			if (product.getBroadleafId() == null) {
				product.setBroadleafId(blcProduct.getId());

				productService.save(product, true);
			}

			broadleafNotification.setStatus(Status.PROCESSED);
		} catch (Exception ex) {
			LOGGER.error("Error while processing broadleaf notification " + broadleafNotification.getId() + ".", ex);

			broadleafNotification.setError(ExceptionUtils.getStackTrace(ex));

			broadleafNotification.setStatus(Status.ERROR);
		} finally {
			save(broadleafNotification);
		}
	}

	@Override
	public BroadleafNotification save(BroadleafNotification broadleafNotification) {
		if (activated) {
			if (broadleafNotification.getCreationDate() == null) {
				broadleafNotification.setCreationDate(new Date());
			}

			if (broadleafNotification.getStatus().equals(Status.PROCESSED)
					&& broadleafNotification.getProcessedDate() == null) {
				broadleafNotification.setProcessedDate(new Date());
			}

			if (broadleafNotification.getStatus().equals(Status.ERROR)
					&& broadleafNotification.getErrorDate() == null) {
				broadleafNotification.setErrorDate(new Date());
			}

			return broadleafNotificationRepository.save(broadleafNotification);
		}else{
			return broadleafNotification;
		}
	}
}
