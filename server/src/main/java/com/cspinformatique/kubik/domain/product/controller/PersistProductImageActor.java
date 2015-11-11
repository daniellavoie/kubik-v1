package com.cspinformatique.kubik.domain.product.controller;

import com.cspinformatique.kubik.domain.product.message.PersistProductImagesMessage;
import com.cspinformatique.kubik.domain.product.service.ProductImageService;

import akka.actor.UntypedActor;

public class PersistProductImageActor extends UntypedActor {
	private ProductImageService productImageService;

	public PersistProductImageActor(ProductImageService productImageService) {
		this.productImageService = productImageService;
	}

	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof PersistProductImagesMessage) {
			productImageService.persistProductImagesToAws(((PersistProductImagesMessage) message).getProductId());
		}
	}
}
