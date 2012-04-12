package org.springsource.examples.sawt.services.batch;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.util.Date;

public class Main {
    static public void main(String[] args) throws Throwable {

        Log log = LogFactory.getLog(Main.class);

        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(Config.class.getPackage().getName());
        applicationContext.start();

        JobLauncher jobLauncher = applicationContext.getBean(JobLauncher.class);
        Job job = applicationContext.getBean("importData", Job.class);

        Resource samplesResource = new ClassPathResource("/sample/a.csv");
        String absFilePath = "file:///" + samplesResource.getFile().getAbsolutePath();

        log.debug("sample file path: " + absFilePath);

        JobParameters params = new JobParametersBuilder()
                .addString("input.file", absFilePath)
                .addDate("date", new Date())
                .toJobParameters();

        JobExecution jobExecution = jobLauncher.run(job, params);
        /// might be 10 days! 
        BatchStatus batchStatus = jobExecution.getStatus();
        while (batchStatus.isRunning()) {
            log.info("Still running...");
            Thread.sleep(1000);
        }
        log.info(String.format("Exit status: %s", jobExecution.getExitStatus().getExitCode()));
        JobInstance jobInstance = jobExecution.getJobInstance();

        log.info(String.format("job instance Id: %d", jobInstance.getId()));


    }
}
