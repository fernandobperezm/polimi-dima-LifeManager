package fernandoperez.lifemanager.models;

/**
 * Created by fernandoperez on 2/4/17.
 */

public class Services {

    private String mName;
    private String mCompany;

    public Services() {}

    public Services(String name, String company) {
        this.mName = name;
        this.mCompany = company;
    }

    public String getName() {
        return mName;
    }

    public String getCompany() {
        return mCompany;
    }


}
