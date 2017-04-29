package com.darelbitsy.dbweather.views.adapters.listAdapter;

import android.content.Intent;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.darelbitsy.dbweather.R;
import com.darelbitsy.dbweather.models.datatypes.news.Article;
import com.darelbitsy.dbweather.views.activities.NewsDialogActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.darelbitsy.dbweather.extensions.holder.ConstantHolder.NEWS_DATA_KEY;

/**
 * Created by Darel Bitsy on 16/02/17.
 * News Adapter for the news recycler view
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder>  {
    private final List<Article> mNewses = new ArrayList<>();

    public NewsAdapter(final List<Article> newses) {
        mNewses.addAll(newses);
    }

    @Override
    public NewsViewHolder onCreateViewHolder(final ViewGroup parent,
                                             final int viewType) {
        return new NewsViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.news_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final NewsViewHolder holder, final int position) {
        holder.bindNews(mNewses.get(position));
    }

    @Override
    public int getItemCount() {
        return mNewses.size();
    }

    public void updateContent(final List<Article> newses) {
        mNewses.clear();
        mNewses.addAll(newses);
        notifyDataSetChanged();
    }

    class NewsViewHolder extends RecyclerView.ViewHolder {
        final ConstraintLayout newsContainer;
        final TextView newsFrom;
        final TextView newsDescription;
        Article mNews;

        final View.OnClickListener newsOnClickListener = view -> {

            final Intent newsActivity = new Intent(view.getContext(), NewsDialogActivity.class);
            newsActivity.putExtra(NEWS_DATA_KEY, mNews);
            view.getContext().startActivity(newsActivity);
        };

        NewsViewHolder(final View itemView) {
            super(itemView);
            newsContainer = (ConstraintLayout) itemView.findViewById(R.id.newsLayout);
            newsFrom = (TextView) itemView.findViewById(R.id.newsFrom);
            newsDescription = (TextView) itemView.findViewById(R.id.newsDescription);
        }

        void bindNews(final Article news) {
            mNews = news;
            newsContainer.setOnClickListener(newsOnClickListener);
            newsFrom.setText(String.format(Locale.getDefault(), itemView
                    .getContext()
                    .getString(R.string.news_from), news.getAuthor()));
            if (news.getAuthor().contains("sport")) {
                newsFrom.setBackgroundColor(Color.BLUE);
            } else {
                newsFrom.setBackgroundColor(Color.RED);
            }
            newsDescription.setText(news.getTitle());
        }
    }
}
