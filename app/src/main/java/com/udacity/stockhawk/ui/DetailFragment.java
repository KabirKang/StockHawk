package com.udacity.stockhawk.ui;

import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.common.collect.ImmutableList;
import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.Contract;
import com.udacity.stockhawk.data.StockPoint;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kabir on 2/3/2017.
 */

public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String LOG_TAG = DetailFragment.class.getSimpleName();
    static final String DETAIL_URI = "URI";

    private static final int DETAIL_LOADER = 0;

    private static ImmutableList<String> DETAIL_COLUMNS = Contract.Quote.QUOTE_COLUMNS;
    private LineData lineData;
    private LineChart lineChart;

    public static final int COL_STOCK_ID = 0;
    public static final int COL_STOCK_SYMBOL = 1;
    public static final int COL_STOCK_PRICE = 2;
    public static final int COL_ABSOLUTE_CHANGE = 3;
    public static final int COL_PERCENTAGE_CHANGE = 4;
    public static final int COL_HISTORY = 5;

    private Uri mUri;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle arguments = getArguments();
        if (arguments != null) {
            mUri = arguments.getParcelable(DetailFragment.DETAIL_URI);
        }

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        lineChart = (LineChart) rootView.findViewById(R.id.chart);
        
        return rootView;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null && data.moveToFirst()) {
            String stockData = data.getString(COL_HISTORY);
            String lines[] = stockData.split("\\r?\\n");
            List<Entry> entries = new ArrayList<Entry>();
            for (int i = 0; i <lines.length; i++) {
                String line = lines[i];
                StockPoint point = new StockPoint(line);
                entries.add(new Entry(i, point.getPrice()));
            }

            LineDataSet dataSet = new LineDataSet(entries, "Label");
            dataSet.setColor(R.color.material_red_700);
            dataSet.setValueTextColor(R.color.material_green_700);
            dataSet.setLineWidth(5);
            lineData = new LineData(dataSet);
            lineChart.getXAxis().setTextColor(Color.WHITE);
            lineChart.setData(lineData);
            lineChart.invalidate();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) { }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if ( null != mUri ) {
            String[] projection = new String[DETAIL_COLUMNS.size()];
            projection = DETAIL_COLUMNS.toArray(projection);
            return new CursorLoader(
                    getActivity(),
                    mUri,
                    projection,
                    null,
                    null,
                    null
            );
        }
        return null;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(DETAIL_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }
}
