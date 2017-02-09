package fernandoperez.lifemanager.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import fernandoperez.lifemanager.R;
import fernandoperez.lifemanager.models.Email;

/**
 * Created by fernandoperez on 2/9/17.
 */

public class EmailAdapter extends RecyclerView.Adapter<EmailAdapter.EmailViewHolder> {
    List<Email> mDataset;
    Context mContext;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class EmailViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        private TextView mSender;
        private TextView mSubject;
        private TextView mSnippet;

        public EmailViewHolder(View emailRow) {
            super(emailRow);
            mSender = (TextView) emailRow.findViewById(R.id.textview_row_email_sender);
            mSubject = (TextView) emailRow.findViewById(R.id.textview_row_email_subject);
            mSnippet = (TextView) emailRow.findViewById(R.id.textview_row_email_snippet);
        }
    }

    //
    public EmailAdapter(List<Email> dataset) {
        mDataset = dataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public EmailAdapter.EmailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }

        // create a new view
        CardView view = (CardView) LayoutInflater.from(mContext)
          .inflate(R.layout.row_email, parent, false);

        return new EmailViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(EmailViewHolder holder, int position) {
        Email email = mDataset.get(position);

        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mSender.setText(email.getSender());
        holder.mSubject.setText(email.getSubject());
        holder.mSnippet.setText(email.getSnippet());
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }


}
