package ru.prsolution.winstrike.common.rvadapter;
/*
 * Created by oleg on 31.01.2018.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.prsolution.winstrike.R;
import ru.prsolution.winstrike.common.entity.NewsModel;
import ru.prsolution.winstrike.common.rvlistener.OnItemNewsClickListener;

import static ru.prsolution.winstrike.common.utils.TextFormat.setTextColor;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {
    private OnItemNewsClickListener itemNewsClickListener;
    
    public static Integer SELECTED_ITEM = 0;
    private Context context;
    List<NewsModel> newsList;

    public NewsAdapter(Context context, List<NewsModel> newsList, OnItemNewsClickListener itemNewsClickListener) {
        this.context = context;
        this.newsList = newsList;
        this.itemNewsClickListener = itemNewsClickListener;
        fillNewsList();
    }


    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_news, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NewsViewHolder holder, int position) {
        NewsModel news = newsList.get(position);
        holder.header.setText(news.getHeadTitle());

        holder.descriptions.setText( news.getDescriptions());
        holder.date.setText(news.getDate());
        holder.views.setText(news.getViews() + " k");

        setTextColor(holder.descriptions, "Ярослав Комков с детства хотел только одного: открыть свой собственный", "...", "#9b9b9b", "#000");

        Glide.with(context).load(news.getThumbnail()).into(holder.thumbnail);

        holder.overflow.setOnClickListener (
//           it ->  context.startActivity(new Intent(context, NewsDetailActivity.class))
            it -> itemNewsClickListener.onItemNewsClick(holder, position)
        );
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }


  public   class NewsViewHolder extends RecyclerView.ViewHolder   {
        @BindView(R.id.tv_head_news)
        TextView header;
        @BindView(R.id.iv_thumbnail)
         ImageView thumbnail;
        @BindView(R.id.tv_descriptions)
        TextView descriptions;
        @BindView(R.id.tv_date_news)
        TextView date;
        @BindView(R.id.tv_views)
        TextView views;
        @BindView(R.id.tv_next)
        View overflow;

        public NewsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }


    }

    private  void fillNewsList()  {
        int[] covers = {R.drawable.img_komkov, R.drawable.img_esl};
        newsList.add( new
                NewsModel("Поиграть на 10 млн $",covers[0],  "Ярослав Комков с дества хотел только одного: открыть свой собственный","27 октября",14 ));
        newsList.add( new
                NewsModel("Поиграть на 10 млн $",covers[1],  "Ярослав Комков с дества хотел только одного: открыть свой собственный","27 октября",14 ));
        newsList.add( new
                NewsModel("Поиграть на 10 млн $",covers[0],  "Ярослав Комков с дества хотел только одного: открыть свой собственный","27 октября",14 ));
        newsList.add( new
                NewsModel("Поиграть на 10 млн $",covers[1],  "Ярослав Комков с дества хотел только одного: открыть свой собственный","27 октября",14 ));
        newsList.add( new
                NewsModel("Поиграть на 10 млн $",covers[0],  "Ярослав Комков с дества хотел только одного: открыть свой собственный","27 октября",14 ));
        newsList.add( new
                NewsModel("Поиграть на 10 млн $",covers[1],  "Ярослав Комков с дества хотел только одного: открыть свой собственный","27 октября",14 ));
        newsList.add( new
                NewsModel("Поиграть на 10 млн $",covers[0],  "Ярослав Комков с дества хотел только одного: открыть свой собственный","27 октября",14 ));
        newsList.add( new
                NewsModel("Поиграть на 10 млн $",covers[1],  "Ярослав Комков с дества хотел только одного: открыть свой собственный","27 октября",14 ));
    }
}
