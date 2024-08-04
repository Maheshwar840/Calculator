import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DiscountCalculatorApp {

    public static void main(String[] args) {
        // Read input from console
        Scanner scanner = new Scanner(System.in);

        // Read number of items
        int numItems = Integer.parseInt(scanner.nextLine().trim());
        List<ShoppingItem> items = new ArrayList<>();

        // Read item details
        for (int i = 0; i < numItems; i++) {
            String line = scanner.nextLine().trim();
            items.add(parseItem(line));
        }

        // Calculate receipt
        Receipt receipt = generateReceipt(items);

        // Print receipt
        System.out.println(receipt.getReceiptDetails());
    }

    private static ShoppingItem parseItem(String line) {
        String[] parts = line.split(" at ");
        String quantityAndName = parts[0].trim();
        double price = Double.parseDouble(parts[1].trim());

        int quantity = Integer.parseInt(quantityAndName.split(" ")[0].trim());
        String name = quantityAndName.substring(quantityAndName.indexOf(' ') + 1).trim();
        boolean isClearance = name.toLowerCase().contains("clearance");

        name = name.replace("clearance", "").trim();

        return new ShoppingItem(name, price, quantity, isClearance);
    }

    private static Receipt generateReceipt(List<ShoppingItem> items) {
        double totalCost = 0;
        double totalSaved = 0;
        StringBuilder receipt = new StringBuilder();

        for (ShoppingItem item : items) {
            double originalPrice = item.getPrice();
            double discountRate = getDiscountRate(item);
            double discountedPrice = originalPrice * (1 - discountRate);

            if (item.isClearance()) {
                discountedPrice *= (1 - 0.20); // Additional 20% discount for clearance
            }

            discountedPrice = Math.round(discountedPrice * 100.0) / 100.0; // Round to nearest cent
            double saved = originalPrice - discountedPrice;

            receipt.append(String.format("%d %s at %.2f%n", item.getQuantity(), item.getName(), discountedPrice));
            totalCost += discountedPrice * item.getQuantity();
            totalSaved += saved * item.getQuantity();
        }

        receipt.append(String.format("Total: %.2f%n", totalCost));
        receipt.append(String.format("You saved: %.2f%n", totalSaved));

        return new Receipt(receipt.toString());
    }

    private static double getDiscountRate(ShoppingItem item) {
        if (item.getName().toLowerCase().contains("book") ||
            item.getName().toLowerCase().contains("food") ||
            item.getName().toLowerCase().contains("drink")) {
            return 0.05; // 5% discount
        } else if (item.getName().toLowerCase().contains("shirt") ||
                   item.getName().toLowerCase().contains("dress")) {
            return 0.20; // 20% discount
        } else {
            return 0.03; // 3% discount
        }
    }

    static class ShoppingItem {
        private String name;
        private double price;
        private int quantity;
        private boolean isClearance;

        public ShoppingItem(String name, double price, int quantity, boolean isClearance) {
            this.name = name;
            this.price = price;
            this.quantity = quantity;
            this.isClearance = isClearance;
        }

        public String getName() {
            return name;
        }

        public double getPrice() {
            return price;
        }

        public int getQuantity() {
            return quantity;
        }

        public boolean isClearance() {
            return isClearance;
        }
    }

    static class Receipt {
        private String receiptDetails;

        public Receipt(String receiptDetails) {
            this.receiptDetails = receiptDetails;
        }

        public String getReceiptDetails() {
            return receiptDetails;
        }
    }
}
