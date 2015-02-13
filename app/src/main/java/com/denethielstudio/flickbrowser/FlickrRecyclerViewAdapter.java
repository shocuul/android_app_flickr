package com.denethielstudio.flickbrowser;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by neopoliticatv on 11/02/15.
 */
public class FlickrRecyclerViewAdapter extends RecyclerView.Adapter<FlickrImageViewHolder> {
    private List<Photo> mphotoList;
    private Context mContext;

    public FlickrRecyclerViewAdapter(Context context,List<Photo> photoList) {
        mContext = context;
        this.mphotoList = photoList;
    }

    @Override
    public void onBindViewHolder(FlickrImageViewHolder holder, int position) {
        Photo photoItem = mphotoList.get(position);
        Picasso.with(mContext).load(photoItem.getmImage())
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .into(holder.thumbnail);
        holder.title.setText(photoItem.getmTitle());
    }

    @Override
    public FlickrImageViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        //Esto crea una vista agarrando de parametros a browse el xml creado
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.browse,null);
        //Inicializa La clase FlickrImage View Holder con la vista xml agregada anteriormente
        FlickrImageViewHolder flickrImageViewHolder = new FlickrImageViewHolder(view);
        // Regresa la vista flickrImage
        return flickrImageViewHolder;
    }

    @Override
    public int getItemCount() {
        return (null != mphotoList ? mphotoList.size() : 0);
    }

    public void loadNewData(List<Photo> newPhotos){
        mphotoList = newPhotos;
        notifyDataSetChanged();
    }
}
