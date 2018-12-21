package sample;

import de.l3s.boilerpipe.BoilerpipeProcessingException;
import de.l3s.boilerpipe.extractors.ArticleExtractor;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class Main extends Application {
    Scene mainScene, afterSubmit;
    Text actualLink, actualTitle, actualDate, actualCompany, actualArticleText;
    StringSelection stringSelection;
    Clipboard clipboard;

    @Override
    public void start(Stage primaryStage) throws Exception{

        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));

        primaryStage.setTitle("Quick Text");
        Button backButton = new Button("Back");
        backButton.setPrefSize(100,20);
        backButton.setOnAction(f -> {
            primaryStage.setScene(mainScene);
            Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
            primaryStage.setX((primScreenBounds.getWidth() - primaryStage.getWidth()) / 2);
            primaryStage.setY((primScreenBounds.getHeight() - primaryStage.getHeight()) / 2);
        });

        Text linkText = new Text("Link: ");
        Text titleText = new Text("Title: ");
        Text dateText = new Text("Date & Time Created: ");
        Text companyText = new Text("Company: ");
        Text articleText = new Text("Article Text: ");

        actualTitle = new Text("title");
        actualDate = new Text("date");
        actualCompany = new Text("company");
        actualArticleText = new Text("article text");

        Button linkCopyButton = new Button("Copy");
        linkCopyButton.setPrefSize(100,20);
        linkCopyButton.setOnAction(g -> {
            stringSelection = new StringSelection(actualLink.getText());
            clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(stringSelection, null);
        });

        Button titleCopyButton = new Button("Copy");
        titleCopyButton.setPrefSize(100,20);
        titleCopyButton.setOnAction(g -> {
            stringSelection = new StringSelection(actualTitle.getText());
            clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(stringSelection, null);
        });

        Button dateCopyButton = new Button("Copy");
        dateCopyButton.setPrefSize(100,20);
        dateCopyButton.setOnAction(g -> {
            stringSelection = new StringSelection(actualDate.getText());
            clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(stringSelection, null);
        });

        Button companyCopyButton = new Button("Copy");
        companyCopyButton.setPrefSize(100,20);
        companyCopyButton.setOnAction(g -> {
            stringSelection = new StringSelection(actualCompany.getText());
            clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(stringSelection, null);
        });

        Button articleCopyButton = new Button("Copy");
        articleCopyButton.setPrefSize(100,20);
        articleCopyButton.setOnAction(g -> {
            stringSelection = new StringSelection(actualArticleText.getText());
            clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(stringSelection, null);
        });


        GridPane infoGrid = new GridPane();
        infoGrid.setHgap(10);
        infoGrid.setVgap(20);
        infoGrid.setPadding(new Insets(12, 12, 12, 12));
        infoGrid.add(linkText,1,0);
        infoGrid.add(titleText,1,1);
        infoGrid.add(dateText,1,2);
        infoGrid.add(companyText,1,3);
        infoGrid.add(articleText,1,4);
        infoGrid.add(linkCopyButton,0,0);
        infoGrid.add(titleCopyButton,0,1);
        infoGrid.add(dateCopyButton,0,2);
        infoGrid.add(companyCopyButton,0,3);
        infoGrid.add(articleCopyButton,0,4);
        infoGrid.add(actualTitle,2,1);
        infoGrid.add(actualDate,2,2);
        infoGrid.add(actualCompany,2,3);
        infoGrid.add(actualArticleText,2,4);

        ScrollPane scrollPane = new ScrollPane(infoGrid);
        scrollPane.setFitToHeight(true);

        VBox allInfo = new VBox();
        allInfo.setAlignment(Pos.CENTER);
        allInfo.setPadding(new Insets(12,12,12,12));
        allInfo.getChildren().addAll(backButton, scrollPane);

        afterSubmit = new Scene(allInfo);

        HBox initialRow = new HBox();
        initialRow.setPadding(new Insets(12,12,12,12));
        initialRow.setSpacing(10);

        Text linkEnter = new Text("Enter Link: ");

        TextField linkField = new TextField();
        linkField.setPrefSize(500,20);

        Button submitButton = new Button("Submit");
        submitButton.setPrefSize(100,20);
        submitButton.setOnAction(e -> {
                    String link = linkField.getText();

                    Document doc = null;
                    try
                    {
                        doc = Jsoup.connect(link).get();
                    }
                    catch (IOException e1)
                    {
                        e1.printStackTrace();
                    }

                    link = doc.location();
                    int lastHyphenIndex;
                    String title, company;

                    if(doc.title().contains("–")) {
                        lastHyphenIndex = doc.title().lastIndexOf('–');
                        title = doc.title().substring(0, lastHyphenIndex-1);
                        company = doc.title().substring(lastHyphenIndex+2);
                    }
                    else if(doc.title().contains("-")) {
                        lastHyphenIndex = doc.title().lastIndexOf('-');
                        title = doc.title().substring(0, lastHyphenIndex-1);
                        company = doc.title().substring(lastHyphenIndex+2);
                    }
                    else {
                        title = doc.title();
                        company = "Company could not be found";
                    }

                    String date = "";

                    if(!doc.getElementsByTag("time").text().equals("")) {
                        date = doc.getElementsByTag("time").text();
                    }
                    else if(doc.body().text().contains("Jan ")||doc.body().text().contains("Feb ")||doc.body().text().contains("Mar ")||doc.body().text().contains("Apr ")||doc.body().text().contains("May ")||doc.body().text().contains("Jun ")||doc.body().text().contains("Jul ")||doc.body().text().contains("Aug ")||doc.body().text().contains("Sep ")||doc.body().text().contains("Oct ")||doc.body().text().contains("Nov ")||doc.body().text().contains("Dec ")) {
                    }
                    else {
                        date = "Date & Time could not be found";
                    }

                    ArticleExtractor extractor = ArticleExtractor.INSTANCE;
                    String article = "";

                    try
                    {
                        article = extractor.getText(new URL(link));
                    }
                    catch (BoilerpipeProcessingException e1)
                    {
                        e1.printStackTrace();
                    }
                    catch (MalformedURLException e1)
                    {
                        e1.printStackTrace();
                    }

            String article2 = doc.getElementsByTag("p").text();
                    //System.out.println(article);
                    //System.out.println(article2);

                    infoGrid.getChildren().remove(actualLink);
                    infoGrid.getChildren().remove(actualTitle);
                    infoGrid.getChildren().remove(actualDate);
                    infoGrid.getChildren().remove(actualCompany);
                    infoGrid.getChildren().remove(actualArticleText);

                    actualLink = new Text(link);
                    actualTitle = new Text(title);
                    actualDate = new Text(date);
                    actualCompany = new Text(company);
                    actualArticleText = new Text(article);

                    infoGrid.add(actualLink, 2, 0);
                    infoGrid.add(actualTitle, 2, 1);
                    infoGrid.add(actualDate, 2, 2);
                    infoGrid.add(actualCompany, 2, 3);
                    infoGrid.add(actualArticleText, 2, 4);


                    linkField.clear();
                    primaryStage.setScene(afterSubmit);
                    Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
                    primaryStage.setX((primScreenBounds.getWidth() - primaryStage.getWidth()) / 2);
                    primaryStage.setY((primScreenBounds.getHeight() - primaryStage.getHeight()) / 2);
                }
        );

        initialRow.getChildren().addAll(linkEnter, linkField, submitButton);
        initialRow.setAlignment(Pos.CENTER);

        mainScene = new Scene(initialRow);

        primaryStage.setScene(mainScene);

        primaryStage.show();
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        primaryStage.setX((primScreenBounds.getWidth() - primaryStage.getWidth()) / 2);
        primaryStage.setY((primScreenBounds.getHeight() - primaryStage.getHeight()) / 2);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
