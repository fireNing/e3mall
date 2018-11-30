package cn.hpn.quest.listener;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class MyMessageListener implements MessageListener {


    public void onMessage(Message message) {
        try {
            TextMessage textMessage = (TextMessage) message;
            //取出消息内容;
            String text = textMessage.getText();
            System.out.println(text);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
