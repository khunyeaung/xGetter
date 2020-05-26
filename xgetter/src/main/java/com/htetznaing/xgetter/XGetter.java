package com.htetznaing.xgetter;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Base64;
import android.webkit.ConsoleMessage;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.htetznaing.xgetter.Core.BitTube;
import com.htetznaing.xgetter.Core.Fembed;
import com.htetznaing.xgetter.Core.GDrive;
import com.htetznaing.xgetter.Core.GDrive2020;
import com.htetznaing.xgetter.Core.GoUnlimitedTO;
import com.htetznaing.xgetter.Core.MP4Upload;
import com.htetznaing.xgetter.Core.MegaUp;
import com.htetznaing.xgetter.Core.Muvix;
import com.htetznaing.xgetter.Core.Pstream;
import com.htetznaing.xgetter.Core.SolidFiles;
import com.htetznaing.xgetter.Core.StreamKIWI;
import com.htetznaing.xgetter.Core.StreamTape;
import com.htetznaing.xgetter.Core.UpToStream;
import com.htetznaing.xgetter.Core.VideoBIN;
import com.htetznaing.xgetter.Core.VideoBmX;
import com.htetznaing.xgetter.Core.Vidoza;
import com.htetznaing.xgetter.Core.VivoSX;
import com.htetznaing.xgetter.Core.Vlare;
import com.htetznaing.xgetter.Core.Vudeo;
import com.htetznaing.xgetter.Model.XModel;
import com.htetznaing.xgetter.Core.Twitter;
import com.htetznaing.xgetter.Core.DailyMotion;
import com.htetznaing.xgetter.Utils.GPhotosUtils;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;

import static com.htetznaing.xgetter.Utils.FacebookUtils.check_fb_video;
import static com.htetznaing.xgetter.Utils.FacebookUtils.getFbLink;
import static com.htetznaing.xgetter.Utils.GDriveUtils.getCookie;
import static com.htetznaing.xgetter.Utils.GDriveUtils.getDRIVE_STREAM;
import static com.htetznaing.xgetter.Utils.GDriveUtils.get_drive_id;
import static com.htetznaing.xgetter.Utils.Utils.getDomainFromURL;
import static com.htetznaing.xgetter.Utils.Utils.putModel;
import static com.htetznaing.xgetter.Utils.Utils.sortMe;

/*
 *      xGetter
 *         By
 *   Khun Htetz Naing
 *   https://facebook.com/KhunHtetzNaing0
 * Repo => https://github.com/KhunHtetzNaing/xGetter
 * Google Drive,Google Photos,Mp4Upload,Facebook,Mediafire,Ok.Ru,VK,Twitter,Youtube,SolidFiles,Vidoza,UptoStream,SendVid,FanSubs,Uptobox,FEmbed,FileRio,DailyMotion,MegaUp,GoUnlimited,CocoScope,VidBM Stream/Download URL Finder!
 *
 */

public class XGetter {
    private String cookie = null;
    private WebView webView;
    private Context context;
    private OnTaskCompleted onComplete;
    public static final String agent = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.99 Safari/537.36";
    private final String mp4upload = "https?:\\/\\/(www\\.)?(mp4upload)\\.[^\\/,^\\.]{2,}\\/.+";
    private final String filerio = "https?:\\/\\/(www\\.)?(filerio)\\.[^\\/,^\\.]{2,}\\/.+";
    private final String sendvid = "https?:\\/\\/(www\\.)?(sendvid)\\.[^\\/,^\\.]{2,}\\/.+";
    private final String gphoto = "https?:\\/\\/(photos.google.com)\\/(u)?\\/?(\\d)?\\/?(share)\\/.+(key=).+";
    private final String mediafire = "https?:\\/\\/(www\\.)?(mediafire)\\.[^\\/,^\\.]{2,}\\/(file)\\/.+";
    private final String okru = "https?:\\/\\/(www.|m.)?(ok)\\.[^\\/,^\\.]{2,}\\/(video|videoembed)\\/.+";
    private final String vk = "https?:\\/\\/(www\\.)?vk\\.[^\\/,^\\.]{2,}\\/video\\-.+";
    private final String twitter = "http(?:s)?:\\/\\/(?:www\\.)?twitter\\.com\\/([a-zA-Z0-9_]+)";
    private final String youtube = "^((?:https?:)?\\/\\/)?((?:www|m)\\.)?((?:youtube\\.com|youtu.be))(\\/(?:[\\w\\-]+\\?v=|embed\\/|v\\/)?)([\\w\\-]+)(\\S+)?$";
    private final String solidfiles = "https?:\\/\\/(www\\.)?(solidfiles)\\.[^\\/,^\\.]{2,}\\/(v)\\/.+";
    private final String vidoza = "https?:\\/\\/(www\\.)?(vidoza)\\.[^\\/,^\\.]{2,}.+";
    private final String uptostream = "https?:\\/\\/(www\\.)?(uptostream|uptobox)\\.[^\\/,^\\.]{2,}.+";
    private final String fansubs = "https?:\\/\\/(www\\.)?(fansubs\\.tv)\\/(v|watch)\\/.+";
    private final String fembed = "https?:\\/\\/(www\\.)?(fembed|vcdn)\\.[^\\/,^\\.]{2,}\\/(v|f)\\/.+";
    private final String megaup = "https?:\\/\\/(www\\.)?(megaup)\\.[^\\/,^\\.]{2,}\\/.+";
    private final String gounlimited = "https?:\\/\\/(www\\.)?(gounlimited)\\.[^\\/,^\\.]{2,}\\/.+";
    private final String cocoscope = "https?:\\/\\/(www\\.)?(cocoscope)\\.[^\\/,^\\.]{2,}\\/(watch\\?v).+";
    private final String vidbm = "https?:\\/\\/(www\\.)?(vidbm)\\.[^\\/,^\\.]{2,}\\/.+";
    private final String muvix = "https?:\\/\\/(www\\.)?(muvix)\\.[^\\/,^\\.]{2,}\\/(video|download).+";
    private final String pstream = "https?:\\/\\/(www\\.)?(pstream)\\.[^\\/,^\\.]{2,}\\/(.*)\\/.+";
    private final String vlareTV = "https?:\\/\\/(www\\.)?(vlare\\.tv)\\/(.*)\\/.+";
    private final String vivoSX = "https?:\\/\\/(www\\.)?(vivo\\.sx)\\/.+";
    private final String streamKiwi = "https?:\\/\\/(www\\.)?(stream\\.kiwi)\\/(.*)\\/.+";
    private final String bitTube = "https?:\\/\\/(www\\.)?(bittube\\.video\\/videos)\\/(watch|embed)\\/.+";
    private final String videoBIN = "https?:\\/\\/(www\\.)?(videobin\\.co)\\/.+";
    private final String fourShared = "https?:\\/\\/(www\\.)?(4shared\\.com)\\/(video|web\\/embed)\\/.+";
    private final String streamtape = "https?:\\/\\/(www\\.)?(streamtape\\.com)\\/(v)\\/.+";
    private final String vudeo = "https?:\\/\\/(www\\.)?(vudeo\\.net)\\/.+";

