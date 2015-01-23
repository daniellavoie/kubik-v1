package com.cspinformatique.kubik.reference.task;

import java.io.File;
import java.util.Date;

import org.apache.commons.net.ftp.FTPFile;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.integration.file.remote.session.SessionFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.cspinformatique.kubik.batch.service.JobExecutionService;
import com.cspinformatique.kubik.config.BatchConfiguration;

@Component
public class FetchDilicomTask implements InitializingBean {
	// private static final Logger logger = LoggerFactory
	// .getLogger(FetchDilicomTask.class);

	@Autowired
	private SessionFactory<FTPFile> dilicomFtpSessionFactory;

	@Autowired
	private JobExecutionService jobExecutionService;

	@Value("${kubik.reference.dilicom.archive.folder}")
	private String archiveDirectoryPath;

	@Value("${kubik.reference.dilicom.references.folder}")
	private String referencesDirectoryPath;
	
	private File archiveDirectory;
	private File referencesDirectory;

	// private AbstractInboundFileSynchronizer<?> ftpInboundFileSynchronizer;
	// private String remoteDirectory = "/pub/O";

	@Override
	public void afterPropertiesSet() throws Exception {
		archiveDirectory = new File(archiveDirectoryPath);
		referencesDirectory = new File(referencesDirectoryPath);

		if(!archiveDirectory.exists()) archiveDirectory.mkdirs();
		if(!referencesDirectory.exists()) referencesDirectory.mkdirs();
	}

	@Scheduled(fixedDelay = 1000 * 60 * 30)
	public void fetchDilicomFiles() {
		// this.archiveFiles();

		// Fetch files from remote FTP server.

		// Process files in local directory.
		for (File file : referencesDirectory.listFiles()) {
			// Fire a new job execution.
			this.jobExecutionService.executeJob(
					BatchConfiguration.IMPORT_DILICOM_REFERENCES_JOB,
					new JobParametersBuilder()
							.addString("filemame", file.getAbsolutePath())
							.addDate("date", new Date()).toJobParameters());
		}

		// this.archiveFiles();
	}

	// private void archiveFiles() {
	// try {
	// for (File file : new File(localDirectory).listFiles()) {
	// logger.info("Archiving file " + file.getAbsolutePath());
	//
	// FileUtils.moveFileToDirectory(file, new File(archiveDirectory),
	// true);
	// }
	// } catch (IOException ioEx) {
	// throw new RuntimeException(ioEx);
	// }
	// }
}
