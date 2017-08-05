package com.stoliarchuk.vasyl.testtaskjunior.fragments;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.stoliarchuk.vasyl.testtaskjunior.R;
import com.stoliarchuk.vasyl.testtaskjunior.RssItem;
import com.stoliarchuk.vasyl.testtaskjunior.database.RecyclerViewCursorAdapter;
import com.stoliarchuk.vasyl.testtaskjunior.database.RssContract;
import com.stoliarchuk.vasyl.testtaskjunior.database.RssCursorWrapper;
import com.stoliarchuk.vasyl.testtaskjunior.database.RssItemsLab;

import java.util.ArrayList;
import java.util.List;


public class RssListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
    private final static int LOADER_ID = 0;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter<RssItemHolder> mAdapter;
    private List<RssItem> mItems = new ArrayList<>();
    private OnRssItemClickListener mListener;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_rss_list, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mItems = RssItemsLab.getRssItems(getActivity());
        return rootView;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getLoaderManager().initLoader(LOADER_ID, null, this);
    }
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), RssContract.BASE_CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (mAdapter == null) {
            mAdapter = new RssAdapter(data);
            mRecyclerView.setAdapter(mAdapter);
        }

        ((RssAdapter) mAdapter).swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        ((RssAdapter) mAdapter).swapCursor(null);
    }

    private class RssItemHolder extends RecyclerView.ViewHolder{
        public TextView mTitleTextView;
        public TextView mDescriptionTextView;
        public TextView mCategoryTextView;
        public TextView mLinkTextView;
        public ImageView mImageView;

        public RssItemHolder(View itemView) {
            super(itemView);
            mTitleTextView = (TextView) itemView.findViewById(R.id.title_text_view);
            mDescriptionTextView = (TextView) itemView.findViewById(R.id.description_text_view);
            mCategoryTextView = (TextView) itemView.findViewById(R.id.category_text_view);
            mLinkTextView = (TextView) itemView.findViewById(R.id.link_text_view);
            mImageView = (ImageView) itemView.findViewById(R.id.image_view);
        }

        public void bindItem(final RssItem item) {
            mTitleTextView.setText(item.getTitle());
            mCategoryTextView.setText(item.getCategory());
            mDescriptionTextView.setText(item.getDescription());
            mLinkTextView.setText("Read more...");
            mLinkTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemClicked(item.getLink());
                }});
            Picasso.with(getActivity())
                    .load(item.getImageLink())
                    .placeholder(R.drawable.placeholder)
                    .into(mImageView);
        }
    }

    private class RssAdapter extends RecyclerViewCursorAdapter<RssItemHolder> {
        private List<RssItem> mItems;

        public RssAdapter(Cursor c) {
            super(c);
        }


        @Override
        public RssItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
            return new RssItemHolder(view);
        }

        @Override
        public void onBindViewHolder(RssItemHolder holder, Cursor cursor) {
            RssCursorWrapper cursorWrapper = new RssCursorWrapper(cursor);
            RssItem item = cursorWrapper.getRssItem();
            holder.bindItem(item);
        }

        @Override
        public void swapCursor(Cursor newCursor) {
            super.swapCursor(newCursor);
        }

    }

    public interface OnRssItemClickListener {
        public void onItemClicked(String url);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnRssItemClickListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnRssItemClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
