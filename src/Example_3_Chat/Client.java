package Example_3_Chat;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

class Client implements Runnable {
    Socket socket;
    String nickname;


    Scanner in;
    PrintStream out;
    ChatServer server;

    public Client(Socket socket, ChatServer server) {

        this.socket = socket;
        this.server = server;
        // запускаем поток для клиента
        Thread thread = new Thread(this);
        thread.start();
    }

    void receive(String message) {
        out.println(message);
    }

    public void run() {
        try {
            // получаем потоки ввода и вывода
            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();

            // создаем удобные средства ввода и вывода
            in = new Scanner(is);
            out = new PrintStream(os);

            // читаем из сети и пишем в сеть
            out.println("Welcome to chat!");

            out.print("Enter your nik: ");
            nickname = in.nextLine();
            out.println("Hello, " + nickname);

            String input = in.nextLine();
            while (!input.equals("bye")) {
                server.sendAll(input, this.nickname);
                input = in.nextLine();
            }
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}