package org.openrepose.poc.multicast;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CullingService {

    private static Logger LOG = LoggerFactory.getLogger(CullingService.class);

    private Map<String, Node> hostList;

    @Autowired
    public CullingService(Map<String, Node> hostList) {
        this.hostList = hostList;
    }

    @Scheduled(fixedRate = 12000)
    public void cullStaleHosts() {
        LOG.info("Culling stale hosts...");

        hostList.entrySet()
                .stream()
                .filter( entry -> entry.getValue().getLastSeen().getTime() <= (new Date().getTime() - 10000) )
                .collect(Collectors.toSet()) //still a dirty hack
                .forEach( entry -> {
                    LOG.info("Culling {}", entry.getKey());
                    hostList.remove(entry.getKey());
                });

        LOG.info("...culling complete");
    }
}
