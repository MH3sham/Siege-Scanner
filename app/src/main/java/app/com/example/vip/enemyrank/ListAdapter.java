package app.com.example.vip.enemyrank;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou;
import com.squareup.picasso.Picasso;

import java.util.List;


public class ListAdapter extends BaseAdapter {
    List<PlayerClass> mPlayersList;
    Context context;
    MainActivity mainActivity;

    public ListAdapter(List<PlayerClass> mPlayersList, MainActivity mainActivity, Context context){
        this.mPlayersList = mPlayersList;
        this.mainActivity = mainActivity;
        this.context = context;
    }
    @Override
    public int getCount() {
        return mPlayersList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder;
        LayoutInflater inflater =  mainActivity.getLayoutInflater();

        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.item, null);
            holder = new ViewHolder();

            holder.tvName = (TextView) convertView.findViewById(R.id.textPlayerName);
            holder.tvPlayerLvl = (TextView) convertView.findViewById(R.id.playerlvl);
            holder.tvPlayerKD = (TextView) convertView.findViewById(R.id.playerkd);
            holder.tvRankName = (TextView) convertView.findViewById(R.id.rankName);

            holder.imageProfile = (ImageView) convertView.findViewById(R.id.imageProfile);
            holder.imageRank = (ImageView) convertView.findViewById(R.id.imageRank);

            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        PlayerClass playerClass = mPlayersList.get(position);
        holder.tvName.setText(playerClass.name);
        holder.tvPlayerLvl.setText(playerClass.playerlvl);
        holder.tvPlayerKD.setText(playerClass.playerKD);
        holder.tvRankName.setText(playerClass.rankName);

        Picasso.with(context).load(playerClass.imageProfile).fit().centerCrop().into(holder.imageProfile);
        //Picasso.with(context).load(playerClass.imageRank).fit().centerCrop().into(holder.imageRank);

        //rank imgs are in .svg so to load them we need to use GlideToVectorYou lib..
        GlideToVectorYou.justLoadImage(mainActivity, Uri.parse(playerClass.imageRank), holder.imageRank);

        return convertView;
    }

    private class ViewHolder {
        TextView tvName , tvPlayerLvl , tvPlayerKD , tvRankName;
        ImageView imageProfile , imageRank;

    }
}
