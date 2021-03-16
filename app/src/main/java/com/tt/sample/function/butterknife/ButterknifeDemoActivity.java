package com.tt.sample.function.butterknife;

import android.os.Bundle;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;


/***
 * 参考
 * github
 * https://github.com/JakeWharton/butterknife
 * 官方文档
 * http://jakewharton.github.io/butterknife/
 * https://www.jianshu.com/p/3678aafdabc7
 *
 */
public class ButterknifeDemoActivity extends AppCompatActivity {


//    @BindView(R.id.title) TextView title;
//    @BindView(R.id.subtitle) TextView subtitle;
//    @BindView(R.id.footer)
//    TextView footer;


//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        //activity不需要解绑，会自动解绑
//        ButterKnife.bind(this);
//    }

    //点击监听
//    @OnClick({R.id.btn_hahaha,R.id.btn_momoda})
//    public void onViewClicked(View v) {
//        switch (v.getId()) {
//            case R.id.btn_hahaha:
//            Toast.makeText(this, "hahaha", Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.btn_momoda:
//                Toast.makeText(this, "momoda", Toast.LENGTH_SHORT).show();
//                break;
//            default:
//                break;
//        }
//    }


//    @OnItemClick(R.id.list_view)
//    public void onItemClick(int position) {
//        Toast.makeText(this, "momoda"+position, Toast.LENGTH_SHORT).show();
//    }


    //在适配器中使用
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        ViewHolder holder;
//        if (convertView == null) {
//            convertView = mInflater.inflate(R.layout.list_view_item, parent, false);
//            holder = new ViewHolder(convertView);
//            convertView.setTag(holder);
//        } else {
//            holder = (ViewHolder) convertView.getTag();
//        }
//        holder.imageView.setImageResource(R.mipmap.ic_launcher);
//        holder.textView.setText(mData.get(position));
//        return convertView;
//    }
//
//    static class ViewHolder {
//        @BindView(R.id.img_item)
//        ImageView imageView;
//        @BindView(R.id.tv_item)
//        TextView textView;
//
//        ViewHolder(View view) {
//            ButterKnife.bind(this, view);
//        }
//    }


}