    public XGetter(Context view) {
        this.context = view;
    }

    private void init() {
        webView = new WebView(context);
        webView.setWillNotDraw(true);
        webView.addJavascriptInterface(new xJavascriptInterface(), "xGetter");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        AndroidNetworking.initialize(context);
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                letFuck(view);
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                return super.onConsoleMessage(consoleMessage);
            }
        });

        letFuck(webView);
    }

    class xJavascriptInterface {
        @SuppressWarnings("unused")
        @JavascriptInterface
        public void fuck(final String url) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                public void run() {
                    ArrayList<XModel> xModels = new ArrayList<>();
                    putModel(url,"",xModels);
                    onComplete.onTaskCompleted(xModels,false);
                }
            });
        }
    }

    public void find(String url) {
        init();
        boolean fb = false;
        boolean run = false;
        boolean isVudeo = false, isStreamTape = false, mfire = false, isOkRu = false,isVk=false,tw=false,gdrive=false,yt=false,solidf=false,isvidoza=false,isuptostream=false,isFanSubs=false,isMP4Uload=false,isSendVid = false,isFembed=false,isVeryStream = false,isFileRio=false,isDailyMotion=false,isMegaUp=false,isGoUnlimited = false,isCocoscope=false,isVidBM=false,isMuvix = false,isPStream=false,isVlareTV = false,isVivoSX=false,isStreamKiwi=false,isBitTube=false,isVideoBin=false,is4Shared;
       if (check(mp4upload, url)) {
            run = true;
            isMP4Uload = true;
            if (!url.contains("embed-")) {
                final String regex = "com\\/([^']*)";
                final Pattern pattern = Pattern.compile(regex);
                final Matcher matcher = pattern.matcher(url);
                if (matcher.find()) {
                    String id = matcher.group(1);
                    if (id.contains("/")) {
                        id = id.substring(0, id.lastIndexOf("/"));
                    }
                    url = "https://www.mp4upload.com/embed-" + id + ".html";
                } else {
                    run = false;
                }
            }

        } else if (check(sendvid, url)) {
            //sendvid
            run = true;
            isSendVid = true;
        } else if (check(gphoto, url)) {
            //gphotos
            run = true;
        } else if (url.contains("drive.google.com") && get_drive_id(url) != null) {
            //gdrive
            run = true;
            gdrive = true;
//            url = get_drive_id(url);
        } else if (check_fb_video(url)) {
            //fb
            run = true;
            fb = true;
        } else if (check(mediafire, url)) {
            //mediafire
            run = true;
            mfire = true;
            if (!url.startsWith("https")){
                url = url.replace("http","https");
            }
        } else if (check(okru,url)){
            run = true;
            isOkRu = true;
            if (!url.startsWith("https")){
                url = url.replace("http","https");
            }

            if (url.contains("m.")){
                url = url.replace("m.","");
            }

            if (url.contains("/video/")){
                url = url.replace("/video/","/videoembed/");
            }

        } else if (check(vk,url)){
            run = true;
            isVk = true;
            if (!url.startsWith("https")){
                url = url.replace("http","https");
            }
        }else if (check(twitter,url)){
            run = true;
            tw = true;
//        }else if (check(youtube,url)){
//            run = true;
//            yt = true;
        }else if (check(solidfiles,url)){
            run = true;
            solidf = true;
        } else if (check(vidoza, url)) {
        //Vidoza

            isvidoza=true;
            run = true;
            if (!url.contains("embed-")) {
                final String regex = "net\\/([^']*)";
                final Pattern pattern = Pattern.compile(regex);
                final Matcher matcher = pattern.matcher(url);
                if (matcher.find()) {
                    String id = matcher.group(1);
                    if (id.contains("/")) {
                        id = id.substring(0, id.lastIndexOf("/"));
                    }
                    url = getDomainFromURL(url)+"/embed-" + id;
                } else {
                    run = false;
                }
            }
        }else if (check(uptostream, url)) {
            //uptostream, uptobox
            isuptostream=true;
            run = true;
        }else if (check(fansubs,url)){
            isFanSubs = true;
            run = true;
        }else if (check(fembed,url)){
            isFembed = true;
            run = true;
        }else if (check(filerio,url)){
            isFileRio = true;
            run = true;
            if (!url.contains("embed-")) {
                final String regex = "in\\/([^']*)";
                final Pattern pattern = Pattern.compile(regex);
                final Matcher matcher = pattern.matcher(url);
                if (matcher.find()) {
                    String id = matcher.group(1);
                    if (id.contains("/")) {
                        id = id.substring(0, id.lastIndexOf("/"));
                    }
                    url = getDomainFromURL(url)+"/embed-" + id + ".html";
                } else {
                    run = false;
                }
            }
        }else if (DailyMotion.getDailyMotionID(url)!=null){
            isDailyMotion = true;
            run = true;
        }else if (check(megaup,url)){
           //Megaup
           isMegaUp = true;
           run = true;
       }else if (check(gounlimited,url)){
           //https://gounlimited.to/
           isGoUnlimited = true;
           run = true;
       }else if (check(cocoscope,url)){
           //https://www.cocoscope.com/
           isCocoscope = true;
           run = true;
       }else if (check(vidbm,url)){
           //https://www.vidbm.com/
           run = true;
           isVidBM = true;
       }else if (check(muvix,url)){
           //https://muvix.us/video/kAoMUo4QMpwWTkn/
           url = url.replaceAll("/video/","/download/");
           isMuvix = true;
           run = true;
       }else if (check(pstream,url)){
           //https://www.pstream.net/v/BRrExbMveZOvP4B
           run = true;
           isPStream = true;
       }else if(check(vlareTV,url)){
           //https://vlare.tv/v/V0egtPxz
           run = true;
           isVlareTV = true;
       }else if (check(vivoSX,url)){
           //https://vivo.sx/60d91cc3aa
           run = true;
           isVivoSX = true;
       }else if (check(streamKiwi,url)){
           //https://stream.kiwi/e/Auy1iW
           run = true;
           isStreamKiwi = true;
       }else if (check(bitTube,url)){
           //https://bittube.video/videos/watch/36231473-613d-47c6-89a3-4bff2502dc92
           run = true;
           isBitTube = true;
       }else if (check(videoBIN,url)){
           //https://videobin.co/4swhhd3thhe7
           isVideoBin = true;
           run = true;
       }else if (check(fourShared,url)){
           //https://www.4shared.com/video/bLza9r9mea/45016068_2204489923208618_5254.html
           is4Shared = true;
           run = true;
       }else if (check(streamtape,url)){
           //https://streamtape.com/v/GbmzAG9ZaVHlzK/%5BAsahi%5D_Fugou_Keiji_-_Balance_-_UNLIMITED_-_01_%5B1080p%5D.mp4
           isStreamTape = true;
           run = true;
       }else if (check(vudeo,url)){
           //https://vudeo.net/azhfxfpzq6yq.html
           isVudeo = true;
           run = true;
       }

        if (run) {
            if (check(gphoto, url)) {
                gphotoORfb(url, false);
            } else if (fb) {
                gphotoORfb(url, true);
            } else if (mfire) {
                mfire(url);
            } else if (isOkRu){
                okru(url);
            } else if (isVk) {
                vk(url);
            } else if (tw) {
                twitter(url);
            } else if (gdrive) {
                gdrive(url);
//            } else if (yt) {
//                youtube(url);
            } else if (solidf){
                solidfiles(url);
            } else if (isvidoza){
                vidozafiles(url);
            } else if (isuptostream) {
                uptoStream(url);
            } else if (isFanSubs) {
                fansubs(url);
            } else if (isMP4Uload) {
                mp4upload(url);
            } else if (isSendVid){
                sendvid(url);
            } else if (isFembed){
                fEmbed(url);
            } else if (isFileRio){
                sendvid(url);
            } else if (isDailyMotion){
                dailyMotion(url);
            } else if (isMegaUp){
                megaUp(url);
            } else if (isGoUnlimited){
                goUnlimited(url);
            } else if (isCocoscope){
                cocoScope(url);
            } else if (isVidBM){
                vidBM(url);
            } else if (isMuvix){
                muvix(url);
            } else if (isPStream){
                Pstream.fetch(url,onComplete);
            } else if (isVlareTV){
                Vlare.fetch(url,onComplete);
            } else if (isVivoSX){
                VivoSX.fetch(url,onComplete);
            } else if (isStreamKiwi){
                StreamKIWI.get(context,url,onComplete);
            } else if (isBitTube){
                BitTube.fetch(url,onComplete);
            } else if (isVideoBin){
                VideoBIN.fetch(url,onComplete);
            } else if (isStreamKiwi){
                StreamKIWI.get(context,url,onComplete);
            } else if (isStreamTape){
                StreamTape.fetch(url,onComplete);
            } else if (isVudeo){
                Vudeo.fetch(url,onComplete);
            }
        }else onComplete.onError();
    }

    private void muvix(String url) {
        AndroidNetworking.get(url)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        ArrayList<XModel> xModels = Muvix.fetch(response);
                        if (xModels!=null){
                            onComplete.onTaskCompleted(xModels,true);
                        }else onComplete.onError();
                    }

                    @Override
                    public void onError(ANError anError) {
                        onComplete.onError();
                    }
                });
    }

    private void vidBM(String url) {
//        url = url.replaceAll(".com/",".com/embed-");
//        AndroidNetworking.get(url)
//                .setUserAgent(agent)
//                .build()
//                .getAsString(new StringRequestListener() {
//                    @Override
//                    public void onResponse(String response) {
//                        ArrayList<XModel> xModels = VidBM.fetch(response);
//                        if (xModels!=null){
//                            onComplete.onTaskCompleted(xModels,false);
//                        }else onComplete.onError();
//                    }
//
//                    @Override
//                    public void onError(ANError anError) {
//                        onComplete.onError();
//                    }
//                });

        VideoBmX.get(context,url,onComplete);
    }

    private void megaUp(String url) {
        new MegaUp().get(context, url,new MegaUp.OnDone() {
            @Override
            public void result(String result) {
                if (result!=null){
                    ArrayList<XModel> xModels = new ArrayList<>();
                    XModel xModel = new XModel();
                    xModel.setUrl(result);
                    xModel.setQuality("Normal");
                    xModels.add(xModel);
                    onComplete.onTaskCompleted(xModels,false);
                }else {
                    onComplete.onError();
                }
            }
        });
    }

    private void solidfiles(final String url){
        AndroidNetworking.get(url)
                .addHeaders("User-Agent","Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.99 Safari/537.36")
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        ArrayList<XModel> xModels = SolidFiles.fetch(response);
                        if (xModels!=null){
                            onComplete.onTaskCompleted(xModels,false);
                        }else onComplete.onError();
                    }

                    @Override
                    public void onError(ANError anError) {
                        onComplete.onError();
                    }
                });
    }

