package amongalen.restaurantsystem;

/**
 *
 * @author Adam Parys
 */
public class Drink extends Meal {

    private boolean withIce;
    private boolean withLemon;

    public Drink(String name, int price, boolean withIce, boolean withLemon) {
        super(name, price);
        this.withIce = withIce;
        this.withLemon = withLemon;

        if (withIce && withLemon) {
            name = name + " with ice and lemon";
            price += 35;
        } else if (withIce) {
            name = name + " with ice";
            price += 20;
        } else if (withLemon) {
            name = name + " with lemon";
            price += 25;
        }
    }

    public Drink(String name, int price) {
        super(name, price);
        this.withIce = false;
        this.withLemon = false;
    }

    public void setWithIce(boolean withIce) {
        this.withIce = withIce;
    }

    public void setWithLemon(boolean withLemon) {
        this.withLemon = withLemon;
    }

    @Override
    public int getPrice() {
        if (withIce && withLemon) {
            return super.getPrice() + 35;
        } else if (withIce) {
            return super.getPrice() + 20;
        } else if (withLemon) {
            return super.getPrice() + 25;
        } else {
            return super.getPrice();
        }
    }

    @Override
    public String getName() {
        if (withIce && withLemon) {
            return super.getName() + "with ice and lemon";
        } else if (withIce) {
            return super.getName() + "with ice";
        } else if (withLemon) {
            return super.getName() + "with lemon";
        } else {
            return super.getName();
        }
    }

}
