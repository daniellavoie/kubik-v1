package com.cspinformatique.kubik.domain.dilicom.task;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.commons.net.ftp.FTPFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.integration.file.remote.session.SessionFactory;
import org.springframework.integration.ftp.filters.FtpSimplePatternFileListFilter;
import org.springframework.integration.ftp.inbound.FtpInboundFileSynchronizer;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.cspinformatique.kubik.config.BatchConfiguration;
import com.cspinformatique.kubik.domain.dilicom.batch.service.JobExecutionService;

@Component
public class FetchDilicomTask implements InitializingBean {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(FetchDilicomTask.class);

	@Autowired
	private SessionFactory<FTPFile> dilicomFtpSessionFactory;

	@Autowired
	private JobExecutionService jobExecutionService;

	@Value("${kubik.dilicom.archive.folder}")
	private String archiveDirectoryPath;

	@Value("${kubik.dilicom.ftp.clean.files}")
	private boolean cleanFiles;

	@Value("${kubik.dilicom.references.folder}")
	private String referencesDirectoryPath;

	@Value("${kubik.dilicom.ftp.out.path}")
	private String remoteDirectoryPath;

	private File archiveDirectory;
	private FtpInboundFileSynchronizer ftpInboundFileSynchronizer;
	private File workDirectory;

	@Override
	public void afterPropertiesSet() throws Exception {
		this.archiveDirectory = new File(this.archiveDirectoryPath);
		this.workDirectory = new File(this.referencesDirectoryPath);

		if (!archiveDirectory.exists())
			archiveDirectory.mkdirs();
		if (!workDirectory.exists())
			workDirectory.mkdirs();

		this.ftpInboundFileSynchronizer = new FtpInboundFileSynchronizer(
				dilicomFtpSessionFactory);
		this.ftpInboundFileSynchronizer
				.setRemoteDirectory(this.remoteDirectoryPath);

		ftpInboundFileSynchronizer
				.setFilter(new FtpSimplePatternFileListFilter("DIF*"));
		this.ftpInboundFileSynchronizer.setDeleteRemoteFiles(cleanFiles);
	}

	@Scheduled(fixedDelay = 1000 * 60 * 30)
	public void fetchDilicomFiles() {
		try {
			// Fetch files from remote FTP server.
			this.ftpInboundFileSynchronizer
					.synchronizeToLocalDirectory(workDirectory);
		} catch (Exception ex) {
			LOGGER.error(
					"Error while retreiving references files from Dilicom FTP server.",
					ex);
		}

		// Process files in local directory.
		File[] files = workDirectory.listFiles();
		Arrays.sort(files);
		for (File file : files) {
			// Fire a new job execution.
			this.jobExecutionService.executeJob(
					BatchConfiguration.IMPORT_DILICOM_REFERENCES_JOB,
					new JobParametersBuilder()
							.addString("filemame", file.getAbsolutePath())
							.addDate("date", new Date()).toJobParameters());

			this.archiveFile(file);
		}
	}

	private void archiveFile(File file) {
		try {
			LOGGER.info("Archiving file " + file.getAbsolutePath());

			new File(archiveDirectoryPath + "/" + file.getName()).delete();
			
			FileUtils.moveFileToDirectory(file, archiveDirectory, true);
		} catch (IOException ioEx) {
			LOGGER.error("Error while arhiving file " + file.getAbsolutePath()
					+ ".", ioEx);
		}
	}
}
