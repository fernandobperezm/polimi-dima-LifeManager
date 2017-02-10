package fernandoperez.lifemanager.models;

import java.util.List;

/**
 * Created by fernandoperez on 2/4/17.
 */

public class Playlist {

    private String mId;
    private String mName;
    private String mUri;
    private List<String> mImagesUri;

    public Playlist() {}

    public Playlist(String id, String name, String uri) {
        this.mId = id;
        this.mName = name;
        this.mUri = uri;
    }

    public Playlist(String id, String name, String uri, List<String> imagesUriList) {
        this.mId = id;
        this.mName = name;
        this.mUri = uri;
        this.mImagesUri = imagesUriList;
    }


    public String getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public String getmUri() {return mUri;}

    public List<String> getImagesUri() {return mImagesUri;}


    public String toString() {
        return String.format("Playlist with id: %s name: %s and uri: %s\n",mId, mName, mUri);
    }
}
