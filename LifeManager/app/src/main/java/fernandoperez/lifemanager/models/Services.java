package fernandoperez.lifemanager.models;

import java.util.Comparator;

import fernandoperez.lifemanager.utils.constants.SERVICES_LIST;

/**
 * Created by fernandoperez on 2/4/17.
 */

public class Services implements Comparable<Services>, Comparator<Services>{

    private String mName;
    private String mCompany;
    private SERVICES_LIST mServiceEnum;

    public Services() {}

    public Services(String name, String company, SERVICES_LIST serviceEnum) {
        this.mName = name;
        this.mCompany = company;
        this.mServiceEnum = serviceEnum;
    }

    public String getName() {
        return mName;
    }

    public String getCompany() {
        return mCompany;
    }

    public SERVICES_LIST getEnum() {return mServiceEnum; }

    @Override
    public int compareTo(Services services) {
        return this.mServiceEnum.ordinal() - services.mServiceEnum.ordinal();
    }

    @Override
    public int compare(Services services1, Services services2) {
        return services1.getEnum().ordinal() - services2.getEnum().ordinal();
    }
}
