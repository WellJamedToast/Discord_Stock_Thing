import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * This guy grabs all the junk from the api every 1 second (changed to 5 because crypto is slow and rate limits)
 * I also start data thread here and pass the json file to it the next layer down
 */
public class HookThread extends Thread {
    private String responseBody;
    private int counter;
    private DataThread dataThread;

    public HookThread() {
        dataThread = new DataThread();
        counter = 0;
    }

    @Override
    public void run() {
        System.out.println("hookThread started");

        dataThread.start();
        try {
            while (counter < 2000) {
                Thread.sleep(5000); // Wait for 5 second
                responseBody = fetchStockPrice();
                dataThread.loadStockDataFromJson(responseBody);
                counter++;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
    public String getResponseBody() {
        return responseBody;
    }

    private String fetchStockPrice(){
        try {
            HttpClient client = HttpClient.newHttpClient();

            // Create HttpRequest
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://apidojo-yahoo-finance-v1.p.rapidapi.com/market/v2/get-quotes?region=US&symbols=BTC-USD%2CUSDT-USD%2CUSDC-USD%2CETH-USD%2CABEY-USD%2CSOL-USD%2CXRP-USD%2CDEDA-USD%2CQAI-USD"))
                    .header("x-rapidapi-key", "4e2c40eb7fmsh12b875456e3516fp10c948jsn3994d95a1842")
                    .header("x-rapidapi-host", "apidojo-yahoo-finance-v1.p.rapidapi.com")
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String responseBody = response.body();
//            System.out.println("Raw JSON Response: " + responseBody);
            return responseBody;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public DataThread getDataThread() {
        return dataThread;
    }

    public Stock[] getStocks() {
        return dataThread.getStocks();
    }


}