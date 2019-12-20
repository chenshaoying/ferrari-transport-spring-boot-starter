package cn.darren.ferrari.transport.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author chensy
 * @date 2019/12/20 14:49
 */
@Data
@ConfigurationProperties(prefix = "transport")
public class NettyServerProperties {

    private String hostname = "localhost";
    private boolean enabled;
    private int port;
    private int bossNumber;
    private int workerNumber;
    private String channelInitializerBean;
}
