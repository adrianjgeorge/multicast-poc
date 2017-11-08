package org.openrepose.poc.multicast;

import java.util.Date;

public class Node {
    private String hostname;
    private String ip;
    private Date lastSeen;

    public Node(String hostname, String ip, Date lastSeen) {
        this.hostname = hostname;
        this.ip = ip;
        this.lastSeen = lastSeen;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Date getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(Date lastSeen) {
        this.lastSeen = lastSeen;
    }
}
