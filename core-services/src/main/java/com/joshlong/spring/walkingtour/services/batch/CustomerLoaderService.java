package com.joshlong.spring.walkingtour.services.batch;

import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.inject.Inject;
import java.io.File;
import java.util.Date;


/**
 * Service facade on top of the Spring Batch {@link Job job}.
 * <p/>
 * Hides the setup with {@link JobLauncher job launcher} and setting up the peculiar parameters.
 *
 * @author Josh Long
 */
@Service
public class CustomerLoaderService {

    private Job job;
    private JobLauncher jobLauncher;

    @Inject
    public CustomerLoaderService(JobLauncher jobLauncher, Job job) {
        this.job = job;
        this.jobLauncher = jobLauncher;
    }

    public void loadCustomersFrom(File file) throws Throwable {

        Assert.notNull(file, "the file parameter can't be null");

        JobParameters params = new JobParametersBuilder()
                .addString("input.file", "file:///" + file.getAbsolutePath())
                .addDate("date", new Date())
                .toJobParameters();

        JobExecution jobExecution = jobLauncher.run(job, params);

        ExitStatus exitStatus = jobExecution.getExitStatus();

        boolean everythingAlright = ExitStatus.COMPLETED.equals(exitStatus);

        Assert.isTrue(everythingAlright,
                String.format("Couldn't complete the batch job. The exit " +
                        "code is ('%s') and exit description is ('%s') ",
                        exitStatus.getExitCode(), exitStatus.getExitDescription()));
    }

}
