package com.kelan.mvvmsmile.binding.viewadapter.radiogroup;

import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.kelan.mvvmsmile.binding.command.BindingCommand;

import androidx.annotation.IdRes;
import androidx.databinding.BindingAdapter;


/**
 * Created by wanghua on 18-1-9.
 */
public class ViewAdapter {
    @BindingAdapter(value = {"onCheckedChangedCommand"}, requireAll = false)
    public static void onCheckedChangedCommand(final RadioGroup radioGroup, final BindingCommand<String> bindingCommand) {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
                if (radioButton != null)
                    bindingCommand.execute(radioButton.getText().toString());
            }
        });
    }

    @BindingAdapter(value = {"onCheckedTagChangedCommand"}, requireAll = false)
    public static void onCheckedTagChangedCommand(final RadioGroup radioGroup, final BindingCommand<String> bindingCommand) {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
                Log.i("whwhid", checkedId + "");
                if (checkedId != -1)
                    bindingCommand.execute(radioButton.getTag().toString());
            }
        });
    }
}
