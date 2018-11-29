package io.renren.modules.iots.entity.log;

public class ServerInfo {

    /**
     * 服务名字
     */
    private String serverName;

    /**
     * 服务ID
     */
    private Long serverId;

    /**
     * 服务的mac地址
     */
    private String mac;

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public Long getServerId() {
        return serverId;
    }

    public void setServerId(Long serverId) {
        this.serverId = serverId;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }
}
