package com.kelan.mvvmsmile.binding.viewadapter.checkbox;

import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.kelan.mvvmsmile.binding.command.BindingCommand;

import androidx.databinding.BindingAdapter;

/**
 * Created by wanghua on 18-1-9.
 */

public class ViewAdapter {
    /**
     * @param bindingCommand //绑定监听
     */
    @SuppressWarnings("unchecked")
    @BindingAdapter(value = {"onCheckedChangedCommand"}, requireAll = false)
    public static void setCheckedChanged(final CheckBox checkBox, final BindingCommand<Boolean> bindingCommand) {
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                bindingCommand.execute(b);
            }
        });
    }
}
