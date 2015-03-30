package com.cspinformatique.kubik.batch.itemProcessor;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.cspinformatique.kubik.domain.product.service.ProductService;
import com.cspinformatique.kubik.domain.reference.model.DilicomReference;
import com.cspinformatique.kubik.domain.reference.model.Reference;
import com.cspinformatique.kubik.model.product.Product;

@Component
public class ReferenceItemProcessor implements
		ItemProcessor<DilicomReference, Reference> {
	private Logger logger = LoggerFactory
			.getLogger(ReferenceItemProcessor.class);

	@Autowired
	private ProductService productService;

	private DateFormat dateFormat;
	private Set<String> productIds;

	public ReferenceItemProcessor() {
		this.dateFormat = new SimpleDateFormat("yyyyMMdd");
	}

	private Date convertDate(String date) throws ParseException {
		if (date == null || date.equals("")) {
			return null;
		}

		return this.dateFormat.parse(date);
	}

	private Double convertDouble(String string, int decimalPositionFromEnd) {
		if (string == null || string.equals("")) {
			return null;
		}

		try {
			int pos = decimalPositionFromEnd;

			return Double
					.parseDouble(string.substring(0, string.length() - pos)
							+ "."
							+ string.substring(string.length() - pos,
									string.length()));
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	private Double convertPrice(String priceWithoutDot) {
		return this.convertDouble(priceWithoutDot, 3);
	}

	private Double convertRate(String rateWithoutDot) {
		return this.convertDouble(rateWithoutDot, 2);
	}

	@PostConstruct
	public void init() {
		logger.info("Caching products.");

		this.productIds = new HashSet<String>();

		Pageable pageRequest = new PageRequest(0, 200);
		Page<Product> page = null;
		do {
			page = productService.findAll(pageRequest);

			for (Product product : page.getContent()) {
				this.productIds.add(product.getEan13() + "-"
						+ product.getSupplier().getEan13());
			}

			pageRequest = pageRequest.next();
		} while (page != null && page.getContent().size() != 0);

		logger.info("Products product completed.");
	}

	@Override
	public Reference process(DilicomReference dilicomReference) {
		try {
			if (dilicomReference.getMovementCode() == null
					|| dilicomReference.getMovementCode().equals("S")) {
				return null;
			}

			if (dilicomReference.getEan13() == null
					|| dilicomReference.getDistributorEan13() == null) {
				return null;
			}

			boolean replacesAReference = false;
			boolean replacedByAReference = false;
			if (dilicomReference.getReferenceLinkType() != null) {
				if (dilicomReference.getReferenceLinkType().equals("2")) {
					replacesAReference = true;
				} else if (dilicomReference.getReferenceLinkType().equals("4")) {
					replacedByAReference = true;
				}
			}

			boolean mainReference = true;
			boolean secondaryReference = false;

			if (dilicomReference.getPerishable() != null
					&& dilicomReference.getPerishable().equals("3")) {
				mainReference = false;
				secondaryReference = true;
			}

			boolean importedInKubik = false;
			if (this.productIds.contains(dilicomReference.getEan13() + "-"
					+ dilicomReference.getDistributorEan13())) {
				importedInKubik = true;
			}

			return new Reference(null, dilicomReference.getEan13(),
					dilicomReference.getDistributorEan13(),
					convertDate(dilicomReference
							.getPriceApplicationOrAvailabilityDate()),
					dilicomReference.getAvailability(),
					dilicomReference.getPriceType(),
					convertPrice(dilicomReference.getPriceTaxIn()),
					dilicomReference.getSchoolbook(),
					convertRate(dilicomReference.getTvaRate1()),
					convertPrice(dilicomReference.getPriceTaxOut1()),
					convertRate(dilicomReference.getTvaRate2()),
					convertPrice(dilicomReference.getPriceTaxOut2()),
					convertRate(dilicomReference.getTvaRate3()),
					convertPrice(dilicomReference.getPriceTaxOut3()),
					dilicomReference.getReturnType(),
					dilicomReference.getAvailableForOrder(),
					convertDate(dilicomReference.getDatePublished()),
					dilicomReference.getProductType(),
					convertDate(dilicomReference.getPublishEndDate()),
					dilicomReference.getStandardLabel(),
					dilicomReference.getCashRegisterLabel(),
					dilicomReference.getThickness(),
					dilicomReference.getWidth(), dilicomReference.getHeight(),
					dilicomReference.getWeight(),
					dilicomReference.getExtendedLabel(),
					dilicomReference.getPublisher(),
					dilicomReference.getCollection(),
					dilicomReference.getAuthor(),
					dilicomReference.getPublisherPresentation(),
					dilicomReference.getIsbn(),
					dilicomReference.getSupplierReference(),
					dilicomReference.getCollectionReference(),
					dilicomReference.getTheme(),
					dilicomReference.getPublisherIsnb(), replacesAReference,
					replacedByAReference,
					replacesAReference ? dilicomReference
							.getLinkedReferenceEan13() : null,
					replacedByAReference ? dilicomReference
							.getLinkedReferenceEan13() : null,
					dilicomReference.getOrderableByUnit(),
					dilicomReference.getBarcodeType(), mainReference,
					secondaryReference, dilicomReference.getReferencesCount(),
					importedInKubik, null, new Date(), new Date());

			/*
			 * this.publisherIsnb = publisherIsnb; this.replacesAReference =
			 * replacesAReference; this.replacedByAReference =
			 * replacedByAReference; this.replacesEan13 = replacesEan13;
			 * this.replacedByEan13 = replacedByEan13;
			 */
		} catch (Exception ex) {
			logger.error(
					"Error while processing ean13 "
							+ dilicomReference.getEan13() + " from supplier "
							+ dilicomReference.getDistributorEan13() + ".", ex);

			return null;
		}
	}
}
