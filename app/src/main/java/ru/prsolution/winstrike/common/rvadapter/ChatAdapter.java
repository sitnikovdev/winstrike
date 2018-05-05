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
import ru.prsolution.winstrike.common.entity.ChatModel;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static Integer SELECTED_ITEM = 0;
    private Context context;
    List<ChatModel> chatList;
    public final static int CHAT_RIGHT = 1;
    public final static int CHAT_LEFT = 2;

    public ChatAdapter(Context context, List<ChatModel> chatList) {
        this.context = context;
        this.chatList = chatList;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
         switch (viewType) {
            case CHAT_RIGHT : {
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_r, parent, false);
                return new ChatRightViewHolder(itemView);
            }
            case CHAT_LEFT : {
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_l, parent, false);
                return new ChatLeftViewHolder(itemView);
            }
            default:  {
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_r, parent, false);
                return new ChatRightViewHolder(itemView);
            }
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case CHAT_RIGHT : {
                initLayoutRight(position, (ChatRightViewHolder) holder);
                break;
            }
            case  CHAT_LEFT : {
                initLayoutLeft(position,  (ChatLeftViewHolder) holder);
                break;
            }
        }
    }

    private void initLayoutRight(int position,ChatRightViewHolder holder) {
        ChatModel chat = chatList.get(position);
        holder.name.setText(chat.getName());
        holder.message.setText(chat.getMessage());
        holder.time.setText(chat.getTime());
        Glide.with(context)
                .load(chat.getAvatar())
                .into(holder.avatar);
        Glide.with(context)
                .load(chat.getBubble())
                .into(holder.bubble);
    }

    private void initLayoutLeft(int position,ChatLeftViewHolder holder) {
        ChatModel chat = chatList.get(position);
        holder.name.setText(chat.getName());
        holder.message.setText(chat.getMessage());
        holder.time.setText(chat.getTime());
    }



    @Override
    public int getItemCount() {
        return chatList.size();
    }

    @Override
    public int getItemViewType(int position) {
        ChatModel chat = chatList.get(position);
        switch (chat.getType()) {
            case 1 : return CHAT_RIGHT;
            case 2 : return CHAT_LEFT;
            default:  {
                return CHAT_RIGHT;
            }
        }
    }

    class ChatRightViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_name_right)
        TextView name;
        @BindView(R.id.iv_avatar_right)
        ImageView avatar;
        @BindView(R.id.tv_message_right)
        TextView message;
        @BindView(R.id.tv_time_right)
        TextView time;
        @BindView(R.id.iv_bubble_big_right)
        ImageView bubble;

        public ChatRightViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class ChatLeftViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_name_left)
        TextView name;
        @BindView(R.id.iv_avatar_left)
        ImageView avatar;
        @BindView(R.id.tv_message_left)
        TextView message;
        @BindView(R.id.tv_time_left)
        TextView time;
        @BindView(R.id.iv_bubble_left)
        ImageView bubble;

        public ChatLeftViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
