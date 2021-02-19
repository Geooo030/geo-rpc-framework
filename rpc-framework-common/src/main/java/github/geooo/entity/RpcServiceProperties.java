package github.geooo.entity;

import lombok.*;

/**
 * @author zhaoqi.wang
 * @date 2021/2/7
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class RpcServiceProperties {

    // service version
    private String version;

    // service name
    private String group;

    // service name
    private String name;

    public String toRpcServiceName() {return this.getName() + this.getGroup() + this.getVersion(); }

}
