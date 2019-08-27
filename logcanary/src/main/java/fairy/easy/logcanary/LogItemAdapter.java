package fairy.easy.logcanary;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;

import java.util.Collections;
import java.util.List;


import fairy.easy.logcanary.loginfo.manager.LogLine;
import fairy.easy.logcanary.loginfo.util.SearchCriteria;

import static android.content.Context.CLIPBOARD_SERVICE;


public class LogItemAdapter extends BaseAdapter implements Filterable {
    private Context mContext;
    protected List<LogLine> list;

    public LogItemAdapter(Context context) {
        mContext = context;
        this.list = new ArrayList<>();
        mClipboard = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
    }

    private ArrayFilter mFilter = new ArrayFilter();

    public void refresh(ArrayList<LogLine> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    private ClipboardManager mClipboard;

    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        LogLine data = list.get(i);
        data.setExpanded(!data.isExpanded());
    }

    public void onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        LogLine data = list.get(i);
        if (data.isExpanded()) {
            ClipData clipData = ClipData.newPlainText("Label", data.getOriginalLine());
            mClipboard.setPrimaryClip(clipData);
            Toast.makeText(mContext, "copy success", Toast.LENGTH_SHORT).show();
        }
    }

    private int logLevelLimit = Log.VERBOSE;

    public int getLogLevelLimit() {
        return logLevelLimit;
    }


    public void setLogLevelLimit(int logLevelLimit) {
        this.logLevelLimit = logLevelLimit;
    }

    @Override
    public int getCount() {
        return this.list.size();
    }

    @Override
    public Object getItem(int position) {
        return this.list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LogLine logLine = list.get(position);
        Holder holder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.log_canary_list_item, null);
            holder = new Holder();
            holder.mLevel = convertView.findViewById(R.id.log_canary_level_text);
            holder.mPid = convertView.findViewById(R.id.log_canary_pid_text);
            holder.mTime = convertView.findViewById(R.id.log_canary_timestamp_text);
            holder.mLogText = convertView.findViewById(R.id.log_canary_output_text);
            holder.mTag = convertView.findViewById(R.id.log_canary_tag_text);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        holder.mLevel.setText(logLine.getLogLevelText());
        holder.mLevel.setTextColor(TagColorUtil.getLevelColor(mContext, logLine.getLogLevel()));
        holder.mLevel.setBackgroundColor(TagColorUtil.getLevelBgColor(mContext, logLine.getLogLevel()));
        holder.mPid.setText(String.valueOf(logLine.getProcessId()));
        holder.mTime.setText(logLine.getTimestamp());

        holder.mLogText.setText(logLine.getLogOutput());

        holder.mTag.setText(logLine.getTag());

        String text = logLine.getLogOutput();
        holder.mLogText.setText(text);
        if (logLine.isExpanded() && logLine.getProcessId() != -1) {
            holder.mLogText.setSingleLine(false);
            holder.mTime.setVisibility(View.VISIBLE);
            holder.mPid.setVisibility(View.VISIBLE);
            holder.mLogText.setTextColor(TagColorUtil.getTextColor(mContext, logLine.getLogLevel(), true));
            holder.mTag.setTextColor(TagColorUtil.getTextColor(mContext, logLine.getLogLevel(), true));
            holder.mTag.setSingleLine(false);
            convertView.setBackgroundColor(Color.BLACK);

        } else {
            holder.mLogText.setSingleLine(true);
            holder.mTime.setVisibility(View.GONE);
            holder.mPid.setVisibility(View.GONE);
            holder.mTag.setSingleLine(true);
            convertView.setBackgroundColor(Color.WHITE);
            holder.mLogText.setTextColor(TagColorUtil.getTextColor(mContext, logLine.getLogLevel(), false));
            holder.mTag.setTextColor(TagColorUtil.getTextColor(mContext, logLine.getLogLevel(), false));
        }
        return convertView;
    }

    class Holder {
        private TextView mLevel, mPid, mTime, mLogText, mTag;
    }

    public List<LogLine> getTrueValues() {
        return mOriginalValues != null ? mOriginalValues : list;
    }

    public void removeFirst(int n) {
        if (mOriginalValues != null) {
            List<LogLine> subList = mOriginalValues.subList(n, mOriginalValues.size());
            for (int i = 0; i < n; i++) {
                // value to delete - delete it from the mObjects as well
                this.list.remove(mOriginalValues.get(i));
            }
            mOriginalValues = new ArrayList<>(subList);
        }
        notifyDataSetChanged();
    }

    public void addWithFilter(LogLine object, CharSequence text) {

        if (mOriginalValues != null) {

            List<LogLine> inputList = Collections.singletonList(object);

            List<LogLine> filteredObjects = mFilter.performFilteringOnList(inputList, text);

            mOriginalValues.add(object);

            this.list.addAll(filteredObjects);

        } else {
            this.list.add(object);
        }
        notifyDataSetChanged();
    }


    @Override
    public Filter getFilter() {
        return mFilter;
    }

    private ArrayList<LogLine> mOriginalValues = new ArrayList<>();


    private class ArrayFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();


            ArrayList<LogLine> allValues = performFilteringOnList(mOriginalValues, prefix);

            results.values = allValues;
            results.count = allValues.size();

            return results;
        }

        public ArrayList<LogLine> performFilteringOnList(List<LogLine> inputList, CharSequence query) {

            SearchCriteria searchCriteria = new SearchCriteria(query);

            // search by log level
            ArrayList<LogLine> allValues = new ArrayList<>();

            ArrayList<LogLine> logLines = new ArrayList<>(inputList);

            for (LogLine logLine : logLines) {
                if (logLine != null && logLine.getLogLevel() >= logLevelLimit) {
                    allValues.add(logLine);
                }
            }
            ArrayList<LogLine> finalValues = allValues;

            // search by criteria
            if (!searchCriteria.isEmpty()) {

                final ArrayList<LogLine> values = allValues;
                final int count = values.size();

                final ArrayList<LogLine> newValues = new ArrayList<>(count);

                for (int i = 0; i < count; i++) {
                    final LogLine value = values.get(i);
                    // search the logline based on the criteria
                    if (searchCriteria.matches(value)) {
                        newValues.add(value);
                    }
                }

                finalValues = newValues;
            }

            return finalValues;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            list = (List<LogLine>) results.values;
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetChanged();
            }
        }
    }

}
