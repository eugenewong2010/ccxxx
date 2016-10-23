package in.co.madhur.chatbubblesdemo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.v4.content.res.ResourcesCompat;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by tanky on 23/10/2016.
 */
public class CustomDialogManager {

    public static CustomDialogManager instance = new CustomDialogManager();

    public Dialog listviewDialog(Context activity, LayoutInflater layoutInflater, List<String> stringList){
        View layout = layoutInflater.inflate(R.layout.lv_layout, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        StringAdapter stringAdapter = new StringAdapter(stringList, activity);
        ListView listView = (ListView) layout.findViewById(R.id.listview);
        listView.setAdapter(stringAdapter);

        builder.setView(layout);
        AlertDialog alert = builder.create();
        return alert;
    }

    public Dialog customDialog(Context activity, LayoutInflater layoutinflater, @DrawableRes int drawableResId, String title, String message, String positiveString, String negativeString, final Dialog.OnClickListener positiveClickListener, final Dialog.OnClickListener negativeClickListener){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        return builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(positiveString, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                        positiveClickListener.onClick(dialog, i);
                    }
                })
                .setNegativeButton(negativeString, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        negativeClickListener.onClick(dialogInterface, i);
                    }
                }).create();
    }

    public Dialog imageDialog(Context activity, LayoutInflater layoutInflater, @DrawableRes int drawableResId, final View.OnClickListener clickListener){
//Preparing views
        View layout = layoutInflater.inflate(R.layout.iv_layout, null);
        Drawable drawable = ResourcesCompat.getDrawable(activity.getResources(), drawableResId, null);
        ImageView iv = (ImageView) layout.findViewById(R.id.iv);
        iv.setImageDrawable(drawable);

//Building dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(layout);
        final AlertDialog alert = builder.create();

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.onClick(view);
                alert.dismiss();
            }
        });

        return alert;
    }

    //Dialog box creator
    public Dialog constructYourDialog(Activity activity, LayoutInflater layoutInflater, int layoutResId) {
//Preparing views
        View layout = layoutInflater.inflate(layoutResId, null);
//Building dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(layout);

//        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        Log.i("", "Confirm Click");
//                        dialog.dismiss();
//                    }
//                }
//        );
//
//        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                Log.i("", "Cancel Click");
//                dialog.dismiss();
//            }
//        });

        AlertDialog alert = builder.create();
        return alert;
    }
}