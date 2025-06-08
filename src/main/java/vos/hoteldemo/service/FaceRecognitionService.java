package vos.hoteldemo.service;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

@Service
public class FaceRecognitionService {
    public JSONObject compareFaces(InputStream checkinImage, InputStream checkoutImage) throws Exception {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost post = new HttpPost("http://127.0.0.1:5000/verify");
            byte[] checkinBytes = readAllBytes(checkinImage);
            byte[] checkoutBytes = readAllBytes(checkoutImage);
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            builder.addBinaryBody("image1", checkinBytes, ContentType.DEFAULT_BINARY, "checkin.jpg");
            builder.addBinaryBody("image2", checkoutBytes, ContentType.DEFAULT_BINARY, "checkout.jpg");
            HttpEntity entity = builder.build();
            post.setEntity(entity);
            try (CloseableHttpResponse response = client.execute(post)) {
                int statusCode = response.getStatusLine().getStatusCode();
                String responseString = EntityUtils.toString(response.getEntity());
                if (statusCode != 200) {
                    throw new RuntimeException("Flask trả mã lỗi: " + statusCode + " với nội dung: " + responseString);
                }
                return new JSONObject(responseString);
            }
        }
    }

    private byte[] readAllBytes(InputStream inputStream) throws Exception {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] temp = new byte[4096];
        int bytesRead;
        while ((bytesRead = inputStream.read(temp)) != -1) {
            buffer.write(temp, 0, bytesRead);
        }
        return buffer.toByteArray();
    }
}