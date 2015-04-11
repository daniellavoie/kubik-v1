package com.cspinformatique.kubik.domain.dilicom.batch.itemProcessor;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cspinformatique.kubik.domain.dilicom.model.Reference;
import com.cspinformatique.kubik.domain.dilicom.model.ReferenceDTO;
import com.cspinformatique.kubik.domain.product.service.ProductService;

@Component
public class ReferenceItemProcessor implements
		ItemProcessor<ReferenceDTO, Reference> {
	private Logger logger = LoggerFactory
			.getLogger(ReferenceItemProcessor.class);

	@Autowired
	private ProductService productService;

	private DateFormat dateFormat;

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

	@Override
	public Reference process(ReferenceDTO referenceDTO) {
		try {
			if (referenceDTO.getMovementCode() == null
					|| referenceDTO.getMovementCode().equals("S")) {
				return null;
			}

			if (referenceDTO.getEan13() == null
					|| referenceDTO.getDistributorEan13() == null) {
				return null;
			}

			boolean replacesAReference = false;
			boolean replacedByAReference = false;
			if (referenceDTO.getReferenceLinkType() != null) {
				if (referenceDTO.getReferenceLinkType().equals("2")) {
					replacesAReference = true;
				} else if (referenceDTO.getReferenceLinkType().equals("4")) {
					replacedByAReference = true;
				}
			}

			boolean mainReference = true;
			boolean secondaryReference = false;

			if (referenceDTO.getPerishable() != null
					&& referenceDTO.getPerishable().equals("3")) {
				mainReference = false;
				secondaryReference = true;
			}

			boolean importedInKubik = false;
			if (this.productService.getProductIdsCache().contains(referenceDTO.getEan13() + "-"
					+ referenceDTO.getDistributorEan13())) {
				importedInKubik = true;
			}

			return new Reference(null, referenceDTO.getEan13(),
					referenceDTO.getDistributorEan13(),
					convertDate(referenceDTO
							.getPriceApplicationOrAvailabilityDate()),
					referenceDTO.getAvailability(),
					referenceDTO.getPriceType(),
					convertPrice(referenceDTO.getPriceTaxIn()),
					referenceDTO.getSchoolbook(),
					convertRate(referenceDTO.getTvaRate1()),
					convertPrice(referenceDTO.getPriceTaxOut1()),
					convertRate(referenceDTO.getTvaRate2()),
					convertPrice(referenceDTO.getPriceTaxOut2()),
					convertRate(referenceDTO.getTvaRate3()),
					convertPrice(referenceDTO.getPriceTaxOut3()),
					referenceDTO.getReturnType(),
					referenceDTO.getAvailableForOrder(),
					convertDate(referenceDTO.getDatePublished()),
					referenceDTO.getProductType(),
					convertDate(referenceDTO.getPublishEndDate()),
					referenceDTO.getStandardLabel(),
					referenceDTO.getCashRegisterLabel(),
					referenceDTO.getThickness(), referenceDTO.getWidth(),
					referenceDTO.getHeight(), referenceDTO.getWeight(),
					referenceDTO.getExtendedLabel(),
					referenceDTO.getPublisher(), referenceDTO.getCollection(),
					referenceDTO.getAuthor(),
					referenceDTO.getPublisherPresentation(),
					referenceDTO.getIsbn(),
					referenceDTO.getSupplierReference(),
					referenceDTO.getCollectionReference(),
					referenceDTO.getTheme(), referenceDTO.getPublisherIsnb(),
					replacesAReference, replacedByAReference,
					replacesAReference ? referenceDTO.getLinkedReferenceEan13()
							: null,
					replacedByAReference ? referenceDTO
							.getLinkedReferenceEan13() : null,
					referenceDTO.getOrderableByUnit(),
					referenceDTO.getBarcodeType(), mainReference,
					secondaryReference, referenceDTO.getReferencesCount(),
					importedInKubik, null, new Date(), new Date());

			/*
			 * this.publisherIsnb = publisherIsnb; this.replacesAReference =
			 * replacesAReference; this.replacedByAReference =
			 * replacedByAReference; this.replacesEan13 = replacesEan13;
			 * this.replacedByEan13 = replacedByEan13;
			 */
		} catch (Exception ex) {
			logger.error(
					"Error while processing ean13 " + referenceDTO.getEan13()
							+ " from supplier "
							+ referenceDTO.getDistributorEan13() + ".", ex);

			return null;
		}
	}
}
