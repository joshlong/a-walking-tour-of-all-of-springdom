package org.springsource.examples.sawt.spel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Date;

@Component
public class SpelSource {

    private Log log = LogFactory.getLog(getClass());

    @Value("#{ utilities.buildRomanAlphabetOnThisComputer()}")
    private char[] alphabet;

    @Value("#{ systemProperties['user.home'] }")
    private String userHome;

    @Value("#{ T(System).currentTimeMillis()  }")
    private long currentTime;

    @PostConstruct
    public void selfDiagnostic() throws Exception {
        log.info("alphabet: " + new String(this.alphabet));
        log.info("the current time is " + new Date(this.currentTime));
        log.info("the user's home directory is " + this.userHome);
    }
}
