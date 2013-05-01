package com.joshlong.spring.walkingtour.services.integration;

import org.apache.commons.logging.*;
import org.springframework.batch.core.*;
import org.springframework.batch.integration.launch.JobLaunchRequest;
import org.springframework.integration.annotation.*;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.io.File;
import java.util.*;

/**
 * Simple transformer to take a file detected by the inbound {@link java.io.File file} adapter and transform it to a
 * {@link org.springframework.batch.integration.launch.JobLaunchRequest job launch request}.
 *
 * @author Josh Long
 */
@Component
public class FileBatchJobRequestTransformer {

    private Log log = LogFactory.getLog(FileBatchJobRequestTransformer.class);

    @Inject
    private Job job;

    @Transformer
    public JobLaunchRequest fromFile(@Headers Map<String, Object> map, @Payload File file) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug(String.format("a new file has appeared (%s).", file.getAbsolutePath()));
            for (String k : map.keySet()) {
                log.debug(String.format("header %s = %s", k, map.get(k)));
            }
        }
        JobParameters parms = new JobParametersBuilder()
                .addDate("date", new Date())
                .addString("input.file", "file:///" + file.getAbsolutePath())
                .toJobParameters();

        return new JobLaunchRequest(this.job, parms);

    }
}
