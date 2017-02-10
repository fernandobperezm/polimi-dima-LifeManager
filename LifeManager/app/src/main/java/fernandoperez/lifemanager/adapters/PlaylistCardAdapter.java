package fernandoperez.lifemanager.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import fernandoperez.lifemanager.R;
import fernandoperez.lifemanager.models.Playlist;

/**
 * Created by fernandoperez on 2/9/17.
 */

public class PlaylistCardAdapter extends RecyclerView.Adapter<PlaylistCardAdapter.PlaylistViewHolder> {
    private List<Playlist> mDataset;
    private Context mContext;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class PlaylistViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        private TextView vName;
        private ImageView vImage;

        public PlaylistViewHolder(View playlistRow) {
            super(playlistRow);
            vName = (TextView) playlistRow.findViewById(R.id.textview_row_spotify_playlistname);
            vImage = (ImageView) playlistRow.findViewById(R.id.imageview_row_spotify_image);
        }
    }

    //
    public PlaylistCardAdapter(List<Playlist> dataset) {
        mDataset = dataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public PlaylistCardAdapter.PlaylistViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }

        // create a new view
        CardView view = (CardView) LayoutInflater.from(mContext)
          .inflate(R.layout.row_playlist, parent, false);

        return new PlaylistViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(PlaylistViewHolder holder, int position) {
        Playlist playlist = mDataset.get(position);

        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.vName.setText(playlist.getName());
        Glide
          .with(mContext)
          .load(playlist.getImagesUri().get(0))
          .centerCrop().crossFade().into(holder.vImage);

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }


}
