import java.net.*;
import java.io.*;
import java.nio.charset.*;
import java.util.Scanner;

public class Server {

    public static void main(String[] args) {

        ServerSocket server = null;
        boolean shutdown = false;

        try {
            server = new ServerSocket(1236);
            System.out.println("Port bound. Accepting connections");

        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        while (!shutdown) {
            Socket client = null;
            InputStream input = null;
            OutputStream output = null;

            try {
                client = server.accept();
                input = client.getInputStream();
                output = client.getOutputStream();
                int fib = 0;
                int num = 0;


                int n = input.read();
                byte[] data = new byte[n];
                input.read(data);

                String clientInput = new String(data, StandardCharsets.UTF_8);
                clientInput.replace("\n", "");

                try {
                    num = Integer.parseInt(clientInput);
                    System.out.println("Client said: " + num);
                } catch (NumberFormatException e) {
                    String response = "A non integer value was entered";
                    output.write(response.length());
                    output.write(response.getBytes());


                }


                if (num < 0) {

                    String response = "Please enter a non-negative number";
                    output.write(response.length());
                    output.write(response.getBytes());

                } else {

                    fib = fibonacci(num);

                    String response = "Fibonacci [" + num + "] is [" + fib + "]";
                    output.write(response.length());
                    output.write(response.getBytes());
                }


                if (clientInput.equalsIgnoreCase("shutdown")) {
                    System.out.println("Shutting down...");
                    String response = "Shutting down...";
                    output.write(response.length());
                    output.write(response.getBytes());
                    shutdown = true;

                }

                client.close();

            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }

        }

    }

    public static int fibonacci(int n) {
        int sum = 0;
        int num1 = 0;
        int num2 = 1;

        if (n < 0) {
            num1 = -1;

        } else if (n > 0) {

            for (int i = 1; i <= n; i++) {
                sum = num1 + num2;
                num1 = num2;
                num2 = sum;

            }

        }

        return num1;

    }

}