package fernandoperez.lifemanager.models;

import android.location.Location;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.JoinEntity;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.Transient;

import java.util.List;

import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

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

    @ToMany
    @JoinEntity(
      entity = ArrivingConfWithServ.class,
      sourceProperty = "configurationId",
      targetProperty = "serviceId"
    )
    private List<Services> arrivingServicesList;

    @ToMany
    @JoinEntity(
      entity = LeavingConfWithServ.class,
      sourceProperty = "configurationId",
      targetProperty = "serviceId"
    )
    private List<Services> leavingServicesList;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 772260358)
    private transient ConfigurationsDao myDao;

    @Generated(hash = 460719181)
    public Configurations(Long Id, String name) {
        this.Id = Id;
        this.name = name;
    }

    @Generated(hash = 2130777203)
    public Configurations() {
    }

    public Long getId() { return this.Id; }

    public String getName() {
        return this.name;
    }

    public Location getLocation() {
        return this.location;
    }

    public String toString() {
        return "Configuration: " + this.name +  " - " + "ID: " + String.valueOf(getId());
    }

    public void setId(Long Id) {
        this.Id = Id;
    }

    public void setName(String name) {
        this.name = name;
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

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 891851738)
    public List<Services> getArrivingServicesList() {
        if (arrivingServicesList == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ServicesDao targetDao = daoSession.getServicesDao();
            List<Services> arrivingServicesListNew = targetDao._queryConfigurations_ArrivingServicesList(Id);
            synchronized (this) {
                if (arrivingServicesList == null) {
                    arrivingServicesList = arrivingServicesListNew;
                }
            }
        }
        return arrivingServicesList;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 129772664)
    public synchronized void resetArrivingServicesList() {
        arrivingServicesList = null;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 631528086)
    public List<Services> getLeavingServicesList() {
        if (leavingServicesList == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ServicesDao targetDao = daoSession.getServicesDao();
            List<Services> leavingServicesListNew = targetDao._queryConfigurations_LeavingServicesList(Id);
            synchronized (this) {
                if (leavingServicesList == null) {
                    leavingServicesList = leavingServicesListNew;
                }
            }
        }
        return leavingServicesList;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 2125745834)
    public synchronized void resetLeavingServicesList() {
        leavingServicesList = null;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1646890137)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getConfigurationsDao() : null;
    }
}