//    private void fruits(final String url){
//        AndroidNetworking.get(url)
//                .build()
//                .getAsString(new StringRequestListener() {
//                    @Override
//                    public void onResponse(String response) {
//                        ArrayList<XModel> xModels = Fruits.fetch(response);
//                        if (xModels!=null){
//                            onComplete.onTaskCompleted(xModels,false);
//                        }else onComplete.onError();
//                    }
//
//                    @Override
//                    public void onError(ANError anError) {
//                        onComplete.onError();
//                    }
//                });
//    }

    private void gdrive(final String url){
        GDrive2020.get(context,url,onComplete);
//
//        CookieJar cookieJar = new CookieJar() {
//            private final HashMap<String, List<Cookie>> cookieStore = new HashMap<>();
//
//            @Override
//            public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
//                System.out.println("saveFromResponse: "+cookies);
//                cookie = getDRIVE_STREAM(cookies.toString())+getCookie(cookies.toString());
//
//                System.out.println("saveFromResponse: Result => "+cookie);
//                cookieStore.put(url.host(), cookies);
//            }
//
//            @Override
//            public List<Cookie> loadForRequest(HttpUrl url) {
//                List<Cookie> cookies = cookieStore.get(url.host());
//                return cookies != null ? cookies : new ArrayList<Cookie>();
//            }
//        };
//
//        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .cookieJar(cookieJar)
//                .build();
//
//        AndroidNetworking.get("https://drive.google.com/get_video_info?docid="+url)
//                .setOkHttpClient(okHttpClient)
//                .build()
//                .getAsString(new StringRequestListener() {
//                    @Override
//                    public void onResponse(String response) {
//                        ArrayList<XModel> xModels = GDrive.fetch(cookie,response);
//                        if (xModels!=null && cookie!=null && !cookie.contains("null")) {
//                            onComplete.onTaskCompleted(sortMe(xModels), true);
//                            cookie = null;
//                        }else onComplete.onError();
//                    }
//
//                    @Override
//                    public void onError(ANError anError) {
//                        onComplete.onError();
//                    }
//                });
    }

    private void twitter(final String url){
        AndroidNetworking.post("https://twdown.net/download.php")
                .addBodyParameter("URL", url)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        onComplete.onTaskCompleted(sortMe(Twitter.fetch(response)),true);
                    }

                    @Override
                    public void onError(ANError anError) {
                        onComplete.onError();
                    }
                });
    }

    private void mp4upload(final String url){
        AndroidNetworking.get(url)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        ArrayList<XModel> xModels = MP4Upload.fetch(response);
                        if (xModels!=null){
                            onComplete.onTaskCompleted(xModels,false);
                        }else onComplete.onError();
                    }

                    @Override
                    public void onError(ANError anError) {
                        onComplete.onError();
                    }
                });
    }

    private void goUnlimited(final String url){
        AndroidNetworking.get(url)
                .setUserAgent(agent)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        ArrayList<XModel> xModels = GoUnlimitedTO.fetch(response);
                        if (xModels!=null){
                            onComplete.onTaskCompleted(xModels,false);
                        }else onComplete.onError();
                    }

                    @Override
                    public void onError(ANError anError) {
                        onComplete.onError();
                    }
                });
    }

    private void mfire(String url) {
        AndroidNetworking.get(url)
                .addHeaders("User-agent", agent)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        final String regex = "aria-label=\"Download file\"\\n.+href=\"(.*)\"";
                        final Pattern pattern = Pattern.compile(regex);
                        final Matcher matcher = pattern.matcher(response);
                        if (matcher.find()) {
                            ArrayList<XModel> xModels = new ArrayList<>();
                            putModel(matcher.group(1),"",xModels);
                            onComplete.onTaskCompleted(xModels,false);
                        }else onComplete.onError();
                    }

                    @Override
                    public void onError(ANError anError) {
                        onComplete.onError();
                    }
                });
    }

    private void gphotoORfb(String url, final boolean fb) {
        if (url != null) {
            if (fb){
                AndroidNetworking.post("https://fbdown.net/download.php")
                        .addBodyParameter("URLz", "https://www.facebook.com/video.php?v="+ url)
                        .addHeaders("User-agent", agent)
                        .build()
                        .getAsString(new StringRequestListener() {
                            @Override
                            public void onResponse(String response) {
                                ArrayList<XModel> xModels = new ArrayList<>();
                                putModel(getFbLink(response, false),"SD",xModels);
                                putModel(getFbLink(response, true),"HD",xModels);
                                onComplete.onTaskCompleted(xModels,true);
                            }

                            @Override
                            public void onError(ANError anError) {
                                onComplete.onError();
                            }
                        });
            }else {
                AndroidNetworking.get(url)
                        .addHeaders("User-agent", agent)
                        .build()
                        .getAsString(new StringRequestListener() {
                            @Override
                            public void onResponse(String response) {
                                ArrayList<XModel> xModels = GPhotosUtils.getGPhotoLink(response);
                                onComplete.onTaskCompleted(xModels,true);
                            }

                            @Override
                            public void onError(ANError anError) {
                                onComplete.onError();
                            }
                        });
            }
        } else onComplete.onError();
    }

    private boolean check(String regex, String string) {
        final Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        final Matcher matcher = pattern.matcher(string);
        return matcher.find();
    }

    public interface OnTaskCompleted {
        void onTaskCompleted(ArrayList<XModel> vidURL,boolean multiple_quality);
        void onError();
    }

    public void onFinish(OnTaskCompleted onComplete) {
        this.onComplete = onComplete;
    }

