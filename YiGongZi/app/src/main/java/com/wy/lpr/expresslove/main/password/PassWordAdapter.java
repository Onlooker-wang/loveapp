package com.wy.lpr.expresslove.main.password;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wy.lpr.expresslove.R;
import com.wy.lpr.expresslove.main.photo.PhotoAdapter;

import java.util.ArrayList;
import java.util.List;

public class PassWordAdapter extends RecyclerView.Adapter<PassWordAdapter.ViewHolder> {
    private static final String TAG = "PassWordAdapter";

    private Context mContext;
    private LayoutInflater mInflater;
    private List<String> mUserNameList = new ArrayList<>();
    private List<String> mPassWordList = new ArrayList<>();

    public PassWordAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        mContext = context;
    }

    public void setList(List<String> userName, List<String> passWord) {
        mUserNameList = userName;
        mPassWordList = passWord;
        Log.i(TAG, "setList: mUserNameList = " + mUserNameList + ",mPassWordList = " + mPassWordList);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.activity_pass_word_item,
                parent, false);
        final ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.i(TAG, "onBindViewHolder: username = " + mUserNameList.get(position) + ",password = " + mPassWordList.get(position));
        holder.mUserName.setText(mUserNameList.get(position));
        holder.mPassWord.setText(mPassWordList.get(position));
    }

    @Override
    public int getItemCount() {
        return mUserNameList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView mUserName;
        TextView mPassWord;

        public ViewHolder(View view) {
            super(view);
            mUserName = (TextView) view.findViewById(R.id.user_name);
            mPassWord = (TextView) view.findViewById(R.id.pass_word);
        }
    }
}
