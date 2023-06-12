package controllers.inventory;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.Style;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.stage.Stage;
import models.interfaces.Animal;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class ReportOptionsController implements Initializable {

    @FXML
    private Button btnGenerateReport;
    @FXML
    private RadioButton rbNumber;
    @FXML
    private RadioButton rbOwner;
    @FXML
    private RadioButton rbAgeMonths;
    @FXML
    private RadioButton rbColor;
    @FXML
    private RadioButton rbIronBrand;
    @FXML
    private RadioButton rbSex;
    @FXML
    private RadioButton rbPurchaseWeigth;
    @FXML
    private RadioButton rbPurchasePrice;
    @FXML
    private RadioButton rbPurchaseDate;
    @FXML
    private RadioButton rbObservations;
    @FXML
    private RadioButton rbSaleWeigth;
    @FXML
    private RadioButton rbSalePrice;
    @FXML
    private RadioButton rbSaleDate;
    @FXML
    private RadioButton rbState;

    private int response = 0;
    private final List<Animal> animals;
    private final List<String> filters;
    private final List<String> columnNames = new ArrayList<>();
    private final List<Float> columnWidths = new ArrayList<>();
    private int selectedColumns = 8;

    public ReportOptionsController(List<Animal> animals, List<String> filters) {
        this.animals = animals;
        this.filters = filters;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        rbNumber.setOnAction(event -> selectColumn(rbNumber));
        rbOwner.setOnAction(event -> selectColumn(rbOwner));
        rbColor.setOnAction(event -> selectColumn(rbColor));
        rbIronBrand.setOnAction(event -> selectColumn(rbIronBrand));
        rbSex.setOnAction(event -> selectColumn(rbSex));
        rbPurchaseWeigth.setOnAction(event -> selectColumn(rbPurchaseWeigth));
        rbPurchasePrice.setOnAction(event -> selectColumn(rbPurchasePrice));
        rbPurchaseDate.setOnAction(event -> selectColumn(rbPurchaseDate));
        rbObservations.setOnAction(event -> selectColumn(rbObservations));
        rbSaleWeigth.setOnAction(event -> selectColumn(rbSaleWeigth));
        rbSalePrice.setOnAction(event -> selectColumn(rbSalePrice));
        rbSaleDate.setOnAction(event -> selectColumn(rbSaleDate));
        rbAgeMonths.setOnAction(event -> selectColumn(rbAgeMonths));
        rbState.setOnAction(event -> selectColumn(rbState));

        btnGenerateReport.setOnAction(event -> generateReport());
    }

    public int getResponse() {
        return response;
    }

    private void selectColumn(RadioButton column) {
        if (column.isSelected()) {
            selectedColumns++;
        } else {
            selectedColumns--;
        }
        if (selectedColumns > 11) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("No puede seleccionar más de 11 columnas");
            alert.showAndWait();
            column.setSelected(false);
            selectedColumns --;
        }
    }

    @FXML
    private void generateReport() {
        String path = generateReportName();

        if (selectedColumns == 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No puede generar un reporte sin columnas seleccionadas");
            alert.showAndWait();
            return;
        }

        try (PdfWriter pdfWriter = new PdfWriter(path)) {

            PdfDocument pdfDocument = new PdfDocument(pdfWriter);

            if (selectedColumns > 8) {
                pdfDocument.setDefaultPageSize(com.itextpdf.kernel.geom.PageSize.A4.rotate());
            } else {
                pdfDocument.addNewPage();
            }

            Document document = new Document(pdfDocument);
            document.setHorizontalAlignment(HorizontalAlignment.CENTER);
            document.setTextAlignment(TextAlignment.CENTER);

            Date currentDate = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String formattedDate = dateFormat.format(currentDate);

            document.add(new Paragraph("INVENTARIO GANADERO").setFontSize(32f));
            document.add(new Paragraph("Documento generado para la fecha " + formattedDate).setMarginBottom(20f));

            Style boldStyle = new Style().setBold();

            if(filters.size() > 0){
                document.add(new Paragraph("Filtros:").setTextAlignment(TextAlignment.LEFT).setFontSize(11).addStyle(boldStyle).setMarginBottom(-1));
                for (String filter: filters
                     ) {
                    document.add(new Paragraph(filter).setTextAlignment(TextAlignment.LEFT).setFontSize(8).setMarginBottom(-1));
                }
            }

            if (rbNumber.isSelected()) {
                columnNames.add("Número");
                columnWidths.add(55f);
            }
            if (rbOwner.isSelected()) {
                columnNames.add("Dueño");
                columnWidths.add(150f);
            }
            if (rbAgeMonths.isSelected()) {
                columnNames.add("E. meses");
                columnWidths.add(50f);
            }
            if (rbColor.isSelected()) {
                columnNames.add("Color");
                columnWidths.add(75f);
            }
            if (rbIronBrand.isSelected()) {
                columnNames.add("Hierro");
                columnWidths.add(55f);
            }
            if (rbSex.isSelected()) {
                columnNames.add("Sexo");
                columnWidths.add(40f);
            }
            if (rbPurchaseWeigth.isSelected()) {
                columnNames.add("Peso compra");
                columnWidths.add(65f);
            }
            if (rbPurchasePrice.isSelected()) {
                columnNames.add("Precio compra");
                columnWidths.add(65f);
            }
            if (rbPurchaseDate.isSelected()) {
                columnNames.add("Fecha compra");
                columnWidths.add(80f);
            }
            if (rbObservations.isSelected()) {
                columnNames.add("Observaciones");
                columnWidths.add(200f);
            }
            if (rbSaleWeigth.isSelected()) {
                columnNames.add("Peso venta");
                columnWidths.add(65f);
            }
            if (rbSalePrice.isSelected()) {
                columnNames.add("Precio venta");
                columnWidths.add(65f);
            }
            if (rbSaleDate.isSelected()) {
                columnNames.add("Fecha venta");
                columnWidths.add(80f);
            }
            if (rbState.isSelected()) {
                columnNames.add("Estado");
                columnWidths.add(55f);
            }

            float[] floatColumnWidths = new float[columnWidths.size()];

            for (int i = 0; i < columnWidths.size(); i++) {
                floatColumnWidths[i] = columnWidths.get(i);
            }

            Table table = new Table(floatColumnWidths).setMarginTop(30);

            for (String columnName : columnNames) {
                table.addCell(new Cell().add(new Paragraph(columnName).addStyle(boldStyle)));
            }

            for (Animal animal : animals) {
                for (String columnName : columnNames) {
                    switch (columnName) {
                        case "Número" -> table.addCell(new Cell().add(new Paragraph(animal.getNumber())));
                        case "Dueño" -> table.addCell(new Cell().add(new Paragraph(animal.getOwner().getName())));
                        case "E. meses" -> table.addCell(new Cell().add(new Paragraph(animal.getMonths() + "")));
                        case "Color" -> table.addCell(new Cell().add(new Paragraph(animal.getColor())));
                        case "Hierro" -> table.addCell(new Cell().add(new Paragraph(animal.getIronBrand())));
                        case "Sexo" -> table.addCell(new Cell().add(new Paragraph(animal.getSex() + "")));
                        case "Peso compra" ->
                                table.addCell(new Cell().add(new Paragraph(animal.getPurchaseWeight() + "")));
                        case "Precio compra" ->
                                table.addCell(new Cell().add(new Paragraph(animal.getPurchasePrice() + "")));
                        case "Fecha compra" ->
                                table.addCell(new Cell().add(new Paragraph(animal.getPurchaseDate() + "")));
                        case "Observaciones" -> table.addCell(new Cell().add(new Paragraph(animal.getObservations())));
                        case "Peso venta" -> table.addCell(new Cell().add(new Paragraph(animal.getSaleWeight() + "")));
                        case "Precio venta" -> table.addCell(new Cell().add(new Paragraph(animal.getSalePrice() + "")));
                        case "Fecha venta" -> table.addCell(new Cell().add(new Paragraph(animal.getSaleDate() + "")));
                        case "Estado" -> table.addCell(new Cell().add(new Paragraph(animal.getState().toString())));
                    }

                }
            }

            table.setHorizontalAlignment(HorizontalAlignment.CENTER);

            document.add(table);

            document.close();
            pdfDocument.close();
        } catch (IOException e) {
            response = -1;
            e.printStackTrace();
        }
        response = 1;
        Stage stage = (Stage) btnGenerateReport.getScene().getWindow();
        stage.close();
    }

    private String generateReportName() {
        String basePath = "D:\\User\\Downloads\\";
        String fileName = "inventario.pdf";

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
