package bdbe.bdbd.pay;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

@Controller
public class kakaoPay {

    @ResponseBody
    @RequestMapping("/reservations/{reservation_id}/payment")
    public String kakaopay() {
        try {
            URL url = new URL("https://kapi.kakao.com/v1/payment/ready");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "KakaoAK 94b415d78ba08d5c99df3577982e974b");
            connection.setRequestProperty("Content-type", "Content-type: application/x-www-form-urlencoded;charset=utf-8");
            connection.setDoInput(true);
            String parameter = "cid=TC0ONETIM&partner_order_id=partner_order_id&partner_user_id=partner_user_id&item_name=하이세차장&quantity=1&total_amount=2200&tax_free_amount=0&approval_url=https://developers.kakao.com/success&cancel_url=https://developers.kakao.com/cancel&fail_url=https://developers.kakao.com/fail&";
            OutputStream outputStream = connection.getOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
            dataOutputStream.writeBytes(parameter);
            dataOutputStream.flush();
            dataOutputStream.close();

            int result = connection.getResponseCode();

            InputStream inputStream;
            if (result == 200) {
                inputStream = connection.getInputStream();
            } else {
                inputStream = connection.getErrorStream();
            }
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            return bufferedReader.readLine();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "{\"result\":\"NO\"}";
    }

}
