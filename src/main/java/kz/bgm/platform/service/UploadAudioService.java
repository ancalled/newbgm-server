package kz.bgm.platform.service;

import com.acrcloud.utils.ACRCloudExtrTool;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class UploadAudioService {

    private final String bucketName;
    private final String accessKey;
    private final String accessSecret;

    public UploadAudioService(String bucketName, String accessKey, String accessSecret) {
        this.bucketName = bucketName;
        this.accessKey = accessKey;
        this.accessSecret = accessSecret;
    }

    public String uploadFingerprint(String audioTitle, String artist, String album, String releaseDate, File file) {
        byte[] buffer = new byte[3 * 1024 * 1024];
        if (!file.exists()) {
            return null;
        }
        FileInputStream fin = null;
        int bufferLen = 0;
        try {
            fin = new FileInputStream(file);
            bufferLen = fin.read(buffer, 0, buffer.length);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fin != null) {
                    fin.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (bufferLen <= 0)
            return null;

        byte[] fp = ACRCloudExtrTool.createFingerprintByFileBuffer(buffer, bufferLen, 0, 0, true);
//        System.out.println(encodeBase64(fp));
        Map<String, Object> postParams = new HashMap<>();
        postParams.put("title", audioTitle);
        postParams.put("data_type", "fingerprint");
//        postParams.put("fingerprint", fp);

        Map<String, String> userParams = new HashMap<>();
        userParams.put("artist", artist);
        userParams.put("album", album);
        userParams.put("release_date", releaseDate);
        return upload(postParams, userParams);
    }

    public String uploadAudio(String audioTitle, String artist, String album, String releaseDate, File file) {
        Map<String, Object> postParams = new HashMap<>();
        postParams.put("title", audioTitle);
        postParams.put("data_type", "audio");
        postParams.put("audio_file", file);

        Map<String, String> userParams = new HashMap<>();
        userParams.put("artist", artist);
        userParams.put("album", album);
        userParams.put("release_date", releaseDate);
        return upload(postParams, userParams);
    }

    public String upload(Map<String, Object> postParams, Map<String, String> userParams) {
        String result;
        String reqUrl = "https://api.acrcloud.com/v1/audios";
        String htttMethod = "POST";
        String httpAction = "/v1/audios";
        String signatureVersion = "1";
        String timestamp = this.getUTCTimeSeconds();

        String sigStr = htttMethod + "\n" + httpAction + "\n" + accessKey
                + "\n" + signatureVersion + "\n" + timestamp;
        String signature = encryptByHMACSHA1(sigStr.getBytes(),
                accessSecret.getBytes());

        Map<String, String> headerParams = new HashMap<String, String>();
        headerParams.put("access-key", accessKey);
        headerParams.put("signature-version", signatureVersion);
        headerParams.put("signature", signature);
        headerParams.put("timestamp", timestamp);

        postParams.put("audio_id", timestamp);
        postParams.put("bucket_name", bucketName);

        if (userParams != null) {
            int i = 0;
            for (String key : userParams.keySet()) {
                String value = userParams.get(key);
                postParams.put("custom_key[" + i + "]", key);
                postParams.put("custom_value[" + i + "]", value);
                i++;
            }
        }

        result = this.postHttp(reqUrl, postParams, headerParams, 15000);

        return result;
    }

    private String postHttp(String url, Map<String, Object> postParams,
                            Map<String, String> headerParams, int timeout) {
        String result = null;

        if (postParams == null) {
            return result;
        }

        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            HttpPost httpPost = new HttpPost(url);

            if (headerParams != null) {
                for (String key : headerParams.keySet()) {
                    String value = headerParams.get(key);
                    httpPost.addHeader(key, value);
                }
            }

            MultipartEntityBuilder mEntityBuilder = MultipartEntityBuilder
                    .create();
            for (String key : postParams.keySet()) {
                Object value = postParams.get(key);
                if (value instanceof String || value instanceof Integer) {
                    ContentType contentType = ContentType.create("text/plain", "UTF-8");
                    StringBody stringBody = new StringBody(value + "", contentType);
                    mEntityBuilder.addPart(key, stringBody);
                } else if (value instanceof File) {
                    mEntityBuilder.addBinaryBody(key, (File) value);
                }
            }

            httpPost.setEntity(mEntityBuilder.build());

            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectionRequestTimeout(timeout)
                    .setConnectTimeout(timeout).setSocketTimeout(timeout)
                    .build();
            httpPost.setConfig(requestConfig);

            HttpResponse response = httpClient.execute(httpPost);

            System.out.println(response.getStatusLine().getStatusCode());

            HttpEntity entity = response.getEntity();
            result = EntityUtils.toString(entity);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
            }
        }
        return result;
    }

    private String encodeBase64(byte[] bstr) {
        Base64 base64 = new Base64();
        return new String(base64.encode(bstr));
    }

    private String encryptByHMACSHA1(byte[] data, byte[] key) {
        try {
            SecretKeySpec signingKey = new SecretKeySpec(key, "HmacSHA1");
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(signingKey);
            byte[] rawHmac = mac.doFinal(data);
            return encodeBase64(rawHmac);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private String getUTCTimeSeconds() {
        Calendar cal = Calendar.getInstance();
        int zoneOffset = cal.get(Calendar.ZONE_OFFSET);
        int dstOffset = cal.get(Calendar.DST_OFFSET);
        cal.add(Calendar.MILLISECOND, -(zoneOffset + dstOffset));
        return cal.getTimeInMillis() / 1000 + "";
    }

    public static void main(String[] args) {
        String bucketName = "Test upload";
        String accessKey = "2f4b42f185058853";
        String accessSecret = "5205c36cdc29f47aa2d406a59550e9de";

        String dataPath = "C:/Users/ik/Downloads/Мейрамбек Беспаев Кус Канат Гумыр.mp3";
        File file = new File(dataPath);
        if (!file.exists()) {
            return;
        }

        UploadAudioService ua = new UploadAudioService(bucketName, accessKey, accessSecret);
        String result = ua.uploadFingerprint("Кус Канат Гумыр", "Мейрамбек Беспаев", "Кус Канат Гумыр", "2012-02-09", file);
        if (result == null) {
            System.out.println("upload error");
        }

        System.out.println(result);
    }

}