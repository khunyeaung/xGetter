package com.htetznaing.xgetter.Core;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.htetznaing.xgetter.Model.XModel;
import com.htetznaing.xgetter.Utils.Utils;
import com.htetznaing.xgetter.XGetter;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StreamTape {

    public static void fetch(String url, final XGetter.OnTaskCompleted onTaskCompleted){
        AndroidNetworking.get(url)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        ArrayList<XModel> xModels = parseVideo(response);
                        if (xModels.isEmpty()){
                            onTaskCompleted.onError();
                        }else onTaskCompleted.onTaskCompleted(xModels, false);
                    }

                    @Override
                    public void onError(ANError anError) {
                        onTaskCompleted.onError();
                    }
                });
    }

    private static ArrayList<XModel> parseVideo(String html){
        final String regex = "<div id=\"videolink\"(.+)>(.*?)<\\/div>";
        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(html);
        ArrayList<XModel> xModels = new ArrayList<>();
        if (matcher.find()) {
            XModel xModel = new XModel();
            xModel.setQuality("Normal");
            xModel.setUrl("https:"+matcher.group(2));
            xModels.add(xModel);
        }
        return xModels;
    }
}
