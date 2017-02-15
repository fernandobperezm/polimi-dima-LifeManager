package fernandoperez.lifemanager.models;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
/**
 * Created by fernandoperez on 2/4/17.
 */

// Class to represent M-N relationship between Configuration and Services.
@Entity
public class ConfigurationWithServices {
    @Id
    private Long Id;

    private Long configurationId;
    private Long serviceId;

    @Generated(hash = 997289079)
    public ConfigurationWithServices(Long Id, Long configurationId, Long serviceId) {
        this.Id = Id;
        this.configurationId = configurationId;
        this.serviceId = serviceId;
    }

    @Generated(hash = 1866723219)
    public ConfigurationWithServices() {
    }

    public Long getId() {return this.Id;}

    public String toString() {
        return configurationId.toString() + " - " + serviceId.toString() + " - " + " - " + "ID: " + Id.toString();
    }

    public void setId(Long Id) {
        this.Id = Id;
    }

    public Long getConfigurationId() {
        return this.configurationId;
    }

    public void setConfigurationId(Long configurationId) {
        this.configurationId = configurationId;
    }

    public Long getServiceId() {
        return this.serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }
}