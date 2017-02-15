package fernandoperez.lifemanager.models;

import android.location.Location;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.JoinEntity;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.Transient;

import java.util.List;

import fernandoperez.lifemanager.utils.constants.CONFIGURATION_TYPES;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.converter.PropertyConverter;

/**
 * Created by fernandoperez on 2/4/17.
 */

@Entity
public class Configurations  {
    @Id
    private Long Id;

    @Index(unique = true, name = "CONFIGURATION_NAME")
    protected String name;

    @Transient
    protected Location location;

    @Index(unique = true)
    @NotNull
    @Convert(converter = ConfigurationTypeConverter.class, columnType = Integer.class)
    private CONFIGURATION_TYPES configurationType;

    @ToMany
    @JoinEntity(
      entity = ConfigurationWithServices.class,
      sourceProperty = "configurationId",
      targetProperty = "serviceId"
    )
    private List<Services> servicesList;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 772260358)
    private transient ConfigurationsDao myDao;

    @Generated(hash = 1601009303)
    public Configurations(Long Id, String name, @NotNull CONFIGURATION_TYPES configurationType) {
        this.Id = Id;
        this.name = name;
        this.configurationType = configurationType;
    }

    @Generated(hash = 2130777203)
    public Configurations() {
    }

//
//    public Configurations() {
//    }
//
//    public Configurations(String name, int configurationType) {
//        this.name = name;
//        this.configurationType = configurationType;
//    }
//
//    public Configurations(String name, Location location, constants.CONFIGURATION_TYPES configurationType) {
//        this.name = name;
//        this.location = location;
//        this.configurationType = configurationType;
//    }

    public Long getId() { return this.Id; }

    public String getName() {
        return this.name;
    }

    public Location getLocation() {
        return this.location;
    }

    public CONFIGURATION_TYPES getConfigurationType () { return this.configurationType; }

    public String toString() {
        return "Configuration: " + this.name + " - " + String.valueOf(configurationType)+ " - " + "ID: " + String.valueOf(getId());
    }

    public void setId(Long Id) {
        this.Id = Id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setConfigurationType(CONFIGURATION_TYPES configurationType) {
        this.configurationType = configurationType;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 385969599)
    public List<Services> getServicesList() {
        if (servicesList == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ServicesDao targetDao = daoSession.getServicesDao();
            List<Services> servicesListNew = targetDao._queryConfigurations_ServicesList(Id);
            synchronized (this) {
                if (servicesList == null) {
                    servicesList = servicesListNew;
                }
            }
        }
        return servicesList;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 1459600032)
    public synchronized void resetServicesList() {
        servicesList = null;
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1646890137)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getConfigurationsDao() : null;
    }


    public static class ConfigurationTypeConverter implements PropertyConverter<CONFIGURATION_TYPES, Integer> {
        @Override
        public CONFIGURATION_TYPES convertToEntityProperty(Integer databaseValue) {
            if (databaseValue == null) {
                return null;
            }
            for (CONFIGURATION_TYPES configuration : CONFIGURATION_TYPES.values()) {
                if (configuration.id == databaseValue) {
                    return configuration;
                }
            }
            return null;
        }

        @Override
        public Integer convertToDatabaseValue(CONFIGURATION_TYPES entityProperty) {
            return entityProperty == null ? null : entityProperty.id;
        }
    }
}