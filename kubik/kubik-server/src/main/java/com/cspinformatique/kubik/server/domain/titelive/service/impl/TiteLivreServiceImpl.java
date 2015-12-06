package com.cspinformatique.kubik.server.domain.titelive.service.impl;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.PostConstruct;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.cspinformatique.kubik.server.domain.product.service.ProductService;
import com.cspinformatique.kubik.server.domain.titelive.service.TiteLiveService;
import com.cspinformatique.kubik.server.domain.warehouse.service.ProductInventoryService;
import com.cspinformatique.kubik.server.model.product.Product;

@Service
public class TiteLivreServiceImpl implements TiteLiveService {
	private static final Logger LOGGER = LoggerFactory.getLogger(TiteLivreServiceImpl.class);

	private static final int PIPE_BUFFER = 2048;

	@Autowired
	private ProductInventoryService productInventoryService;

	@Autowired
	private ProductService productService;

	@Value("${titelive.id}")
	private String titeliveId;

	@Value("${kubik.environment}")
	private String environment;

	@Value("${titelive.url}")
	private String ftpUrl;

	@Value("${titelive.username}")
	private String ftpUsername;

	@Value("${titelive.password}")
	private String ftpPassword;

	private DecimalFormat decimalFormat;
	private String remoteFileName;

	@PostConstruct
	public void init() {
		if (environment.equals("development")) {
			this.sendInventoryToTiteLiveServer();
		}
	}

	@Override
	public void sendInventoryToTiteLiveServer() {
		if (remoteFileName == null) {
			remoteFileName = "." + titeliveId + "_ART.asc";
		}

		if (decimalFormat == null) {
			decimalFormat = new DecimalFormat("0.00");
			decimalFormat.setDecimalSeparatorAlwaysShown(false);
		}

		FTPClient ftpClient = new FTPClient();
		try (PipedInputStream inPipe = new PipedInputStream(PIPE_BUFFER)) {
			ftpClient.connect(ftpUrl);

			ftpClient.login(ftpUsername, ftpPassword);

			new Thread(() -> {
				try (PipedOutputStream outPipe = new PipedOutputStream(inPipe)) {
					IOUtils.write(
							"EXTRACTION STOCK DU " + new SimpleDateFormat("dd/MM/YYYY").format(new Date()) + "\r\n",
							outPipe);
					for (Integer productId : productInventoryService.findProductIdWithInventory()) {
						Product product = productService.findOne(productId);

						if (product.getEan13().length() == 13) {
							IOUtils.write(titeliveId + product.getEan13()
									+ String.format("%04d",
											new Double(product.getProductInventory().getQuantityOnHand()).intValue())
									+ StringUtils.leftPad(
											decimalFormat.format(product.getPriceTaxIn()).replace(String.valueOf(
													decimalFormat.getDecimalFormatSymbols().getDecimalSeparator()), ""),
											10, "0")
									+ "\r\n", outPipe);
						}
					}
				} catch (IOException ioEx) {
					LOGGER.error("Error while writing content to FTP Server", ioEx);
				}
			}).start();

			ftpClient.storeFile(remoteFileName, inPipe);

			ftpClient.rename(remoteFileName, titeliveId + "_ART.asc");

			LOGGER.info("Succesfully transfered inventory to Tite");
		} catch (IOException ioEx) {
			throw new RuntimeException(ioEx);
		} finally {
			try {
				ftpClient.disconnect();
			} catch (IOException ioEx) {
				LOGGER.error("Error while closing FTP connection.", ioEx);
			}
		}
	}

}
