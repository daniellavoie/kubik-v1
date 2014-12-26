package com.cspinformatique.livronet.dilicom.batch;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.batch.item.ItemProcessor;

import com.cspinformatique.livronet.dilicom.model.AvailabilityCodeEnum;
import com.cspinformatique.livronet.dilicom.model.BarcodeType;
import com.cspinformatique.livronet.dilicom.model.DilicomReference;
import com.cspinformatique.livronet.dilicom.model.PriceTypeEnum;
import com.cspinformatique.livronet.dilicom.model.ProductType;
import com.cspinformatique.livronet.dilicom.model.PublisherPresentation;
import com.cspinformatique.livronet.dilicom.model.Reference;
import com.cspinformatique.livronet.dilicom.model.ReturnType;

public class ReferenceItemProcessor implements
		ItemProcessor<DilicomReference, Reference> {

	private DateFormat dateFormat;

	public ReferenceItemProcessor() {
		this.dateFormat = new SimpleDateFormat("yyyyMMdd");
	}

	private Date convertDate(String date) throws ParseException {
		return this.dateFormat.parse(date);
	}

	private Double convertDouble(String string, int decimalPositionFromEnd) {
		int pos = decimalPositionFromEnd;

		return Double.parseDouble(string.substring(0, string.length() - pos)
				+ "."
				+ string.substring(string.length() - pos, string.length()));
	}

	private Double convertPrice(String priceWithoutDot) {
		return this.convertDouble(priceWithoutDot, 3);
	}

	private Double convertRate(String rateWithoutDot) {
		return this.convertDouble(rateWithoutDot, 2);
	}

	@Override
	public Reference process(DilicomReference dilicomReference)
			throws Exception {
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

		return new Reference(
				dilicomReference.getEan13(),
				dilicomReference.getDistributorEan13(),
				this.convertDate(dilicomReference
						.getPriceApplicationOrAvailabilityDate()),
				AvailabilityCodeEnum.valueOf(dilicomReference.getAvailability()), // availability,
				PriceTypeEnum.valueOf(dilicomReference.getPriceType()), // priceType,
				this.convertPrice(dilicomReference.getPriceTaxIn()), // priceTaxIn,
				dilicomReference.getSchoolbook(), // schoolbook,
				this.convertRate(dilicomReference.getTvaRate1()), // tvaRate1,
				this.convertPrice(dilicomReference.getPriceTaxOut1()), // priceTaxOut1,
				this.convertRate(dilicomReference.getTvaRate2()), // tvaRate2,
				this.convertPrice(dilicomReference.getPriceTaxOut2()), // priceTaxOut2,
				this.convertRate(dilicomReference.getTvaRate3()), // tvaRate3,
				this.convertPrice(dilicomReference.getPriceTaxOut3()), // priceTaxOut3,
				ReturnType.valueOf(dilicomReference.getReturnType()), // returnType,
				dilicomReference.getAvailableForOrder(), // availableForOrder,
				this.convertDate(dilicomReference.getDatePublished()), // datePublished,
				ProductType.valueOf(dilicomReference.getProductType()), // productType,
				this.convertDate(dilicomReference.getPublishEndDate()), // publishEndDate,
				dilicomReference.getStandardLabel(), // standardLabel,
				dilicomReference.getCashRegisterLabel(), // cashRegisterLabel,
				dilicomReference.getThickness(), // thickness,
				dilicomReference.getWidth(), // width,
				dilicomReference.getHeight(), // height,
				dilicomReference.getWeight(), // weight,
				dilicomReference.getExtendedLabel(), // extendedLabel,
				dilicomReference.getPublisher(), // publisher,
				dilicomReference.getCollection(), // collection,
				dilicomReference.getAuthor(), // author,
				PublisherPresentation.valueOf(dilicomReference
						.getPublisherPresentation()), // publisherPresentation,
				dilicomReference.getIsbn(), // isbn,
				dilicomReference.getSupplierReference(), // supplierReference,
				dilicomReference.getCollectionReference(), // collectionReference,
				dilicomReference.getTheme(), // theme,
				dilicomReference.getPublisherIsnb(), // publisherIsnb,
				replacesAReference, // remplacesAReference,
				replacedByAReference, // replacedByAReference,
				replacesAReference ? dilicomReference.getLinkedReferenceEan13()
						: null, // replacesEan13,
				replacedByAReference ? dilicomReference
						.getLinkedReferenceEan13() : null, // replacedByEan13,
				dilicomReference.getOrderableByUnit(), // orderableByUnit,
				BarcodeType.valueOf(dilicomReference.getBarcodeType()), // barcodeType,
				mainReference, // mainReference,
				secondaryReference, // secondaryReference,
				dilicomReference.getReferencesCount() // referencesCount
		);
	}
}
