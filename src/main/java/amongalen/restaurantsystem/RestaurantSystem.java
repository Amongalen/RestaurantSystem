package amongalen.restaurantsystem;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Adam Parys
 */
public class RestaurantSystem {

    final static String FILENAME = "menu.csv";
    private HashMap<String, ArrayList<Meal>> menu = new HashMap();
    private Scanner keyboard = new Scanner(System.in);
    private ArrayList<Meal> order = new ArrayList<>();

    /**
     * @param args the command line arguments
     */
    public void loadMenu(String filename) {
        Path path = Paths.get(filename);
        try (Scanner scanner = new Scanner(path)) {

            while (scanner.hasNextLine()) {
                String lane = scanner.nextLine();
                if (lane.startsWith("%") || lane.isEmpty()) {
                    continue;
                }
                String[] laneValues = Arrays.stream(lane.split(";")).map(l -> {
                    return l.trim().toLowerCase();
                }).toArray(String[]::new);

                addToMenu(laneValues[0], laneValues[1], Integer.parseInt(laneValues[2]));
            }
        } catch (IOException ex) {
            Logger.getLogger(RestaurantSystem.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void addToMenu(String category, String name, int price) {
        ArrayList<Meal> mealsInCategory = menu.get(category);
        if (mealsInCategory == null) {
            mealsInCategory = new ArrayList();
        }
        if (category.equalsIgnoreCase("drink")) {
            mealsInCategory.add(new Drink(name, price));
        } else {
            mealsInCategory.add(new Meal(name, price));
        }
        menu.put(category, mealsInCategory);
    }

    public int selectOrderTypeDialog() {
        System.out.println("Choose what do You want to order (type number preceding an option):");
        System.out.println("1. A lunch");
        System.out.println("2. A drink");
        System.out.println("3. Both");
        String answer = keyboard.nextLine();
        int typeSelection;
        try {
            typeSelection = Integer.parseInt(answer);
        } catch (NumberFormatException e) {
            System.out.println("You must enter a valid number! Try again.");
            return selectOrderTypeDialog();
        }
        if (typeSelection < 1 || typeSelection > 3) {
            System.out.println("That isn't a valid option! Try again.");
            return selectOrderTypeDialog();
        }
        return typeSelection;
    }

    public void processOrder(int typeSelection) throws SomethingWentWrongException {
        switch (typeSelection) {
            case 1:
                chooseLunchDialog();
                break;
            case 2:
                chooseDrinkDialog();
                break;
            case 3:
                chooseLunchDialog();
                chooseDrinkDialog();
                break;
            default:
                throw new SomethingWentWrongException();
        }
    }

    private void chooseLunchDialog() {
        String cuisine = chooseCuisineDialog();
        chooseMealDialaog(cuisine);
        System.out.println("Are You interested in some dessert?");
        chooseMealDialaog("dessert");
    }

    private String chooseCuisineDialog() {

        System.out.println("In our restaurant we serve dishes from different cuisines. Choose one of them:");
        int count = 0;
        String[] cuisines = menu.keySet().stream().filter(c -> {
            if (!c.equalsIgnoreCase("drink") && !c.equalsIgnoreCase("dessert")) {
                return true;
            } else {
                return false;
            }
        }).toArray(String[]::new);
        for (String cuisine : cuisines) {
            count++;
            System.out.println(count + ". " + cuisine);
        }
        String answer = keyboard.nextLine();
        int cuisineSelection = 0;
        try {
            cuisineSelection = Integer.parseInt(answer);
        } catch (NumberFormatException e) {
            System.out.println("You must enter a valid number! Try again.");
            return chooseCuisineDialog();
        }
        if (cuisineSelection < 1 || cuisineSelection > cuisines.length) {
            System.out.println("That isn't a valid option! Try again.");
            return chooseCuisineDialog();
        }
        return cuisines[cuisineSelection - 1];
    }

    private void chooseDrinkDialog() {
        chooseMealDialaog("drink");

    }

    private void chooseMealDialaog(String category) {
        System.out.println("In this category we have following options to offer. Choose one of them:");
        int count = 0;
        for (Meal meal : menu.get(category)) {
            count++;
            System.out.println(count + ". " + meal.getName() + " for " + formatPrice(meal.getPrice()));
        }
        if (category.equalsIgnoreCase("dessert")) {
            count++;
            System.out.println(count + ". No, thanks.");
        }
        String answer = keyboard.nextLine();
        int mealSelection = 0;
        try {
            mealSelection = Integer.parseInt(answer);
        } catch (NumberFormatException e) {
            System.out.println("You must enter a valid number! Try again.");
            chooseMealDialaog(category);
            return;
        }
        if (mealSelection < 1 || mealSelection > count) {
            System.out.println("That isn't a valid option! Try again.");
            chooseMealDialaog(category);
            return;
        }
        if (category.equalsIgnoreCase("dessert") && mealSelection == count) {
            return;
        }
        Meal selectedMeal;
        if (category.equalsIgnoreCase("drink")) {
            selectedMeal = chooseDrinkAdditionsDialog((Drink) menu.get(category).get(mealSelection - 1));
        } else {
            selectedMeal = menu.get(category).get(mealSelection - 1);
        }
        order.add(selectedMeal);
    }

    private Drink chooseDrinkAdditionsDialog(Drink drink) {
        System.out.println("We can offer You following additions to Your drink. Choose one of them:");
        System.out.println("1. With lemon");
        System.out.println("2. With ice cubes");
        System.out.println("3. With both");
        System.out.println("4. With none");
        String answer = keyboard.nextLine();
        int additionSelection = 0;
        try {
            additionSelection = Integer.parseInt(answer);
        } catch (NumberFormatException e) {
            System.out.println("You must enter a valid number! Try again.");

            return chooseDrinkAdditionsDialog(drink);
        }
        if (additionSelection < 1 || additionSelection > 4) {
            System.out.println("That isn't a valid option! Try again.");

            return chooseDrinkAdditionsDialog(drink);
        }
        switch (additionSelection) {
            case 1:
                drink.setWithLemon(true);
                break;
            case 2:
                drink.setWithIce(true);
                break;
            case 3:
                drink.setWithIce(true);
                drink.setWithLemon(true);
                break;
        }
        return drink;
    }

    public static String formatPrice(int price) {
        return new DecimalFormat("0.00").format(price / 100f);
    }

    public void displayOrder() {
        System.out.println("You order looks as follow:");
        int totalPrice = 0;
        for (Meal meal : order) {
            System.out.println(meal.getName() + " for " + formatPrice(meal.getPrice()));
            totalPrice += meal.getPrice();
        }
        System.out.println("Total cost: " + formatPrice(totalPrice));
    }
}
