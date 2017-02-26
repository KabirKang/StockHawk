package com.udacity.stockhawk.widget;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.Contract;
import com.udacity.stockhawk.data.PrefUtils;

import java.util.Set;

/**
 * Created by kabir on 2/25/2017.
 */

public class StockWidgetService extends RemoteViewsService {
    private static final String LOG_TAG = StockWidgetService.class.getSimpleName();
    private static final String[] DATA_COLUMNS = {
            Contract.Quote._ID + "." + Contract.Quote._ID,
            Contract.Quote.COLUMN_PRICE,
            Contract.Quote.COLUMN_SYMBOL,
            Contract.Quote.COLUMN_PERCENTAGE_CHANGE
    };

    static final int INDEX_STOCK_ID = 0;
    static final int INDEX_STOCK_SYMBOL = 1;
    static final int INDEX_STOCK_PRICE = 2;
    static final int INDEX_STOCK_PERCENTAGE_CHANGE = 3;

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RemoteViewsFactory() {
            private Cursor data = null;
            @Override
            public void onCreate() {

            }

            @Override
            public void onDataSetChanged() {
                if (data != null) {
                    data.close();
                }

                final long identityToken = Binder.clearCallingIdentity();
                data = getApplicationContext().getContentResolver().query(Contract.Quote.URI,
                        new String[]{ Contract.Quote._ID, Contract.Quote.COLUMN_SYMBOL, Contract.Quote.COLUMN_PRICE,
                                Contract.Quote.COLUMN_PERCENTAGE_CHANGE, Contract.Quote.COLUMN_ABSOLUTE_CHANGE},
                        null,
                        null,
                        null);

                Binder.restoreCallingIdentity(identityToken);
            }

            @Override
            public void onDestroy() {

            }

            @Override
            public int getCount() {
                if (data == null) {
                    return 0;
                } else {
                    return data.getCount();
                }
            }

            @Override
            public RemoteViews getViewAt(int i) {
                if (!data.moveToPosition(i)) {
                    return null;
                }
                RemoteViews views = new RemoteViews(getApplicationContext().getPackageName(), R.layout.quote_list_item);
                views.setTextViewText(R.id.stock_symbol, data.getString(data.getColumnIndex(Contract.Quote.COLUMN_SYMBOL)));
                views.setTextViewText(R.id.bid_price, "$" + data.getString(data.getColumnIndex(Contract.Quote.COLUMN_PRICE)));
                views.setTextViewText(R.id.change, data.getString(data.getColumnIndex(Contract.Quote.COLUMN_PERCENTAGE_CHANGE)) + "%");
                final Intent fillInIntent = new Intent();
                fillInIntent.setData(Contract.Quote.makeUriForStock(data.getString(data.getColumnIndex(Contract.Quote.COLUMN_SYMBOL))));
                views.setOnClickFillInIntent(R.id.list_item, fillInIntent);
                return views;
            }

            @Override
            public RemoteViews getLoadingView() {
                return null;
            }

            @Override
            public int getViewTypeCount() {
                return 1;
            }

            @Override
            public long getItemId(int i) {
                if (data != null && data.moveToPosition(i)) {
                    final int QUOTES_ID_COL = 0;
                    return data.getLong(QUOTES_ID_COL);
                }
                return i;
            }

            @Override
            public boolean hasStableIds() {
                return true;
            }
        };
    }
}
