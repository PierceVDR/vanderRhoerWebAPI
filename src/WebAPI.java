import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

public class WebAPI {

    public static void getNowPlaying() {
        String APIkey = "0a647dab7bbb303e8148d2c501afeaf1"; // your personal API key on TheMovieDatabase
        String queryParameters = "?api_key=" + APIkey;
        String endpoint = "https://api.themoviedb.org/3/movie/now_playing";
        String url = endpoint + queryParameters;
        String urlResponse = "";
        try {
            URI myUri = URI.create(url); // creates a URI object from the url string
            HttpRequest request = HttpRequest.newBuilder().uri(myUri).build();
            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            urlResponse = response.body();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        // when determining HOW to parse the returned JSON data,
        // first, print out the urlResponse, then copy/paste the output
        // into the online JSON parser: https://jsonformatter.org/json-parser
        // use the visual model to help you determine how to parse the data!
        JSONObject jsonObj = new JSONObject(urlResponse);
        JSONArray movieList = jsonObj.getJSONArray("results");
        for (int i = 0; i < movieList.length(); i++) {
            JSONObject movieObj = movieList.getJSONObject(i);
            String movieTitle = movieObj.getString("title");
            int movieID = movieObj.getInt("id");
            String posterPath = movieObj.getString("poster_path");
            String fullPosterPath = "https://image.tmdb.org/t/p/w500" + posterPath;
            System.out.println(movieID + " " + movieTitle + " " + fullPosterPath);
        }

        System.out.print("\nEnter a movie ID to learn more: ");
        String id = SCAN.nextLine();

        String endpoint2 = "https://api.themoviedb.org/3/movie/" + id;

        String url2 = endpoint2 + queryParameters;
        String urlResponse2 = "";
        try {
            URI myUri = URI.create(url2); // creates a URI object from the url string
            HttpRequest request = HttpRequest.newBuilder().uri(myUri).build();
            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            urlResponse2 = response.body();

            JSONObject jsonObj2 = new JSONObject(urlResponse2);

            System.out.println("Title: " + jsonObj2.getString("title"));
            System.out.println("Homepage: " + jsonObj2.getString("homepage"));
            System.out.println("Overview: " + jsonObj2.getString("overview"));
            System.out.println("Released on: " + jsonObj2.getString("release_date"));
            System.out.println("Runtime: " + jsonObj2.getInt("runtime")  + " minutes");
            System.out.println("Revenue: $" + jsonObj2.getInt("revenue"));

            System.out.println("Genres:");
            for (Object v : jsonObj2.getJSONArray("genres")) {
                System.out.println("    " + ((JSONObject) v).getString("name"));
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static final Scanner SCAN  = new Scanner(System.in);
    private static final String KEY = "0a647dab7bbb303e8148d2c501afeaf1";

    public static void getTopRatedTV() {
        String url = ("https://api.themoviedb.org/3/tv/top_rated") +
                ("?api_key="+KEY); // I find it helpful to split up the URL like this, rather than with multiple variables

        JSONObject showsResponse = getResponse(url);

        JSONArray shows = showsResponse.getJSONArray("results");
        for (int i = 0; i < shows.length(); i++) {
            JSONObject s = shows.getJSONObject(i);
            String name = s.getString("name");
            int id = s.getInt("id");
            String posterPath = s.getString("poster_path");
            String fullPosterPath = "https://image.tmdb.org/t/p/w500" + posterPath;
            System.out.println(id + " " + name + " " + fullPosterPath);
        }


        // "Learn more" function:
        System.out.print("\nEnter an ID to learn more: ");
        String id = SCAN.nextLine();

        String url2 = ("https://api.themoviedb.org/3/tv/" + id) + ("?api_key="+KEY);

        JSONObject s = getResponse(url2);

        // Printing out info:
        System.out.println();
        String tagline = s.getString("tagline");
        if (!tagline.equals("")){
            System.out.println("Tagline: " + tagline);
        }

        System.out.println("Name: " + s.getString("name"));
        System.out.println("Overview: " + s.getString("overview"));
        System.out.println("Homepage: " + s.getString("homepage"));

        System.out.println("Genres:");
        for (Object v : s.getJSONArray("genres")) {
            System.out.println("  -  " + ((JSONObject) v).getString("name"));
        }

        System.out.println("# of Seasions: " + s.getInt("number_of_seasons"));
        System.out.println("Last episode aired: " + s.getString("last_air_date"));
        System.out.println("First episode aired: " + s.getString("first_air_date"));

    }


    private static JSONObject getResponse(String url){
        try {
            URI myUri = URI.create(url); // creates a URI object from the url string
            HttpRequest request = HttpRequest.newBuilder().uri(myUri).build();
            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String  urlResponse = response.body();

            return new JSONObject(urlResponse);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}