package expense_tracker_api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/expenses")
@CrossOrigin(origins = "*")
public class ExpenseController {

    @Autowired
    private ExpenseRepository repository;

    @Autowired
    private CategorizerService categorizer;

    @PostMapping
    public Expense addExpense(@RequestBody Map<String, Object> body) {
        double amount = Double.parseDouble(body.get("amount").toString());
        String description = body.get("description").toString();
        String category = categorizer.categorize(description);

        Expense expense = new Expense(amount, description, category, LocalDate.now());
        return repository.save(expense);
    }

    @PutMapping("/{id}")
    public Expense updateExpense(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        Expense expense = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expense not found: " + id));

        double amount = Double.parseDouble(body.get("amount").toString());
        String description = body.get("description").toString();
        String category = categorizer.categorize(description);

        expense.setAmount(amount);
        expense.setDescription(description);
        expense.setCategory(category);

        return repository.save(expense);
    }

    @DeleteMapping("/{id}")
    public String deleteExpense(@PathVariable Long id) {
        repository.deleteById(id);
        return "Deleted expense " + id;
    }

    @GetMapping
    public List<Expense> listExpenses() {
        return repository.findAll();
    }

    @GetMapping("/summary")
    public Map<String, Object> getSummary() {
        List<Expense> all = repository.findAll();

        double total = all.stream().mapToDouble(Expense::getAmount).sum();

        Map<String, Double> byCategory = all.stream()
                .collect(Collectors.groupingBy(Expense::getCategory,
                        Collectors.summingDouble(Expense::getAmount)));

        String topCategory = byCategory.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("N/A");

        Map<String, Object> summary = new LinkedHashMap<>();
        summary.put("totalSpend", total);
        summary.put("byCategory", byCategory);
        summary.put("topCategory", topCategory);
        return summary;
    }

    @GetMapping("/monthly")
    public Map<String, Double> getMonthlyTrends() {
        List<Expense> all = repository.findAll();
        return all.stream()
                .collect(Collectors.groupingBy(
                        e -> e.getExpenseDate().getYear() + "-" + String.format("%02d", e.getExpenseDate().getMonthValue()),
                        Collectors.summingDouble(Expense::getAmount)
                ));
    }

    @PostMapping("/sample")
    public String loadSampleData() {
        String[][] samples = {
                {"250", "Swiggy dinner order"},
                {"120", "Uber ride to college"},
                {"15000", "Monthly rent"},
                {"499", "Netflix subscription"},
                {"800", "Grocery shopping"},
                {"1200", "Online course purchase"}
        };

        int dayOffset = samples.length;
        for (String[] s : samples) {
            double amount = Double.parseDouble(s[0]);
            String description = s[1];
            String category = categorizer.categorize(description);
            Expense expense = new Expense(amount, description, category,
                    LocalDate.now().minusDays(dayOffset--));
            repository.save(expense);
        }
        return "Sample data loaded.";
    }
}