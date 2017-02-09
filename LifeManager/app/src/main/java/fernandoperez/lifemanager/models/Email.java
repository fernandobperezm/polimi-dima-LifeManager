package fernandoperez.lifemanager.models;

import fernandoperez.lifemanager.utils.constants.SERVICES_LIST;

/**
 * Created by fernandoperez on 2/4/17.
 */

public class Email {

    private String mId;
    private String mSnippet;
    private String mBody;

    public Email() {}

    public Email(String id, String snippet) {
        this.mId = id;
        this.mSnippet = snippet;
    }

    public Email(String id, String snippet, String body) {
        this.mId = id;
        this.mSnippet = snippet;
        this.mBody = body;
    }

    public String getId() {
        return mId;
    }

    public String getSnippet() {
        return mSnippet;
    }

    public String getBody() {return mBody; }

    public void setBody(String body) {mBody = body;}

    public String toString() {
        return String.format("Email with id: %s snippet: %s and body: %s\n",mId, mSnippet, mBody );
    }
}
