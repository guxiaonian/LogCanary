package fairy.easy.logcanary;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;

import java.util.List;

import androidx.annotation.Nullable;
import fairy.easy.logcanary.loginfo.manager.LogInfoManager;
import fairy.easy.logcanary.loginfo.manager.LogLine;


public class LogInfoActivity extends Activity implements LogInfoManager.OnLogCatchListener {


    private ListView mLogList;
    private EditText mLogFilter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_canary_float_log_info_activity);
        initView();
        setTitle(String.format(getResources().getString(R.string.log_canary_title), getApplicationContext().getPackageName()));
        LogInfoManager.getInstance().registerListener(this);
        LogInfoManager.getInstance().start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogInfoManager.getInstance().stop();
        LogInfoManager.getInstance().removeListener();
    }


    private LogItemAdapter listAdapter;
    private boolean mAutoscrollToBottom = true;

    @SuppressLint("ClickableViewAccessibility")
    public void initView() {
        mLogList = findViewById(R.id.log_canary_list);
        listAdapter = new LogItemAdapter(LogInfoActivity.this);
        mLogList.setAdapter(listAdapter);
        mLogList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                listAdapter.onItemClick(adapterView, view, i, l);
            }
        });

        mLogList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                listAdapter.onItemLongClick(adapterView, view, i, l);
                return true;
            }
        });
        mLogList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
                mAutoscrollToBottom = mLogList.getLastVisiblePosition() == listAdapter.getCount() - 1;
            }
        });


        mLogFilter = findViewById(R.id.log_canary_filter);
        mLogFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                listAdapter.getFilter().filter(s);
            }
        });

        RadioGroup mRadioGroup = findViewById(R.id.log_canary_radio_group);
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.log_canary_verbose) {
                    listAdapter.setLogLevelLimit(Log.VERBOSE);
                } else if (checkedId == R.id.log_canary_debug) {
                    listAdapter.setLogLevelLimit(Log.DEBUG);
                } else if (checkedId == R.id.log_canary_info) {
                    listAdapter.setLogLevelLimit(Log.INFO);
                } else if (checkedId == R.id.log_canary_warn) {
                    listAdapter.setLogLevelLimit(Log.WARN);
                } else if (checkedId == R.id.log_canary_error) {
                    listAdapter.setLogLevelLimit(Log.ERROR);
                }
                listAdapter.getFilter().filter(mLogFilter.getText());
            }
        });

        mRadioGroup.check(R.id.log_canary_verbose);
    }


    @Override
    public void onLogCatch(List<LogLine> logLines) {
        if (mLogList == null || listAdapter == null) {
            return;
        }
        for (LogLine line : logLines) {
            listAdapter.addWithFilter(line, mLogFilter.getText());
        }

        if (listAdapter.getTrueValues().size() > LogCanary.getMaxNum()) {
            int numItemsToRemove = listAdapter.getTrueValues().size() - LogCanary.getMaxNum();
            listAdapter.removeFirst(numItemsToRemove);
        }
        if (mAutoscrollToBottom) {
            mLogList.setSelection(listAdapter.getCount() - 1);
        }
    }


}