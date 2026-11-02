package hu.berenice.meriem.forex;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import soapclient.MNBArfolyamServiceSoap;
import soapclient.MNBArfolyamServiceSoapImpl;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Controller
public class SoapController {

    @GetMapping("/soap")
    public String showForm(Model model) {
        SoapForm form = new SoapForm();
        form.setStartDate(LocalDate.now().minusDays(30).toString());
        form.setEndDate(LocalDate.now().toString());
        model.addAttribute("soapForm", form);
        return "soap";
    }

    @PostMapping("/soap")
    public String getRates(@ModelAttribute("soapForm") SoapForm form, Model model) {
        try {
            if (form.getStartDate() == null || form.getStartDate().isBlank()
                    || form.getEndDate() == null || form.getEndDate().isBlank()) {
                model.addAttribute("error", "Please choose a start date and an end date.");
                return "soap";
            }

            LocalDate start = LocalDate.parse(form.getStartDate());
            LocalDate end = LocalDate.parse(form.getEndDate());

            if (end.isBefore(start)) {
                model.addAttribute("error", "The end date must not be before the start date.");
                return "soap";
            }

            MNBArfolyamServiceSoapImpl impl = new MNBArfolyamServiceSoapImpl();
            MNBArfolyamServiceSoap service = impl.getCustomBindingMNBArfolyamServiceSoap();
            String xml = service.getExchangeRates(form.getStartDate(), form.getEndDate(), form.getCurrency());

            List<RateRow> rows = parseRates(xml);
            model.addAttribute("rows", rows);
            model.addAttribute("labels", rows.stream().map(RateRow::date).toList());
            model.addAttribute("values", rows.stream().map(RateRow::rate).toList());
        } catch (Exception e) {
            model.addAttribute("error", "Could not get the data: " + e.getMessage());
        }
        return "soap";
    }

    private List<RateRow> parseRates(String xml) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        Document doc = factory.newDocumentBuilder().parse(new InputSource(new StringReader(xml)));

        List<RateRow> rows = new ArrayList<>();
        NodeList days = doc.getElementsByTagName("Day");
        for (int i = 0; i < days.getLength(); i++) {
            Element day = (Element) days.item(i);
            NodeList rates = day.getElementsByTagName("Rate");
            if (rates.getLength() == 0) {
                continue;
            }
            Element rate = (Element) rates.item(0);
            double value = Double.parseDouble(rate.getTextContent().trim().replace(",", "."));
            rows.add(new RateRow(day.getAttribute("date"), rate.getAttribute("unit"), value));
        }
        rows.sort(Comparator.comparing(RateRow::date));
        return rows;
    }
}