import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ThroughputHttpServer {

    public static final String INPUT_FILE = "./thread-throughput-optimization/resources/war_and_peace.txt";

    // 스레드 풀이 1일 때 처리량이 960, 2일 때 1333, 4일 때 2170 정도 나옴.
    public static final int NUMBER_OF_THREADS = 4;

    public static void main(String[] args) throws IOException {
        String text = new String(Files.readAllBytes(Paths.get(INPUT_FILE)));
        startServer(text);
    }

    private static void startServer(String text) throws IOException {
        // 두 번째 변수는 백로그 크기로, HTTP 서버 요청에 맞는 대기열 크기를 지정. => 모든 요청이 스레드 풀 대기열에 들어가게 하기 위함.
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);

        //spring controller라고 보면 됨.
        server.createContext("/search", new WordCountHandler(text));

        Executor executor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
        server.setExecutor(executor);

        server.start();
    }

    private static class WordCountHandler implements HttpHandler {
        private String text;

        public WordCountHandler(String text) {
            this.text = text;
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String query = exchange.getRequestURI().getQuery();

            String[] keyValue = query.split("=");
            String action = keyValue[0];
            String word = keyValue[1];

            if (!action.equals("word")) {
                exchange.sendResponseHeaders(400, 0);
                return;
            }

            long count = countWord(word);

            byte[] response = Long.toString(count).getBytes();
            exchange.sendResponseHeaders(200, response.length);
            OutputStream outputStream = exchange.getResponseBody();
            outputStream.write(response);
            outputStream.close();
        }

        private long countWord(String word) {
            long count = 0;
            int index = 0;
            while (index >= 0) {
                index = text.indexOf(word, index);

                if (index >= 0) {
                    count++;
                    index++;
                }
            }
            return count;
        }
    }
}