//    private void openload(final String url) {
//        if (url != null) {
//            AndroidNetworking.get(url)
//                    .addHeaders("User-agent", agent)
//                    .build()
//                    .getAsString(new StringRequestListener() {
//                        @Override
//                        public void onResponse(String response) {
//                            String longString = getLongEncrypt(response);
//                            if (longString==null){
//                                longString = getLongEncrypt2(response);
//                            }
//                            String key1 = getKey1(response);
//                            String key2 = getKey2(response);
//                            String js = "ZnVuY3Rpb24gZ2V0T3BlbmxvYWRVUkwoZW5jcnlwdFN0cmluZywga2V5MSwga2V5MikgewogICAgdmFyIHN0cmVhbVVybCA9ICIiOwogICAgdmFyIGhleEJ5dGVBcnIgPSBbXTsKICAgIGZvciAodmFyIGkgPSAwOyBpIDwgOSAqIDg7IGkgKz0gOCkgewogICAgICAgIGhleEJ5dGVBcnIucHVzaChwYXJzZUludChlbmNyeXB0U3RyaW5nLnN1YnN0cmluZyhpLCBpICsgOCksIDE2KSk7CiAgICB9CiAgICBlbmNyeXB0U3RyaW5nID0gZW5jcnlwdFN0cmluZy5zdWJzdHJpbmcoOSAqIDgpOwogICAgdmFyIGl0ZXJhdG9yID0gMDsKICAgIGZvciAodmFyIGFyckl0ZXJhdG9yID0gMDsgaXRlcmF0b3IgPCBlbmNyeXB0U3RyaW5nLmxlbmd0aDsgYXJySXRlcmF0b3IrKykgewogICAgICAgIHZhciBtYXhIZXggPSA2NDsKICAgICAgICB2YXIgdmFsdWUgPSAwOwogICAgICAgIHZhciBjdXJySGV4ID0gMjU1OwogICAgICAgIGZvciAodmFyIGJ5dGVJdGVyYXRvciA9IDA7IGN1cnJIZXggPj0gbWF4SGV4OyBieXRlSXRlcmF0b3IgKz0gNikgewogICAgICAgICAgICBpZiAoaXRlcmF0b3IgKyAxID49IGVuY3J5cHRTdHJpbmcubGVuZ3RoKSB7CiAgICAgICAgICAgICAgICBtYXhIZXggPSAweDhGOwogICAgICAgICAgICB9CiAgICAgICAgICAgIGN1cnJIZXggPSBwYXJzZUludChlbmNyeXB0U3RyaW5nLnN1YnN0cmluZyhpdGVyYXRvciwgaXRlcmF0b3IgKyAyKSwgMTYpOwogICAgICAgICAgICB2YWx1ZSArPSAoY3VyckhleCAmIDYzKSA8PCBieXRlSXRlcmF0b3I7CiAgICAgICAgICAgIGl0ZXJhdG9yICs9IDI7CiAgICAgICAgfQogICAgICAgIHZhciBieXRlcyA9IHZhbHVlIF4gaGV4Qnl0ZUFyclthcnJJdGVyYXRvciAlIDldIF4ga2V5MSBeIGtleTI7CiAgICAgICAgdmFyIHVzZWRCeXRlcyA9IG1heEhleCAqIDIgKyAxMjc7CiAgICAgICAgZm9yICh2YXIgaSA9IDA7IGkgPCA0OyBpKyspIHsKICAgICAgICAgICAgdmFyIHVybENoYXIgPSBTdHJpbmcuZnJvbUNoYXJDb2RlKCgoYnl0ZXMgJiB1c2VkQnl0ZXMpID4+IDggKiBpKSAtIDEpOwogICAgICAgICAgICBpZiAodXJsQ2hhciAhPSAiJCIpIHsKICAgICAgICAgICAgICAgIHN0cmVhbVVybCArPSB1cmxDaGFyOwogICAgICAgICAgICB9CiAgICAgICAgICAgIHVzZWRCeXRlcyA9IHVzZWRCeXRlcyA8PCA4OwogICAgICAgIH0KICAgIH0KICAgIC8vY29uc29sZS5sb2coc3RyZWFtVXJsKQogICAgcmV0dXJuIHN0cmVhbVVybDsKfQp2YXIgZW5jcnlwdFN0cmluZyA9ICJIdGV0ekxvbmdTdHJpbmciOwp2YXIga2V5TnVtMSA9ICJIdGV0ektleTEiOwp2YXIga2V5TnVtMiA9ICJIdGV0ektleTIiOwp2YXIga2V5UmVzdWx0MSA9IDA7CnZhciBrZXlSZXN1bHQyID0gMDsKdmFyIG9ob3N0ID0gIkh0ZXR6SG9zdCI7Ci8vY29uc29sZS5sb2coZW5jcnlwdFN0cmluZywga2V5TnVtMSwga2V5TnVtMik7CnRyeSB7CiAgICB2YXIga2V5TnVtMV9PY3QgPSBwYXJzZUludChrZXlOdW0xLm1hdGNoKC9wYXJzZUludFwoJyguKiknLDhcKS8pWzFdLCA4KTsKICAgIHZhciBrZXlOdW0xX1N1YiA9IHBhcnNlSW50KGtleU51bTEubWF0Y2goL1wpXC0oW15cK10qKVwrLylbMV0pOwogICAgdmFyIGtleU51bTFfRGl2ID0gcGFyc2VJbnQoa2V5TnVtMS5tYXRjaCgvXC9cKChbXlwtXSopXC0vKVsxXSk7CiAgICB2YXIga2V5TnVtMV9TdWIyID0gcGFyc2VJbnQoa2V5TnVtMS5tYXRjaCgvXCsweDRcLShbXlwpXSopXCkvKVsxXSk7CiAgICBrZXlSZXN1bHQxID0gKGtleU51bTFfT2N0IC0ga2V5TnVtMV9TdWIgKyA0IC0ga2V5TnVtMV9TdWIyKSAvIChrZXlOdW0xX0RpdiAtIDgpOwogICAgdmFyIGtleU51bTJfT2N0ID0gcGFyc2VJbnQoa2V5TnVtMi5tYXRjaCgvXCgnKFteJ10qKScsLylbMV0sIDgpOwogICAgdmFyIGtleU51bTJfU3ViID0gcGFyc2VJbnQoa2V5TnVtMi5zdWJzdHIoa2V5TnVtMi5pbmRleE9mKCIpLSIpICsgMikpOwogICAga2V5UmVzdWx0MiA9IGtleU51bTJfT2N0IC0ga2V5TnVtMl9TdWI7CiAgICBjb25zb2xlLmxvZyhrZXlOdW0xLCBrZXlOdW0yKTsKfSBjYXRjaCAoZSkgewogICAgLy9jb25zb2xlLmVycm9yKGUuc3RhY2spOwogICAgdGhyb3cgRXJyb3IoIktleSBOdW1iZXJzIG5vdCBwYXJzZWQhIik7Cn0KdmFyIHNyYyA9IG9ob3N0ICsgJy9zdHJlYW0vJyArIGdldE9wZW5sb2FkVVJMKGVuY3J5cHRTdHJpbmcsIGtleVJlc3VsdDEsIGtleVJlc3VsdDIpOwp4R2V0dGVyLmZ1Y2soc3JjKTs=";
//                            js = base64Decode(js);
//                            js = js.replace("HtetzLongString", longString);
//                            js = js.replace("HtetzKey1", key1);
//                            js = js.replace("HtetzKey2", key2);
//                            js = js.replace("HtetzHost",getDomainFromURL(url));
//                            js = base64Encode(js);
//                            webView.loadUrl("javascript:(function() {" +
//                                    "var parent = document.getElementsByTagName('head').item(0);" +
//                                    "var script = document.createElement('script');" +
//                                    "script.type = 'text/javascript';" +
//                                    // Tell the browser to BASE64-decode the string into your script !!!
//                                    "script.innerHTML = window.atob('" + js + "');" +
//                                    "parent.appendChild(script)" +
//                                    "})()");
//                        }
//
//                        @Override
//                        public void onError(ANError anError) {
//                            onComplete.onError();
//                        }
//                    });
//        }
//    }

    private void okru(String url) {
        if (url != null) {

            AndroidNetworking.get(url)
                    .addHeaders("User-agent", "Mozilla/5.0 (Linux; Android 4.1.1; Galaxy Nexus Build/JRO03C) AppleWebKit/535.19 (KHTML, like Gecko) Chrome/18.0.1025.166 Mobile Safari/535.19")
                    .build()
                    .getAsString(new StringRequestListener() {
                        @Override
                        public void onResponse(String response) {
                            String json = getJson(response);
                            if (json!=null) {
                                json = StringEscapeUtils.unescapeHtml4(json);
                                try {
                                    json = new JSONObject(json).getJSONObject("flashvars").getString("metadata");
                                    if (json!=null) {
                                        JSONArray jsonArray = new JSONObject(json).getJSONArray("videos");
                                        ArrayList<XModel> models = new ArrayList<>();
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            String url = jsonArray.getJSONObject(i).getString("url");
                                            String name = jsonArray.getJSONObject(i).getString("name");
                                            if (name.equals("mobile")) {
                                                putModel(url, "144p", models);
                                            } else if (name.equals("lowest")) {
                                                putModel(url, "240p", models);
                                            } else if (name.equals("low")) {
                                                putModel(url, "360p", models);
                                            } else if (name.equals("sd")) {
                                                putModel(url, "480p", models);
                                            } else if (name.equals("hd")) {
                                                putModel(url, "720p", models);
                                            } else if (name.equals("full")) {
                                                putModel(url, "1080p", models);
                                            } else if (name.equals("quad")) {
                                                putModel(url, "2000p", models);
                                            } else if (name.equals("ultra")) {
                                                putModel(url, "4000p", models);
                                            } else {
                                                putModel(url, "Default", models);
                                            }
                                        }
                                        onComplete.onTaskCompleted(sortMe(models), true);
                                    }else {
                                        onComplete.onError();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    onComplete.onError();
                                }
                            }else onComplete.onError();
                        }

                        private String getJson(String html){
                            final String regex = "data-options=\"(.*?)\"";
                            final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
                            final Matcher matcher = pattern.matcher(html);
                            if (matcher.find()) {
                                return matcher.group(1);
                            }
                            return null;
                        }

                        @Override
                        public void onError(ANError anError) {
                            onComplete.onError();
                        }
                    });
        }
    }

    private void fansubs(final String mUrl){
        AndroidNetworking.get(mUrl)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        ArrayList<XModel> models = new ArrayList<>();
                        Document document = Jsoup.parse(response);
                        if (document.html().contains("<source")){
                            Elements element = document.getElementsByTag("source");
                            for (int i=0;i<element.size();i++){
                                Element temp = element.get(i);
                                if (temp.hasAttr("src")) {
                                    String url = temp.attr("src");
                                    putModel(url, temp.attr("label"), models);
                                }
                            }
                        }
                        if (models.size()!=0){
                            onComplete.onTaskCompleted(sortMe(models),true);
                        }else onComplete.onError();
                    }

                    @Override
                    public void onError(ANError anError) {
                        System.out.println(anError.getErrorBody());
                        onComplete.onError();
                    }
                });
    }

    private void vk(String url) {
        if (url != null) {
            AndroidNetworking.get(url)
                    .addHeaders("User-agent", agent)
                    .build()
                    .getAsString(new StringRequestListener() {
                        @Override
                        public void onResponse(String response) {
                            String json = get("al_video.php', ?(\\{.*])",response);
                            json = get("\\}, ?(.*)",json);

                            try {
                                ArrayList<XModel> models = new ArrayList<>();
                                String x240="url240",x360="url360",x480="url480",x720="url720",x1080="url1080";
                                JSONObject object = new JSONArray(json).getJSONObject(4).getJSONObject("player").getJSONArray("params").getJSONObject(0);

                                if (object.has(x240)){
                                    putModel(object.getString(x240),"240p",models);
                                }

                                if (object.has(x360)){
                                    putModel(object.getString(x360),"360p",models);
                                }

                                if (object.has(x480)){
                                    putModel(object.getString(x480),"480p",models);
                                }

                                if (object.has(x720)){
                                    putModel(object.getString(x720),"720p",models);
                                }

                                if (object.has(x1080)){
                                    putModel(object.getString(x1080),"1080p",models);
                                }
                                onComplete.onTaskCompleted(sortMe(models),true);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                onComplete.onError();
                            }
                        }

                        private String get(String regex,String html){
                            final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
                            final Matcher matcher = pattern.matcher(html);
                            if (matcher.find()) {
                                return matcher.group(1);
                            }
                            return null;
                        }

                        @Override
                        public void onError(ANError anError) {
                            onComplete.onError();
                        }
                    });
        }
    }

    public static String getUpToStreamID(String string) {
        final String regex = "[-\\w]{12,}";
        final Pattern pattern = Pattern.compile(regex);
        final Matcher matcher = pattern.matcher(string);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }

    private void uptoStream(final String url) {
        if (url != null) {
            AndroidNetworking.get("https://uptostream.com/api/streaming/source/get?token=&file_code="+getUpToStreamID(url))
                    .build()
                    .getAsString(new StringRequestListener() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                response = new JSONObject(response).getJSONObject("data").getString("sources");
                                new UpToStream().get(context, response, new UpToStream.OnDone() {
                                    @Override
                                    public void result(String result) {
                                        if (result!=null){
                                            try {
                                                JSONArray array = new JSONArray(result);
                                                ArrayList<XModel> xModels = new ArrayList<>();
                                                for (int i=0;i<array.length();i++){
                                                    String src = array.getJSONObject(i).getString("src");
                                                    String label = array.getJSONObject(i).getString("label");
                                                    String lang = array.getJSONObject(i).getString("lang");
                                                    if (lang!=null && !lang.isEmpty()){
                                                        lang = lang.toUpperCase();
                                                    }

                                                    String quality=label+","+ lang;
                                                    putModel(quality,src,xModels);
                                                    putModel(src,quality,xModels);
                                                }

                                                if (xModels.size()!=0) {
                                                    onComplete.onTaskCompleted(sortMe(xModels), true);
                                                }else onComplete.onError();
                                            } catch (JSONException e) {
                                                onComplete.onError();
                                            }
                                        }else {
                                            onComplete.onError();
                                        }
                                    }

                                    @Override
                                    public void retry() {
                                        uptoStream(url);
                                    }
                                });
                            } catch (JSONException e) {
                                e.printStackTrace();
                                onComplete.onError();
                            }
                        }

                        private String get(String regex,String code){
                            final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
                            final Matcher matcher = pattern.matcher(code);
                            code = null;
                            while (matcher.find()) {
                                for (int i = 1; i <= matcher.groupCount(); i++) {
                                    code = matcher.group(i);
                                }
                            }

                            return code;
                        }

                        @Override
                        public void onError(ANError anError) {
                            onComplete.onError();
                        }
                    });
        }
    }

    private void vidozafiles(final String url){
        AndroidNetworking.get(url)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        ArrayList<XModel> xModels = Vidoza.fetch(response);
                        if (xModels!=null) {
                            onComplete.onTaskCompleted(xModels, false);
                        }else onComplete.onError();
                    }

                    @Override
                    public void onError(ANError anError) {
                        onComplete.onError();
                    }
                });
    }


    private void sendvid(String url){
        AndroidNetworking.get(url)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        String src = getSrc(response);
                        if (src!=null){
                            ArrayList<XModel> xModels = new ArrayList<>();
                            putModel(src,"Normal",xModels);
                            onComplete.onTaskCompleted(xModels,false);
                        }else onComplete.onError();
                    }

                    private String getSrc(String response){
                        final String regex = "<source ?src=\"(.*?)\"";
                        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
                        final Matcher matcher = pattern.matcher(response);

                        if (matcher.find()) {
                            return matcher.group(1);
                        }
                        return null;
                    }

                    @Override
                    public void onError(ANError anError) {
                        onComplete.onError();
                    }
                });
    }

    private void cocoScope(String url){
        AndroidNetworking.get(url)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        String src = getSrc(response);
                        boolean lowQuality = response.contains("VHSButton");
                        if (src!=null){
                            ArrayList<XModel> xModels = new ArrayList<>();
                            putModel(src,"Normal",xModels);
                            if (lowQuality) {
                                String last = src.substring(src.lastIndexOf("."));
                                src = src.replace(last, "_360" + last);
                                putModel(src, "Low", xModels);
                            }
                            onComplete.onTaskCompleted(xModels,lowQuality);
                        }else onComplete.onError();
                    }

                    private String getSrc(String response){
                        final String regex = "<source ?src=\"(.*?)\"";
                        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
                        final Matcher matcher = pattern.matcher(response);

                        if (matcher.find()) {
                            return matcher.group(1);
                        }
                        return null;
                    }

                    @Override
                    public void onError(ANError anError) {
                        onComplete.onError();
                    }
                });
    }

    private void fEmbed(String url){
        String id = Fembed.get_fEmbed_video_ID(url);
        if (id!=null){
            AndroidNetworking.post("https://www.fembed.com/api/source/"+id)
                    .build()
                    .getAsString(new StringRequestListener() {
                        @Override
                        public void onResponse(String response) {
                            ArrayList<XModel> xModels = Fembed.fetch(response);
                            if (xModels!=null){
                                onComplete.onTaskCompleted(sortMe(xModels),true);
                            }else onComplete.onError();
                        }

                        @Override
                        public void onError(ANError anError) {
                            onComplete.onError();
                        }
                    });
        }else onComplete.onError();
    }

    private void dailyMotion(String url){
        AndroidNetworking.get("https://www.dailymotion.com/embed/video/"+DailyMotion.getDailyMotionID(url))
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        new DailyMotion().fetch(response, new DailyMotion.OnDone() {
                            @Override
                            public void onResult(ArrayList<XModel> xModels) {
                                if (xModels!=null){
                                    onComplete.onTaskCompleted(sortMe(xModels),true);
                                }else onComplete.onError();
                            }
                        });
                    }

                    @Override
                    public void onError(ANError anError) {
                        onComplete.onError();
                    }
                });
    }


