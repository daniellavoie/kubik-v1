package com.cspinformatique.kubik.server.domain.dilicom.service.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.transaction.Transactional;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.cspinformatique.kubik.server.domain.dilicom.converter.DilicomOrderStringConverter;
import com.cspinformatique.kubik.server.domain.dilicom.exception.DilicomOrderProcessTimeoutException;
import com.cspinformatique.kubik.server.domain.dilicom.repository.jpa.DilicomOrderRepository;
import com.cspinformatique.kubik.server.domain.dilicom.service.DilicomOrderService;
import com.cspinformatique.kubik.server.model.dilicom.DilicomOrder;
import com.cspinformatique.kubik.server.model.dilicom.DilicomOrder.Status;

@Service
@ConditionalOnProperty(name = "kubik.dilicom.ftp.enabled")
public class DilicomOrderServiceImpl implements DilicomOrderService {
	private static final Logger LOGGER = LoggerFactory.getLogger(DilicomOrderServiceImpl.class);
	@Autowired
	private DilicomOrderRepository dilicomOrderRepository;

	@Autowired
	private DilicomOrderStringConverter dilicomOrderStringConverter;

	@Value("${kubik.dilicom.ftp.username}")
	private String ftpUsername;

	@Value("${kubik.dilicom.ftp.url}")
	private String ftpUrl;

	@Value("${kubik.dilicom.ftp.password}")
	private String ftpPassword;

	@Value("${kubik.dilicom.ftp.in.path}")
	private String remoteDirectory;

	private DateFormat fileDateFormat;

	public DilicomOrderServiceImpl() {
		this.fileDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
	}

	@Override
	public Page<DilicomOrder> findAll(Pageable pageable) {
		return this.dilicomOrderRepository.findAll(pageable);
	}

	@Override
	@Transactional
	public DilicomOrder save(DilicomOrder dilicomOrder) {
		if (dilicomOrder.getStatus().equals(Status.SHIPPED) && dilicomOrder.getValidationDate() == null) {
			dilicomOrder.setValidationDate(new Date());
		}

		return this.dilicomOrderRepository.save(dilicomOrder);
	}

	private void sendDilicomOrder(DilicomOrder dilicomOrder) {
		try {
			String filename = this.fileDateFormat.format(new Date());

			dilicomOrder.setRemoteFilename("." + filename);

			dilicomOrder.setRemoteFileContent(
					this.dilicomOrderStringConverter.convertToString(dilicomOrder.getPurchaseOrder()));

			String remoteFileName = remoteDirectory + "/" + dilicomOrder.getRemoteFilename();

			FTPClient client = new FTPClient();
			try {
				client.connect(ftpUrl);

				client.login(ftpUsername, ftpPassword);

				client.storeFile(remoteFileName,
						new ByteArrayInputStream(dilicomOrder.getRemoteFileContent().getBytes()));

				int dotIndex = remoteFileName.lastIndexOf(".");

				String renamedFilename = remoteFileName.substring(0, dotIndex) + remoteFileName.substring(dotIndex + 1);

				client.rename(remoteFileName, renamedFilename);

				dilicomOrder.setTransferDate(new Date());
				dilicomOrder.setStatus(Status.TRANSFERED);

				dilicomOrder.setRemoteFilename(renamedFilename);

				this.save(dilicomOrder);

				boolean processed;
				DateTime timeoutTime = new DateTime().plusMinutes(5);
				do {
					processed = true;
					for (FTPFile ftpFile : client.listFiles(remoteDirectory)) {
						if ((remoteDirectory + "/" + ftpFile.getName()).equals(dilicomOrder.getRemoteFilename())) {
							processed = false;
							break;
						}
					}

					if (!processed && new DateTime().isAfter(timeoutTime)) {
						throw new DilicomOrderProcessTimeoutException("Order file " + dilicomOrder.getRemoteFilename()
								+ " has not been process by Dilicom after 5 minutes");
					} else if (!processed) {
						Thread.sleep(1000);
					}

				} while (!processed);

				dilicomOrder.setProcessDate(new Date());
				dilicomOrder.setStatus(Status.PROCESSED);

				this.save(dilicomOrder);
			} catch (IOException | InterruptedException ex) {
				throw new RuntimeException(ex);
			} finally {
				try {
					client.disconnect();
				} catch (IOException ioEx) {
					LOGGER.error("Error while closing FTP connection.", ioEx);
				}
			}
		} catch (RuntimeException runtimeEx) {
			LOGGER.error("Error while processing dilicom order " + dilicomOrder.getId(), runtimeEx);

			dilicomOrder.setStatus(Status.ERROR);
			dilicomOrder.setErrorMessage(ExceptionUtils.getStackTrace(runtimeEx));

			this.save(dilicomOrder);
		}
	}

	@Override
	public void sendPendingDilicomOrders() {
		for (DilicomOrder dilicomOrder : this.dilicomOrderRepository.findByStatus(Status.PENDING)) {
			this.sendDilicomOrder(dilicomOrder);
		}
	}

}
