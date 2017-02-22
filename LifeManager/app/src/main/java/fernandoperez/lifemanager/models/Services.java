package fernandoperez.lifemanager.models;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;

import java.util.Comparator;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.converter.PropertyConverter;

import fernandoperez.lifemanager.utils.constants.SERVICES_LIST;


/**
 * Created by fernandoperez on 2/4/17.
 */

@Entity
public class Services implements Comparable<Services>, Comparator<Services>{
    @Id
    private Long Id;

    @Index(unique = true, name = "SERVICE_NAME")
    private String name;

    @NotNull
    @Convert(converter = ServiceConverter.class, columnType = Integer.class)
    private SERVICES_LIST serviceType;

    @Generated(hash = 231287931)
    public Services(Long Id, String name, @NotNull SERVICES_LIST serviceType) {
        this.Id = Id;
        this.name = name;
        this.serviceType = serviceType;
    }

    @Generated(hash = 2131255380)
    public Services() {
    }

    public Long getId() { return this.Id; }

    public String getName() {
        return name;
    }

    @Override
    public int compareTo(Services services) {
        return this.serviceType.ordinal() - services.serviceType.ordinal();
    }

    @Override
    public int compare(Services services1, Services services2) {
        return serviceType.ordinal() - serviceType.ordinal();
    }

    public String toString() {
        return "Service: " + this.name;
    }

    public void setId(Long Id) {
        this.Id = Id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SERVICES_LIST getServiceType() {
        return this.serviceType;
    }

    public void setServiceType(SERVICES_LIST serviceType) {
        this.serviceType = serviceType;
    }

    public static class ServiceConverter implements PropertyConverter<SERVICES_LIST, Integer> {
        @Override
        public SERVICES_LIST convertToEntityProperty(Integer databaseValue) {
            if (databaseValue == null) {
                return null;
            }
            for (SERVICES_LIST service : SERVICES_LIST.values()) {
                if (service.id == databaseValue) {
                    return service;
                }
            }
            return null;
        }

        @Override
        public Integer convertToDatabaseValue(SERVICES_LIST entityProperty) {
            return entityProperty == null ? null : entityProperty.id;
        }
    }

}