//    private void youtube(String url){
//        if (check(youtube,url)) {
//            new YouTubeExtractor(context) {
//                @Override
//                public void onExtractionComplete(SparseArray<YtFile> ytFiles, VideoMeta vMeta) {
//                    if (ytFiles != null) {
//                        ArrayList<XModel> xModels = new ArrayList<>();
//
//                        for (int i = 0, itag; i < ytFiles.size(); i++) {
//                            itag = ytFiles.keyAt(i);
//                            YtFile ytFile = ytFiles.get(itag);
//                            if (ytFile.getFormat().getExt().equals("mp4") && ytFile.getFormat().getAudioBitrate()!=-1){
//                                putModel(ytFile.getUrl(), ytFile.getFormat().getHeight() + "p", xModels);
//                            }
//                        }
//
//                        onComplete.onTaskCompleted(sortMe(xModels), true);
//                    }else {
//                        onComplete.onError();
//                    }
//                }
//            }.extract(url, false, false);
//        }else onComplete.onError();
//    }

    private void letFuck(WebView view) {
        byte[] bytes = Base64.decode("aHR0cHM6Ly9yYXcuZ2l0aGFjay5jb20vS2h1bkh0ZXR6TmFpbmcvRmlsZXMvbWFzdGVyL3hnZXR0ZXIuanM=".getBytes(),Base64.DEFAULT);
        view.loadUrl("javascript:(function() {" +
                "var parent = document.getElementsByTagName('head').item(0);" +
                "var script = document.createElement('script');" +
                "script.type = 'text/javascript';" +
                "script.src = '"+new String(bytes)+"';"+
                "parent.appendChild(script)" +
                "})()");
    }
}
