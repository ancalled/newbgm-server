package kz.bgm.test;

import com.acrcloud.utils.ACRCloudRecognizer;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

public class TestUrlStream {

    public static void main(String[] args) {
        TestUrlStream test = new TestUrlStream();
        test.acr(test.listen());
    }

    private byte[] listen() {
        try {
            URLConnection conn = new URL("http://icecast.rmg.cdnvideo.ru/rr.mp3").openConnection();
            InputStream is = conn.getInputStream();
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] buffer = new byte[8192];
            int len;
            long t = System.currentTimeMillis();
            while ((len = is.read(buffer)) > 0 && System.currentTimeMillis() - t <= 20000) {
                output.write(buffer, 0, len);
            }
            return output.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void acr(byte[] buffer) {
        Map<String, Object> config = new HashMap<>();
        config.put("access_key", "dcc6f08c2ebb2b7d84d288f33f75eb26");
        config.put("access_secret", "Dn6TLuxFYFnCQLrXS9v854IhUEkkLlAIrtUKTvz3");
        config.put("debug", false);
        config.put("timeout", 5);

        ACRCloudRecognizer re = new ACRCloudRecognizer(config);

        int bufferLen = buffer.length;
        if (bufferLen <= 0)
            return;

//        byte[] fp = ACRCloudExtrTool.createFingerprintByFileBuffer(buffer, bufferLen, 0, 12, true);
//        System.out.println(re.encodeBase64(fp));
        String result = re.recognizeByFileBuffer(buffer, bufferLen, 0);
        System.out.println(result);
    }
}
