package org.springsource.examples.sawt.lifecycles.jsr250and330;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class ChatRoom {

    private Log log = LogFactory.getLog(getClass());

    @PostConstruct
    public void onChatRoomCreation() {
        log.info("creating chat room.");
    }
}
