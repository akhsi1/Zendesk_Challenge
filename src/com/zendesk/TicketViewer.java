package com.zendesk;
import com.google.gson.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Scanner;

public class TicketViewer {

    private final String zendeskUsername = "akhsi1994@gmail.com/token";
    private final String zendeskToken = ""; // Enter the Authentication Token here
    private String GET_URL = "https://alston.zendesk.com/api/v2/tickets?page[size]=25"; // Page size can be changed here
    private int pageNumber = 0;
    private String nextPageURL;
    private String prevPageURL;
    private boolean hasMore;

    /**
     * MAIN method
     */
    public static void main(String[] args) {
        TicketViewer tl = new TicketViewer();
        tl.start();
    }

    /**
     * Controls how the program responds and handles user input
     */
    private void start(){
        Scanner sc = new Scanner(System.in);
        if(zendeskToken == ""){
            System.out.println("Could not authenticate you.\nPlease put auth token in the class variable \"zendeskToken\"");
            sc.nextLine();
            return;
        }
        displayWelcome();
        sc.nextLine();
        try{
            // Fetch initial 25 tickets
            System.out.println("Fetching your tickets...");
            Ticket[] tickets = parseList(getTickets(GET_URL));
            pageNumber++;
            displayList(tickets);

            // Loop the UI until user chooses to Quit (Q)
            boolean quit = false;
            int invalidInputs = 0;
            while(!quit){
                String input = sc.nextLine().toUpperCase().trim();
                switch(input){
                    case "N": // Go to next page
                        if(hasMore){
                            System.out.println("Working on it...");
                            tickets = parseList(getTickets(nextPageURL));
                            pageNumber++;
                            displayList(tickets);
                        } else {
                            System.out.println("No more pages");
                        }
                        break;
                    case "P": // Go to previous page
                        if(pageNumber > 1){
                            pageNumber--;
                            System.out.println("Working on it...");
                            tickets = parseList(getTickets(prevPageURL));
                            displayList(tickets);
                        } else {
                            System.out.println("No previous page");
                        }
                        break;
                    case "Q": // Exit the program
                        quit = true;
                        System.out.println("Thanks for using Ticket Viewer!");
                        break;
                    default:
                        // Validate input is purely number
                        if(input.matches("-?(0|[1-9]\\d*)")){ //View individual ticket
                            displayOneTicket(input);
                            sc.nextLine();
                            displayList(tickets);
                        } else {
                             System.out.println("Invalid input");
                             invalidInputs++;
                             if(invalidInputs%7 == 0) System.out.println(":)");
                        }
                }
            }
        } catch (IOException io){
            System.out.println("Error sending HTTP Request");
        }
    }

    /**
     * Displays the Welcome Page
     */
    private void displayWelcome(){
        clrscr();
        System.out.println("\nWelcome to the Tickets Viewer!\n");
        System.out.println("We Recommend you Maximize this window for the best User Experience!\n");
        System.out.println("Please press \"Enter\" to continue");
    }

    /**
     * Displays the HOME SCREEN, displays TICKETS, and displays NAVIGATION
     * @param tickets
     */
    private void displayList(Ticket[] tickets){
        clrscr();
        System.out.println("Welcome to Zendesk Tickets Viewer!\n");
        System.out.println("Displaying PAGE " + pageNumber + "\n");
        System.out.printf("%-6s%-60s%-14s\n", "ID", "Subject", "Date Created");
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        // Handles if API is unavailable
        if(tickets != null){
            for (Ticket t: tickets) {
                String s = t.getSubject();
                if(s.length() > 60){
                    s = s.substring(0,58);
                    s += "..";
                }
                System.out.printf("%-6s%-60s%-14s\n", t.getId(), s, sdf.format(t.getCreated_at()));
            }
        } else {
            System.out.println("Sorry! Failed to retrieve tickets. The API might be unavailable or Username / Token is incorrect");
        }

        if(pageNumber > 1){
            System.out.print("<-- Previous Page (P)     |     ");
        }
        if(hasMore){
            System.out.println("Next Page (N) -->");
        }
        System.out.println("");
        System.out.println("View Ticket Details (-Enter ID Number-)  |  Quit (Q)");
        System.out.println("");
        if (hasMore && pageNumber > 1) {
            System.out.println("What would you like to do? ( P / N / Q / #ID )");
        } else if (pageNumber > 1 && !hasMore){
            System.out.println("What would you like to do? ( P / Q / #ID )");
        } else if (pageNumber == 1 && hasMore){
            System.out.println("What would you like to do? ( N / Q / #ID )");
        } else {
            System.out.println("What would you like to do? ( Q / #ID )");
        }
    }

