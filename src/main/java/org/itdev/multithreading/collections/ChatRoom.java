package org.itdev.multithreading.collections;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public class ChatRoom {

    private CopyOnWriteArrayList<String> messages = new CopyOnWriteArrayList<>();

    public static void main(String[] args) throws InterruptedException {
        ChatRoom chatRoom = new ChatRoom();

        Map<String, List<String>> messages = new HashMap<>(4);

        for (int i = 0; i < 4; i++) {
            List<String> list = new ArrayList<>(25);
            messages.put("supplier" + i, list);
            for (int j = 0; j < 25; j++) {
                list.add("Message " + j + "-" + i);
            }
        }

        Thread supplier0 = new Thread(() -> {
            for (int i = 0; i < 25; i++) {
                chatRoom.postMessage("supplier0", messages.get("supplier0").get(i));
            }
        });

        Thread supplier1 = new Thread(() -> {
            for (int i = 0; i < 25; i++) {
                chatRoom.postMessage("supplier1", messages.get("supplier1").get(i));
            }
        });

        Thread supplier2 = new Thread(() -> {
            for (int i = 0; i < 25; i++) {
                chatRoom.postMessage("supplier2", messages.get("supplier2").get(i));
            }
        });

        Thread supplier3 = new Thread(() -> {
            for (int i = 0; i < 25; i++) {
                chatRoom.postMessage("supplier3", messages.get("supplier3").get(i));
            }
        });

        Thread consumer = new Thread(() -> System.out.println(chatRoom.getRecentMessages(20)));

        supplier0.start();
        supplier1.start();
        supplier2.start();
        supplier3.start();
        supplier0.join();
        supplier1.join();
        supplier2.join();
        supplier3.join();
        consumer.start();
        consumer.join();
    }

    public void postMessage(String user, String text) {
        System.out.println(user + " post text: " + text);
        messages.add(user + ": " + text);
    }

    public List<String> getRecentMessages(int count) {

        System.out.println("Getting messages");
        List<String> res = new ArrayList<>(count);
        int size = messages.size();
        for (int i = size - 1; i >= size - count; i--) {
            res.add(messages.get(i));
        }
        return res;
    }
}
