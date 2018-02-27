package com.banking.boumzebra.switchingbetweenlistviewandgridview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import model.Article;

/**
 * Created by BOUMZEBRA on 27/02/2018.
 */

public class ListViewAdapter extends BaseAdapter implements Filterable {private Context context;
    private int layout;
    ArrayList<Article> arrayList;
    ArrayList<Article> mStringFilterList;
    ValueFilter valueFilter;
    public ListViewAdapter(Context context, int layout, ArrayList<Article> arrayList) {
        this.context = context;
        this.layout = layout;
        this.arrayList = arrayList;
        mStringFilterList = arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_item, null);
        }

        Article article = arrayList.get(position);
        ImageView img = (ImageView) view.findViewById(R.id.list_imageView);
        TextView name = (TextView) view.findViewById(R.id.list_title);
        TextView description = (TextView) view.findViewById(R.id.list_description);

        name.setText(article.getTitle());
        description.setText(article.getDescription());
        img.setImageResource(article.getImage());
        return view;
    }

    @Override
    public Filter getFilter() {
        if (valueFilter == null) {
            valueFilter = new ValueFilter();
        }
        return valueFilter;
    }

    private class ValueFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if (constraint != null && constraint.length() > 0) {
                ArrayList<Article> filterList = new ArrayList<Article>();
                for (int i = 0; i < mStringFilterList.size(); i++) {
                    if ( (mStringFilterList.get(i).getTitle().toUpperCase() )
                            .contains(constraint.toString().toUpperCase())) {

                        Article country = new Article(mStringFilterList.get(i)
                                .getId() ,mStringFilterList.get(i)
                                .getTitle() ,  mStringFilterList.get(i)
                                .getDescription() ,  mStringFilterList.get(i)
                                .getImage());

                        filterList.add(country);
                    }
                }
                results.count = filterList.size();
                results.values = filterList;
            } else {
                results.count = mStringFilterList.size();
                results.values = mStringFilterList;
            }
            return results;

        }

        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            arrayList = (ArrayList<Article>) results.values;
            notifyDataSetChanged();
        }

    }
}