    /**
     * Display a single ticket's details based on ID
     * @param id the ID number of ticket
     */
    private void displayOneTicket(String id){
        clrscr();
        System.out.println("Viewing Ticket Details\n");
        try{
            String response = getTickets("https://alston.zendesk.com/api/v2/tickets/" + id +".json");
            if(response == null){
                System.out.println("Sorry! Failed to find a ticket with ID: " + id + " This ticket might not exist.");
                System.out.println("Enter anything to continue...");
                return;
            }
            JsonParser parser = new JsonParser();
            try{
                JsonObject ticket = parser.parse(response).getAsJsonObject().getAsJsonObject("ticket");
                System.out.printf("%-25s%s\n", "ID: ", ticket.get("id").toString());
                System.out.printf("%-25s%s\n", "Subject: ", ticket.get("subject").toString());

                Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(ticket.getAsJsonPrimitive("created_at").getAsString());
                System.out.printf("%-25s%s\n", "Date Created: ", date);

                date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(ticket.getAsJsonPrimitive("updated_at").getAsString());
                System.out.printf("%-25s%s\n", "Last Updated: ", date);

                System.out.printf("%-25s%s\n", "Description: ", ticket.getAsJsonPrimitive("description").getAsString().replace("\n", "\n                         "));
                System.out.printf("%-25s%s\n", "Requester ID: ", ticket.get("requester_id").toString());
                System.out.printf("%-25s%s\n", "Submitter ID: ", ticket.get("submitter_id").toString());
                System.out.printf("%-25s%s\n", "Organization ID: ", ticket.get("organization_id").toString());
                System.out.printf("%-25s%s\n", "Priority: ", ticket.get("priority").toString());
                System.out.printf("%-25s%s\n", "Allow Channelback: ", ticket.get("allow_channelback").toString());
                System.out.printf("%-25s%s\n", "Allow Attachments: ", ticket.get("allow_attachments").toString());
                System.out.printf("%-25s%s\n", "Status: ", ticket.get("status").toString());

                String tagString = "";
                JsonArray tags = ticket.get("tags").getAsJsonArray();
                for (JsonElement t: tags) {
                    tagString += t + ", ";
                }
                if(tagString.length()>2){
                    tagString = tagString.substring(0, tagString.length()-2);
                } else {
                    tagString = null;
                }
                System.out.printf("%-25s%s\n", "Tags: ", tagString);

                System.out.println("\nPlease Press \"Enter\" to go Back");
            }
            catch(JsonSyntaxException jse){
                System.out.println("Not a valid Json String:"+jse.getMessage());
            } catch (ParseException e) {
                System.out.println("Error parsing date while fetching ticket details");
                e.printStackTrace();
            }
        } catch (IOException e){
            System.out.println("Error with HTTP connection while displaying ticket");
        }

    }

    /**
     * Send HTTP GET Request based on passed URL
     * @param passedUrl The Zendesk API URL which HttpURLConnection tries to GET
     * @return String of the response, or NULL if response code is not 200
     * @throws IOException if HttpURLConnection fails to open
     */
    private String getTickets(String passedUrl) throws IOException {
        URL url = new URL(passedUrl);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        //Create Authentication
        String encoded = Base64.getEncoder().encodeToString((zendeskUsername+":"+zendeskToken).getBytes(StandardCharsets.UTF_8));
        con.setRequestProperty("Accept", "application/json");
        con.setRequestProperty("Authorization", "Basic "+encoded);
        con.setRequestMethod("GET");
        int responseCode = con.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) { // success
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String inputLine;
            String response ="";
            while ((inputLine = in.readLine()) != null)
                response += inputLine;
            in.close();
            return response;
        } else {
            System.out.println("HTTP Error. Response: " + responseCode);
        }
        return null;
    }

    /**
     * Parse the Http response into Java Objects (Tickets)
     * @param HttpResponse HTTP response in plain String
     * @return Ticket[] list of ticket objects
     */
    private Ticket[] parseList(String HttpResponse){
        if(HttpResponse == null){
            return null;
        }
        //Parse JSON to Object
        Gson g = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();
        JsonParser parser = new JsonParser();
        try{ // Try to parse the response
            JsonElement root = parser.parse(HttpResponse);
            if(root.isJsonObject()){
                JsonObject ob = root.getAsJsonObject();
                JsonArray ticketsArray = ob.get("tickets").getAsJsonArray();
                Ticket[] tickets = g.fromJson(ticketsArray, Ticket[].class);
                prevPageURL = ob.getAsJsonObject("links").get("prev").getAsString();
                nextPageURL = ob.getAsJsonObject("links").get("next").getAsString();
                hasMore = ob.getAsJsonObject("meta").get("has_more").getAsBoolean();
                return tickets;
            }
        }
        catch(JsonSyntaxException jse){
            System.out.println("Not a valid Json String:"+jse.getMessage());
        }
        return null;
    }

    /**
     * Clears the screen depending on user's Operating System (may not work in IDEs)
     */
    public static void clrscr() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                Runtime.getRuntime().exec("clear");
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (IOException | InterruptedException ex) {
            System.out.println("Error while trying to clear the screen");
        }
    }
}
