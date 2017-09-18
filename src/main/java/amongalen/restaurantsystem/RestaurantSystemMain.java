package amongalen.restaurantsystem;

import static amongalen.restaurantsystem.RestaurantSystem.FILENAME;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Adam Parys
 */
public class RestaurantSystemMain {

    public static void main(String[] args) {
        try {
            RestaurantSystem rs = new RestaurantSystem();
            rs.loadMenu(FILENAME);
            int typeSelection = rs.selectOrderTypeDialog();
            rs.processOrder(typeSelection);
            rs.displayOrder();
            
        } catch (Exception ex) {
            Logger.getLogger(RestaurantSystem.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
