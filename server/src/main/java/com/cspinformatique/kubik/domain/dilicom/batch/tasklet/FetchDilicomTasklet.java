package com.cspinformatique.kubik.domain.dilicom.batch.tasklet;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.net.ftp.FTPFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.integration.file.filters.SimplePatternFileListFilter;
import org.springframework.integration.file.remote.session.SessionFactory;
import org.springframework.integration.file.remote.synchronizer.AbstractInboundFileSynchronizer;
import org.springframework.integration.ftp.filters.FtpSimplePatternFileListFilter;
import org.springframework.integration.ftp.inbound.FtpInboundFileSynchronizer;
import org.springframework.util.Assert;

public class FetchDilicomTasklet implements Tasklet, InitializingBean {
	private static Logger logger = LoggerFactory
			.getLogger(FetchDilicomTasklet.class);

	private File localDirectory;
	private AbstractInboundFileSynchronizer<?> ftpInboundFileSynchronizer;
	private SessionFactory<FTPFile> sessionFactory;
	private boolean autoCreateLocalDirectory = true;
	private boolean deleteLocalFiles = true;
	private String fileNamePattern;
	private String remoteDirectory;

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(sessionFactory,
				"sessionFactory attribute cannot be null");
		Assert.notNull(localDirectory,
				"localDirectory attribute cannot be null");
		Assert.notNull(remoteDirectory,
				"remoteDirectory attribute cannot be null");
		Assert.notNull(fileNamePattern,
				"fileNamePattern attribute cannot be null");

		this.setupFileSynchronizer();

		if (!this.localDirectory.exists()) {
			if (this.autoCreateLocalDirectory) {
				if (logger.isDebugEnabled()) {
					logger.debug("The '" + this.localDirectory
							+ "' directory doesn't exist; Will create.");
				}
				this.localDirectory.mkdirs();
			} else {
				throw new FileNotFoundException(this.localDirectory.getName());
			}
		}
	}

	private void deleteLocalFiles() {
		if (deleteLocalFiles) {
			SimplePatternFileListFilter filter = new SimplePatternFileListFilter(
					fileNamePattern);
			
			List<File> matchingFiles = filter.filterFiles(localDirectory
					.listFiles());
			
			if (CollectionUtils.isNotEmpty(matchingFiles)) {
				for (File file : matchingFiles) {
					FileUtils.deleteQuietly(file);
				}
			}
		}
	}

	@Override
	public RepeatStatus execute(StepContribution contribution,
			ChunkContext chunkContext) throws Exception {
		deleteLocalFiles();

        ftpInboundFileSynchronizer.synchronizeToLocalDirectory(localDirectory);

        SimplePatternFileListFilter filter = new SimplePatternFileListFilter(fileNamePattern);
        
        if(filter.filterFiles(localDirectory.listFiles()).size() == 0){
            logger.info("New notification file not found from Dilicom.");
        }

        return RepeatStatus.FINISHED;
	}

	private void setupFileSynchronizer() {
		ftpInboundFileSynchronizer = new FtpInboundFileSynchronizer(
				sessionFactory);

		((FtpInboundFileSynchronizer) ftpInboundFileSynchronizer)
				.setFilter(new FtpSimplePatternFileListFilter(fileNamePattern));

		ftpInboundFileSynchronizer.setRemoteDirectory(remoteDirectory);
	}

	public File getLocalDirectory() {
		return localDirectory;
	}

	public void setLocalDirectory(File localDirectory) {
		this.localDirectory = localDirectory;
	}

	public AbstractInboundFileSynchronizer<?> getFtpInboundFileSynchronizer() {
		return ftpInboundFileSynchronizer;
	}

	public void setFtpInboundFileSynchronizer(
			AbstractInboundFileSynchronizer<?> ftpInboundFileSynchronizer) {
		this.ftpInboundFileSynchronizer = ftpInboundFileSynchronizer;
	}

	public SessionFactory<FTPFile> getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory<FTPFile> sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public boolean isAutoCreateLocalDirectory() {
		return autoCreateLocalDirectory;
	}

	public void setAutoCreateLocalDirectory(boolean autoCreateLocalDirectory) {
		this.autoCreateLocalDirectory = autoCreateLocalDirectory;
	}

	public boolean isDeleteLocalFiles() {
		return deleteLocalFiles;
	}

	public void setDeleteLocalFiles(boolean deleteLocalFiles) {
		this.deleteLocalFiles = deleteLocalFiles;
	}

	public String getFileNamePattern() {
		return fileNamePattern;
	}

	public void setFileNamePattern(String fileNamePattern) {
		this.fileNamePattern = fileNamePattern;
	}

	public String getRemoteDirectory() {
		return remoteDirectory;
	}

	public void setRemoteDirectory(String remoteDirectory) {
		this.remoteDirectory = remoteDirectory;
	}

}
