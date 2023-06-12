package controllers;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.Style;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import models.Model;
import models.interfaces.Task;
import models.responses.ReportActiveAnimalsResponse;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

public class Dashboard implements Initializable {

    private final Model model = new Model();

    @FXML
    private Label txtMales;
    @FXML
    private Label txtTotalAnimals;
    @FXML
    private Label txtFemales;
    @FXML
    private TableView<Task> tblTasks;
    @FXML
    private TableColumn<Task, String> colTaskName;
    @FXML
    private TableColumn<Task, String> colTaskDescription;
    @FXML
    private TableColumn<Task, Date> colTaskCreationDate;
    @FXML
    private TableColumn<Task, Date> colTaskAssignedDate;

    private int totalAnimals = 0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.colTaskName.setCellValueFactory(new PropertyValueFactory<>("name"));
        this.colTaskDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        this.colTaskCreationDate.setCellValueFactory(new PropertyValueFactory<>("creationDate"));
        this.colTaskAssignedDate.setCellValueFactory(new PropertyValueFactory<>("assignedDate"));

        ObservableList<Task> tasks = model.getActiveTasksMonth();
        this.tblTasks.setItems(tasks);

        tblTasks.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && tblTasks.getSelectionModel().getSelectedItem() != null) {
                TablePosition<Task, ?> pos = tblTasks.getSelectionModel().getSelectedCells().get(0);
                int row = pos.getRow();
                TableColumn<Task, ?> col = pos.getTableColumn();
                Task task = tblTasks.getItems().get(row);
                if (col == colTaskDescription) {
                    String observations = task.getDescription();
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText("Descripción del recordatorio");
                    alert.setContentText(observations);
                    alert.showAndWait();
                }
            }
        });

        int totalFemales = model.getCountActiveAnimalsBySex('H');
        int totalMales = model.getCountActiveAnimalsBySex('M');
        totalAnimals = totalFemales + totalMales;
        txtFemales.setText(totalFemales + "");
        txtMales.setText(totalMales + "");
        txtTotalAnimals.setText(totalAnimals + "");
    }

    @FXML
    private void generateReportActiveAnimals() {
        String path = generateReportName();

        if (totalAnimals == 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No hay animales activos");
            alert.showAndWait();
            return;
        }

        try (PdfWriter pdfWriter = new PdfWriter(path)) {

            PdfDocument pdfDocument = new PdfDocument(pdfWriter);

            pdfDocument.addNewPage();

            Document document = new Document(pdfDocument);
            document.setHorizontalAlignment(HorizontalAlignment.CENTER);
            document.setTextAlignment(TextAlignment.CENTER);

            java.util.Date currentDate = new java.util.Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String formattedDate = dateFormat.format(currentDate);

            document.add(new Paragraph("INVENTARIO GANADERO").setFontSize(32f));
            document.add(new Paragraph("Documento generado para la fecha " + formattedDate).setMarginBottom(20f));

            Style boldStyle = new Style().setBold();

            document.add(new Paragraph("Informe animales activos").setTextAlignment(TextAlignment.LEFT).setFontSize(11).addStyle(boldStyle).setMarginBottom(-1));

            float[] columnWidths = {
                    150f, 50f, 50f, 50f
            };

            Table table = new Table(columnWidths).setMarginTop(30);

            String[] columnNames = {"Dueño", "Machos", "Hembras", "Total"};

            for (String columnName : columnNames) {
                table.addCell(new Cell().add(new Paragraph(columnName).addStyle(boldStyle)));
            }

            ObservableList<ReportActiveAnimalsResponse> response = model.getReportActiveAnimals();

            if (response == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Ocurrió un error al obtener la información de los animales activos");
                alert.showAndWait();
                return;
            }

            int totalAnimals = 0;

            for (ReportActiveAnimalsResponse reportActiveAnimalsResponse : response) {
                table.addCell(new Cell().add(new Paragraph(reportActiveAnimalsResponse.name())));
                table.addCell(new Cell().add(new Paragraph(reportActiveAnimalsResponse.males() + "")));
                table.addCell(new Cell().add(new Paragraph(reportActiveAnimalsResponse.females() + "")));
                table.addCell(new Cell().add(new Paragraph(reportActiveAnimalsResponse.getTotalAnimals() + "")));
                totalAnimals += reportActiveAnimalsResponse.getTotalAnimals();
            }

            table.setHorizontalAlignment(HorizontalAlignment.CENTER);

            document.add(table);

            document.add(new Paragraph("Animales totales: " + totalAnimals).setTextAlignment(TextAlignment.LEFT).setFontSize(11).addStyle(boldStyle).setMarginTop(20));

            document.close();
            pdfDocument.close();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Se generó el reporte. Busque el pdf en descargas");
            alert.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Ocurrió un error");
            alert.setContentText("No se pudo generar el reporte. Intente más tarde");
            alert.showAndWait();
        }
    }

    private String generateReportName() {
        String basePath = "D:\\User\\Downloads\\";
        String fileName = "animales_activos.pdf";

        String newFilePath = basePath + fileName;

        int counter = 1;
        File file = new File(newFilePath);

        while (file.exists()) {
            String tempFileName = fileName.substring(0, fileName.lastIndexOf('.'));
            String extension = fileName.substring(fileName.lastIndexOf('.'));

            String newFileName = tempFileName + " (" + counter + ")" + extension;

            newFilePath = basePath + newFileName;
            file = new File(newFilePath);

            counter++;
        }

        return newFilePath;
    }
}
