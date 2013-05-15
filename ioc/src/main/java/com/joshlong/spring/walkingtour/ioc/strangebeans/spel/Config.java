package com.joshlong.spring.walkingtour.ioc.strangebeans.spel;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import java.io.File;

/**
 *
 */
@Configuration
public class Config {

    @Value("#{ someOtherBean.dataSource  }")
    private DataSource aRandomValue ;

    @Value("#{systemProperties['user.home']}")
    private String userHome;

    @Value("#{systemProperties['java.io.tmpdir']}")
    public void setIoTmpDir(String tmpDir) {
        this.ioTmpDir = new File(tmpDir);
    }

    

    private File ioTmpDir;

    @PostConstruct
    public void printOutTheUserHome() throws Throwable {
        System.out.println("User Home:" + this.userHome);
        System.out.println("A Random Value: " + this.aRandomValue);
        System.out.println("IO Temporary Directory: " + this.ioTmpDir.getAbsolutePath());
    }
}
