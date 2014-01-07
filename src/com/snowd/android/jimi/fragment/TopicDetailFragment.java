package com.snowd.android.jimi.fragment;

import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.snowd.android.jimi.R;
import com.snowd.android.jimi.adapter.AbsEndlessAdapter;
import com.snowd.android.jimi.common.Constants;
import com.snowd.android.jimi.common.DateAndTimeHepler;
import com.snowd.android.jimi.common.MyApp;
import com.snowd.android.jimi.common.SystemHelper;
import com.snowd.android.jimi.model.Board;
import com.snowd.android.jimi.model.Topic;
import com.snowd.android.jimi.rpc.ImageLoader;
import com.snowd.android.jimi.rpc.RpcHelper;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by xuelong.wenxl on 13-12-20.
 */
public class TopicDetailFragment extends AbsForumListFragment<Topic> implements RpcHelper.DataProcesser<List<Topic>> {

    private static final String TAG = "TAG";
    private static final int PAGE_SIZE = 20;
    private int pageStart = 1;
    private long id;

    private int mCurrent;
    private int mTotal;

    private MyApp myApp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle param = getArguments();
        id = param.getLong("_key_tid");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        myApp = (MyApp) getActivity().getApplication();
    }

    @Override
    protected RpcHelper.RpcResult doInBackground() {
        String url = Constants.URL_TOPIC_DETAIL_DEFAULT + id
                + "&" + Constants.PARAM_PAGESIZE + "=" + PAGE_SIZE
                + "&" + Constants.PARAM_PAGENO + "=" + pageStart;
        return RpcHelper.get(url, this);
    }

    @Override
    protected boolean doInBackgroundAppend() {
        String url = Constants.URL_TOPIC_DETAIL_DEFAULT + id
                + "&" + Constants.PARAM_PAGESIZE + "=" + PAGE_SIZE
                + "&" + Constants.PARAM_PAGENO + "=" + (mCurrent + 1);
        RpcHelper.RpcResult result = RpcHelper.get(url, this);
        if (result != null && result.data != null && result.data instanceof List)
        ((AbsEndlessAdapter) getListAdapter()).postAppendData((List) result.data);
        return mCurrent < mTotal;
    }

    @Override
    public List<Topic> process(String input) throws Exception {
        JSONObject json = new JSONObject(input);
        ArrayList<Topic> topics = Topic.newInstanceList(json
                .getJSONArray("post"));
        JSONObject pageInfo = json.optJSONObject("page");
        if (pageInfo == null) {
            mTotal = pageStart;
            mCurrent = pageStart;
        } else {
            mTotal = pageInfo.getInt("max_pages");
            mCurrent = pageInfo.getInt("curpage");
        }
        return topics;
    }


    @Override
    protected List<Topic> onPostResult(RpcHelper.RpcResult o) {
        if (o != null && o.data != null) return (List<Topic>) o.data;
        return null;
    }

    @Override
    protected View preparedView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getActivity()).inflate(R.layout.item_postlist, null);
            ViewHolder holder = new ViewHolder();
            holder.txt_id = (TextView) convertView.findViewById(R.id.topic_id);
            holder.txt_floor = (TextView) convertView.findViewById(R.id.author_floor);
            holder.txt_author = (TextView) convertView.findViewById(R.id.author_id);
            holder.txt_visit_replies = (TextView) convertView.findViewById(R.id.view_replies);
            holder.txt_pubtime = (TextView) convertView.findViewById(R.id.dateline);
            holder.txt_content = (TextView)convertView.findViewById(R.id.topic_content);
            holder.btn_view_image = (Button) convertView.findViewById(R.id.btn_view_image);
            holder.avatar = (ImageView)convertView.findViewById(R.id.author_avatar);
            holder.txt_from=(TextView)convertView.findViewById(R.id.topic_from);
            holder.view_huifu=(TextView)convertView.findViewById(R.id.view_huifu);
            holder.topic_quote=(TextView)convertView.findViewById(R.id.topic_quote);
            convertView.setTag(holder);
        }
        return convertView;
    }

    @Override
    protected void bindData(int position, View contentView, ViewGroup parent, Object item) {
        final Topic topic = (Topic) item;
        ViewHolder holder = (ViewHolder) contentView.getTag();

        //用于存放帖子正文内容中的图片地址
        //final LinkedHashSet<String> img_urls = new LinkedHashSet<String>();

        //需要把帖子内容中的code转换成html标签，再用HTML进行格式化显示
        //String str = BBCodeHelper.processBBCode(topic.getMessage());
        String str=topic.getMessage();
//		//判断帖子来源：iPhone Android
//		String str3=new String();
//		String yinyong="[/quote]";
//		//static/image/
//		int c=topic.getMessage().lastIndexOf(yinyong);
//		int d=topic.getMessage().lastIndexOf("本帖最后由");
//		int e=topic.getMessage().lastIndexOf("编辑");
//		if(c!=-1){
//			vh.view_huifu.setVisibility(View.VISIBLE);
//			str3=topic.getMessage().substring(0,c+8);
//			str=str.replace(str3, "");
//			str3=str3.replace("[img]"+WAY+Constants.HUIFU+"[/img]","<br/>\t");
//			str3=str3.replace(WAY+Constants.HUIFU,"<br/>\t");
//			if(d!=-1 && e!=-1){
//				str3=str3.replaceFirst(str3.substring(d, e+"编辑".length()),"");
//			}
//			vh.view_huifu.setText(Html.fromHtml("\t"+BBCodeHelper.processBBCode(str3),new SmileyImageGetter(getActivity()), null));
//		}else{
//			vh.view_huifu.setVisibility(View.GONE);
//		}
//		/**判断来自那个客户端*/
//		if(topic.getStatus()!=null &&topic.getStatus().length()>5 &&!"".equals(topic.getStatus())){
//			vh.txt_from.setVisibility(View.VISIBLE);
//			vh.txt_from.setText(topic.getStatus());
//		}else{
//			vh.txt_from.setVisibility(View.GONE);
//		}


//		str=BBCodeHelper.processBBCode(str);
//		img_invisible = myApp.isImg_invisible();
//		if(ConnectivityManager.TYPE_WIFI == SystemHelper.getNetworkType(getActivity()) || !img_invisible){
//		//显示
//		}else{
//			str=BBCodeHelper.parseHtmlExcludeImgTag(str);
//		}

        holder.txt_content.setText(topic.getMessage());
//		holder.txt_content.setText(Html.fromHtml(str,new SmileyImageGetter(getActivity()), null));
        if(null != myApp.getUid() && !"".equals(myApp.getUid())
                && null != myApp.getSid() && !"".equals(myApp.getSid())){
            if(topic.getSubject()!=null && !"".equals(topic.getSubject())){
                holder.topic_quote.setVisibility(View.GONE);
            }else{
                holder.topic_quote.setVisibility(View.VISIBLE);
                holder.topic_quote.setText(Html.fromHtml("<a href=''>引用回复</a>"));
                holder.topic_quote.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //QIUJY 回帖权限判断
                        HashMap<Long, Board> subBoardMap = myApp.getSubBoardMap();
                        Log.d(TAG, subBoardMap.toString());
                        if(subBoardMap != null){
                            Board b2 = subBoardMap.get(Long.valueOf(topic.getFid()));
                            Log.d(TAG, b2.toString());
                            if(b2.getIsreply() == 1){
//								Intent it = new Intent(getActivity(), QuoteTopicActivity.class);
//								it.putExtra("author", topic.getAuthor());
//								String str="[/quote]";
//								int a=topic.getMessage().lastIndexOf(str);
//								if(a!=-1){
//									mage=topic.getMessage().replace(topic.getMessage().substring(0,a), "");
//									mage=mage.replace("[/quote]", "");
//								}else{
//									mage=topic.getMessage();
//								}
//								int b=mage.lastIndexOf("[img");
//								int c=mage.lastIndexOf("[/img]");
//								if(b!=-1&&c!=-1){
//									mage=mage.replace(mage.substring(b,c+6), "");
//								}
//								it.putExtra("mage",mage);
//								it.putExtra("tid", String.valueOf(getTid()));
//								it.putExtra("fid",  String.valueOf(topic.getFid()));
//								it.putExtra("pid", String.valueOf(topic.getPid()));
//								it.putExtra("date",String.valueOf(topic.getDateline()));
//								it.putExtra("ispostimage",String.valueOf(b2.getIspostimage()));
//								((Activity) getActivity()).startActivityForResult(it, 300);
                            }else{
                                Toast.makeText(getActivity(), "你没有权限在本版回帖！", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }});
            }
        }else{
            holder.topic_quote.setVisibility(View.GONE);
        }
        if(ConnectivityManager.TYPE_WIFI == SystemHelper.getNetworkType(getActivity())/* || !img_invisible*/){
            Log.d("avatar-->", topic.getAvatar());
//			if(!"".equals(topic.getAvatar())){
            holder.avatar.setTag(topic.getAvatar());
            final ImageView temp = holder.avatar;
            String avatar = "http://login.guitarchina.com/avatar.php?uid=" + topic.getAuthorId() + "&size=small";
            //配图异步下载
            ImageLoader.getInstance().asyncLoadBitmap(avatar, new ImageLoader.ImageCallback() {
                @Override
                public void imageLoaded(Bitmap bmp, String url) {
						/*
						ImageView temp = (ImageView)lv.findViewWithTag(url);
						if(null != temp && bmp != null){
							temp.setImageBitmap(bmp);
						}
						*/
                    temp.setImageBitmap(bmp);
                }
            });
//			}
            /////////////////////////////是否带图片////////////////////////////
            final String [] img_urls = topic.getPic_info();
            Log.d("", "img_urls==" + Arrays.toString(img_urls));
            holder.btn_view_image.setVisibility(View.GONE);
//			if(null !=  img_urls && img_urls.length > 0){
//				Log.d(TAG, "jinlail====" + Arrays.toString(img_urls));
//				holder.btn_view_image.setText(getActivity().getString(R.string.topic_detail_show_pic, img_urls.length));
//				holder.btn_view_image.setVisibility(View.VISIBLE);
//				holder.btn_view_image.setOnClickListener(new View.OnClickListener() {
//					@Override
//					public void onClick(View v) {
////						//启动图片显示的界面
////						Intent intent = new Intent(getActivity(), ShowImageActivity.class);
////						intent.putExtra("img_urls", img_urls);
////						
////						getActivity().startActivity(intent);
//					}
//				});
//			}
        } else {
            holder.avatar.setVisibility(View.GONE);
            holder.btn_view_image.setVisibility(View.GONE);
        }

        holder.txt_id.setText(String.valueOf(topic.getTid()));

        long floor = (pageStart - 1) * PAGE_SIZE + position + 1;

        if(floor == 1){
            holder.txt_floor.setText(getActivity().getString(R.string.topic_display_floor_landload));
        }else if(floor == 2){
            holder.txt_floor.setText(getActivity().getString(R.string.topic_display_floor_1));
        }else if(floor == 3){
            holder.txt_floor.setText(getActivity().getString(R.string.topic_display_floor_2));
        }else if(floor == 4){
            holder.txt_floor.setText(getActivity().getString(R.string.topic_display_floor_3));
        }else{
            holder.txt_floor.setText(getActivity().getString(R.string.topic_display_floor_num, floor));
        }

        holder.txt_author.setText(topic.getAuthor());
        holder.txt_visit_replies.setText(getActivity().getString(R.string.topic_visits_replies,
                topic.getReplies(), topic.getViews()));
        holder.txt_visit_replies.setVisibility(View.GONE);

        holder.txt_pubtime.setText(DateAndTimeHepler.friendly_time(getActivity(),
                topic.getDateline() * 1000));
    }

    static class ViewHolder {
        ImageView avatar;
        TextView txt_id;
        TextView txt_floor;
        TextView txt_author;
        TextView txt_visit_replies;
        TextView txt_pubtime;
        TextView txt_content;
        TextView txt_from;
        TextView topic_quote;
        TextView view_huifu;
        Button btn_view_image;
    }

}
