package org.openrepose.poc.multicast;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class RandoMessageService {

    private static Logger LOG = LoggerFactory.getLogger(RandoMessageService.class);

    private Map<String, Node> hostList;
    private String hostName;
    private String port;
    private RestTemplate rest;

    private Random random = new Random();

    @Autowired
    public RandoMessageService(Map<String, Node> hostList, @Value("${HOSTNAME}") String hostName, @Value("${server.port}") String port) {
        this.hostList = hostList;
        this.hostName = hostName;
        this.port = port;
        this.rest = new RestTemplate(Arrays.asList(new StringHttpMessageConverter()));
    }

    @Scheduled(fixedRate = 3000)
    public void sendRandoMessage() {
        List<String> hosts = new ArrayList<>(hostList.keySet());
        if(hosts.size() > 0) {
            String target = hosts.get(random.nextInt(hosts.size()));
            LOG.info("Sending a message to {} ...", target);
            rest.postForLocation("http://" + hostList.get(target).getIp() + ":" + port, hostName);
            LOG.info("...message sent");
        } else {
            LOG.info("Nobody to send messages to yet.");
        }
    }
}
