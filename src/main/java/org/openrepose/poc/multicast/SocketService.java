package org.openrepose.poc.multicast;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Date;
import java.util.Map;

@Service
public class SocketService {

    private static Logger LOG = LoggerFactory.getLogger(SocketService.class);

    private Map<String, Node> hostList;
    private String hostName;
    private MulticastSocket socket;
    private InetAddress group;
    private Thread listeningThread;
    private boolean shouldRun = true;

    @Autowired
    public void ListeningService(Map<String, Node> hostList, @Value("${HOSTNAME}") String hostName) {
        this.hostList = hostList;
        this.hostName = hostName;
    }

    @PostConstruct
    public void setup() {
        try {
            LOG.info("Opening the multicast socket...");
            socket = new MulticastSocket(9999);
            group = InetAddress.getByName("224.0.0.1");
            socket.joinGroup(group);
            LOG.info("...socket open.");
        } catch (IOException e) {
            LOG.error("Something went wrong setting up the listening socket", e);
            e.printStackTrace();
        }
        listeningThread = new Thread(() -> {
            while(shouldRun) {
                try {
                    LOG.info("Listening for hostname...");
                    byte[] buffer = new byte[8192];
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    socket.receive(packet);
                    String receivedHostname = new String(packet.getData(), 0, packet.getLength());
                    LOG.info("...received hostname {}", receivedHostname);
                    hostList.put(receivedHostname, new Node(receivedHostname, packet.getAddress().getHostAddress(), new Date()));
                } catch (IOException e) {
                    LOG.error("Something went wrong while trying to get a host", e);
                    e.printStackTrace();
                }
            }
        });
        listeningThread.start();
    }

    @PreDestroy
    public void teardown() {
        shouldRun = false;
        try {
            listeningThread.join();
            socket.leaveGroup(group);
        } catch (Exception e) {
            LOG.error("Something went wrong while trying to shutdown the listening socket", e);
            e.printStackTrace();
        }
        socket.close();
    }

    @Scheduled(fixedRate = 5000)
    public void sendHeartbeat() {
        LOG.info("Sending heartbeat for {}...", hostName);

        try {
            DatagramPacket packet = new DatagramPacket(hostName.getBytes(), hostName.getBytes().length, group, 9999);
            socket.send(packet);
        } catch (IOException e) {
            LOG.error("Something went super wrong sending a heartbeat.", e);
            e.printStackTrace();
        }

        LOG.info("...heartbeat sent");
    }
}
