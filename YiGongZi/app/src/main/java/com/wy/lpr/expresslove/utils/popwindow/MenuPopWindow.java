package com.wy.lpr.expresslove.utils.popwindow;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.wy.lpr.expresslove.R;

import java.util.List;

public class MenuPopWindow extends PopupWindow {
    private View mContentView;
    private ListView mContentList;

    public MenuPopWindow(Activity context, List<MenuPopWindowBean> list){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContentView = inflater.inflate(R.layout.menu_popwindow,null);
        mContentList = (ListView)mContentView.findViewById(R.id.lv_toptitle_menu);
        mContentList.setAdapter(new PopListAdapter(context, list));
        int h = context.getWindowManager().getDefaultDisplay().getHeight();
        int w = context.getWindowManager().getDefaultDisplay().getWidth();
        // 设置SelectPicPopupWindow的View
        this.setContentView(mContentView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(w / 3-30);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        // 刷新状态
        this.update();
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0000000000);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        this.setBackgroundDrawable(dw);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.PopUpAnimation);
    }

    public void setOnItemClick(AdapterView.OnItemClickListener myOnItemClickListener) {
        mContentList.setOnItemClickListener(myOnItemClickListener);
    }

    static class PopListAdapter extends BaseAdapter {
        private List<MenuPopWindowBean> list;
        private LayoutInflater inflater;
        public PopListAdapter(Context context, List<MenuPopWindowBean> list) {
            inflater = LayoutInflater.from(context);
            this.list = list;
        }
        @Override
        public int getCount() {
            return list == null ? 0 : list.size();
        }
        @Override
        public Object getItem(int position) {
            return list.get(position);
        }
        @Override
        public long getItemId(int position) {
            return position;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Holder holder;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.menu_popwindow_item, null);
                holder = new Holder();
                holder.ivItem = (ImageView) convertView.findViewById(R.id.iv_menu_item);
                holder.tvItem = (TextView) convertView.findViewById(R.id.tv_menu_item);
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }
            holder.ivItem.setImageResource(list.get(position).getIcon());
            holder.tvItem.setText(list.get(position).getText());
            return convertView;
        }
        class Holder {
            ImageView ivItem;
            TextView tvItem;
        }
    }
    /**
     * 显示popupWindow
     *
     * @param parent
     */
    public void showPopUpWindow(View parent) {
        if (!this.isShowing()) {
            // 以下拉方式显示popupwindow
            this.showAsDropDown(parent);
        } else {
            this.dismiss();
        }
    }


}
