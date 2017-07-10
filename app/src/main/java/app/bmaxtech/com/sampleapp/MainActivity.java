package app.bmaxtech.com.sampleapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    String[] spinner_list;
    Spinner activity_picker;
    EditText user_input;
    Button add_btn;
    ListView activity_list;

    ArrayAdapter<String> itemsAdapter;
    CustomAdapter mAdapter;

    // variables
    String activity_type = "";
    String input_value = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // initialize textfield
        user_input = (EditText) findViewById(R.id.user_input);

        // initialize spinner
        activity_picker = (Spinner) findViewById(R.id.activity_picker);
        spinner_list = new String[]{
                "Activity 1",
                "Activity 2",
                "Activity 3",
                "Activity 4",
        };
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, spinner_list);
        activity_picker.setAdapter(arrayAdapter);

        // initialize button
        add_btn = (Button) findViewById(R.id.add_btn);
        add_btn.setOnClickListener(this);

        // initialize activity_list
        activity_list = (ListView) findViewById(R.id.activity_list);
        mAdapter = new CustomAdapter();
        activity_list.setAdapter(mAdapter);

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        activity_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                builder.setMessage("Are you sure you want to exit?").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                        alertDialog.setTitle("Change Value ?");
                        alertDialog.setMessage("Update Value");

                        final EditText input = new EditText(MainActivity.this);
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                        lp.setMargins(10, 5, 10, 5);
                        input.setLayoutParams(lp);
                        input.setInputType(InputType.TYPE_CLASS_NUMBER);
                        input.setText("" + mAdapter.getItem(position).value);
                        alertDialog.setView(input);

                        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // update value
                                mAdapter.updateItem(Integer.parseInt(input.getText().toString()), position);
                            }
                        });

                        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                        alertDialog.show();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    /**
     * for button click
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        // get activity type
        activity_type = spinner_list[activity_picker.getSelectedItemPosition()];
        // get user input
        input_value = user_input.getText().toString();
        // update list
        mAdapter.addItem(activity_type, Integer.parseInt(input_value));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class CustomAdapter extends BaseAdapter {

        private ArrayList<Data> mData = new ArrayList<Data>();
        private LayoutInflater mInflater;

        public CustomAdapter() {
            mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public void addItem(final String item, final int value) {
            Data data = new Data();
            data.text = item;
            data.value = value;
            mData.add(data);
            notifyDataSetChanged();
        }

        public void updateItem(int value, int position) {
            mData.get(position).value = value;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public Data getItem(int position) {
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.list_item, null);
                holder.textView_1 = (TextView) convertView.findViewById(R.id.list_item_text_1);
                holder.textView_2 = (TextView) convertView.findViewById(R.id.list_item_text_2);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.textView_1.setText(mData.get(position).text);
            holder.textView_2.setText("" + mData.get(position).value);
            return convertView;
        }

    }

    public static class ViewHolder {
        public TextView textView_1;
        public TextView textView_2;
    }

    // holds data
    public class Data {
        String text;
        int value;
    }

}
