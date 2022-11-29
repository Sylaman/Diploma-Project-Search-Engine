import com.itextpdf.kernel.pdf.*;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;

import java.io.*;
import java.util.*;

public class BooleanSearchEngine implements SearchEngine {
    private HashMap<String, List<PageEntry>> db = new HashMap<>();

    public BooleanSearchEngine(File pdfsDir) throws IOException, NullPointerException {
        for (File pdf : Objects.requireNonNull(pdfsDir.listFiles(), "Неверный путь к файлам")) {
            PdfDocument pdfDocument = new PdfDocument(new PdfReader(pdf));
            for (int i = 0; i < pdfDocument.getNumberOfPages(); i++) {
                int currentPage = i + 1;
                String text = PdfTextExtractor.getTextFromPage(pdfDocument.getPage(currentPage));
                String[] words = text.split("\\P{IsAlphabetic}+");
                Map<String, Integer> freqs = new HashMap<>();
                for (var word : words) {
                    if (word.isEmpty()) {
                        continue;
                    }
                    freqs.put(word.toLowerCase(), freqs.getOrDefault(word, 0) + 1);
                }

                for (var entry : freqs.entrySet()) {
                    List<PageEntry> found;
                    if (db.containsKey(entry.getKey())) {
                        found = db.get(entry.getKey());
                    } else {
                        found = new ArrayList<>();
                    }
                    found.add(new PageEntry(pdf.getName(), currentPage, entry.getValue()));
                    found.sort(Collections.reverseOrder());
                    db.put(entry.getKey(), found);
                }
            }
        }
    }

    @Override
    public List<PageEntry> search(String word) {
        return db.get(word.toLowerCase());
    }
}