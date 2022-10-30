package com.study.jwt.smsTest;

import net.nurigo.java_sdk.api.GroupMessage;
import net.nurigo.java_sdk.api.Image;
import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.api.SenderID;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.Test;

import java.util.HashMap;

public class smsTest {
  String api_key = "NCSCNGQWTYMTSURP";
  String api_secret = "CIMMFR6MNPYFWE5HQSF4EQZTOYDFL3B2";

  Message message = new Message(api_key, api_secret);
  GroupMessage groupMessage = new GroupMessage(api_key, api_secret);
  Image image = new Image(api_key, api_secret);
  SenderID senderID;
  JSONObject result;
  JSONArray result_array;
  HashMap<String, String> params = new HashMap<String, String>();

  @Test
  public void MessageTest() {
    Message coolsms = new Message(api_key, api_secret);

    // 4 params(to, from, type, text) are mandatory. must be filled
    HashMap<String, String> params = new HashMap<String, String>();
    params.put("to", "01057444274"); // 수신번호
    params.put("from", "01057444274"); // 발신번호
    params.put("type", "SMS"); // Message type ( SMS, LMS, MMS, ATA )
    params.put("text", "Test Message"); // 문자내용
    params.put("app_version", "JAVA SDK v1.2"); // application name and version

    try {
      JSONObject obj = (JSONObject) coolsms.send(params);
      System.out.println(obj.toString());
    } catch (CoolsmsException e) {
      System.out.println(e.getMessage());
      System.out.println(e.getCode());
    }
  }

}
