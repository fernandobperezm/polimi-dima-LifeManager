package fernandoperez.lifemanager.models;

import fernandoperez.lifemanager.utils.constants.SERVICES_LIST;

/**
 * Created by fernandoperez on 2/4/17.
 */

public class Email {

    private String mId;
    private String mSender;
    private String mSubject;
    private String mSnippet;
    private String mBody;

    public Email() {}

    public Email(String id, String snippet) {
        this.mId = id;
        this.mSnippet = snippet;
    }

    public Email(String id, String snippet, String sender, String subject) {
        this.mId = id;
        this.mSnippet = snippet;
        this.mSender = sender;
        this.mSubject = (subject != null && !subject.isEmpty()) ? subject : "*No Subject*";
    }
    public Email(String id, String snippet, String body, String sender, String subject) {
        this.mId = id;
        this.mSnippet = snippet;
        this.mBody = body;
        this.mSender = sender;
        this.mSubject = (subject != null && !subject.isEmpty()) ? subject : "*No Subject*";
    }

    public String getId() {
        return mId;
    }

    public String getSnippet() {
        return mSnippet;
    }

    public String getSender() {return mSender;}

    public String getSubject() {return mSubject;}

    public String getBody() {return mBody; }

    public void setBody(String body) {mBody = body;}

    public String toString() {
        return String.format("Email with id: %s snippet: %s and body: %s\n",mId, mSnippet, mBody );
    }
}
