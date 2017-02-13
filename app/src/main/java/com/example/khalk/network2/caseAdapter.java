package com.example.khalk.network2;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by khalk on 2/13/2017.
 * this is adapter for testcases
 */

public class CaseAdapter extends ArrayAdapter<TestCase> {

    public CaseAdapter(Context context, ArrayList<TestCase> testCases) {
        super(context, 0, testCases);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView =convertView;
        if(listItemView==null){
            listItemView=LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item,parent,false);
        }

        TestCase testCase=getItem(position);

        TextView nameTestTextView=(TextView)listItemView.findViewById(R.id.case_name);
        nameTestTextView.setText(testCase.getTestName());

        Button testButton=(Button)listItemView.findViewById(R.id.test_button);


        TextView resultTestTextView=(TextView)listItemView.findViewById(R.id.test_result);









        return super.getView(position, convertView, parent);
    }
}
